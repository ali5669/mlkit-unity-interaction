from __future__ import print_function
import torch
import torch.utils.data
from models.lstm.LSTMClassifier import LSTMClassifier
from dataloader import MyDataset, collate_fn
from tqdm import tqdm
from models.stgcn.st_gcn import Model as STGCN
import matplotlib.pyplot as plt 

from util import get_logger, get_real_length

input_size = 48
hidden_size = 512
num_layers = 16
num_class = 6
# [[48]: 1]

logger = get_logger('lstm')


learning_rate = 1e-4
num_epochs = 20

dataset = MyDataset()
train_dataset, test_dataset = dataset.get_split(0.75)
print(train_dataset.__len__())
print(test_dataset.__len__())
# model = LSTMClassifier(input_size=input_size, hidden_size=hidden_size, 
#                         num_layers=num_layers, num_classes=num_class).to('cuda')
model = STGCN(in_channels=2, num_class=num_class).to('cuda')
criterion = torch.nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr = learning_rate)
train_loader = torch.utils.data.DataLoader(dataset=train_dataset, batch_size=64, shuffle=True, num_workers=0)
test_loader = torch.utils.data.DataLoader(dataset=test_dataset, batch_size=512, shuffle=True, num_workers=0)

def train():
    
    losses = []
    accs = []

    for epoch in tqdm(range(num_epochs)):
        model.train()
        all_corrects = 0
        all_counts = 0
        for index, (pose, label) in enumerate(train_loader):
            pose = pose.to('cuda')
            # print(pose)
            label = label.to('cuda')
            outputs = model(pose)
            # print(outputs)
            optimizer.zero_grad()

            loss = criterion(outputs, label)

            losses.append(loss.item())

            loss.backward()
            optimizer.step()

            pred_label = torch.argmax(outputs, dim=1)
            corrects = torch.sum(pred_label == label)
            all_corrects += corrects
            accs.append(corrects.item() / pose.shape[0])
            all_counts += pose.shape[0]

            

        # if epoch % 10 == 0:
        test_loss, test_acc = test(test_loader)
        train_acc = all_corrects / all_counts
        logger.info(f"Epoch: {epoch}, loss: {loss.item():.3g}, test_loss: {test_loss:.3g}," + \
                    f"train_acc: {train_acc:.3g}, test_acc: {test_acc:.3g}")
        
    torch.save(model, 'stgcn.pt')
    # torch.save(model, 'lstm.pt')
    # plt.title("st-gcn loss")
    plt.title("st-gcn accuracy")
    # plt.title("lstm loss")
    # plt.title("lstm accuracy")
    plt.xlabel("step")
    # plt.ylabel("loss")
    plt.ylabel("accuracy")
    plt.plot(range((len(accs))), accs)
    plt.show()


    


@torch.no_grad
def test(test_loader):
    model.eval()
    losses = []
    all_corrects = 0
    all_counts = 0
    for index, (pose, label) in enumerate(test_loader):
        pose = pose.to('cuda')
        label = label.to('cuda')
        outputs = model(pose)
        
        loss = criterion(outputs, label)
        losses.append(loss.item())

        pred_label = torch.argmax(outputs, dim=1)
        corrects = torch.sum(pred_label == label)
        all_corrects += corrects
        all_counts += pose.shape[0]
        
    mean_loss = sum(losses) / len(losses)
    test_acc = all_corrects / all_counts
    return mean_loss, test_acc


train()
# test()
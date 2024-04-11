from __future__ import print_function
import torch
import torch.utils.data
from models.lstm.LSTMClassifier import LSTMClassifier
from dataloader import MyDataset, collate_fn
from tqdm import tqdm
from models.stgcn.st_gcn import Model as STGCN

from util import get_logger, get_real_length

input_size = 48
hidden_size = 512
num_layers = 16
num_class = 6
# [[48]: 1]

logger = get_logger('lstm')


learning_rate = 1e-4
num_epochs = 5

dataset = MyDataset()
train_dataset, test_dataset = dataset.get_split(0.75)
print(train_dataset.__len__())
print(test_dataset.__len__())
# model = LSTMClassifier(input_size=input_size, hidden_size=hidden_size, 
#                         num_layers=num_layers, num_classes=num_class).to('cuda')
model = STGCN(in_channels=2, num_class=num_class).to('cuda')
criterion = torch.nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr = learning_rate)

def train():
    train_loader = torch.utils.data.DataLoader(dataset=train_dataset, batch_size=64, shuffle=True)
    test_loader = torch.utils.data.DataLoader(dataset=test_dataset, batch_size=512, shuffle=True)

    for epoch in tqdm(range(num_epochs)):
        model.train()
        all_corrects = 0
        all_counts = 0
        for index, (pose, label) in enumerate(train_loader):
            pose = pose.to('cuda')
            label = label.to('cuda')
            outputs = model(pose)

            optimizer.zero_grad()

            loss = criterion(outputs, label)
            loss.backward()
            optimizer.step()

            pred_label = torch.argmax(outputs, dim=1)
            corrects = torch.sum(pred_label == label)
            all_corrects += corrects
            all_counts += pose.shape[0]

            

        # if epoch % 10 == 0:
        test_loss, test_acc = test(test_loader)
        train_acc = all_corrects / all_counts
        logger.info(f"Epoch: {epoch}, loss: {loss.item():.3g}, test_loss: {test_loss:.3g}," + \
                    f"train_acc: {train_acc:.3g}, test_acc: {test_acc:.3g}")

    


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
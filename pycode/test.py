import torch
from torch.utils.data import Dataset
from util import get_data
from numpy import random

class TestDataLoader(Dataset):
    def __init__(self):
        super().__init__()
        data, label = get_data('./test/test.txt')
        self.datas = list(map(torch.FloatTensor, data))
        self.labels = label
        self.shuffle()

    def __len__(self):
        return len(self.datas)
    
    def __getitem__(self, index):
        array = self.datas[index]
        label = self.labels[index]

        return array, label
    
    def shuffle(self):
        all_datas = [(data, label) for data, label in zip(self.datas, self.labels)]

        random.shuffle(all_datas)
        
        self.datas = [all_data[0] for all_data in all_datas]
        self.labels = [all_data[1] for all_data in all_datas]

model = torch.load('stgcn_mobile.pt')
model.eval()

test_loader = TestDataLoader()

for index, (pose, label) in enumerate(test_loader):
    pose = pose.view(1, pose.shape[0], 24, 2)
    outputs = model(pose)
    print(outputs)
    pred_label = torch.argmax(outputs, dim=1)
    print(pred_label)
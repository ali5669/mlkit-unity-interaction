from torch.utils.data import Dataset, DataLoader
from util import get_data
import os
import random

import torch


from torch.nn.utils.rnn import pad_sequence

class MyDataset(object):
    def __init__(self, folder_path = './data'):
        super().__init__()

        self.datas = []
        self.labels = []

        # folder_path = './data'

        for file in os.listdir(folder_path):
            data, label = get_data(os.path.join(folder_path, file))
            self.datas += list(map(torch.FloatTensor, data))
            self.labels += label

        print(self.datas.__len__())
        print(self.labels.__len__())

    def get_split(self, train_test_rate):
        
        self.shuffle()
        
        train_amount = int(len(self.datas) * train_test_rate)

        train_dataset = GeneratorDataset(self.datas[: train_amount], self.labels[: train_amount])
        test_dataset = GeneratorDataset(self.datas[train_amount :], self.labels[train_amount :])

        return train_dataset, test_dataset
    
    def shuffle(self):
        all_datas = [(data, label) for data, label in zip(self.datas, self.labels)]

        random.shuffle(all_datas)
        
        self.datas = [all_data[0] for all_data in all_datas]
        self.labels = [all_data[1] for all_data in all_datas]

        

class GeneratorDataset(Dataset):

    def __init__(self, datas, labels):
        self.datas = datas
        self.labels = labels

    def __len__(self):
        return len(self.datas)
    
    def __getitem__(self, index):
        array = self.datas[index]
        label = self.labels[index]

        return array, label

def collate_fn(batch):

    datas = [data[0] for data in batch]

    datas = pad_sequence(datas, batch_first=True, padding_value=0)

    labels = [data[1] for data in batch]

    labels = torch.LongTensor(labels)

    return datas, labels
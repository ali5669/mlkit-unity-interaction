import torch
import torch.nn as nn
from torch.autograd import Variable
import torch.nn.functional as F

class LSTMClassifier(nn.Module):

    def __init__(self, input_size, hidden_size, num_layers, num_classes):
        super(LSTMClassifier, self).__init__()

        self.hidden_size = hidden_size
        self.num_layers = num_layers

        self.lstm = nn.LSTM(input_size = input_size, hidden_size = hidden_size, 
                num_layers = num_layers, batch_first = True)
        
        self.fc = nn.Linear(hidden_size, num_classes)
        

    def forward(self, x):
        _, (h_hout, _) = self.lstm(x)

        h_hout = h_hout[-1, :, :].squeeze()

        out = self.fc(h_hout)


        return out
    
class CustomLSTM(nn.LSTM):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
    
    def forward(self, input, lengths):
        package = nn.utils.rnn.pack_padded_sequence(input, lengths.cpu(), batch_first=self.batch_first, enforce_sorted=False)
        result, hn = super().forward(package)
        output, lens = nn.utils.rnn.pad_packed_sequence(result, batch_first=self.batch_first, total_length=input.shape[self.batch_first])
        return output, hn

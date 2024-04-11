import random
import os
import logging
from tqdm.auto import tqdm
import torch

TEST = 1000

def get_data(f, sample_num = 2000):
    
    logcat_data = []
    with open(f, 'r', encoding='utf-16-le') as file:
        for line in file:
            datas = line.split(':')[-1]
            datas = datas.strip().split(' ')

            values = list(map(eval, datas))

            x = values[4]
            y = values[5]

            for i in range(0, len(values), 2):
                values[i] -= x
                values[i + 1] -= y

            logcat_data.append(values)

    # print(logcat_data)

    datas = []

    min_len = 100

    for _ in range(sample_num):
        start = random.randint(0, len(logcat_data) - min_len)
        sub_data = logcat_data[start:start + min_len]
        datas.append(sub_data)
    
    file_name = os.path.splitext(os.path.basename(f))[0]
    labels = [{
        'celebrate' : 0,
        'clap' : 1,
        'hug' : 2,
        'punch' : 3,
        'sad' : 4,
        'none' : 5,
    }[file_name]] * sample_num

    return datas, labels

def get_logger(name='logger', file_name = 'log.txt'):
    
    console_handler = logging.StreamHandler()
    file_handler = logging.FileHandler(file_name, mode="a", encoding="utf-8")

    console_fmt = "(%(levelname)s) %(asctime)s - %(name)s:\n%(message)s"
    file_fmt = "(%(levelname)s) %(asctime)s  - %(name)s:\n%(message)s"

    console_formatter = logging.Formatter(fmt = console_fmt)
    file_formatter = logging.Formatter(fmt = file_fmt)

    tqdm_handler = TqdmLoggingHandler()
    tqdm_handler.setLevel(logging.INFO)
    tqdm_handler.setFormatter(fmt = console_formatter)
    file_handler.setFormatter(fmt = file_formatter)

    logging.basicConfig(level='INFO', handlers=[tqdm_handler, file_handler])
    logger = logging.getLogger(name)

    return logger

class TqdmLoggingHandler(logging.StreamHandler):

    def __init__(self, level = logging.NOTSET):
        logging.StreamHandler.__init__(self)

    def emit(self, record):

        msg = self.format(record)
        tqdm.write(msg)
        self.flush()

def get_real_length(t):
    mask = torch.any(t, dim = 2)

    lengths = mask.sum(dim = 1)

    # print(lengths)
    return lengths


if __name__ == '__main__':
    # datas, labels = get_data('data/celebrate.txt')
    # print(datas)
    # print(labels)
    get_real_length(torch.tensor([[[1,2,3], [0,0,0], [1,2,3], [0,0,0]], [[1,2,3], [1,2,3], [0,0,0], [0,0,0]]]))
from unittest import TestCase
import os, sys
import requests

currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
import datetime
from slates import dk


# x = datetime.datetime(2020, 11, 9, 20, 27, 15, 414721)
# x_sunday = datetime.datetime(2020, 11, 15, 20, 27, 15, 414721)
# print(x_sunday)


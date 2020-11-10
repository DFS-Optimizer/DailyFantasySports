from unittest import TestCase
import os, sys

from slates.dk import changedate_nfl

currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
import datetime
from slates import dk

class MainNFLDKTest(TestCase):
    def test_nfl_sunday(self):
        x_sunday = datetime.datetime(2020, 11, 15, 20, 27, 15, 414721)
        x_sunday = x_sunday.strftime("%Y-%m-%d")
        checking = False
        x_new = changedate_nfl()
        if x_new == x_sunday:
            checking = True
        self.assertEquals(True, checking)


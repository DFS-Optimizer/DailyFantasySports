from unittest import TestCase
import Main
import pandas as pd


# check that the FD slate returned is the same one being updated in the csv
# must be run on the server
class MainGetSlateNFLFD(TestCase):
    def test_nflrun_draftkings(self):
        OutSeason = Main.get_slate_nfl_fd()
        sl = pd.read_csv(OutSeason)
        df = pd.read_csv('/home/ubuntu/gitrepositories/DailyFantasySports/DFOptimizerApp/app/src/main/python/slates/NFLslateFD.csv')
        self.assertEquals(sl, df)

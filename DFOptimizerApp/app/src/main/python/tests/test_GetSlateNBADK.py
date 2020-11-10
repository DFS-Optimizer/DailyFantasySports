from unittest import TestCase
import Main
import json



class MainGetSlateNBADK(TestCase):
    def test_nflrun_draftkings(self):
        OutSeason = Main.get_slate_nba_dk()
        self.assertEquals(OutSeason, "slates not available, nba out of season")

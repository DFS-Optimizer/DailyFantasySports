from unittest import TestCase
import Main
import json



class MainNFLDKTest(TestCase):
    def test_nflrun_draftkings(self):
        namelist = ["Drew Brees", "Alex Armah", "Kenny Stills"]
        one = Main.nflrun_draftkings("Drew Brees", "Alex Armah", "Kenny Stills")
        checking = False
        for name in namelist:
            if name in one:
                checking = True
            else:
                checking = False
                break
        self.assertEquals(True, checking)

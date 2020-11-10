from unittest import TestCase
import Main
import json



class MainNFLDKTest(TestCase):
    def test_nflrun_draftkings(self):
        name = "Drew Brees"
        one = Main.nflrun_fanduel(name)
        checking = False
        if name in one:
            checking = True
        self.assertEquals(True, checking)

from unittest import TestCase
import Main


# Make sure we can return multiple lineups
#total appears at the end of each lineup so count each total
class MainGetSlateNFLFD(TestCase):
    def test_nflrun_draftkings(self):
        check = 10
        count = 0
        lineups = Main.nflrun_draftkings(num=check)
        lineup = lineups.split("\"")
        for a in lineup:
            if a == 'Total':
                count = count + 1
        self.assertEquals(count, check)
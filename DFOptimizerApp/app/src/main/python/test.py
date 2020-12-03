import csv
from unittest import TestCase
import Main

count = 0
check = 1
lineups = Main.nflrun_draftkings(num = check)
lineup = lineups.split("\"")
for a in lineup:
    if a == 'Total':
        count = count+1
print(count)
# from nba_api.stats.static import players
# from nba_api.stats.static import teams as teams_nba
# from nba_api.stats import endpoints
#import pandas as pd
#from Database import query as sql_query

# from NBAstats_Functions import nba_players
# from NBAstats_Functions import nba_seasons
# from NBAstats_Functions import nba_teams
# from NBAstats_Functions import league_game_logs


class Player:
    def __init__(self, name, team, position, score, cost):
        self.playerName = name
        self.playerTeam = team
        self.playerPosition = position
        self.projectedScore = score
        self.playerCost = cost


lineup = [Player('LeBron James', 'Lakers', 'test', 100, 10000)\
    , Player('Lonzo Ball', 'Pelicans', 'test', 21, 5000)]
 

def getName():
    names = []
    for p in lineup:
        report = p.playerName + " " + str(p.projectedScore)
        names.append(report)
    return names


for x in getName():
    print(x)
#LeBron James 100, Lonzo Ball 21


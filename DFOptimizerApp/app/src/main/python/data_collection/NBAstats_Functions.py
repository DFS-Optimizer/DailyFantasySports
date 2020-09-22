from nba_api.stats.static import players
from nba_api.stats.static import teams as teams_nba
from nba_api.stats import endpoints
import pandas as pd
from Database import query as sql_query
from constant import constant
import re
import time
import random

UPPER_RANGE = '30000'
LOWER_RANGE = '20000'
CONNECTION_TIMEOUT = 30

def nba_seasons():
    season = 1970  # start season
    tuple_list = []  # list of tuples holds season data
    while season < 2021:
        year = str(season) + "-" + str(season + 1)
        tuple_list.append((season + 210000, year))
        season += 1
    with sql_query.SqlQuery() as query:  # insert into sql table5
        query.insert_many(tuple_list, table='season')


def nba_teams():
    teams = pd.DataFrame(teams_nba.get_teams())
    teams = teams.drop(columns='year_founded', axis=0)
    teams = teams.drop(columns='nickname', axis=0)
    with sql_query.SqlQuery() as query:
        query.insert_many(teams, 'team', True)


def nba_players():
    with sql_query.SqlQuery() as query:
        df = pd.DataFrame(players.get_players())
        query.insert_many(df, table='player')


#
# game log for every game since 1995 season
#
def league_game_logs():
    with sql_query.SqlQuery() as query:
        for season in range(1995, 2020):    # loop through seasons we want

            season_games = endpoints.LeagueGameLog(season=season, headers=constant.headers).get_normalized_dict()['LeagueGameLog']

            season_game_data = []     # list of tuples representing a season, each tuple object representing a game in the season
            for x in range(0, len(season_games), 2):
                game = season_games[x]

                season_id = int(game['SEASON_ID']) + 190000

                season_game_data.append((game['GAME_ID'], game['GAME_DATE'], season_id))

            query.insert_many(season_game_data, table='event')

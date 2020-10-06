from nba_api.stats.static import players
from nba_api.stats.static import teams as teams_nba
from nba_api.stats import endpoints
import pandas as pd
from Database import query as sql_query
from constant import constant
import re
import time
import random

# use these values to filter out preseason and postseason data
UPPER_RANGE = '30000'
LOWER_RANGE = '20000'
CONNECTION_TIMEOUT = 30


#
# a list of al the season years sense 1970
#
def nba_seasons():
    season = 1970  # start season
    tuple_list = []  # list of tuples holds season data
    while season < 2021:
        year = str(season) + "-" + str(season + 1)
        tuple_list.append((season + 210000, year))
        season += 1
    with sql_query.SqlQuery() as query:  # insert into sql table5
        query.insert_many(tuple_list, table='season')


#
# all the nba teams without the year found or nickname
#
def nba_teams():
    teams = pd.DataFrame(teams_nba.get_teams())
    teams = teams.drop(columns='year_founded', axis=0)
    teams = teams.drop(columns='nickname', axis=0)
    with sql_query.SqlQuery() as query:
        query.insert_many(teams, 'team', True)


#
# all the players from the nba website
#
def nba_players():
    with sql_query.SqlQuery() as query:
        df = pd.DataFrame(players.get_players())
        query.insert_many(df, table='player')


#
# game log for every game since 1995 season
#
def league_game_logs():
    with sql_query.SqlQuery() as query:
        for season in range(1995, 2020):  # loop through seasons we want
            season_games = endpoints.LeagueGameLog(season=season, headers=constant.headers).get_normalized_dict()['LeagueGameLog']

            season_game_data = []  # list of tuples representing a season, each tuple object representing a game in the season
            for x in range(0, len(season_games), 2):
                game = season_games[x]
                season_id = int(game['SEASON_ID']) + 190000
                season_game_data.append((game['GAME_ID'], game['GAME_DATE'], season_id, 21))

            query.insert_many(season_game_data, table='event',)


#
# gets the game log for each team
#
def team_game_log():
    with sql_query.SqlQuery() as query:
        for season in range(1995, 2020):
            season_games = endpoints.LeagueGameLog(season=season, headers=constant.headers).get_normalized_dict()[
                'LeagueGameLog']

            game_data = []  # array of tuples to hold teams season games
            for game in season_games:  # iterate over teams games played in a season
                home = False
                win = False

                opponent_id = constant.team_abbr[re.split(r'\W\s', game['MATCHUP'])[1]]

                if not re.findall('@', game['MATCHUP']):  # if not away team set home team to true
                    home = True

                if game['WL'] is 'W':  # if team won set win to true
                    win = True

                game_data.append([game['GAME_ID'], game['TEAM_ID'], home, opponent_id, win])

            query.insert_many(game_data, 'team_event')
            time.sleep(random.randint(4, 7))


def active_player_seasons():
    upper = UPPER_RANGE
    lower = LOWER_RANGE

    with sql_query.SqlQuery() as query:
        active_players = query.get_table('player', '*','WHERE is_active = 1 ', dataframe=False)
        a = 0
        for x in range(0, len(active_players)):
            a = a+1
            if a > 313:

                player_id = active_players[x][0]  # current player_id
                season_logs = endpoints.CommonPlayerInfo(player_id, headers=constant.headers)
                team_id = season_logs.get_normalized_dict()['CommonPlayerInfo'][0]['TEAM_ID']

                seasons = season_logs.get_data_frames()[2]  # available seasons for player
                seasons = seasons[(seasons['SEASON_ID'] > lower) & (seasons['SEASON_ID'] < upper)]  # filtering out preseason and playoffs
                seasons = seasons.astype(int)

                seasons['player_id'] = player_id
                seasons = seasons[['player_id', 'SEASON_ID']]
                seasons.columns = ['player_id', 'season_id']

                seasons['season_id'] += 190000

                arg = f"WHERE id={player_id}"

                query.insert_many(seasons, 'player_season')
                print(a)

                if team_id != 0:
                    query.update_row(team_id, 'player', 'team_id', arg)

                time.sleep(random.randint(2, 4))

from nba_api.stats.static import players
from nba_api.stats.static import teams as teams_nba
from nba_api.stats import endpoints
import pandas as pd
from python.Database.query import SqlQuery

# from python.data_collection.NBAstats_Functions import nba_players
# from python.data_collection.NBAstats_Functions import nba_seasons
# from python.data_collection.NBAstats_Functions import nba_teams
# from python.data_collection.NBAstats_Functions import league_game_logs
# from python.data_collection.NBAstats_Functions import team_game_log
# from python.data_collection.NBAstats_Functions import active_player_seasons
# from python.data_collection.NBAstats_Functions import player_game_log
# from python.data_collection.NBAstats_Functions import team_game_event


#nba_players()
#nba_seasons()
#nba_teams()
#league_game_logs()
#team_game_log()
#active_player_seasons()
#player_game_log()
#team_game_event()


# #get role from past_6 and put in new table roles
# with SqlQuery() as query:
#     df = query.get_table("model_past6",  dataframe=True)
#
# player = df.iloc[:, 0]
# season = df.iloc[:, 1]
# others = df.iloc[:, 3:6]
# r = df.iloc[:, 2]
#
# setup = pd.concat([player, season, r], axis = 1)
# setup = setup.iloc[:, 0:3]
# setup.drop_duplicates(subset=['player_id', 'season_id'], keep='first', inplace=True)
# query.insert_many(setup, "roles")


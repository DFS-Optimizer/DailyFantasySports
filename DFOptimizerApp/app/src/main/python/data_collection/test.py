from nba_api.stats.static import players
from nba_api.stats.static import teams as teams_nba
from nba_api.stats import endpoints
import pandas as pd
from main.python.Database import query as sql_query

from main.python.data_collection.NBAstats_Functions import nba_players
from main.python.data_collection.NBAstats_Functions import nba_seasons
from main.python.data_collection.NBAstats_Functions import nba_teams
from main.python.data_collection.NBAstats_Functions import league_game_logs
from main.python.data_collection.NBAstats_Functions import team_game_log
from main.python.data_collection.NBAstats_Functions import active_player_seasons
from main.python.data_collection.NBAstats_Functions import player_game_log


#nba_players()
#nba_seasons()
#nba_teams()
#league_game_logs()
#team_game_log()
#active_player_seasons()
#player_game_log()
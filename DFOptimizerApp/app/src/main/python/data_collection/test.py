from nba_api.stats.static import players
from nba_api.stats.static import teams as teams_nba
from nba_api.stats import endpoints
import pandas as pd
from Database import query as sql_query

with sql_query.SqlQuery() as query:
    df = pd.DataFrame(players.get_players())
    query.insert_many(df, 'player')
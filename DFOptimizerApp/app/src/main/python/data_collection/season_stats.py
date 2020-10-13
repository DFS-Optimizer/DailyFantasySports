from python.Database import query as sql_query

query = sql_query.SqlQuery()

playerSeasons = query.get_table("player_season", dataframe=False )
x = 0
for season in playerSeasons:
    x = x+1
    playerID = season[0]   # player id
    playerS = season[1]     # players season_id
    stats = query.call_procedure("thisS", (playerID, playerS), dataframe= True)  # call to procedure to get players season stats

    if stats.shape[0] < 10:  # skip players who played < 10 games
        continue

    # taking sum of season stats and getting a points per minute calculation
    stats = stats.sum(axis = 0)
    tot_min = stats.iloc[0]
    pts = stats.iloc[1] / tot_min
    reb = stats.iloc[2] / tot_min
    ast = stats.iloc[3] / tot_min
    stl = stats.iloc[4] / tot_min
    blk = stats.iloc[5] / tot_min
    tov = stats.iloc[6] / tot_min

    arr = [float(i) for i in [pts, reb, ast, stl, blk, tov]]  # setting types to float
    data = arr + [playerID, playerS, 0]
    query.insert(data, "model_seasonavg")   # season stat per min values into sql
    print(x)




import pandas as pd
from python.Database import query as sql_query


with sql_query.SqlQuery() as query:         # create sql connection
    player_seasons = query.get_table("player_season", dataframe = False )   # get avaliable player seasons from sports_db.player_season
   # roles_table = query.get_table("model_roles_5", dataframe=True)  # get roles for players

    for season in player_seasons:
        playerID = season[0]   # player id
        playerS = season[1]     # players season_id
        # rolerow = roles_table[(roles_table.season_id == playerS) & (roles_table.player_id == playerID)]
        # if rolerow["role"].empty:
        #     print(rolerow)
        #     print("empty")
        #     continue
        #
        # roleS = rolerow["role"]
        #
        # role = roleS.iloc[0]
        # role = int(role)
        a = 0
        stats = query.call_procedure("thisS", (playerID, playerS), dataframe = True)  # call to procedure to get players season stats

        total = stats.shape[0]
        for x in range(7, total):
            a = a+1
            # get the average stats for season to date
            seas = stats.iloc[0:x-1]
            seas = seas.sum(axis=0)
            tot_min = seas.iloc[0]
            spts = seas.iloc[1] / tot_min
            sreb = seas.iloc[2] / tot_min
            sast = seas.iloc[3] / tot_min
            sstl = seas.iloc[4] / tot_min
            sblk = seas.iloc[5] / tot_min
            stov = seas.iloc[6] / tot_min
            sthree = seas.iloc[7] / tot_min
            tot_min = tot_min / (x-1)

            # get the average stats for past 6 games
            p6 = stats.iloc[x-7:x-1]
            p6 = p6.sum(axis=0)
            ptot_min = p6.iloc[0]
            ppts = p6.iloc[1] / ptot_min
            preb = p6.iloc[2] / ptot_min
            past = p6.iloc[3] / ptot_min
            pstl = p6.iloc[4] / ptot_min
            pblk = p6.iloc[5] / ptot_min
            ptov = p6.iloc[6] / ptot_min
            pthree = p6.iloc[7] / ptot_min
            ptot_min = ptot_min / 6

            # get the results for the next game
            res = stats.iloc[x]
            rtot_min = res.iloc[0]
            rpts = res.iloc[1]
            rreb = res.iloc[2]
            rast = res.iloc[3]
            rstl = res.iloc[4]
            rblk = res.iloc[5]
            rtov = res.iloc[6]
            rthree = res.iloc[7]

            fanduel = rpts + (rreb * 1.2) + (rast * 1.5) + (rstl * 3) + (rblk * 3) - rtov       # calculate fanduel points scored
            fanduel = round(fanduel,2)
            draftkings = rpts + (rreb * 1.25) + (rast * 1.5) + (rstl * 2) + (rblk * 2) + (rthree * .5) - (rtov *.5)
            draftkings = round(fanduel,2)
            checkdoubles = 0
            if rpts > 9:
                checkdoubles = checkdoubles + 1
            if rreb > 9:
                checkdoubles = checkdoubles + 1
            if rast > 9:
                checkdoubles = checkdoubles + 1
            if rstl > 9:
                checkdoubles = checkdoubles + 1
            if rblk > 9:
                checkdoubles = checkdoubles + 1

            if checkdoubles == 2:
                draftkings = draftkings + 1.5
            if checkdoubles > 2:
                draftkings = draftkings + 3

            # set to floats and put in array
            arr = [float(i) for i in [ppts, preb, past, pstl, pblk, pthree, ptov, ptot_min, spts, sreb, sast, sstl, sblk, sthree, stov, tot_min]]
            arr = pd.Series([playerID, playerS, 0, fanduel, draftkings, rtot_min] + arr)
            arr.fillna(0, inplace=True)    # NaN values to 0

            # send to table
            query.insert(arr.tolist(), "model_past6")
            print(a)









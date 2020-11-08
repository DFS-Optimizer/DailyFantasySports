import pandas as pd
import datetime
from draft_kings.data import Sport
from draft_kings.client import contests
from draft_kings.client import available_players
from draft_kings.client import draftables
from draft_kings.client import draft_group_details


contests = contests(sport=Sport.NFL)


def changedate():
    day = datetime.datetime.now()
    while day.weekday() != 6:
        day+=datetime.timedelta(days=1)
    return day.strftime("%Y-%m-%d")



# print(changedate())


def updatenflDKslate():


    # contests = contests(sport=Sport.NFL)
    df = pd.DataFrame()


    for label in contests["contests"]:
        if not str(label["starts_at"]).startswith(changedate()):
            continue
        # print(label["starts_at"])
        id = label["draft_group_id"]
        break
        

    players = draftables(id)

    playerName = [x['names']['display'] for x in players['draftables']]
    sal = [x['salary'] for x in players['draftables']]
    pos = [x['position'] for x in players['draftables']]
    team =  [x['team_abbreviation'] for x in players['draftables']]
    opp = [x['competition']['name'] for x in players['draftables']]

    index = 0
    while index < len(playerName):
        
        if index > 0 and playerName[index] == playerName[index-1]:
            del playerName[index]
            del sal[index]
            del pos[index]
            del team[index]
            del opp[index]

        else:
            temp = opp[index].split()
            if temp[0] == team[index]:
                opp[index] = temp[-1]
            else:
                opp[index] = temp[0]
            index+=1

    df['playerName'] = playerName
    df['sal'] = sal
    df['pos'] = pos
    df['team'] = team
    df['opp'] = opp


    df['proj'] = [0 for _ in range(len(playerName))]

    df.to_csv('/home/ubuntu/gitrepositories/DailyFantasySports/DFOptimizerApp/app/src/main/python/slates/NFLslateDK.csv', index=False)
    




# file = open("draftables.txt", "w")
# print(available_players(draft_group_id=41096))
# print(str(draftables(41096))[:20000])
# # print(draft_group_details(id))

# print(available_players(draft_group_id=41391))

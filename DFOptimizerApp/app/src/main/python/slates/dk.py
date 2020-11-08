import pandas as pd
import datetime
from draft_kings.data import Sport
from draft_kings.client import contests
from draft_kings.client import available_players
from draft_kings.client import draftables
from draft_kings.client import draft_group_details


contests_nfl = contests(sport=Sport.NFL)
contests_nba = contests(sport=Sport.NBA)
contests_mlb = contests(sport=Sport.MLB)


def changedate_nfl():
    day = datetime.datetime.now()
    while day.weekday() != 6:
        day+=datetime.timedelta(days=1)
    return day.strftime("%Y-%m-%d")

def getdate():
    day = datetime.datetime.now()
    return day.strftime("%Y-%m-%d")



# print(changedate())


def update_nfl_DK_slate():

    if not contesets_nfl["contests"]:
        return 0
    # contests = contests(sport=Sport.NFL)
    df = pd.DataFrame()


    for label in contests_nfl["contests"]:
        if not str(label["starts_at"]).startswith(changedate_nfl()):
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


def update_nba_DK_slate():

    if not contests_nba["contests"]:
        return 0
    # contests = contests(sport=Sport.NFL)
    df = pd.DataFrame()


    for label in contests_nba["contests"]:
        if not str(label["starts_at"]).startswith(getdate()):
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

    df.to_csv('/home/ubuntu/gitrepositories/DailyFantasySports/DFOptimizerApp/app/src/main/python/slates/NBAslateDK.csv', index=False)


def update_mlb_DK_slate():

    if not contests_mlb["contests"]:
        return 0
    # contests = contests(sport=Sport.NFL)
    df = pd.DataFrame()


    for label in contests_mlb["contests"]:
        if not str(label["starts_at"]).startswith(getdate()):
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

    df.to_csv('/home/ubuntu/gitrepositories/DailyFantasySports/DFOptimizerApp/app/src/main/python/slates/MLBslateDK.csv', index=False)
    




# file = open("draftables.txt", "w")
# print(available_players(draft_group_id=41096))
# print(str(draftables(41096))[:20000])
# # print(draft_group_details(id))

# print(available_players(draft_group_id=41391))

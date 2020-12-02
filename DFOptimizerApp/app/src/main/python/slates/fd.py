import pandas as pd
import datetime
from draft_kings.data import Sport
from draft_kings.client import contests
from draft_kings.client import available_players
from draft_kings.client import draftables
from draft_kings.client import draft_group_details
import functools
import inspect
import os
import sys
import requests
from bs4 import BeautifulSoup
from mappings import COLUMN_MAPPINGS
from mappings import NAME_CORRECTIONS
import pandas as pd

contests_nfl = contests(sport=Sport.NFL)
contests_nba = contests(sport=Sport.NBA)
contests_mlb = contests(sport=Sport.MLB)


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


def changedate_nfl():
    day = datetime.datetime.now()
    while day.weekday() != 6:
        day += datetime.timedelta(days=1)
    return day.strftime("%Y-%m-%d")


def getdate():
    day = datetime.datetime.now()
    return day.strftime("%Y-%m-%d")


# print(changedate())


def update_nfl_FD_slate():
    if not contests_nfl["contests"]:
        return 0
    df = pd.DataFrame()
    id = None

    for label in contests_nfl["contests"]:
        if not str(label["starts_at"]).startswith(changedate_nfl()):
            continue
        # print(label["starts_at"])
        id = label["draft_group_id"]
        break

    if id is None:
        return 0

    players = draftables(id)

    playerName = [x['names']['display'] for x in players['draftables']]
    pos = [x['position'] for x in players['draftables']]
    team = [x['team_abbreviation'] for x in players['draftables']]
    opp = [x['competition']['name'] for x in players['draftables']]

    index = 0
    while index < len(playerName):

        if index > 0 and playerName[index] == playerName[index - 1]:
            del playerName[index]
            del pos[index]
            del team[index]
            del opp[index]

        else:
            temp = opp[index].split()
            if temp[0] == team[index]:
                opp[index] = temp[-1]
            else:
                opp[index] = temp[0]
            index += 1

    df['playerName'] = playerName
    df['sal'] = [0 for _ in range(len(playerName))]
    df['pos'] = pos
    df['team'] = team
    df['opp'] = opp
    df['proj'] = [0 for _ in range(len(playerName))]

    sl = df

    # set up some parameters for scrape
    base_url = 'http://www.fantasypros.com/nfl/projections'
    position_list = ['qb', 'rb', 'wr', 'te', 'k', 'dst']

    frames = []
    for position in position_list:
        temp = position + '.php?scoring=PPR'
        url = '%s/%s' % (base_url, temp)


        response = requests.get(url)


        # use expert:expert in request to get only one expert at a time
        # use pandas to parse the HTML table for us
        df = pd.io.html.read_html(
            response.text,
            attrs={'id': 'data'}
        )[0]
        # df['WEEK'] = current_week
        # df['POSITION'] = position.upper()
        df.rename(columns=COLUMN_MAPPINGS[position.upper()], inplace=True)
        if position is not 'dst' and position is not 'k':
            df.columns = ['{}'.format(x[1]) for x in df.columns]

        # print(df.columns.values)
        # print(df)
        frames.append(df)

        # msg = 'getting projections for {}, week {}, postition {}'

    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "NFLtemp.csv")

    check = len(frames)
    count = 0
    for frame in frames:
        count = count + 1
        # print(frame)
        for i in range(len(frame)):
            if count < check:
                name = frame.loc[i, "Player"]
                name = name.rsplit(' ', 1)[0]
                proj = frame.loc[i, "FPTS"]
                # print(name, proj)
                name = str(name)
                proj = str(proj)
                sl.loc[sl['playerName'] == name, 'proj'] = proj
            else:
                name = frame.loc[i, "Player"]
                words = name.split()
                name = words[-1]
                if name == 'Team':
                    name = 'Washington'
                proj = frame.loc[i, "FPTS"]
                name = name + ' '
                name = str(name)
                proj = str(proj)
                # print(name, proj)
                sl.loc[sl['playerName'] == name, 'proj'] = proj


    # sl = pd.read_csv(const_path)


    # set up some parameters for scrape
    base_url = 'https://www.fantasypros.com/daily-fantasy/nfl/fanduel-salary-changes.php'

    response = requests.get(base_url)

    df = pd.io.html.read_html(
        response.text)[0]
    # print(df)
    # print(df[['Player','This Week']])
    for i in range(len(df)):
        name = df.loc[i, "Player"]
        pos = name[-4:-1].strip()
        if pos =='DST' :
            name=name.split('(',1)[0].strip()
            words = name.split()
            name = words[-1].strip()
            name = name +' '
        else:
            name=name.split('(',1)[0].strip()
        sal = df.loc[i, "This Week"]
        sal = sal[1:]
        sal = sal.replace(',', '')
        #Renames
        if(name =='D.K. Metcalf'):
            name = 'DK Metcalf'
        if (name == 'Duke Johnson Jr.'):
            name = 'Duke Johnson'
        if (name == 'Wayne Gallman'):
            name = 'Wayne Gallman Jr.'
        if (name == 'D.J. Chark Jr.'):
            name = 'DJ Chark Jr.'
        if (name == 'Dwayne Haskins'):
            name = 'Dwayne Haskins Jr.'
        if (name == 'Team '):
            name = 'WAS Football Team '
        if (name == 'Darrell Henderson'):
            name = 'Darrell Henderson Jr.'
        # print(name, sal, pos)
        sl.loc[sl['playerName'] == name, 'sal'] = sal
    # print(sl.loc[sl['sal']==0])
    sl.to_csv(
        '/home/ubuntu/gitrepositories/DailyFantasySports/DFOptimizerApp/app/src/main/python/slates/NFLslateFD.csv',
        index=False)

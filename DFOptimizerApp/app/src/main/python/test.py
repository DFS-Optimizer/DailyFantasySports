import functools
import inspect
import os
import sys
import requests
from bs4 import BeautifulSoup
from mappings import COLUMN_MAPPINGS
import pandas as pd


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


# set up some parameters for scrape
base_url = 'http://www.fantasypros.com/nfl/projections'
current_week = 12
week_list = range(1, current_week)
position_list = ['qb', 'rb', 'wr', 'te', 'k', 'dst']

frames = []
for position in position_list:
    temp = position + '.php?scoring=PPR'
    url = '%s/%s' % (base_url, temp)
    params = {
        'week': current_week,
    }

    response = requests.get(url, params=params)

    msg = 'getting projections for week {}, postition {}'
    print(msg.format(current_week, position))

    # use expert:expert in request to get only one expert at a time
    # use pandas to parse the HTML table for us
    df = pd.io.html.read_html(
        response.text,
        attrs={'id': 'data'}
    )[0]
    # df['WEEK'] = current_week
    # df['POSITION'] = position.upper()
    df.rename(columns=COLUMN_MAPPINGS[position.upper()], inplace=True)
    # df = df.dropna(axis=0, thresh=4)
    if position is not 'dst' and position is not 'k':
        df.columns = ['{}'.format(x[1]) for x in df.columns]

    # print(df.columns.values)
    print(df)
    frames.append(df)

    msg = 'getting projections for {}, week {}, postition {}'

path = get_my_path()
path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
const_path = os.path.join(path, "slates", "NFLslateDK.csv")

print(frames)
sl = pd.read_csv(const_path)
check = len(frames)
count = 0
for frame in frames:
    count = count+1
    # print(frame)
    for i in range(len(frame)):
        if count < check:
            name = frame.loc[i, "Player"]
            name = name.rsplit(' ', 1)[0]
            proj = frame.loc[i, "FPTS"]
            print(name, proj)
            name = str(name)
            proj = str(proj)
            sl.loc[sl['playerName'] == name, 'proj'] = proj
        else:
            name = frame.loc[i, "Player"]
            words = name.split()
            name = words[-1]
            if name == 'Team':
                name ='Washington'
            proj = frame.loc[i, "FPTS"]
            name = name + ' '
            name = str(name)
            proj = str(proj)
            print(name, proj)
            sl.loc[sl['playerName'] == name, 'proj'] = proj

print(sl)
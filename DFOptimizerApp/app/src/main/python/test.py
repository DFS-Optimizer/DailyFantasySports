import sys
import requests
from bs4 import BeautifulSoup
from mappings import COLUMN_MAPPINGS
import pandas as pd

# set up some parameters for scrape
base_url = 'http://www.fantasypros.com/nfl/projections'
current_week = 12
week_list = range(1, current_week )
position_list = ['qb', 'rb', 'wr', 'te', 'k', 'dst']

frames = []
for position in position_list:
    temp = position + '.php?scoring=PPR'
    url = '%s/%s' % (base_url,temp)
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
    df['WEEK'] = current_week
    df['POSITION'] = position.upper()
    df.rename(columns=COLUMN_MAPPINGS[position.upper()], inplace=True)
    frames.append(df)

    msg = 'getting projections for {}, week {}, postition {}'


# final = pd.concat(frames, ignore_index=True)
for frame in frames:
    print(frame)
# filename = 'fantasypros-projections-{}.csv'
# frames.to_csv(filename, index=False)

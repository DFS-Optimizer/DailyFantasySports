import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
from Optimization.NFLFanduel import Fanduel as NFLFanduel
from Optimization.NFLDraftKings import Draftkings as NFLDraftKings
from flask import Flask


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


def nfloptimizeDK(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDKTest.csv")
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    # while True:
    #     num = input("How many lineups do you want to generate?")
    #     try:
    #         val = int(num)
    #         break;
    #     except ValueError:
    #         try:
    #             float(num)
    #             print("Input is an float number.")
    #             print("Input number is: ", val)
    #             break;
    #         except ValueError:
    #             print("This is not a number. Please enter a valid number")
    # print()

    # set the optimizer based on the user input for the site
    # enter the parameters
    optimizer = NFLDraftKings(num_lineups=int(num),
                              overlap=4,
                              solver=pulp.CPLEX_PY(msg=0),
                              players_filepath=const_path,
                              output_filepath=out_path)

    # create the indicators used to set the constraints to be used by the formula
    optimizer.create_indicators()
    # generate the lineups with the formula and the indicators
    lineups = optimizer.generate_lineups(formula=optimizer.type_1)
    # fill the lineups with player names - send in the positions indicator
    filled_lineups = optimizer.fill_lineups(lineups)
    # save the lineups
    # optimizer.save_file(optimizer.header, filled_lineups)
    # optimizer.save_file(optimizer.header, filled_lineups, show_proj=True)
    return filled_lineups


def nflrun_draftkings(*players, num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDKTest.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nfloptimizeDK(num)
    # for line in lineup:
    #     print(line)


    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
        e = players.__len__() * 100
    total = final.pop() - e
    total = round(total, 2)
    print(final)

    df = pd.read_csv(const_path)
    result = "["
    for player in final:
        temp = df.loc[df['playerName'] == player, 'proj'].values[0]
        temp = round(temp, 2)
        if not result == "[":
            result += ","
        result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    result += ',{"Total":"' + str(total) + '"}]'
    # print(result)

    return result


def nfloptimizeFD(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDKTest.csv")
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    # while True:
    #     num = input("How many lineups do you want to generate?")
    #     try:
    #         val = int(num)
    #         break;
    #     except ValueError:
    #         try:
    #             float(num)
    #             print("Input is an float number.")
    #             print("Input number is: ", val)
    #             break;
    #         except ValueError:
    #             print("This is not a number. Please enter a valid number")
    # print()

    # set the optimizer based on the user input for the site
    # enter the parameters
    optimizer = NFLFanduel(num_lineups=int(num),
                              overlap=4,
                              solver=pulp.CPLEX_PY(msg=0),
                              players_filepath=const_path,
                              output_filepath=out_path)

    # create the indicators used to set the constraints to be used by the formula
    optimizer.create_indicators()
    # generate the lineups with the formula and the indicators
    lineups = optimizer.generate_lineups(formula=optimizer.type_1)
    # fill the lineups with player names - send in the positions indicator
    filled_lineups = optimizer.fill_lineups(lineups)
    # save the lineups
    # optimizer.save_file(optimizer.header, filled_lineups)
    # optimizer.save_file(optimizer.header, filled_lineups, show_proj=True)
    return filled_lineups


def nflrun_fanduel(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDKTest.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nfloptimizeFD(1)

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
    e = players.__len__() * 100
    total = final.pop() - e
    total = round(total, 2)
    print(final)

    df = pd.read_csv(const_path)
    result = "["
    for player in final:
        temp = df.loc[df['playerName'] == player, 'proj'].values[0]
        temp = round(temp, 2)
        if not result == "[":
            result += ","
        result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    result += ',{"Total":"' + str(total) + '"}]'
    print(result)

    return result

nflrun_draftkings( num = 2)
# nflrun_fanduel()

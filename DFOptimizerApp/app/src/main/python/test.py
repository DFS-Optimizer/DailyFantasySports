import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
from Optimization.MLBFanduel import Fanduel as MLBFanduel
from Optimization.MLBDraftKings import Draftkings as MLBDraftKing
from flask import Flask


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


def mlboptimizeDK(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "MLBslateDK.csv")
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
    optimizer = MLBDraftKing(num_lineups=int(num),
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


def mlbrun_draftkings(*players, num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "MLBslateDK.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = mlboptimizeDK(num)
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
    # total = final.pop() - e
    # total = round(total, 2)
    # print(final)

    df = pd.read_csv(const_path)
    # print(df)

    count = 0
    result = "["
    for player in final:
        count = count +1
        if count == 11:
            total = player
            count = 0
            result += ',{"Total":"' + str(total) + '"}],['
        else:
            temp = df.loc[df['playerName'] == player, 'proj'].values[0]
            temp = round(temp, 2)
            last = result[-1]
            if not last == "[":
                result += ","
            result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    result = result[:-2]
    print(result)

    return result


def mlboptimizeFD(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "MLBslateFD.csv")
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
    optimizer = MLBFanduel(num_lineups=int(num),
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


def mlbrun_fanduel(*players, num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "MLBslateFD.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = mlboptimizeFD(num)

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
    e = players.__len__() * 100
    # print(final)

    df = pd.read_csv(const_path)
    count = 0
    result = "["
    for player in final:
        count = count + 1
        if count == 10:
            total = player
            count = 0
            result += ',{"Total":"' + str(total) + '"}],['
        else:
            temp = df.loc[df['playerName'] == player, 'proj'].values[0]
            temp = round(temp, 2)
            last = result[-1]
            if not last == "[":
                result += ","
            result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    result = result[:-2]
    print(result)
    return result

mlbrun_draftkings(num = 3)
mlbrun_fanduel(num = 3)

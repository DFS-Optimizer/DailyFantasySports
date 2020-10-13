import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
from python.Optimization.Fanduel_optimize import Fanduel as NBAFanduel


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


def optimize(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "slate.csv")
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
    optimizer = NBAFanduel(num_lineups=int(num),
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
    optimizer.save_file(optimizer.header, filled_lineups, show_proj=True)

def run_fanduel(num, player1):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "slate.csv")

    df = pd.read_csv(const_path)
    df.loc[df['playerName'] == player1, 'proj'] = df['proj']+100
    df.to_csv(const_path)

    optimize(num)



run_fanduel(5, 'Miye Oni')












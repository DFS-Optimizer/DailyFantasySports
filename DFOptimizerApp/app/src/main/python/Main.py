import datetime
import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
import slates.dk
import slates.fd
from Optimization.NBAFanduel import Fanduel as NBAFanduel
from Optimization.NBADraftKings import Draftkings as NBADraftKings
from Optimization.NFLFanduel import Fanduel as NFLFanduel
from Optimization.NFLDraftKings import Draftkings as NFLDraftKings
from Optimization.MLBFanduel import Fanduel as MLBFanduel
from Optimization.MLBDraftKings import Draftkings as MLBDraftKings
from flask import Flask
from flask_apscheduler import APScheduler



app = Flask(__name__)
app.config["DEBUG"] = True


# test function for job done every day at same time
# def job():
#     slates.dk.updatenflDKslate()


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


@app.route("/")
def hello_world():
    return "Hello Flask"


# url routes for draftkings nfl lineups


@app.route("/dk/nfl/<num>")
def nflchoice0(num):
    lineup = nflrun_draftkings(num = num)
    return lineup

@app.route("/dk/nfl/<player1>/<num>")
def nflchoice1(player1, num):
    lineup = nflrun_draftkings(player1, num = num)
    return lineup

@app.route("/dk/nfl/<player1>/<player2>/<num>")
def nflchoice2(player1, player2, num):
    lineup = nflrun_draftkings(player1, player2, num = num)
    return lineup

@app.route("/dk/nfl/<player1>/<player2>/<player3>/<num>")
def nflchoice3(player1, player2, player3, num):
    lineup = nflrun_draftkings(player1, player2, player3, num = num)
    return lineup

@app.route("/dk/nfl/<player1>/<player2>/<player3>/<player4>/<num>")
def nflchoice4(player1, player2, player3, player4,num):
    lineup = nflrun_draftkings(player1, player2, player3, player4, num = num)
    return lineup


# url routes for fanduel nfl lineups


@app.route("/fd/nfl/<num>")
def fdnflchoice0(num):
    lineup =  nflrun_fanduel(num = num)
    return lineup

@app.route("/fd/nfl/<player1>/<num>")
def fdnflchoice1(player1, num):
    lineup =  nflrun_fanduel(player1, num = num)
    return lineup

@app.route("/fd/nfl/<player1>/<player2>/<num>")
def fdnflchoice2(player1, player2,num):
    lineup =  nflrun_fanduel(player1, player2, num = num)
    return lineup

@app.route("/fd/nfl/<player1>/<player2>/<player3>/<num>")
def fdnflchoice3(player1, player2, player3,num):
    lineup =  nflrun_fanduel(player1, player2, player3, num = num)
    return lineup

@app.route("/fd/nfl/<player1>/<player2>/<player3>/<player4>/<num>")
def fdnflchoice4(player1, player2, player3, player4,num):
    lineup = nflrun_fanduel(player1, player2, player3, player4, num = num)
    return lineup


# url routes for nba draftkings lineups


@app.route("/dk/nba/<num>")
def dkuser_choice0(num):
    lineup = nbarun_draftkings(num = num)
    return lineup


@app.route("/dk/nba/<player1>/<num>")
def dkuser_choice1(player1,num):
    lineup = nbarun_draftkings(player1, num = num)
    return lineup


@app.route("/dk/nba/<player1>/<player2>/<num>")
def dkuser_choice2(player1, player2, num):
    lineup = nbarun_draftkings(player1, player2, num = num)
    return lineup


@app.route("/dk/nba/<player1>/<player2>/<player3>/<num>")
def dkuser_choice3(player1, player2, player3,num):
    lineup = nbarun_draftkings(player1, player2, player3, num = num)
    return lineup


@app.route("/dk/nba/<player1>/<player2>/<player3>/<player4>/<num>")
def dkuser_choice4(player1, player2, player3, player4,num):
    lineup = nbarun_draftkings(player1, player2, player3, player4, num = num)
    return lineup


# url routes for fanduel nba lineups


@app.route("/fd/nba/<num>")
def user_choice0(num):
    lineup = nbarun_fanduel(num = num)
    return lineup


@app.route("/fd/nba/<player1>/<num>")
def user_choice1(player1,num):
    lineup = nbarun_fanduel(player1,num = num)
    return lineup


@app.route("/fd/nba/<player1>/<player2>/<num>")
def user_choice2(player1, player2,num):
    lineup = nbarun_fanduel(player1, player2,num = num)
    return lineup


@app.route("/fd/nba/<player1>/<player2>/<player3>/<num>")
def user_choice3(player1, player2, player3,num):
    lineup = nbarun_fanduel(player1, player2, player3,num = num)
    return lineup


@app.route("/fd/nba/<player1>/<player2>/<player3>/<player4>/<num>")
def user_choice4(player1, player2, player3, player4,num):
    lineup = nbarun_fanduel(player1, player2, player3, player4,num = num)
    return lineup




@app.route("/dk/mlb/<num>")
def dkmlb_choice0(num):
    lineup = mlbrun_draftkings(num = num)
    return lineup


@app.route("/dk/mlb/<player1>/<num>")
def dkmlb_choice1(player1,num):
    lineup = mlbrun_draftkings(player1, num = num)
    return lineup


@app.route("/dk/mlb/<player1>/<player2>/<num>")
def dkmlb_choice2(player1, player2, num):
    lineup = mlbrun_draftkings(player1, player2, num = num)
    return lineup


@app.route("/dk/mlb/<player1>/<player2>/<player3>/<num>")
def dkmlb_choice3(player1, player2, player3,num):
    lineup = mlbrun_draftkings(player1, player2, player3, num = num)
    return lineup


@app.route("/dk/mlb/<player1>/<player2>/<player3>/<player4>/<num>")
def dkmlb_choice4(player1, player2, player3, player4,num):
    lineup = mlbrun_draftkings(player1, player2, player3, player4, num = num)
    return lineup






@app.route("/fd/mlb/<num>")
def fdmlbchoice0(num):
    lineup = mlbrun_fanduel( num = num)
    return lineup

@app.route("/fd/mlb/<player1>/<num>")
def fdmlbchoice1(player1, num):
    lineup = mlbrun_fanduel(player1, num = num)
    return lineup

@app.route("/fd/mlb/<player1>/<player2>/<num>")
def fdmlbchoice2(player1, player2,num):
    lineup = mlbrun_fanduel(player1, player2,  num = num)
    return lineup

@app.route("/fd/mlb/<player1>/<player2>/<player3>/<num>")
def fdmlbchoice3(player1, player2, player3,num):
    lineup = mlbrun_fanduel(player1, player2, player3, num = num)
    return lineup

@app.route("/fd/mlb/<player1>/<player2>/<player3>/<player4>/<num>")
def fdmlbchoice4(player1, player2, player3, player4,num):
    lineup = mlbrun_fanduel(player1, player2, player3, player4, num = num)
    return lineup





# url routes for grabbing updated slates when front end picks the sports

@app.route("/dk/nfl/getslate")
def get_slate_nfl_dk():
    if slates.dk.update_nfl_DK_slate() == 0:
        return "slate unavailable"
    else:
        # slates.dk.update_nfl_DK_slate()
        path = get_my_path()
        path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
        const_path = os.path.join(path, "slates", "NFLslateDK.csv")
        df = pd.read_csv(const_path)
        result = "["
        for index, row in df.iterrows():
            result += '{"player":"' + str(row['playerName']) + '","Salary":"' + str(
                row['sal']) + '","Position":"' + str(row['pos']) + '","Team":"' + str(
                row['team']) + '","Opponent":"' + str(row['opp']) + '","Projection":"' + str(row['proj']) + '"},'
        result = result[:-1]
        result += "]"
        return result


@app.route("/dk/nba/getslate")
def get_slate_nba_dk():
    if slates.dk.update_nba_DK_slate() == 0:
        return "slate unavailable"
    else:
        slates.dk.update_nba_DK_slate()
        path = get_my_path()
        path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
        const_path = os.path.join(path, "slates", "NBAslateDK.csv")
        df = pd.read_csv(const_path)
        result = "["
        for index, row in df.iterrows():
            result += '{"player":"' + str(row['playerName']) + '","Salary":"' + str(
                row['sal']) + '","Position":"' + str(row['pos']) + '","Team":"' + str(
                row['team']) + '","Opponent":"' + str(row['opp']) + '","Projection":"' + str(row['proj']) + '"},'
        result = result[:-1]
        result += "]"
        return result

@app.route("/dk/mlb/getslate")
def get_slate_mlb_dk():
    if slates.dk.update_mlb_DK_slate() == 0:
        return "slate unavailable"
    else:
        slates.dk.update_mlb_DK_slate()
        path = get_my_path()
        path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
        const_path = os.path.join(path, "slates", "MLBslateDK.csv")
        df = pd.read_csv(const_path)
        result = "["
        for index, row in df.iterrows():
            result += '{"player":"' + str(row['playerName']) + '","Salary":"' + str(
                row['sal']) + '","Position":"' + str(row['pos']) + '","Team":"' + str(
                row['team']) + '","Opponent":"' + str(row['opp']) + '","Projection":"' + str(row['proj']) + '"},'
        result = result[:-1]
        result += "]"
        return result

@app.route("/fd/nfl/getslate")
def get_slate_nfl_fd():
    if slates.fd.update_nfl_FD_slate() == 0:
        return "slate unavailable"
    else:
        # slates.dk.update_nfl_DK_slate()
        path = get_my_path()
        path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
        const_path = os.path.join(path, "slates", "NFLslateFD.csv")
        df = pd.read_csv(const_path)
        result = "["
        for index, row in df.iterrows():
            result += '{"player":"' + str(row['playerName']) + '","Salary":"' + str(
                row['sal']) + '","Position":"' + str(row['pos']) + '","Team":"' + str(
                row['team']) + '","Opponent":"' + str(row['opp']) + '","Projection":"' + str(row['proj']) + '"},'
        result = result[:-1]
        result += "]"
        return result

@app.route("/fd/nba/getslate")
def get_slate_nba_fd():
    return "slate unavailable"

@app.route("/fd/mlb/getslate")
def get_slate_mlb_fd():
    return "slate unavailable"



# Optimization code section
# 
# 
# 
# 



def nbaoptimizeFD(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslateFD.csv")
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
    # optimizer.save_file(optimizer.header, filled_lineups, show_proj=True)
    return filled_lineups


def nbarun_fanduel(*players,num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslateFD.csv")
    print(const_path)
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nbaoptimizeFD(num)

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)

    e = players.__len__() * 100

    df = pd.read_csv(const_path)
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
    return result


def nbaoptimizeDK(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslate.csv")
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
    optimizer = NBADraftKings(num_lineups=int(num),
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


def nbarun_draftkings(*players,num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslate.csv")
    print(const_path)
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nbaoptimizeDK(num)

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
    e = players.__len__() * 100
    print(final)

    df = pd.read_csv(const_path)
    df = pd.read_csv(const_path)
    count = 0
    result = "["
    for player in final:
        count = count + 1
        if count == 9:
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

    return result


def nfloptimizeDK(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDK.csv")
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


def nflrun_draftkings(*players,num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateDK.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    # df.to_csv(const_path, index=False)

    lineup = nfloptimizeDK(num)

    # df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    # df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
    e = players.__len__() * 100
    # print(final)

    # df = pd.read_csv(const_path)
    count = 0
    result = "["
    for player in final:
        count = count + 1
        if count == 10:
            total = player
            count = 0
            result += ',{"Total":"' + str(total) + '"}'
        else:
            temp = df.loc[df['playerName'] == player, 'proj'].values[0]
            temp = round(temp, 2)
            last = result[-1]
            if not last == "[":
                result += ","
            result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    # result = result[:-1]
    result+="]"

    return result


def nfloptimizeFD(num):
    # get the path to the slate
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateFD.csv")
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


def nflrun_fanduel(*players,num):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateFD.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    # df.to_csv(const_path, index=False)

    lineup = nfloptimizeFD(num)

    # df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    # df.to_csv(const_path, index=False)

    final = []
    for sublist in lineup:
        for item in sublist:
            final.append(item)
    e = players.__len__() * 100
    print(final)

    # df = pd.read_csv(const_path)
    count = 0
    result = "["
    for player in final:
        count = count + 1
        if count == 10:
            total = player
            count = 0
            result += ',{"Total":"' + str(total) + '"}'
        else:
            temp = df.loc[df['playerName'] == player, 'proj'].values[0]
            temp = round(temp, 2)
            last = result[-1]
            if not last == "[":
                result += ","
            result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    # result = result[:-2]
    result += "]"

    return result

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
    optimizer = MLBDraftKings(num_lineups=int(num),
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
    # result = result[:-2]
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




if __name__ == '__main__':
    # scheduler = APScheduler()
    # scheduler.add_job(func=job, trigger='interval', id='job', hours=1)
    # scheduler.start()
    app.run(host='0.0.0.0')


# str(players.get(player, 0))

import datetime
import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
import slates.dk
from Optimization.NBAFanduel import Fanduel as NBAFanduel
from Optimization.NBADraftKings import Draftkings as NBADraftKings
from Optimization.NFLFanduel import Fanduel as NFLFanduel
from Optimization.NFLDraftKings import Draftkings as NFLDraftKings
from flask import Flask
from flask_apscheduler import APScheduler



app = Flask(__name__)
app.config["DEBUG"] = True


# test function for job done every day at same time
# def job():
#     slates.dk.updatenflDKslate()



@app.route("/")
def hello_world():
    return "Hello Flask"


# url routes for draftkings nfl lineups


@app.route("/dk/nfl")
def nflchoice0():
    lineup = "run nfl draftkings function"
    return lineup

@app.route("/dk/nfl/<player1>")
def nflchoice1():
    lineup = "run nfl draftkings function"
    return lineup

@app.route("/dk/nfl/<player1>/<player2>")
def nflchoice2():
    lineup = "run nfl draftkings function here"
    return lineup

@app.route("/dk/nfl/<player1>/<player2>/<player3>")
def nflchoice3():
    lineup = "run nfl draftkings function here"
    return lineup

@app.route("/dk/nfl/<player1>/<player2>/<player3>/<player4>")
def nflchoice4():
    lineup = "run nfl draftkings function here"
    return lineup


# url routes for fanduel nfl lineups


@app.route("/fd/nfl")
def fdnflchoice0():
    lineup = "run nfl fanduel function"
    return lineup

@app.route("/fd/nfl/<player1>")
def fdnflchoice1():
    lineup = "run nfl fanduel function"
    return lineup

@app.route("/fd/nfl/<player1>/<player2>")
def fdnflchoice2():
    lineup = "run nfl fanduel function here"
    return lineup

@app.route("/fd/nfl/<player1>/<player2>/<player3>")
def fdnflchoice3():
    lineup = "run nfl fanduel function here"
    return lineup

@app.route("/fd/nfl/<player1>/<player2>/<player3>/<player4>")
def fdnflchoice4():
    lineup = "run nfl fanduel function here"
    return lineup


# url routes for nba draftkings lineups


@app.route("/dk/nba")
def dkuser_choice0():
    lineup = "run nba draftkings lineups"
    return lineup


@app.route("/dk/nba/<player1>")
def dkuser_choice1(player1):
    lineup = "run nba draftkings lineups"
    return lineup


@app.route("/dk/nba/<player1>/<player2>")
def dkuser_choice2(player1, player2):
    lineup = "run nba draftkings lineups"
    return lineup


@app.route("/dk/nba/<player1>/<player2>/<player3>")
def dkuser_choice3(player1, player2, player3):
    lineup = "run nba draftkings lineups"
    return lineup


@app.route("/dk/nba/<player1>/<player2>/<player3>/<player4>")
def dkuser_choice4(player1, player2, player3, player4):
    lineup = "run nba draftkings lineups"
    return lineup


# url routes for fanduel nba lineups


@app.route("/fd/nba")
def user_choice0():
    lineup = nbarun_fanduel()
    return lineup


@app.route("/fd/nba/<player1>")
def user_choice1(player1):
    lineup = nbarun_fanduel(player1)
    return lineup


@app.route("/fd/nba/<player1>/<player2>")
def user_choice2(player1, player2):
    lineup = nbarun_fanduel(player1, player2)
    return lineup


@app.route("/fd/nba/<player1>/<player2>/<player3>")
def user_choice3(player1, player2, player3):
    lineup = nbarun_fanduel(player1, player2, player3)
    return lineup


@app.route("/fd/nba/<player1>/<player2>/<player3>/<player4>")
def user_choice4(player1, player2, player3, player4):
    lineup = nbarun_fanduel(player1, player2, player3, player4)
    return lineup



# url routes for grabbing updated slates when front end picks the sports
@app.route("dk/nfl/getslate")
def get_slate_nfl_dk():
    slates.dk.updatenflDKslate()


def get_my_path():
    try:
        filename = __file__  # where we were when the module was loaded
    except NameError:  # fallback
        filename = inspect.getsourcefile(get_my_path)
    return os.path.realpath(filename)


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


def nbarun_fanduel(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslateFD.csv")
    print(const_path)
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nbaoptimizeFD(1)

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


def nbaoptimizeDK(num):
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


def nbarun_draftkings(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NBAslateFD.csv")
    print(const_path)
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nbaoptimizeDK(1)

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


def nfloptimizeDK(num):
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


def nflrun_draftkings(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateFD.csv")
    print(const_path)
    # out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] + 100
    df.to_csv(const_path, index=False)

    lineup = nfloptimizeDK(1)

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


def nflrun_fanduel(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "NFLslateFD.csv")
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






if __name__ == '__main__':
    # scheduler = APScheduler()
    # scheduler.add_job(func=job, trigger='interval', id='job', hours=1)
    # scheduler.start()
    app.run(host='0.0.0.0')


# str(players.get(player, 0))

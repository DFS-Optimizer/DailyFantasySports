import pulp
import inspect
import os
import functools
import pandas as pd
import sys
import csv
from Optimization.Fanduel_optimize import Fanduel as NBAFanduel
from flask import Flask

app = Flask(__name__)
app.config["DEBUG"] = True

@app.route("/")
def hello_world():
    return "Hello Flask"

@app.route("/selectplayer")
def user_choice0():
   lineup = run_fanduel()
   return lineup

@app.route("/selectplayer/<player1>")
def user_choice1(player1):
   lineup = run_fanduel(player1)
   return lineup

@app.route("/selectplayer/<player1>/<player2>")
def user_choice2(player1, player2):
   lineup = run_fanduel(player1, player2)
   return lineup

@app.route("/selectplayer/<player1>/<player2>/<player3>")
def user_choice3(player1, player2, player3):
   lineup = run_fanduel(player1, player2, player3)
   return lineup
    
@app.route("/selectplayer/<player1>/<player2>/<player3>/<player4>")
def user_choice4(player1, player2, player3, player4):
   lineup = run_fanduel(player1, player2, player3, player4)
   return lineup


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
    #optimizer.save_file(optimizer.header, filled_lineups, show_proj=True)
    return filled_lineups



def run_fanduel(*players):
    path = get_my_path()
    path = functools.reduce(lambda x, f: f(x), [os.path.dirname] * 1, path)
    const_path = os.path.join(path, "slates", "slate.csv")
    print(const_path)
    out_path = os.path.join(path, "slates", "output_fanduel.csv")

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj']+100
    df.to_csv(const_path, index= False)

    lineup = optimize(1)

    df = pd.read_csv(const_path)
    for player in players:
        df.loc[df['playerName'] == player, 'proj'] = df['proj'] - 100
    df.to_csv(const_path,index= False)

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
        temp =round(temp, 2)
        if not result == "[":
            result += ","
        result += '{"player":"' + player + '","score":"' + str(temp) + '"}'
    result += ',{"Total":"' + str(total) + '"}]'
    #print(result)

    return result


# run_fanduel('Miye Oni', 'Sindarius Thornwell')

if __name__ == '__main__':
    app.run(host='0.0.0.0')
# run_fanduel('Miye Oni', 'Sindarius Thornwell')

#str(players.get(player, 0))

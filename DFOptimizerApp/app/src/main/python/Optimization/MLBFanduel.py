import pulp
from Optimization.MLBOptimizer import Optimizer


class Fanduel(Optimizer):
    """
    DK Optimizer Settings
    DK will inherit from the super class Optimizer
    """

    def __init__(self, num_lineups, overlap, solver, players_filepath, output_filepath):
        super().__init__(num_lineups, overlap, solver, players_filepath, output_filepath)
        self.salary_cap = 35000
        self.header = ['P', 'C/1B', '2B', 'SS', '3B', 'OF', 'OF', 'OF', 'UTIL']

    def type_1(self, lineups):
        """
        Sets up the pulp LP problem, adds all of the constraints and solves for the maximum value for each generated lineup.

        Returns a single lineup (i.e all of the players either set to 0 or 1) indicating if a player was included in a lineup or not.
        """
        # define the pulp object problem
        prob = pulp.LpProblem('NFL', pulp.LpMaximize)

        # define the player variables
        players_lineup = [pulp.LpVariable("player_{}".format(i + 1), cat="Binary") for i in range(self.num_players)]

        # add the max player constraints
        prob += (pulp.lpSum(players_lineup[i] for i in range(self.num_players)) == 9)

        # add the positional constraints
        prob += (1 <= pulp.lpSum(self.positions['P'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['P'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (0 <= pulp.lpSum(self.positions['C'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['C'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (0 <= pulp.lpSum(self.positions['1B'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['1B'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (1 <= pulp.lpSum(self.positions['2B'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['2B'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (1 <= pulp.lpSum(self.positions['SS'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['SS'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (1 <= pulp.lpSum(self.positions['3B'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['3B'][i] * players_lineup[i] for i in range(self.num_players)) <= 2)
        prob += (3 <= pulp.lpSum(self.positions['OF'][i] * players_lineup[i] for i in range(self.num_players)))
        prob += (pulp.lpSum(self.positions['OF'][i] * players_lineup[i] for i in range(self.num_players)) <= 4)



        # add the salary constraint
        prob += ((pulp.lpSum(self.players_df.loc[i, 'sal'] * players_lineup[i] for i in range(self.num_players))) <= self.salary_cap)

        # at least 3 teams for the 8 players and no more than 4 players  on the same team constraints
        used_team =[pulp.LpVariable("u{}".format(i+1), cat="Binary") for i in range(self.num_teams)]
        for i in range(self.num_teams):
            prob += (used_team[i] <= (pulp.lpSum(self.players_teams[k][i] * players_lineup[k] for k in range(self.num_players))))
            prob += (pulp.lpSum(self.players_teams[k][i] * players_lineup[k] for k in range(self.num_players))  <= 4 * used_team[i])
        prob += (pulp.lpSum(used_team[i] for i in range(self.num_teams)) >= 3)

        # variance constraints - each lineup can't have more than the num overlap of any combination of players in any previous lineups
        for i in range(len(lineups)):
            prob += (pulp.lpSum(lineups[i][k] * players_lineup[k] for k in range(self.num_players)) <= self.overlap)

        # add the objective
        prob += pulp.lpSum((pulp.lpSum(self.players_df.loc[i, 'proj'] * players_lineup[i] for i in range(self.num_players))))

        # solve the problem
        status = prob.solve(self.solver)

        # check if the optimizer found an optimal solution
        if status != pulp.LpStatusOptimal:
            print('Only {} feasible lineups produced'.format(len(lineups)), '\n')
            return None

        # Puts the output of one lineup into a format that will be used later
        lineup_copy = []
        for i in range(self.num_players):
            if 0.9 <= players_lineup[i].varValue <= 1.1:
                lineup_copy.append(1)
            else:
                lineup_copy.append(0)
        return lineup_copy

    def fill_lineups(self, lineups):
        """
        Takes in the lineups with 1's and 0's indicating if the player is used in a lineup.
        Matches the player in the dataframe and replaces the value with their name.
        Adds up projected points and actual points (if provided) to save to each lineup.
        """
        filled_lineups = []
        for lineup in lineups:
            a_lineup = ["", "", "", "", "", "", "", "", ""]
            players_lineup = lineup[:self.num_players]
            total_proj = 0
            if self.actuals:
                total_actual = 0
            for num, player in enumerate(players_lineup):
                if 0.9 < player < 1.1:
                    if self.positions['P'][num] == 1:
                        if a_lineup[0] == "":
                            a_lineup[0] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['C'][num] == 1:
                        if a_lineup[1] == "":
                            a_lineup[1] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['1B'][num] == 1:
                        if a_lineup[1] == "":
                            a_lineup[1] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['2B'][num] == 1:
                        if a_lineup[2] == "":
                            a_lineup[2] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['SS'][num] == 1:
                        if a_lineup[3] == "":
                            a_lineup[3] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['3B'][num] == 1:
                        if a_lineup[4] == "":
                            a_lineup[4] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    elif self.positions['OF'][num] == 1:
                        if a_lineup[5] == "":
                            a_lineup[5] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[6] == "":
                            a_lineup[6] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[7] == "":
                            a_lineup[7] = self.players_df.loc[num, 'playerName']
                        elif a_lineup[8] == "":
                            a_lineup[8] = self.players_df.loc[num, 'playerName']
                    if self.players_df.loc[num, 'proj'] > 90:
                        total_proj += self.players_df.loc[num, 'proj'] - 100
                    else:
                        total_proj += self.players_df.loc[num, 'proj']
            a_lineup.append(round(total_proj, 2))
            filled_lineups.append(a_lineup)
            print(filled_lineups)
        return filled_lineups

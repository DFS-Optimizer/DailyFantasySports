from python.Database.query import SqlQuery
from python.constant import constant


class Player:

    def __init__(self, id, role):
        self.id = id
        self.role = role
        self.season_stats = []
        self.past_n = []
        self.prev_game = None
        self.game_count = 0

    def update_game(self, game):
        self.game_count += 1
        self.prev_game = game[0]
        self.season_stats = [(x + y) / 2 for x, y in zip(self.season_stats, game[1:])]
        self.past_n = [(x + y) / 2 for x, y in zip(self.past_n, game[1:])]

    def get_stats(self):
        stats = self.past_n + self.season_stats
        self.past_n = []
        self.game_count -= 1
        return stats


def past_n(n):

    with SqlQuery() as query:
        season = query.get_table("basketball_player_game_basic", args="WHERE season_id = 212018 ORDER BY date ASC")
        print("got table")
        players_table = query.get_table("player_role", args="WHERE season_id = 212018 ORDER BY player_id ASC")
        print("got players")

    players = [Player(players_table[i][0], players_table[i][1]) for i in range(len(players_table))]   # makes list of player objects
    game_avgs = []
    for game in season:
        player = constant.binary_search(players, game[1])
        if player is not None:   # if player exists
            if player.game_count == n:        # if game is target game
                game_avgs.append(list(game[4:14]) + player.get_stats())
            else:
                player.update_game(game[4:14])   # update player games if not target game
    #
    # for game in game_avgs:
    #     print(game)


past_n(6)
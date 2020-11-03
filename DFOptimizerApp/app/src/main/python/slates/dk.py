from draft_kings.data import Sport
from draft_kings.client import contests
from draft_kings.client import available_players
from draft_kings.client import draftables
from draft_kings.client import draft_group_details

contests = contests(sport=Sport.NFL)

for label in contests["contests"]:
    if not str(label["starts_at"]).startswith("2020-11-08"):
        continue
    print(label["starts_at"])
    id = label["draft_group_id"]
    print(id+1)
    break


# print(available_players(draft_group_id=id))
print(draftables(id))
# print(draft_group_details(id))

# print(available_players(draft_group_id=11513))
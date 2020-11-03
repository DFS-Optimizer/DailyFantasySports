from draft_kings.data import Sport
from draft_kings.client import contests
from draft_kings.client import available_players


contests = contests(sport=Sport.NFL)

for label in contests["contests"]:
    if not str(label["starts_at"]).startswith("2020-11-01"):
        continue
    id = label["draft_group_id"]
    break
print(id)

print(available_players(draft_group_id=id))

# print(available_players(draft_group_id=11513))
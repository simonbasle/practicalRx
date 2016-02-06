# Step 6 - Combine
Start from branch `solution/step5`, solution is in branch `solution/step6`.

## Operators (by difficulty)
Simple: `concat`

Intermediate: `merge`

Advanced: `zip`

## Migrate Services
 - `StatService.getAllStats`
	- for each `User`
		- retrieve his hashrate
		- retrieve how many coins he mined
		- combine (zip) both and make a `UserStat`
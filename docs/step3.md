# Step 3 - Filter
Start from branch `solution/step2`, solution is in branch `solution/step3`.

## Migrate Services
 - UserService
    - `findAll()`  for now naively adapted (`Observable.from`)
    - compose on `findAll` for `getUser` / `getUserByLogin`
 - SearchService

## Useful Operators
 - `filter`
 - `take`
 - `flatMap` to asynchronously retrieve additional data needed for filter
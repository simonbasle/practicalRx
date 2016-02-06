# Step 007 - Live & Let Die (and Retry)
Start from branch `solution/step6`, solution is in branch `solution/step7`.

## The Problem
`StatService.lastBlockFoundBy` has an intermittent bug that causes it to crash with an `IndexOutOfBoundsException`.

We'd like to retry the call when this happens to prevent this error.

## The Plan
Make the method observable:

  - Have the index randomly generated inside an `Observable.defer`.
  - Have the generated index logged in a `doOnNext`.
  - Chain in a `flatMap` that calls `UserService.findAll()` and picks the user via `elementAt(i)`.

Migrate code that uses this method as before. It still randomly fails.

Make it automatically retry by chaining in the `retry()` operator.

Edit the `PoolController.lastBlock()` method so it doesn't catch and recover.
Execute test `PoolControllerTest.testLastBlock()` and verifies it succeeds, sometimes printing
several "ELECTED" messages (the retry in action).

## Useful Operators
* `defer`
* `flatMap`
* `elementAt`
* `retry`
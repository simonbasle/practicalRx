# Step 1 - Simple Creation
Start from branch `master`, solution is in branch `solution/step1`. You can look at the history
of the solution to see changes in the services and implied modifications separately.


## Migrate Services
 - AdminService
 - CoinService
 - HashrateService
 - PoolService

## Useful Operators
 - `Observable.just`
 - `Observable.from`
 - `Observable.create` (try it on PoolService.connect)
 - `map`

## How to easily (and naively) make things compile?
By blocking on the `Observable<T>`:

 - first chain a `toList()` if expected result is a `List<T>`
 - chain `toBlocking()`
 - collect the result (or throw an `Exception`) with various operators
    - `take(n)`
    - `single()`
    - `first()`
    - `last()`
    - `???orDefault(T defaultValue)`
    
A cleaner solution (going async all the way) will be presented later.
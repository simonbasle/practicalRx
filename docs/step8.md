# Step 8 - Chain
Start from branch `solution/step7`, solution is in branch `solution/step8`.

## Migrate Services
 - ExchangeRateService
	- calls two external APIs: doge-to-dollar rate and currency-to-currency exchange rate
	- two chained REST calls
	- combination of the two gives doge-to-anyCurrency

## Useful Operators
 - `Observable.create`
	- wrap the REST call on each subscription
	- the `HttpStatusCodeException` indicate error inside external API, transform into `DogePoolException` but forward the `message` and keep the `status code`.
	- the other `RestClientException` most probably indicate a timeout, wrap them into `DogePoolException` as well, with `418` status code.

 - `zipWith`
	- similar to `zip` but called from stream A directly instead of statically
	- combine both rates to get the final one

# NEW REQUIREMENTS
> the free exchange rate API crashes too often, make it so we switch to an alternative non-free API when it’s the case
>
> as a bonus, track costs of these non-free calls
>
>-- the Product Owner

## The Plan
**AdminService**:
add method+variable to track cost for this month

**ExchangeRateService**:
add a rate retrieval method similar to current one,
but use endpoint from `exchange.nonfree.api.baseUrl`

**upon error**
switch thanks to `OnErrorResumeNext`

**use side effects**
to log when we switch & track the cost
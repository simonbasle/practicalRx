# Step 9 - Clean-up Controllers
Start from branch `solution/step9pre`, solution is in branch `solution/step9`.

The starting branch changes the tests to account for the code migrating to a fully asynchronous one.

## Asynchronous Response
**prepare a `DeferredResult<T>`**
that will be returned without blocking.
Default status is `200 - OK` on a success, use a `DeferredResult<ResponseEntity<T>>` to customize that.

**subscribe on the `Observable`**
to inject results or errors into the `DeferredResult`.

* `onNext`: inject `T` via `setResult`
* `onError`: create a `DogePoolException` and inject it via `setErrorResult`

## Testing
- make sure that the endpoint is **asynchronous**
	- perform the request using `mockMvc`, but finish with a `andReturn()` to get a `MvcResult`
	- assert `status().isOk()` and `request().asyncStarted()`

- **trigger** the async processing using the previously acquired *mvcResult*
	- `mockMvc.perform(asyncDispatch(mvcResult))`

- from this point **assert as before** on the status, content, etc...

## Migrate Services
 - AdminController *(simple)*
 - IndexController *(trickier)*
	- use `zip`, `flatMap`, `single`… to detect bad users
 - UserProfileController *(also trickier)*

## A Way to Generalize this
using `ReturnValueHandler` and a simple adapter of `Observable` to `DeferredResult`, you wouldn't need to make code changes everywhere.

each time an `Observable` is returned, it’ll be converted into this adapter
> `WebAsyncUtils.getAsyncManager(...).startDeferredResultProcessing(...)`
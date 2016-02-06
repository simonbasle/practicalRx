# Practical RxJava Workshop
# README
This project is the skeleton (and solution) for the `PracticalRxjava` workshop.

The aim of the workshop is to discover [`RxJava`](http://reactivex.io) through a practical example, a legacy application that we want to gradually migrate to a fully **asynchronous** and **reactive** app.

The application is a `REST API`, based on `Spring Boot` and layered into `Controllers` and `Services`. Services call themselves, a database and even `external REST APIs`.

> /!\ This project should only be cloned for the duration of the workshop. It can (and will) be recreated from [upstream](http://github.com/simonbasle/practicalRxOrigin) if feedback warrants it or new features are brought into the workshop. **Don't submit issues or PR here** but rather in the upstream [practicalRxOrigin repo](http://github.com/simonbasle/practicalRxOrigin).

### You are now doing [step1](docs/step1.md)

## Pre-requisites
 * The workshop application assumes there is a machine running the [`practicalRxExternal` project](https://github.com/simonbasle/practicalRxExternal) (usually the workshop's presenter).

 > **PracticalRxExternal**: a fake set of "external REST APIs" called by the legacy application. Some of these are unreliable and will simulate timeouts and delays.

 * The workshop can also make use of a [Couchbase database](http://www.couchbase.com).

 > `User`s can be stored as JSON in a Couchbase database, a key/value and document-oriented NoSQL database for which a fully reactive [Java SDK](http://github.com/couchbase/couchbase-java-client) exists.

 * Developers will need to copy and edit the configuration files
   * Copy `application.main` into `/src/main/resources/application.properties`
   * Copy `application.test` into `/src/test/resources/applications.properties`
   * Edit the copied files. Git will ignore them when switching branches.

 > There's a property to **change the IP** on which both the fake external APIs and the database will be running (usualy the workshop's presenter).
 >
 > IPs can also independently be modified (eg. if the attendee wants to run the database on his own machine).
 >
 > The need for a database can be deactivated (two `User`s will be created in memory).

## Running the Workshop
This is a `Maven` project, developed and tested with the `IntelliJ` IDE.

The REST API is fully functional and can be played with (eg. with a REST client like POSTMAN) by running the `Main` class.

Controllers are unit tested (see `/src/test/java`) and attendees are encouraged to run tests from within the IDE to check that their modifications at each step are correct.

> **Step 9** makes a deep change to the Controllers so **tests will change at this point**. One can checkout branch `step9pre` to see both the solution to step 8 and the new version of the tests in preparation for step 9.

Each step is described in the ad hoc `stepX.md` document under `/docs`.

One can **catch up if late or peek at the solution of each step** in its associated branch (eg. branch `solution/step2` contains the solution to step #2 described in `/docs/steps2.md` and can be used as a starting point for step #3).
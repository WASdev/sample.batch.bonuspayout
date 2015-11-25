# Building and running with Maven

This sample features a somewhat complete automation using [Apache Maven](http://maven.apache.org/) to set up and execute a small set of integration tests against a Liberty server.

In addition to compiling and packaging the Java application into a WAR, the Maven automation performs all these tasks:

- Downloads and installs the latest version of WebSphere Liberty
- Downloads additional features such as ***batchManagement-1.0*** required by this sample
- Creates the application database tables used by the sample
- Copies the WAR into the server config
- Executes the integration tests and reports results

In addition, it is designed to allow for [use in a WDT environment][wdt] and the Maven environment at the same time.

The [Liberty maven plugin][liberty-maven-plugin] is used to install Liberty, add the required features, and start and stop the server during the integration tests.

## Running with maven

### From top-level

Run everything:  Create a new Liberty install in default location, create new application tables, run tests

```bash
[sample.batch.bonuspayout]$ mvn clean install
```

Reuse Liberty install in default location, create new application tables, run tests

```bash
[sample.batch.bonuspayout]$ mvn    # Same thing as 'mvn install'
```

### From batch-bonuspayout-application

Create a new Liberty install in default location, run tests
```bash
[batch-bonuspayout-application]$ mvn clean install
```

Reuse Liberty install in default location, run tests
```bash
[batch-bonuspayout-application]$ mvn install
```

Install needed features in existing Liberty install, run tests
```bash
[batch-bonuspayout-application]$ mvn install -Dwlp.install.dir=/my/path/to/wlp
```

Just build and package WAR, but skip all the server-related install and testing
```bash
[batch-bonuspayout-application]$ mvn install -DskipTests
```

Just start the server, using pre-existing Liberty install
```bash
[batch-bonuspayout-application]$ mvn liberty:start-server -Dwlp.install.dir=/my/path/to/wlp
```

Just stop the server, using default Liberty install
```bash
[batch-bonuspayout-application]$ mvn liberty:stop-server

Just run the integration tests, using pre-existing Liberty install
```bash
[batch-bonuspayout-application]$ mvn failsafe:integration-test -Dwlp.install.dir=/my/path/to/wlp
```

#### Other configuration

The sample relies on these two properties which can be overridden on the command line:

```bash
[batch-bonuspayout-application]$ mvn -DserverHost=my.domain.com -DhttpsPort=9444 
```
(Defaults: ***serverHost*** => **localhost**, ***httpsPort*** => **9443**)

### From batch-bonuspayout-wlpcfg

Clear server logs, workarea, create new application tables

```bash
[batch-bonuspayout-wlpcfg]$ mvn clean install
```

Clear server logs and workarea, reuse app tables

```bash
[batch-bonuspayout-wlpcfg]$ mvn clean install -DreuseDB  
```
## General warning

One problem with starting and stopping servers during Maven execution is that there are some gaps where test failure can leave the system in a state where we're not ready to attempt the next execution.   

In general, this can be dealt with simply by stopping the *BonusPayout* server, either using **mvn liberty:stop-server** or any other mechanism.

## Using Maven with WDT

Follow for [more info][wdt-maven-notes] on organizing the project to support WDT publish and deploy within Maven build

## Links

* Jump to [main page](/README.md)
* [Using WDT][wdt]
* [Using Maven with the app published by WDT][wdt-maven-notes]
* The [Liberty maven plugin][liberty-maven-plugin]

[wdt]: /docs/Using-WDT.md
[wdt-maven-notes]: /docs/Using-Maven-With-WDT-Published-App.md
[liberty-maven-plugin]: https://github.com/WASdev/ci.maven


# Building and running with Maven

This sample features a fairly complete automation using [Apache Maven](http://maven.apache.org/) to set up and execute a small set of integration tests against a Liberty server.

In addition to compiling and packaging the Java application into a WAR, the Maven automation performs all these tasks:

- Downloads and installs the latest version of WebSphere Liberty
- Downloads additional features such as ***batchManagement-1.0*** required by this sample
- Creates the application database tables used by the sample
- Copies the WAR into the server config
- Executes the integration tests and reports results

In addition, it is designed to allow for [use in a WDT environment][wdt] and the Maven environment at the same time.

The [Liberty maven plugin](https://github.com/WASdev/ci.maven) is used to install Liberty, add the required features, and start and stop the server during the integration tests.

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

## Links

[Back](../README.md) to main page.
[Using-WDT][wdt]

[wdt]: docs/Using-WDT.md


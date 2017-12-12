# Building and running with Maven

This sample provides a full-featured [Apache Maven](http://maven.apache.org/) integration to set up and execute a small set of integration tests against a Liberty server.

In addition to compiling and packaging the Java application into a WAR, the Maven automation performs all these tasks:

- Downloads and installs the latest version of WebSphere Liberty
- Downloads additional features such as ***batchManagement-1.0*** required by this sample
- Creates the application database tables used by the sample
- Copies the WAR into the server config
- Executes the integration tests and reports results

The [Liberty maven plugin](https://github.com/WASdev/ci.maven) is used to install Liberty, add the required features, and start and stop the server during the integration tests.   

In addition, it is designed to allow for [use in a WebSphere Developer Tools (WDT) environment](/docs/Using-WDT.md) and the Maven environment at the same time.

## Running with maven

### From top-level

Everything:  Build app, create a new Liberty install in default location, create new application tables, run tests
```bash
$ mvn clean install
```

Same thing as 'mvn install'
```bash
$ mvn    
```

Same (will even install needed features) but use an existing Liberty install 
```bash
$ mvn install -DwlpInstallDir="C:\WLPS\a17003\wlp"
```

Same but use a non-default database location (a DB external to the sample can decouple the lifecycle).
* ***Note***:  The default database location is: **target/liberty/wlp/usr/shared/BatchDB**

```bash
$ mvn -Ddb.url=C:/dbs/bp1   # Forward slashes work even on Windows (where Derby slash/path issues can arise)
```

Existing Liberty install plus non-default database location lifecycle).
* ***Note***:  The default database location is: ***$wlpInstallDir*/usr/shared/BatchDB**

```bash
$ mvn -DwlpInstallDir="C:\WLPS\a17003\wlp" -Ddb.url=C:/dbs/bp2   
```


Does everything including install features, create tables, except for actually running the tests 
```bash
$ mvn install -DskipTests
```

Package server  
* ***Note***:  This only includes the DB if the tables have been created in a previous execution.  And the **db.url** will be an absolute path, not necessarily useful wherever you unpackage it.

```bash
$ mvn package   
```


#### Running the tests      

Assuming the **mvn install** has already been executed against a given install, you can simply (re)run the tests through a sequence like below.
* ***Note***: You can combine other config like **-DwlpInstallDir=wlp**, **-Ddb.url=url** in each

Just start the server
```bash
$ mvn liberty:start-server 
```

Just run the integration tests
```bash
$ mvn failsafe:integration-test
```

Just stop the server


```bash
$ mvn liberty:stop-server
```



### Cleaning

Clear server logs, workarea, create new application tables

```bash
$ mvn clean install
```

Clear server logs and workarea, reuse app tables

```bash
$ mvn clean install -DreuseDB  
```

#### Cleaning runtime tables

This sample application uses the job instance id (typically generated from the runtime tables) in the primary key used to persist application data into the application table.   So if a job instance id is going to be reused within the lifetime of working with this sample, the application table will first need to be "cleaned" (which is why we have the drop of the application tables integrated into `mvn clean` above).  

On the other hand there is no particular need to clean the runtime tables, whether you are cleaning the application tables or not, since the id generated will typically always be a valid one.

In the above configurations, if you use the default database location *and* the WLP installation (in the project **target** directory), the runtime tables will be cleaned during a `mvn clean`.  If you use either a non-default WLP install or a non-default DB URL, the runtime tables will not be affected by `mvn clean`.
 

## General warning

One problem with starting and stopping servers during Maven execution is that there are some gaps where test failure can leave the system in a state where we're not ready to attempt the next execution.   

In general, this can be dealt with simply by stopping the *BonusPayout* server, either using **mvn liberty:stop-server** or any other mechanism.

## Links

* Jump to [main page](/README.md)
* On to [Using WDT](/docs/Using-WDT.md)
* The [Liberty maven plugin](https://github.com/WASdev/ci.maven)


# Bonus Payout in detail

## Level of included code

This sample assumes the December 2014 beta driver level (both the main **runtime** and the **extended** JARs).  However instructions are
only given using the server packaged with this sample, not using an existing Liberty installation of the December 2014 beta.

## Application Overview

The **BonusPayout** job is structured in 3 steps:   

1. The first step, **generate**, is a batchlet step which generates some random values (representing account balances), and writes them into a text file in CSV format.

1. The second step, **addBonus**, is a chunk step which reads these values from the text file, adds an amount representing a 'bonus' to each record, and writes the updated value into a database table.

1. The third step, **validation**, is a chunk step which loops through the database table updated in step 2 as well as the text file generated in step 1 validating the calculation in the second step.
For each record, in confirms that the value now read from the database table corresponds to the value in the generated text file, plus the bonus amount.  It also confirms that steps 1 and 2 have written the same number of records. 

## Building and deploying the sample with Maven

Both of the two options immediately following use the pre-packaged server archive at [Liberty/BonusPayoutServer.jar](Liberty/BonusPayoutServer.jar) to supply the Liberty installation binaries
as well as the server config [server.xml](Liberty/src/test/resources/server.xml).

This does all of:

  1. Compile the Java classes and zip up the WAR package
  1. Create a new Liberty server (from the packaged server)
  1. Deploy the newly-built WAR to the new server

### Default server name and location
``` 
    $ mvn clean install
```

* Note this creates a server named **BonusPayout** with new Liberty installation directly under the Git repository at location:  **Liberty\target\liberty\wlp**

### Specified server name and location
``` 
    $ mvn clean install -Dserver.name=MyBonusPayout -Dinstall.dir=/my/path/toinstallation.dir
```

* Note this creates a server named **MyBonusPayout** with new Liberty installation at location:  **/my/path/toinstallation.dir**

### TODO 

Not shown is how to create a new server and deploy starting from an existing Liberty installation (rather than one created from the packaged server)


## Creating the database tables

1. To create the runtime tables, see this link
[Runtime DDL templates] (https://github.com/WASdev/sample.batch.templateddls)
for the latest.  A local copy is included in this repository at
[batch-derby.ddl](Liberty/src/test/resources/batch-derby.ddl)

2. To create the application table, **BONUSPAYOUT.ACCOUNT** used in the 2nd and 3rd steps, use DDLs in the 
[BonusPayout/src/main/webapp/resources] (BonusPayout/src/main/webapp/resources/) directory, e.g. 
[bonusPayout.derby.ddl] (BonusPayout/src/main/webapp/resources/bonusPayout.derby.ddl) directory 

## Job Parameters - detailed look

 Parameter | Default | Description 
 :------------- | :----| :-----------
numRecords  | **1000** | Total number of records generated in step 1 and processed later
chunkSize | **100** | The chunk (transaction) size used in steps 2 and 3
generateFileNameRoot | *\<None\>*  |  Directory+prefix of file generated in step 1.  No default defined in JSL, but there is a default in the Java logic.
dsJNDI | **java:comp/env/jdbc/BonusPayoutDS** | DataSource JNDI location for application table (the sample could be refactored so this isn't also used for container persistence as it is right now).
bonusAmount | **100** |	Amount the “account balance” will be incremented by in step 2
tableName | **BONUSPAYOUT.ACCOUNT** | Application database table
fileEncoding | *\<None\>* | Char encoding used to write text file generated in step 1 and read in step 3

## Important BonusPayout flows/constructs

### What is the logic flow of this application?

#### Note:
One thing to keep in mind looking at the **BonusPayout** source is that we intend to provide a couple variants 
of essentially the same application.   For example, we will likely next publish a variant which uses a 
partitioned third step.  Some other variants would include different techniques for performing the DB query
in the third step, and a variant using CDI as well.

With this in mind, some of the constructs are structured to allow for later inheritance/extension.  In some cases
the logic could be captured a bit more compactly if this were not the goal, so do keep this in mind in case it looks
like the sample is unnecessarily complex.

### Some key constructs:

#### The number of records validated 

As part of the validation performed in **validate**, the app checks that the number of records read from the DB (written in the 2nd step), is equal to value of the *numRecords* job parameter.  
We leverage the checkpoint capabilities of the batch chunk step by ensuring the validation count will pick up where it left off in case of restart.

In more detail: 
- We use the step's persistent user data to hold this count of validated records.
- We update this count with the listener ValidationCountUpdatingWriteListener, updating the persistent user data (and also the exit status) with the updated count value.
- Finally, at the end of the step we use listener ValidationCountAfterStepListener to compare the count from the exit status (and persistent user data) with the original *numRecords* value.
- **Note:**  We decouple the reader checkpoint logic from this flow by not trying to also directly use the persistent user data in checkpointing (even though the values may be the same after each checkpoint).

#### Mechanism for DB query in the validate step

In this sample we issue a separate query (and get a separate ResultSet) per chunk.  We use the StepContext transient user data to pass query parameters from the reader to the
ChunkListener, and we also use the transient user data to pass the ResultSet back from the ChunkListener to the reader.  

The ItemReader stores values into the transient user data both in*open()* as well as in each *checkpointInfo()* invocation.  The ChunkListener's *beforeChunk()*  executes the query, and sets the ResultSet into the transient user data.  The ItemReader's *readItem()* then iterates through this chunk's ResultSet.


# Change History

## December 2014 beta

* Application name changed from **--applicationName BonusPayout** to **--applicationName BonusPayout-1.0** on jbatch **submit** command.
* Refactored job from EJB built with WDT batch tooling to Maven WAR
* Renamed application DB table to BONUSPAYOUT.ACCOUNT
* Changed default for *dsJNDI* String to use a **java:comp/env** lookup with resource reference (and removed *useGlobalJNDI* parameter).  This parameter value can be a global one or one of the supported, standard scopes such as **java:comp/env/**
* Adjusted defaulting of *generateFileNameRoot* in **BonusPayoutUtils.java**


# Bonus Payout in detail

The [BonusPayoutJob.xml](BonusPayout/src/main/webapp/WEB-INF/classes/META-INF/batch-jobs/BonusPayoutJob.xml) is structured in 3 steps:   

1. The first step, **generate**, is a batchlet step which generates some random values (representing account balances), and writes them into a text file in CSV format.

1. The second step, **addBonus**, is a chunk step which reads these values from the text file, adds an amount representing a 'bonus' to each record, and writes the updated value into a database table.

1. The third step, **validation**, is a chunk step which loops through the database table updated in step 2 as well as the text file generated in step 1 validating the calculation in the second step.
For each record, in confirms that the value now read from the database table corresponds to the value in the generated text file, plus the bonus amount.  It also confirms that steps 1 and 2 have written the same number of records. 
## SimpleBonusPayoutJob - A simplified sample

We provide a simplified job definition [SimpleBonusPayoutJob.xml](BonusPayout/src/main/webapp/WEB-INF/classes/META-INF/batch-jobs/SimpleBonusPayoutJob.xml) which only includes the first two steps of the BonusPayoutJob.

Though the third step, **validation**, of **BonusPayoutJob** provides a way to force a failure and demonstrate restart, and makes the job more interesting overall, it does make the sample signficantly more complicated.

If you just want to see a simple example of a chunk step without as much application detail to filter through, the SimpleBonusPayoutJob may be a better place to start.

Note there is no separate application for SimpleBonusPayoutJob, it's just another JSL within the same WAR file as BonusPayoutJob, which would be submitted via commands like:

    ```
    $ ./wlp/bin/batchManager submit --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --applicationName=BonusPayout-1.0 --jobXMLName=SimpleBonusPayoutJob --jobPropertiesFile=$WLP_USER_DIR/shared/resources/runToCompletionParms.txt --wait --pollingInterval_s=2 
    ```

## Application database tables

The application table, **BONUSPAYOUT.ACCOUNT**, used in the 2nd and 3rd steps, is defined by the DDL in the 
[batch-bonuspayout-wlpcfg/shared/resources/ddls](batch-bonuspayout-wlpcfg/shared/resources/ddls) directory, e.g.  [BonusPayout.derby.ddl](batch-bonuspayout-wlpcfg/shared/resources/ddls/BonusPayout.derby.ddl)


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

### CDI integration

The sample shows that a CDI bean can be injected into a batch artifact.

The ***GenerateDataBatchlet*** class contains a field ***AccountType acctType*** with a setter injection marked with the @PriorityAccount qualifier, defined within this sample (and note that ***PreferredAccountType*** is annotated with this qualifier). 

    public class GenerateDataBatchlet implements Batchlet, BonusPayoutConstants {
    ...

    /*
     * For CDI version of sample this will be injectable.
     */
    private AccountType acctType = new CheckingAccountType();

    /*
     * Included for CDI version of sample.
     */
    @Inject
    public void setAccountType(@PriorityAccount AccountType acctType) {
        this.acctType = acctType;
    }


Note that while the included server configuration uses:

    <feature>cdi-1.2</feature>


This feature may be removed and the application will be restarted, after which it will continue to work.

You can see this by doing (from the directory that contains your 'wlp' install directory)

1.  submit job with predefined server config (including cdi-1.2)

    ```
    ./wlp/bin/batchManager submit  ....
    ```

2. Look in messages.log (to see ***account code = PREF***) from ***PreferredAccountType***

    ```
    $ tail $WLP_USER_DIR/servers/BonusPayout/logs/messages.log
    ...
    ...
    [5/23/15 18:45:46:633 EDT] 0000001e BonusPayout   I In GenerateDataBatchlet, using account code = PREF
   ```
    
3. Edit server.xml, comment out ***cdi-1.2*** feature, save, and wait for app to be restarted

    ```
    $ tail $WLP_USER_DIR/servers/BonusPayout/logs/messages.log
    ...
    [5/23/15 22:20:46:306 EDT] 00000028 com.ibm.ws.app.manager.AppMessageHelper    A CWWKZ0003I: The application BonusPayout-1.0 updated in 0.081 seconds.
    ```

4.  submit job again

    ```
    ./wlp/bin/batchManager submit  ....
    ```

5. Look in messages.log again (to see ***account code = CHK***) from ***CheckingAccountType***

    ```
    $ tail $WLP_USER_DIR/servers/BonusPayout/logs/messages.log
    ...
    ...
    [5/23/15 22:21:27:546 EDT] 00000033 BonusPayout   I In GenerateDataBatchlet, using account code = CHK
    ```


# Change History

## November 2015, (for maven-liberty-plugin 1.1)

* Another major restructure to leverage the maven-liberty-plugin to install Liberty and needed features, and to better integrate Maven and WDT
* Added default file root to generated CSV file names.

## August 2015, (for 8.5.5.6 GA)

* Restructured project to match [https://github.com/WASdev/sample.async.websocket
* Abandoned the pre-packaged server 


## May 2015 beta

* Refactored the Maven automation to point to an existing WLP installation rather than the one pre-packaged within the sample
* Added the SimpleBonusPayoutJob
* Provided CDI-enabled injection of AccountType into the GenerateDataBatchlet

## December 2014 beta

* Application name changed from **--applicationName BonusPayout** to **--applicationName BonusPayout-1.0** on jbatch **submit** command.
* Refactored job from EJB built with WDT batch tooling to Maven WAR
* Renamed application DB table to BONUSPAYOUT.ACCOUNT
* Changed default for *dsJNDI* String to use a **java:comp/env** lookup with resource reference (and removed *useGlobalJNDI* parameter).  This parameter value can be a global one or one of the supported, standard scopes such as **java:comp/env/**
* Adjusted defaulting of *generateFileNameRoot* in **BonusPayoutUtils.java**

## Links

* Jump to [main page](/README.md)

# sample.batch.bonuspayout

This sample shows the javaBatch-1.0 and managedBatch-1.0 features on the WebSphere Liberty Profile

## Quick Start - Install and run the sample

These instructions assume you're running from the directory which holds your 'wlp' installation directory

1. Run the self-extracting sample archive:
    ```
    $ java -jar BonusPayoutServer.jar
    ```

2. Start the BonusPayout server:
    ```
    $ wlp/bin/server start BonusPayout
    ```

3. Point to server keystore
    ```
    $ export JVM_ARGS=-Djavax.net.ssl.trustStore=wlp/usr/servers/BonusPayout/resources/security/key.jks
    ```

4. Submit (start) job, and wait for it to run to completion
    ```
    $ wlp/bin/jbatch submit --host=localhost --port=9443  --user=bob --password=bobpwd --applicationName=BonusPayout --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/servers/BonusPayout/runToCompletionParms.txt  --wait
    [2014/09/15 21:57:57.767 -0400] CWWKY0101I: Job BonusPayout with instance ID 1 has been submitted.
    [2014/09/15 21:58:27.871 -0400] CWWKY0105I: Job BonusPayout with instance ID 1 has finished. Batch status: COMPLETED. Exit status: COMPLETED
    [2014/09/15 21:58:27.875 -0400] CWWKY0107I: JobExecution:executionId=1,jobName=BonusPayout,createTime=2014/09/15 21:57:57.676 -0400,startTime=2014/09/15 21:57:57.684 -0400,endTime=2014/09/15 21:57:58.535 -0400,lastUpdatedTime=2014/09/15 21:57:58.535 -0400,batchStatus=COMPLETED,exitStatus=COMPLETED,jobParameters={generateFileNameRoot=bonusPayoutGen}
    ```

## Application Overview

The **BonusPayout** job is structured in 3 steps:   

1. The first step, **generate**, is a batchlet step which generates some random values (representing account balances), and writes them into a text file in CSV format.

1. The second step, **addBonus**, is a chunk step which reads these values from the text file, adds an amount representing a 'bonus' to each record, and writes the updated value into a database table.

1. The third step, **validation**, is a chunk step which loops through the database table updated in step 2 as well as the text file generated in step 1 validating the calculation in the second step.
For each record, in confirms that the value now read from the database table corresponds to the value in the generated text file, plus the bonus amount.  It also confirms that steps 1 and 2 have written the same number of records. 


## Other execution variants 
### Restart - run the variant forcing failure, then performing a restart

1. Submit job using "force failure" parameters, (notice it ends with **FAILED** status).

    ```
    $ wlp/bin/jbatch submit --host=localhost --port=9443  --user=bob --password=bobpwd --applicationName=BonusPayout --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/servers/BonusPayout/forceFailureParms.txt --wait 
    [2014/09/15 22:37:44.966 -0400] CWWKY0101I: Job BonusPayout with instance ID 2 has been submitted.
    [2014/09/15 22:38:15.025 -0400] CWWKY0105I: Job BonusPayout with instance ID 2 has finished. Batch status: FAILED. Exit status: FAILED
    [2014/09/15 22:38:15.029 -0400] CWWKY0107I: JobExecution:executionId=2,jobName=BonusPayout,createTime=2014/09/15 22:37:44.896 -0400,startTime=2014/09/15 22:37:44.906 -0400,endTime=2014/09/15 22:37:45.635 -0400,lastUpdatedTime=2014/09/15 22:37:45.635 -0400,batchStatus=FAILED,exitStatus=FAILED,jobParameters={forceFailure=500, generateFileNameRoot=bonusPayoutGen}
    ```

2. Restart job, this time it will run to **COMPLETED** status.
    ```
    $ wlp/bin/jbatch restart --host=localhost --port=9443  --user=bob --password=bobpwd --jobInstanceId=2 --jobPropertiesFile=wlp/usr/servers/BonusPayout/forceFailureParms.txt --wait
    [2014/09/15 22:38:53.174 -0400] CWWKY0102I: A restart request has been submitted for job BonusPayout with instance ID 2.
    [2014/09/15 22:39:23.222 -0400] CWWKY0105I: Job BonusPayout with instance ID 2 has finished. Batch status: COMPLETED. Exit status: COMPLETED
    [2014/09/15 22:39:23.225 -0400] CWWKY0107I: JobExecution:executionId=3,jobName=BonusPayout,createTime=2014/09/15 22:38:53.079 -0400,startTime=2014/09/15 22:38:53.083 -0400,endTime=2014/09/15 22:38:53.213 -0400,lastUpdatedTime=2014/09/15 22:38:53.213 -0400,batchStatus=COMPLETED,exitStatus=COMPLETED,jobParameters={forceFailure=500, generateFileNameRoot=bonusPayoutGen}
    ```

### Status - run without waiting, check status

1. Submit job without wait (use 'lotsOfRecords.txt' parameters file with 50K records)
    ```
    $ wlp/bin/jbatch submit --host=localhost --port=9443  --user=bob --password=bobpwd --applicationName=BonusPayout --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/servers/BonusPayout/lotsOfRecords.txt
    [2014/09/15 22:50:55.706 -0400] CWWKY0101I: Job BonusPayout with instance ID 8 has been submitted.
    ```
    
2. Check status (**STARTED** is shown, you might see different results depending on timing, of course).
    ```
    $ wlp/bin/jbatch status --host=localhost --port=9443  --user=bob --password=bobpwd --jobInstanceId=8
    [2014/09/15 22:51:02.860 -0400] CWWKY0106I: JobInstance:instanceId=8,jobName=BonusPayout,appName=BonusPayout,appTag=bob
    [2014/09/15 22:51:02.866 -0400] CWWKY0107I: JobExecution:executionId=9,jobName=BonusPayout,createTime=2014/09/15 22:50:55.631 -0400,startTime=2014/09/15 22:50:55.634 -0400,endTime=,lastUpdatedTime=2014/09/15 22:50:55.634 -0400,batchStatus=STARTED,exitStatus=,jobParameters={numRecords=50000, generateFileNameRoot=bonusPayoutGen}
    ```

2. Check status again (now it's **COMPLETED**)
    ```
    $ wlp/bin/jbatch status --host=localhost --port=9443  --user=bob --password=bobpwd --jobInstanceId=8
    [2014/09/15 22:51:20.181 -0400] CWWKY0106I: JobInstance:instanceId=8,jobName=BonusPayout,appName=BonusPayout,appTag=bob
    [2014/09/15 22:51:20.186 -0400] CWWKY0107I: JobExecution:executionId=9,jobName=BonusPayout,createTime=2014/09/15 22:50:55.631 -0400,startTime=2014/09/15 22:50:55.634 -0400,endTime=2014/09/15 22:51:03.204 -0400,lastUpdatedTime=2014/09/15 22:51:03.204 -0400,batchStatus=COMPLETED,exitStatus=COMPLETED,jobParameters={numRecords=50000, generateFileNameRoot=bonusPayoutGen}
    ```

## Building the sample from source using WDT

### Define the server environment
1. Windows->Preferences, Server->Runtime Environments -> Add 
2. Type of runtime enviroment: IBM -> WebSphere Application Server Liberty Profile -> Next
3. Installation Folder -> New Directory (Any path), Also use any Java 7+ JRE, Next
4. Install a new runtime environment from an archive (point to BonusPayoutServer.jar)
5. Skip through "Install Add-Ons" 
6. Accept License then Finish

### Import POJO project and build EAR

1. Import **BonusPayout** project into Eclipse via *Import->Existing Projects into Workspace*
    - **Note:**  If you have compile failures, then the server environment was probably not defined correctly
2. Right click the **BonusPayout** project, select **Generate->Java Batch Packaging Code**
3. Export BonusPayoutControllerEAR project via Export -> EAR file 
    - **Important:** The file name MUST be exported with the name of **BonusPayoutControllerEAR.ear** exactly as shown.


## Deeper look at Application

### Job Parameters - detailed look

 Parameter | Default | Description 
 :------------- | :----| :-----------
numRecords  | **1000** | Total number of records generated in step 1 and processed later
chunkSize | **100** | The chunk (transaction) size used in steps 2 and 3
generateFileNameRoot | **/tmp/bpgen ** |  Directory+prefix of file generated in step 1 (more detail below)
dsJNDI | **jdbc/batch** | DataSource JNDI location for application table (not necessarily for container persistence as well)
bonusAmount | **100** |	Amount the “account balance” will be incremented by in step 2
tableName | **SAMPLE.ACCOUNT** | Application database table
fileEncoding | **<None>** | Char encoding used to write text file generated in step 1 and read in step 3
useGlobalJNDI | **true** | If set to **true**, look up **dsJNDI** name in the global JNDI namespace.  Otherwise, lookup DataSource at location *java:/comp/env/<**dsJNDI**>*






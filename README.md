# sample.batch.bonuspayout

This sample makes use of the javaBatch-1.0 and managedBatch-1.0 features on the WebSphere Liberty Profile.

It involves a three-step job, which generates some random data, performs a calculation on it writing the
result into the database, and then verifies the result.

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

## Further reading

For a deeper look at the application look at:
[ApplicationDetails.md](ApplicationDetails.md)



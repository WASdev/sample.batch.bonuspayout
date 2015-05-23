# sample.batch.bonuspayout

This sample makes use of the ***batch-1.0*** and ***batchManagement-1.0*** features of the WebSphere Liberty Profile.

It involves a three-step job, which generates some random data, performs a calculation on it (writing the result into the database), and then verifies the result.

It uses the *batchManager* command-line utility to conveniently wrapper the REST-based remote dispatch and management interface.

This document simply shows how to execute the sample, and the internal details of the application (including how to 
modify, build, etc.) are described here: [ApplicationDetails.md](ApplicationDetails.md)


## Install Option #1 - Extract prepackaged server with sample app


These instructions assume you're running from the directory which holds your 'wlp' installation directory

* Run the self-extracting sample archive (and accept all license agreements, etc.):

    ```
    $ java -jar <git repository home>/PrePackaged/BonusPayoutServer.jar    
    ```

## Install Option #2 - Install with Maven to your WLP install location

1. Install with Maven

   ```
   mvn -Dinstall.dir=/usr/websphere clean install
   ```

This will create a server at directory:   ***/usr/websphere/wlp/usr/servers/BonusPayout***


## Run the sample

2. Start the BonusPayout server:

   ```
   $ ./wlp/bin/server start BonusPayout
   ```

3. Submit (start) job, and wait for it to run to completion
    ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --applicationName=BonusPayout-1.0 --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/shared/resources/runToCompletionParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 12:49:17.041 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 31 has been submitted.
   [2015/05/23 12:49:17.042 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":31,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   [2015/05/23 12:49:19.159 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 31 has finished. Batch status: COMPLETED. Exit status: COMPLETED
   [2015/05/23 12:49:19.161 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":28,"instanceId":31,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 12:49:16.942 -0400","endTime":"2015/05/23 12:49:18.062 -0400","lastUpdatedTime":"2015/05/23 12:49:18.062 -0400","startTime":"2015/05/23 12:49:17.021 -0400","jobParameters":{"generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.31\\execution.28\\","stepExecutions":[{"stepExecutionId":130,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/generate"},{"stepExecutionId":131,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/addBonus"},{"stepExecutionId":132,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"1000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/validation"}]}
    ```

## Other execution variants 
### Restart - run the variant forcing failure, then performing a restart

1. Submit job using "force failure" parameters, (notice it ends with **FAILED** status).

   ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --applicationName=BonusPayout-1.0 --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/shared/resources/forceFailureParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 13:13:25.650 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 33 has been submitted.
   [2015/05/23 13:13:25.652 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":33,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   [2015/05/23 13:13:27.715 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 33 has finished. Batch status: FAILED. Exit status: FAILED
   [2015/05/23 13:13:27.716 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":33,"instanceId":33,"batchStatus":"FAILED","exitStatus":"FAILED","createTime":"2015/05/23 13:13:25.611 -0400","endTime":"2015/05/23 13:13:26.316 -0400","lastUpdatedTime":"2015/05/23 13:13:26.316 -0400","startTime":"2015/05/23 13:13:25.627 -0400","jobParameters":{"forceFailure":"500","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.33\\execution.33\\","stepExecutions":[{"stepExecutionId":139,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/generate"},{"stepExecutionId":140,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/addBonus"},{"stepExecutionId":141,"stepName":"validation","batchStatus":"FAILED","exitStatus":"500","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/validation"}]}
   ```

2. Restart job, this time it will run to **COMPLETED** status.

   ```
   $ ./wlp/bin/batchManager restart --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=33 --jobPropertiesFile=wlp/usr/shared/resources/forceFailureParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 13:13:38.283 -0400] CWWKY0102I: A restart request has been submitted for job BonusPayoutJob with instance ID 33.
   [2015/05/23 13:13:38.285 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":33,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"FAILED","jobXMLName":"BonusPayoutJob","instanceState":"FAILED"}
   [2015/05/23 13:13:40.352 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 33 has finished. Batch status: COMPLETED. Exit status: COMPLETED
   [2015/05/23 13:13:40.353 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":34,"instanceId":33,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 13:13:38.256 -0400","endTime":"2015/05/23 13:13:38.513 -0400","lastUpdatedTime":"2015/05/23 13:13:38.513 -0400","startTime":"2015/05/23 13:13:38.271 -0400","jobParameters":{"forceFailure":"500","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.33\\execution.34\\","stepExecutions":[{"stepExecutionId":142,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"1000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/34/stepexecutions/validation"}]}
   ```

### Status - run without waiting, check status

1. Submit job without wait (use 'lotsOfRecords.txt' parameters file with 50K records)

   ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443   --trustSslCertificates --user=bob --password=bobpwd --applicationName=BonusPayout-1.0 --jobXMLName=BonusPayoutJob --jobPropertiesFile=wlp/usr/shared/resources/lotsOfRecords.txt
   [2015/05/23 13:18:29.237 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 35 has been submitted.
   [2015/05/23 13:18:29.239 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   ```
    
2. Check status (**STARTED** is shown, you might see different results depending on timing, of course).  Also, you might have a different instance ID than 35 (see result of the **submit**).

   ```
   $  ./wlp/bin/batchManager status --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=35
   [2015/05/23 13:18:33.050 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"STARTED","jobXMLName":"BonusPayoutJob","instanceState":"DISPATCHED"}
   [2015/05/23 13:18:33.052 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":36,"instanceId":35,"batchStatus":"STARTED","exitStatus":"","createTime":"2015/05/23 13:18:29.199 -0400","endTime":"","lastUpdatedTime":"2015/05/23 13:18:33.003 -0400","startTime":"2015/05/23 13:18:29.211 -0400","jobParameters":{"numRecords":"100000","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.35\\execution.36\\","stepExecutions":[{"stepExecutionId":146,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/generate"},{"stepExecutionId":147,"stepName":"addBonus","batchStatus":"STARTED","exitStatus":"","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/addBonus"}]}
   ```

2. Check status again (now it's **COMPLETED**)

   ```
   $  ./wlp/bin/batchManager status --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=35
   [2015/05/23 13:18:50.940 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"BonusPayout-1.0#BonusPayout-1.0.war","submitter":"bob","batchStatus":"COMPLETED","jobXMLName":"BonusPayoutJob","instanceState":"COMPLETED"}
   [2015/05/23 13:18:50.941 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":36,"instanceId":35,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 13:18:29.199 -0400","endTime":"2015/05/23 13:18:43.978 -0400","lastUpdatedTime":"2015/05/23 13:18:43.978 -0400","startTime":"2015/05/23 13:18:29.211 -0400","jobParameters":{"numRecords":"100000","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.35\\execution.36\\","stepExecutions":[{"stepExecutionId":146,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/generate"},{"stepExecutionId":147,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/addBonus"},{"stepExecutionId":148,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"100000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/validation"}]}
   ```

## Further reading

For a deeper look at the application look at:
[ApplicationDetails.md](ApplicationDetails.md)


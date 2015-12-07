* Control the server [directly from the command line](#running-on-the-command-line) for manual testing.
* Use [maven](/docs/Maven-integration.md) to control and manipulate the server for use in automated builds to support continuous integration.
* Use [Eclipse and WDT](/docs/Using-WDT.md) for development in an IDE with live, incremental updates to your application without restarting the server

# Running on the command line

These instructions assume you're running from the directory which holds your 'wlp' installation directory.

## Start the server

Based on the server directory generated earlier, use the following to start the server and run the application:

```bash
$ export WLP_USER_DIR=/path/to/sample.batch.bonuspayout/batch-bonuspayout-wlpcfg
$ ./wlp/bin/server start BonusPayout
```

* `run` runs the server in the foreground.
* `start` runs the server in the background. Look in the logs directory for console.log to see what's going on, e.g.

```bash
$ tail -f $WLP_USER_DIR/servers/BonusPayout/logs/console.log
```

## Run the sample


3. Submit (start) job, and wait for it to run to completion

    ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --applicationName=batch-bonuspayout-application --jobXMLName=BonusPayoutJob --jobPropertiesFile=$WLP_USER_DIR/shared/resources/runToCompletionParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 12:49:17.041 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 31 has been submitted.
   [2015/05/23 12:49:17.042 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":31,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   [2015/05/23 12:49:19.159 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 31 has finished. Batch status: COMPLETED. Exit status: COMPLETED
   [2015/05/23 12:49:19.161 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":28,"instanceId":31,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 12:49:16.942 -0400","endTime":"2015/05/23 12:49:18.062 -0400","lastUpdatedTime":"2015/05/23 12:49:18.062 -0400","startTime":"2015/05/23 12:49:17.021 -0400","jobParameters":{"generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.31\\execution.28\\","stepExecutions":[{"stepExecutionId":130,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/generate"},{"stepExecutionId":131,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/addBonus"},{"stepExecutionId":132,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"1000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/28/stepexecutions/validation"}]}
    ```

## Other execution variants 
### Restart - run the variant forcing failure, then performing a restart

1. Submit job using "force failure" parameters, (notice it ends with **FAILED** status).

   ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --applicationName=batch-bonuspayout-application --jobXMLName=BonusPayoutJob --jobPropertiesFile=$WLP_USER_DIR/shared/resources/forceFailureParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 13:13:25.650 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 33 has been submitted.
   [2015/05/23 13:13:25.652 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":33,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   [2015/05/23 13:13:27.715 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 33 has finished. Batch status: FAILED. Exit status: FAILED
   [2015/05/23 13:13:27.716 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":33,"instanceId":33,"batchStatus":"FAILED","exitStatus":"FAILED","createTime":"2015/05/23 13:13:25.611 -0400","endTime":"2015/05/23 13:13:26.316 -0400","lastUpdatedTime":"2015/05/23 13:13:26.316 -0400","startTime":"2015/05/23 13:13:25.627 -0400","jobParameters":{"forceFailure":"500","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.33\\execution.33\\","stepExecutions":[{"stepExecutionId":139,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/generate"},{"stepExecutionId":140,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/addBonus"},{"stepExecutionId":141,"stepName":"validation","batchStatus":"FAILED","exitStatus":"500","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/33/stepexecutions/validation"}]}
   ```

2. Restart job, this time it will run to **COMPLETED** status.

   ```
   $ ./wlp/bin/batchManager restart --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=33 --jobPropertiesFile=$WLP_USER_DIR/shared/resources/forceFailureParms.txt --wait --pollingInterval_s=2 
   [2015/05/23 13:13:38.283 -0400] CWWKY0102I: A restart request has been submitted for job BonusPayoutJob with instance ID 33.
   [2015/05/23 13:13:38.285 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":33,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"FAILED","jobXMLName":"BonusPayoutJob","instanceState":"FAILED"}
   [2015/05/23 13:13:40.352 -0400] CWWKY0105I: Job BonusPayoutJob with instance ID 33 has finished. Batch status: COMPLETED. Exit status: COMPLETED
   [2015/05/23 13:13:40.353 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":34,"instanceId":33,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 13:13:38.256 -0400","endTime":"2015/05/23 13:13:38.513 -0400","lastUpdatedTime":"2015/05/23 13:13:38.513 -0400","startTime":"2015/05/23 13:13:38.271 -0400","jobParameters":{"forceFailure":"500","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.33\\execution.34\\","stepExecutions":[{"stepExecutionId":142,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"1000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/34/stepexecutions/validation"}]}
   ```

### Status - run without waiting, check status

1. Submit job without wait (use 'lotsOfRecords.txt' parameters file with 50K records)

   ```
   $ ./wlp/bin/batchManager submit --batchManager=localhost:9443   --trustSslCertificates --user=bob --password=bobpwd --applicationName=batch-bonuspayout-application --jobXMLName=BonusPayoutJob --jobPropertiesFile=$WLP_USER_DIR/shared/resources/lotsOfRecords.txt
   [2015/05/23 13:18:29.237 -0400] CWWKY0101I: Job BonusPayoutJob with instance ID 35 has been submitted.
   [2015/05/23 13:18:29.239 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"STARTING","jobXMLName":"BonusPayoutJob","instanceState":"SUBMITTED"}
   ```
    
2. Check status (**STARTED** is shown, you might see different results depending on timing, of course).  Also, you might have a different instance ID than 35 (see result of the **submit**).

   ```
   $  ./wlp/bin/batchManager status --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=35
   [2015/05/23 13:18:33.050 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"STARTED","jobXMLName":"BonusPayoutJob","instanceState":"DISPATCHED"}
   [2015/05/23 13:18:33.052 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":36,"instanceId":35,"batchStatus":"STARTED","exitStatus":"","createTime":"2015/05/23 13:18:29.199 -0400","endTime":"","lastUpdatedTime":"2015/05/23 13:18:33.003 -0400","startTime":"2015/05/23 13:18:29.211 -0400","jobParameters":{"numRecords":"100000","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.35\\execution.36\\","stepExecutions":[{"stepExecutionId":146,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/generate"},{"stepExecutionId":147,"stepName":"addBonus","batchStatus":"STARTED","exitStatus":"","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/addBonus"}]}
   ```

2. Check status again (now it's **COMPLETED**)

   ```
   $  ./wlp/bin/batchManager status --batchManager=localhost:9443 --trustSslCertificates --user=bob --password=bobpwd --jobInstanceId=35
   [2015/05/23 13:18:50.940 -0400] CWWKY0106I: JobInstance:{"jobName":"BonusPayoutJob","instanceId":35,"appName":"batch-bonuspayout-application#batch-bonuspayout-application.war","submitter":"bob","batchStatus":"COMPLETED","jobXMLName":"BonusPayoutJob","instanceState":"COMPLETED"}
   [2015/05/23 13:18:50.941 -0400] CWWKY0107I: JobExecution:{"jobName":"BonusPayoutJob","executionId":36,"instanceId":35,"batchStatus":"COMPLETED","exitStatus":"COMPLETED","createTime":"2015/05/23 13:18:29.199 -0400","endTime":"2015/05/23 13:18:43.978 -0400","lastUpdatedTime":"2015/05/23 13:18:43.978 -0400","startTime":"2015/05/23 13:18:29.211 -0400","jobParameters":{"numRecords":"100000","generateFileNameRoot":"bonusPayoutGen"},"restUrl":"https://localhost:9443/ibm/api/batch","serverId":"localhost/C:/eclipseWork/libx1/build.image/wlp/usr/BonusPayout","logpath":"C:\\eclipseWork\\libx1\\build.image\\wlp\\usr\\servers\\BonusPayout\\logs\\joblogs\\BonusPayoutJob\\2015-05-23\\instance.35\\execution.36\\","stepExecutions":[{"stepExecutionId":146,"stepName":"generate","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/generate"},{"stepExecutionId":147,"stepName":"addBonus","batchStatus":"COMPLETED","exitStatus":"COMPLETED","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/addBonus"},{"stepExecutionId":148,"stepName":"validation","batchStatus":"COMPLETED","exitStatus":"100000","stepExecution":"https://localhost:9443/ibm/api/batch/jobexecutions/36/stepexecutions/validation"}]}
   ```

## Additional Notes

:star: *Note:* The maven target and Gradle clean task will clean server output (logs and workarea, etc) from the batch-bonuspayout-wlpcfg directory, however, if you wanted to maintain strict separation between what is checked into batch-bonuspayout-wlpcfg and what is generated by a running server, you could also specify the WLP_OUTPUT_DIR environment variable, e.g. into the maven target directory.

```bash
$ export WLP_OUTPUT_DIR=$WLP_USER_DIR/target
```

## Tips

* If you use bash, consider trying the [command line tools](https://github.com/WASdev/util.bash.completion), which provide tab-completion for the server and other commands.

## Links

* Jump to [main page](/README.md)



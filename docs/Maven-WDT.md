# Using Maven and WDT together

Finally we get to our ultimate goal, and the whole point of structuring the project and importing it following the steps described up until now (see [previous instructions][wdt]).

## Modifying the app or server config

You could modify the app in any number of ways:
* You could cause the job to fail.   
* You could cause the job to complete with a different exit status, which would appear in the  
***Java EE Batch Job Logs*** view in Eclipse.   (Search the app for `setExitStatus` to see where it's being set from now).
* You could cause the job to log some additional information which would appear in the server logs (if using `System.out.println` or the job logs (if using **java.util.logging** logger methods). 

### Example sequence  
There's nothing special about the sequences detailed here.  The idea is just to show the flexibility.

#### Work with the app in both Maven & WDT
1. Start the server from WDT 
1. make app change, then save (watch app get updated)
1. Run job in WDT (detailed in [previous instructions][wdt]), and observe new effects)
1. Note WDT's application update **does NOT** re-execute the integration tests.
	* To do so, go to the command-line and do:  
	`$ mvn failsafe:integration-test`
	* **Note:** we're just running this goal from the failsafe plugin, **and NOT** the full `mvn integration-test`
1. Make another change, save.
1. Run the integration tests again (run them first this time). 
1. Run the job in WDT again
1. Etc.....  
  
#### Package via Maven

Say you have iterated through the above, and are happy with your updates and are ready to package and build a new application binary (and server package). 
 
1. Do a full Maven build:
`$ mvn clean install`
	* This will stop the server, wipe out and replace your WLP install with a clean installation, run the integration tests, and produce new binaries.
	
At this point, you could start fresh from "Work with the app in both Maven & WDT" above and iterate through the process again. 

### Changing server configuration

9. server config change

## Further Reading
* [WDT in Eclipse and Maven integration](https://developer.ibm.com/wasdev/blog/2017/06/28/wdt-in-eclipse-and-maven-integration)
* [Building and running Liberty apps with Maven in Eclipse](https://developer.ibm.com/wasdev/docs/building-liberty-apps-maven-in-eclipse/) 

## Links
* Jump to [main page](/README.md)
* Back to [Maven integration](/docs/Maven-integration.md)
* Back to [Using WDT]()


[wdt]: /docs/Using-WDT.md  "Using WDT"
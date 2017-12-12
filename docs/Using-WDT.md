# Eclipse / WDT

The WebSphere Development Tools (WDT) for Eclipse can be used to control the server (start/stop/dump/etc.), it also supports incremental publishing with minimal restarts, working with a debugger to step through your applications, etc.

WDT also provides:

* content-assist for server configuration (a nice to have: server configuration is minimal, but the tools can help you find what you need and identify typos, etc.)
* automatic incremental publish of applications so that you can write and test your changes locally without having to go through a build/publish cycle or restart the server (which is not that big a deal given the server restarts in a few seconds, but still!)

# Installing

Installing WDT on Eclipse is as simple as a drag-and-drop, but the process is explained [on wasdev.net] [wasdev-wdt].

[wasdev-wdt]: https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/

## To install the Java batch tools (an optional feature of WDT):

* In Eclipse, click *Help->Install WebSphere Software*.
* Click *Install* in the ***IBM Java EE Batch*** option then click *Finish*.

## Tested versions:

The latest versions of WDT and the ***Java EE Batch*** tools are  recommended.  This sample was tested at versions:
 * [Eclipse Oxygen 1a](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen1a)
 * WebSphere Developer Tools v17.0.0.2 or above
 * The level of **IBM Java EE Batch Tools for WebSphere Liberty** tested is:  
     * Version: 1.0.251.v20171004_2101

## Other necessary Eclipse plugins (m2e, etc.)

If this is the first time you've ever used Maven from within your Eclipse installation, you may need to install other plugins like m2e (Maven-to-Eclipse).  Probably most install methods would include these anyway as dependencies, but just noting since there are a lot of possible routes to install/update a given Eclipse installation with this full set of pluigns.

# Running project

## Clone Git Repo

If the sample git repository hasn't been cloned yet, WDT has git tools integrated into the IDE:

1.  Open the Git repositories view
    * *Window -> Show View -> Other*
    * Type "git" in the filter box, and select *Git Repositories*
2.  Copy Git repo url:
	* **https://github.com/WASdev/sample.batch.bonuspayout**
3.  In the Git repositories view, select the hyperlink `Clone a Git repository`
4.  The git repo url should already be filled in.  Select *Next -> Next -> Finish*
	* **Note:** the repository does not contain Eclipse project metadata, these are created by Maven import below. 
5.  The "sample.batch.bonuspayout [master]" repo should appear in the view

## Import Maven projects into WDT

**Note:** If you did not use Eclipse/WDT to clone the git repository, begin at step 3, and supply the path of the cloned repository directory in step 4.

1.  In the Git Repository view, expand this bonuspayout repository to see the "Working Directory" folder
2.  Right-click on this folder, and select *Copy path to Clipboard*
3.  Select menu *File -> Import -> Maven -> Existing Maven Projects*
4.  In the Root Directory textbox, Paste in the repository directory and click *Finish*.

5.  This will pop-up a dialog from the WDT Maven integration.
![wdtMavenImport Image](images/wdtMavenImport.png)

7. Select *Yes* (and optionally check *Remember this decision...*).

### Now wait for the WDT-Maven import processing to complete

The import will do a Maven build and will create:

* a Java project for the application
* a Java project representing the Liberty runtime
* a **batch-bonuspayout-application**Runtime environment containing 
	* a **BonusPayout** Liberty server which has published to it:
		* the *batch-bonuspayout-application** application 

####------------------


### Prime

1. needs a mvn integration-test to gen app tables

### Troubleshooting

* JMX publish
* Stale runtime contents issue workaround

### EXPERIMENT - TODO


* How do I change app?
* When does Maven run tests?
* mvn then WDT	
* role of WLP_USER_DIR from shell env?
* just update .war for now, but with SNAPSHOT version # ?
	
####------------------ 


## Run the BonusPayoutJob from WDT !

Finally, execute these steps:

1.  Select the `batch-bonuspayout-application` project
2.  Find the Job definition (XML) in Package Explorer view (or possibly Enterprise Explorer) in folder ***src/main/resources/META-INF/batch-jobs***
3.  Right-click on BonusPayoutJob.xml and select *Run As->Java EE Batch Job*.
4.  Most of the wizard will be pre-populated, except for the userid and password.
5.  Supply the values:
```
 User ID:   bob
 Password:  bobpwd
```
6.  You should see a dialog like the following showing that the job was successfully submitted.
![jobSubmitted Image](images/jobSubmitted.png)  
	* Typically the response will occur quickly enough relative to the job execution that you will see its Batch Status as **STARTED** (though it's possible it could end up **COMPLETED** by then).  
7. Leave the checkbox selected to go to the ***Java EE Batch Job Logs*** view afterwards, refreshing if necessary until the exit status shows as **COMPLETED** (with default job parameters this will happen quickly).





## Links

* Jump to [main page](/README.md)
* [Running with Maven](/docs/Maven-integration.md)



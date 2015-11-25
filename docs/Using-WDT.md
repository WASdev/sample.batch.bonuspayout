## Eclipse / WDT

The WebSphere Development Tools (WDT) for Eclipse can be used to control the server (start/stop/dump/etc.), it also supports incremental publishing with minimal restarts, working with a debugger to step through your applications, etc.

WDT also provides:

* content-assist for server configuration (a nice to have: server configuration is minimal, but the tools can help you find what you need and identify finger-checks, etc.)
* automatic incremental publish of applications so that you can write and test your changes locally without having to go through a build/publish cycle or restart the server (which is not that big a deal given the server restarts lickety-split, but less is more!).

## Installing

Installing WDT on Eclipse is as simple as a drag-and-drop, but the process is explained [on wasdev.net] [wasdev-wdt].

[wasdev-wdt]: https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/

### To install the Java batch tools:

* In Eclipse, click *Help->Install WebSphere Software*.
* Click *Install* in the ***IBM Java EE Batch*** option then click *Finish*.

### Tested versions:

The latest versions of WDT and the ***Java EE Batch*** tools are  recommended.  This sample was tested at versions:
 * [Eclipse Mars SR 1](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/mars1)
 * ***IBM Java EE Batch Tools for WebSphere Liberty Profile***  
     * Version: 1.0.100.v20151015_1628

### m2e installs

If this is the first time you've ever used Maven from within your Eclipse installation, you will probably be prompted somewhere along the way to install some m2e-related Eclipse plugins into your Eclipse installation (some m2e connector).   We don't heavily rely on m2e in particular but it's nice to see the red X's disappear.

## Clone Git Repo

If the sample git repository hasn't been cloned yet, WDT has git tools integrated into the IDE:

1.  Open the Git repositories view
    * *Window -> Show View -> Other*
    * Type "git" in the filter box, and select *Git Repositories*
2.  Copy Git repo url by finding the textbox under "HTTPS clone URL" at the top of this page, and select *Copy to clipboard*
3.  In the Git repositories view, select the hyperlink `Clone a Git repository`
4.  The git repo url should already be filled in.  Select *Next -> Next -> Finish*
5.  The "sample.batch.bonuspayout [master]" repo should appear in the view

### Import Maven projects into WDT

**Note:** If you did not use Eclipse/WDT to clone the git repository, begin at step 3, and supply the path of the cloned repository directory in step 4.

1.  In the Git Repository view, expand the bonuspayout repo to see the "Working Directory" folder
2.  Right-click on this folder, and select *Copy path to Clipboard*
3.  Select menu *File -> Import -> Maven -> Existing Maven Projects*
4.  In the Root Directory textbox, Paste in the repository directory.
5.  Select *Browse...* button.
6. **IMPORTANT:**  Un-select the parent project (*/pom.xml net.wasdev....*)
![mvnImport Image](images/mvnImport.jpg)
7. Select *Finish* now that only 2 checkboxes are selected.
8.  This will create 2 projects in Eclipse: batch-bonuspayout-application, and batch-bonuspayout-wlpcfg.    

## Create a Runtime Environment and a Liberty Server

For the purposes of this sample, we will create the Liberty server (step 3 in the wasdev.net instructions) a little differently to create and customize a Runtime Environment that will allow the server to directly use the configuration in the `batch-bonuspayout-wlpcfg` project.

### Create a Runtime Environment in Eclipse

1. Open the 'Runtime Explorer' view:
    * *Window -> Show View -> Other*
    * type `runtime` in the filter box to find the view (it's under the Server heading).
2. Right-click in the view, and select *New -> Runtime Environment*
3. Give the Runtime environment a name, e.g. `wlp.bp.sample`
4. Either:
    * Select an existing installation, or
        * ***Note:*** If you copy/paste a path into the Path: dialog, and you see a red X complaining about an invalid path to wlp, try hitting <Backspace> which may trigger the dialog to recognize the path in its dropdown.
    * select *Install from an archive or a repository* to download a new Liberty archive.
  
5. Follow the prompts (and possibly choose additional features to install) until you *Finish* creating the Runtime Environment

### Add the User directory from the maven project, and create a server

1. Right-click on the Runtime Environment created above in the 'Runtime Explorer' view, and select *Edit*
2. Click the `Advanced Options...` link
3. If the `batch-bonuspayout-wlpcfg` directory is not listed as a User Directory, we need to add it:
    1. Click New
    2. Select the `batch-bonuspayout-wlpcfg` project
    3. Select *Finish*, *OK*, *Finish*
4. Right-click on the `batch-bonuspayout-wlpcfg` user directory listed under the target Runtime Environment in the Runtime Explorer view, and select *New->Server* (not *New->Liberty Server* which you might have expected).
5. The resulting dialog should be pre-populated with the `BonusPayout` Liberty profile server.
   The default name for this server can vary, you might also opt to rename it from the Right-click menu in the Servers view to make it easier to identify.
6. Click *Finish*

### Publish the application to the new server.  

1. From the ***Servers*** view, right-click the newly-created server and select *Add and Remove*.
2. Select **batch-bonuspayout-application** from the left-hand pane ("Available") and click "Add" so that it ends up on the right-hand pane ("Configured").   Click "Finish".
3. Start the server by right-clicking in the ***Servers*** view and selecting ***Start***. Ignore any warnings or error messages that "Problem Occurred" or "Publishing failed".

### Running Liberty and the sample application from WDT

1.  Select the `batch-bonuspayout-application` project
2.  Find the Job definition (XML) in Package Explorer view (or possibly Enterprise Explorer) in folder ***src/main/java/META-INF/batch-jobs***
3.  Right-click on BonusPayoutJob.xml and select *Run As->Java EE Batch Job*.
4.  Most of the wizard will be pre-populated, except for the userid and password.
5.  Supply the values:
```
 User ID:   bob
 Password:  bobpwd
```
6.  You should see a dialog like the following showing that the job was successfully committed.  Typically the response will occur quickly enough relative to the job execution that you will see its Batch Status as "STARTED".  

7. Leave the checkbox selected to go to the ***Java EE Batch Job Logs*** view afterwards, refreshing if necessary until the exit status shows as "COMPLETED" (with default job parameters this will happen quickly).

## Using Maven with WDT

Follow for [more info][wdt-maven-notes] on organizing the project to support WDT publish and deploy within Maven build


## Links

* Jump to [main page](/README.md)
* [Running with Maven](/docs/Maven-integration.md)
* [Using Maven with the app published by WDT](/docs/Using-Maven-With-WDT-Published-App.md)



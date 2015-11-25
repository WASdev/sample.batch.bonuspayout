# Using Maven with WDT-Published Apps

This sample allows you to jump back and forth between working with the application in WDT and running the Maven build/automation.

To do so, we employ a few constructs that we'll mention here in a bit of detail.

This isn't meant to be the final word on the subject, the [Liberty maven plugin][liberty-maven-plugin] is under active development with new features continually being added.

## Constructs

* server config separated into version-controlled core named [server.versioned.xml](../batch-bonuspayout-wlpcfg/servers/BonusPayout/server.versioned.xml) included by [server.xml wrapper](../batch-bonuspayout-wlpcfg/servers/BonusPayout/server.xml).
    * The idea here is that publishing the app will cause WDT to update the wrapper server.xml, which you won't want to include in version control.
    * You might want to use this command to have git not treat WDT-initiated updates as a file update:
    ```
    git update-index --assume-unchanged batch-bonuspayout-wlpcfg/servers/BonusPayout/server.xml
    ```
* application not configured in version-controlled server.versioned.xml (within **apps** dir)
* application conditionally copied into **dropins** directory during Maven build, and deleted after the tests have run.
    * The Maven build conditionally deploys the application WAR with the Ant build script [install.if.not.published.by.wdt.xml](../batch-bonuspayout-application/install.if.not.published.by.wdt.xml), which, as its name suggests, checks to see if WDT seems to have already published the "loose config" version of the application

### 

## Links

* Jump to [main page](/README.md)
* [Using WDT](/docs/Using-WDT.md)
* [Building and running with Maven](/docs/Maven-integration.md)
* The [Liberty maven plugin][liberty-maven-plugin]
[liberty-maven-plugin]: https://github.com/WASdev/ci.maven


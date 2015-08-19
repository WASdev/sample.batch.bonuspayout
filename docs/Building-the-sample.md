This sample can be built using [Maven](#building-with-maven).

In addition to publishing the war to the local maven repository, the built war file is copied into the apps directory of the server configuration located in the batch-bonuspayout-wlpcfg directory (though it is copied under a different file name, ***BonusPayout-1.0.war*** than in the Maven repository, so that some existing documentation continues to be accurate).

```text
batch-bonuspayout-wlpcfg
 +- servers
     +- BonusPayout                   <-- specific server configuration
        +- server.xml                 <-- server configuration
        +- apps                       <- directory for applications
           +- BonusPayout-1.0.war     <- sample application
        +- logs                       <- created by running the server locally
        +- workarea                   <- created by running the server locally
```

## Building with maven

This sample can be built using [Apache Maven](http://maven.apache.org/).

```bash
$ mvn install
```

## Next step

[Downloading WAS Liberty](/docs/Downloading-WAS-Liberty.md)

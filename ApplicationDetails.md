# Bonus Payout in detail

## Application Overview

The **BonusPayout** job is structured in 3 steps:   

1. The first step, **generate**, is a batchlet step which generates some random values (representing account balances), and writes them into a text file in CSV format.

1. The second step, **addBonus**, is a chunk step which reads these values from the text file, adds an amount representing a 'bonus' to each record, and writes the updated value into a database table.

1. The third step, **validation**, is a chunk step which loops through the database table updated in step 2 as well as the text file generated in step 1 validating the calculation in the second step.
For each record, in confirms that the value now read from the database table corresponds to the value in the generated text file, plus the bonus amount.  It also confirms that steps 1 and 2 have written the same number of records. 

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

## Job Parameters - detailed look

 Parameter | Default | Description 
 :------------- | :----| :-----------
numRecords  | **1000** | Total number of records generated in step 1 and processed later
chunkSize | **100** | The chunk (transaction) size used in steps 2 and 3
generateFileNameRoot | **/tmp/bpgen ** |  Directory+prefix of file generated in step 1 (more detail below)
dsJNDI | **jdbc/batch** | DataSource JNDI location for application table (not necessarily for container persistence as well)
bonusAmount | **100** |	Amount the “account balance” will be incremented by in step 2
tableName | **SAMPLE.ACCOUNT** | Application database table
fileEncoding | **<None>** | Char encoding used to write text file generated in step 1 and read in step 3
useGlobalJNDI | **true** | If set to **true**, look up **dsJNDI** name in the global JNDI namespace.  Otherwise, lookup DataSource at location *java:/comp/env/<dsJNDI>**






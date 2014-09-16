# Bonus Payout in detail


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
useGlobalJNDI | **true** | If set to **true**, look up **dsJNDI** name in the global JNDI namespace.  Otherwise, lookup DataSource at location *java:/comp/env/<**dsJNDI**>*






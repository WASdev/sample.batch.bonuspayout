FROM websphere-liberty:latest


COPY src/main/liberty/config/server.xml /config/server.xml
COPY target/liberty/wlp/usr/servers/BonusPayout/configDropins/defaults /config/configDropins/defaults
COPY target/liberty/wlp/usr/shared/resources /opt/ibm/wlp/usr/shared/resources
COPY target/batch-bonuspayout-application.war /config/apps/batch-bonuspayout-application.war
COPY batchprops /opt/batchprops

ENV db.url /opt/ibm/wlp/usr/shared/BatchDB

#Handle exit code 22 - the feature already exists.
RUN installUtility install --acceptLicense defaultServer || if [ $? -ne 22 ]; then exit $?; fi





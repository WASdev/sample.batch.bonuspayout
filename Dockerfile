FROM websphere-liberty:latest

#Copy server.xml
COPY src/main/liberty/config/server.xml /config/server.xml

#Copy derby database artifacts
COPY target/liberty/wlp/usr/shared /opt/ibm/wlp/usr/shared

#Copy application war
COPY target/batch-bonuspayout-application.war /config/dropins/batch-bonuspayout-application.war

#Copy batch job props
COPY batchprops /opt/batchprops

#Set database path env var
ENV db.url /opt/ibm/wlp/usr/shared/BatchDB

#Install Liberty features
#Handle exit code 22 - the feature already exists.
RUN installUtility install --acceptLicense defaultServer || if [ $? -ne 22 ]; then exit $?; fi

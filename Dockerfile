FROM websphere-liberty:latest

#Copy server config
COPY src/main/liberty/config/server.xml /config/server.xml
COPY target/liberty/wlp/usr/servers/BonusPayout/bootstrap.properties /config/bootstrap.properties

#Copy derby database artifacts
COPY target/liberty/wlp/usr/shared /opt/ibm/wlp/usr/shared

#Copy application war
COPY target/batch-bonuspayout-application.war /config/dropins/batch-bonuspayout-application.war

#Copy database ddls
COPY ddls /opt/ddls

#Copy batch job props
COPY batchprops /opt/batchprops

#Install Liberty features
#Handle exit code 22 - the feature already exists.
RUN installUtility install --acceptLicense defaultServer || if [ $? -ne 22 ]; then exit $?; fi

#Prepopulate DB
RUN java -Dij.database="jdbc:derby:/opt/ibm/wlp/usr/shared/BatchDB;create=true" -Djava.ext.dirs=/opt/ibm/wlp/usr/shared/resources/derby/ org.apache.derby.tools.ij /opt/ddls/BonusPayout.derby.ddl

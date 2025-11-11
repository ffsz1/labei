#!/bin/sh
export JAVA_HOME=/usr/java/jdk1.7.0_75
kill -9 $(ps -aef | grep tomcat/conf | grep -v grep | awk '{print $2}')
cd /app/programs/tomcat_test1/webapps
mv ROOT.war ROOT_`date +%Y%m%d%H%M%S`.war.bak
#rm -rf ROOT
mv  /app/programs/temp/mvcDemo3*-SNAPSHOT.war ROOT.war
cd /app/programs/tomcat_test1/bin

./startup.sh

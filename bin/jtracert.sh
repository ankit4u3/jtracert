#!/bin/bash
#

JAR_FILE_NAME="jTracert-gui.jar"

if [ "x$JAVA_HOME" == "x" ] ; then
JAVA=`which java`
else
JAVA="$JAVA_HOME/bin/java"
fi

exec $JAVA -jar $JAR_FILE_NAME
#!/bin/sh

JAR_FILE_NAME="jTracert-gui-0.1.0.jar"

if [ "x$JAVA_HOME" == "x" ] ; then
    JAVA=`which java`
else
    JAVA=$JAVA_HOME/bin/java
fi

exec $JAVA -jar $JAR_FILE_NAME
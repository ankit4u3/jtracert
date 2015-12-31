# Introduction #

There's a "Quick Start" chapter on the project main page: http://code.google.com/p/jtracert/

The only difference for J2EE application is the place, where you should specify jTracert JVM parameters (-javaagent: ...).
This place depends on your application server.

A few examples are stated below.

# WebLogic 9+ #

**Windows**

File: %DOMAIN\_HOME%\bin\startWeblogic.cmd. Add the following line:

set JAVA\_OPTIONS=-DanalyzerOutput=sdEditRtClient -DsdEditHost=127.0.0.1 -DsdEditPort=60001 -javaagent:jTracert.jar=7007 %JAVA\_OPTIONS%

**Linux**

File: $DOMAIN\_HOME\bin\startWeblogic.sh. Add the following line:

JAVA\_OPTIONS="-DanalyzerOutput=sdEditRtClient -DsdEditHost=127.0.0.1 -DsdEditPort=60001 -javaagent:jTracert.jar=7007 $JAVA\_OPTIONS"

# GlassFish V2 #

**Windows & Linux**

File: %DOMAIN\_HOME%\config\domain.xml

Search for jvm-options element and add the elements -DanalyzerOutput=sdEditRtClient -DsdEditHost=127.0.0.1 -DsdEditPort=60001 -javaagent:jTracert.jar=7007 Start the domain as usual.

# Other Application Servers #

Just use your application server reference in order to find out where your should specify these parameters (JVM parameters actually).
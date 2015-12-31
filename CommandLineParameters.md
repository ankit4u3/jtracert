# Executing jTracert agent #

In order to start your application with jTracert agent, just add the following JVM parameter: _-javaagent:jTracert.jar_


There're two modes of using jTracert agent: one is for jTracert GUI and the other one is for other frontends.

In order to user jTracert togerther with jTracert GUI, you just need to add the port number after javaagent definition.
For example -javaagent:/home/dmitrybedrin/java/jTracert.jar=7007

If the port isn't specified, jTracert will rely on analyzerOutput parameter for choosing frontend. See below for details.

# Configuring jTracert agent for standard frontend #

Since version 0.1.0, jTracert comes with a special GUI.
The following short article describes how to use it: [Quick Start](http://code.google.com/p/jtracert/#Quick_Start)

# Configuring jTracert agent for non-standard frontend #

You can also configure jTracert for using it together with another frontend. Please note that some of these features will be removed in future.

## Overview ##

jTracert agent is configurable via system properties (set with -D parameter).
A typical command line for executing jTracert agent as a SDEdit realtime client is as below:

'_java -DanalyzerOutput=sdEditRtClient -DsdEditHost=127.0.0.1 -DsdEditPort=60001 -javaagent:jTracert.jar -jar yourApplicationJar.jar_'

## Parameters description ##

| **Parameter** | **Description** | **Default** | **Example** |
|:--------------|:----------------|:------------|:------------|
| analyzerOutput | Selects an output mode for jTracert data. Available values: **none** - output is omitted; **sdEditOut** - output has format of [SDEdit](http://sdedit.sourceforge.net/) files and printed to System.out stream; **sdEditRtClient** - output has format of SDEdit files and sent to a given host:port via network; **sdEditFileSystem** - output has format of SDEdit files, which are stored to a filesystem; **sequenceOut** - output has format of [Zanthan SEQUENCE](http://www.zanthan.com/itymbi/archives/cat_sequence.html) application and printed to System.out stream; **sequenceFileSystem** - output has format of Zanthan SEQUENCE application, which are stored to a filesystem; **webSequenceDiagramsOut** - output has format of [websequencediagrams.com](http://www.websequencediagrams.com/) application and printed to System.out stream; **webSequenceDiagramsFileSystem** - output has format of websequencediagrams.com application, which are stored to a filesystem | none        | sdEditRtClient |
| classNameRegEx | Regular expression for filtering classnames | n/a         | net\.sf.`*` |
| verboseInstrumentation | Print debug information while instrumenting classes | false       | true        |
| verboseAnalyze | Print debug information while analyzing method call trace | false       | true        |
| sdEditHost    | host of SDEdit RT server; used if jTracert is run with -DanalyzerOutput=sdEditRtClient | 127.0.0.1   | 127.0.0.1   |
| sdEditPort    | port of SDEdit RT server; used if jTracert is run with -DanalyzerOutput=sdEditRtClient | 60001       | 60001       |
| outputFolder  | folder where jTracert stores generated sequence diagram files; used if jTracert is run with -DanalyzerOutput=sdEditFileSystem, -DanalyzerOutput=webSequenceDiagramsFileSystem or -DanalyzerOutput=sequenceFileSystem | your temp folder | /tmp/sdeditdiagrams |
| shortenClassNames | Shorten class names if applicable (remove package name) | true        | true        |
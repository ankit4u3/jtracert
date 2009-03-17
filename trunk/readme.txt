jTracert allows you to generate sequence diagrams directly from running java applications.

Consider you application is foo.jar. Usually you execute it like 'java -jar foo.jar'

In order to start using jTracert you need to add following JVM parameter:
'-javaagent:<path-to-jtracert-jar>=<port>'

For example 'java -javaagent:/home/dmitrybedrin/jTracert.jar=7007'

In the line above, the 7007 is port number for negotiating with jTracert GUI.
Please note that you need Java 5 or higher

After adding -javaagent parameter and executing your application, you will see the following message:

jTracert agent started
Waiting for a connection from jTracert GUI on port 7007

Now start jTracert GUI by executing appropriate shell wrapper (jtracert.cmd on Windows,
jtracert.sh on GNU/Linux) or just type java -jar jTracert-gui.jar, and fill in a few fields
in connection dialog:

1) Working folder (in which diagram files will be stored)
2) Host & port of jTracert agent
3) A regular expression for filtering classes to be instrumented and analyzed

That's it! In SDEdit window you will get the sequence diagrams describing your application runtime!

Other command line parameters are described on project wiki page:
http://code.google.com/p/jtracert/wiki/CommandLineParameters

Any feedback much appreciated!

You can find the jTracert license in license.txt file
Third party library licenses can be found in 3pplicense folder

http://jtracert.googlecode.com/
Dmitry.Bedrin@gmail.com

17 March 2009
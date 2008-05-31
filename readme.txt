jTracert allows you to generate sequence diagrams directly from running java applications.

Consider you application is foo.jar. Usually you execute it like 'java -jar foo.jar'
In order to start using jTracert you need to add following JVM parameter: '-javaagent:jTracert-0.0.1.jar'
Please note that you need Java 5 or higher

After executing your application with parameter above, you'll get method call traces in standart output stream.
Call traces are of SDEdit format. For more details please refer to http://sdedit.sourceforge.net/enter_text/index.html

You can even generate sequence diagrams directly in SDEdit! Just execute SDEdit in RT server mode
(Item 'Start/stop RT server' in menu 'Extras'), and add following parameters to your application:
'-Dtype=client -Dhost=127.0.0.1 -Dport=60001'
where host&port are host and port of SDEdit RT server.

That's it!

Current version of jTracert is a early alpha, but already working however.
Any feedback appreciated!

http://jtracert.googlecode.com/
Dmitry.Bedrin@gmail.com

31 May 2008

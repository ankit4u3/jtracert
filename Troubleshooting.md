# Introduction #

Some common problems reported by jTracert users and resolution for these problems.

# Troubles #

## I'm running Microsoft Windows (c) and cannot execute jTracert exe files ##

.exe wrappers are an experimental feature in jTracert. If you're experiencing any issues with using them, please download archive with jTracert binaries (i.e. jTracert-0.1.2.zip) and use jtracert.cmd script instead of .exe files

## jTracert GUI closes immediately after pressing Connect button ##

Since jTracert UI disappears it seems that it fails to connect to jTracert agent running inside tomcat process.

You can download archive with jTracert(i.e. jTracert-0.1.3.zip) and use jtracert.cmd script instead of .exe files
In this case you'll be able to see the errors.

Most probably you've specified the -javaagent in the wrong place or did it incorrectly.

## jTracert show no errors, but at the same time no data is generated ##

The most probable reason is that you have set incorrect class file filter. Please note that this filter is a regular expression. If you want to analyze classes from package _com.foo_, you need to set this filter to the following value:
```
com\.foo.*
```

The other possible reason is that you're using too old JVM. One of jTracert users has faced this problem on 1.5.0\_01 JVM. Update to a newer version has solved the issue.

## Error opening zip file: jTracert.jar ##

You may face the following problem when starting your application together with jTracert:
"Error occurred during initialization of VM agent library failed to init: instrument Error opening zip file: jTracert.jar"

In this case, please check jTracert actuall file name with full path.
For example it can be /home/dmitrybedrin/java/jTracert.jar, but not just jTracert.jar

See also [Issue 2](https://code.google.com/p/jtracert/issues/detail?id=2)

## java.lang.noclassdeffounderror: com/google/code/jtracert/tracebuilder/methodcalltracebuilderfactory ##

You can get this error when your application is using custom classloaders, for example OSGi framework. Custom class loaders support was greatly improved in jTracert version 0.1.2 - please update jTracert to the latest version.

If it doesn't help, please [submit an issue report](http://code.google.com/p/jtracert/issues/entry) or ask for help is [jTracert mailing list](http://groups.google.com/group/jtracert).

## warning - cannot append jtracert agent to bootstrap class ##

This warning message was shown by previous versions jTracert  you shouldn't see it in jTracert 0.1.2 and higher.

It was concerned to analyzing classes loaded by bootstrap class loader. This feature is not implemented completely and still under development.

## java.net.BindException: Address already in use: JVM\_Bind ##

If you're getting this exception, it looks like you already have a Java process running with jTracert agent.

Kill other java processes or just try another port number (say 7008). Restart should also help.


## Other issues ##

If you're experiencing issues with using jTracert, please [submit an issue report](http://code.google.com/p/jtracert/issues/entry) or ask for help is [jTracert mailing list](http://groups.google.com/group/jtracert)

Do not forget to specify the jTracert version, your operation system and java version (including vendor)

# Other Frequently Asked Questions #

## Where can I find jTracert Eclipse plugin? ##

jTracert currently doesn't support Eclipse integration. You should use it as a standalone tool.

## Is there any tutorial for using jTracert? ##

Yes, you can find a simple tutorial here: http://code.google.com/p/jtracert/#Quick_Start

## Does jTracert support J2EE applications? ##

Sure, it does - [HowToAnalyzeJ2EEApplication](HowToAnalyzeJ2EEApplication.md)

jTracert actually can be used to analyze any java application running on JRE 1.5 or higher.

You can add jTracert parameters to applet viewer and analyze applets, and so on...
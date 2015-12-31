# Discontinue information #

jTracert is now discontinued and no longer supported! Please use jSonde instead. It's a new tool similar to jTracert but much more powerfull, and it's absolutely free!

Find more details at jSonde site: https://github.com/bedrin/jsonde

# Introduction #

jTracert will allow you to generate sequence diagrams directly from your application runtime! This gives you a lot of advantages:
  * Understand the code created by your colleagues/partners in a short time
  * Rapidly generate documentation for your partners or users.
  * Easily investigate what's happening in large Java applications
  * Excellent companion for a common debugger

[Quick Start](http://code.google.com/p/jtracert/#Quick_Start)

# News #

## 8 February 2015 - jSonde moved to github ##
We have moved jSonde to github!

https://github.com/bedrin/jsonde

## 30 March 2010 - jSonde is now free and licensed under GPL v3 ##

A successor of jTracert, a bran new tool called jSonde is now licensed under GPL v3. All users of jTracert should use jSonde instead!
http://www.jsonde.com/

## 26 October 2009 - replace for jTracert, jSonde 1.0 will be released soon ##

We're now working on a new tool which gonna replace jTracert. It's called jSonde, and it's beta version is available here: http://www.jsonde.com/

It's not open source any more, but we plan to keep it free for personal use. Perhaps open source version will be available in future.

## 24 October 2009 - jtracert 0.1.3, has been tested 100% clean and rated 5 stars on GearDownload.com ##

![http://www.geardownload.com/images/100clean.jpg](http://www.geardownload.com/images/100clean.jpg)

jTracert was tested thoroughly and was found 100% clean. Click the following links for details: http://www.geardownload.com/development/jtracert.html

## 14 September 2009 - jTracert 0.1.3 released ##

A new minor version of jTracert is released - 0.1.3

Changes list:

  * Bugfixing - Issues 16, 17, 19
  * Improved instrumentation of classes, loaded by system class laoder
  * Better large method call trees support

ChangesList
NewsArchive

# Quick Start #

## Executing jTracert agent ##

jTracert is implemented as a small javaagent, consisting from only one jar with no dependencies. In order to profile your application with jTracert, just add the following option to VM options: **-javaagent:jTracert.jar=7007**

**IMPORTANT!** You should specify a path to jTracert.jar, so your parameter will look like: -javaagent:/home/dmitrybedrin/java/jTracert.jar=7007

7007 here is the port number for communicating with jTracert GUI - you can replace it with any other port.

Please note, that you're not limited to J2SE applications.
Add this parameter to your J2EE App server options, and [profile your J2EE application](HowToAnalyzeJ2EEApplication.md). It's also possible to profile applets, and almost all other types of java applications. This is a benefit of implementing jTracert in pure Java.

## Executing jTracert GUI ##

When executing your java application with jTracert agent, you will see the following message:

jTracert agent started
Waiting for a connection from jTracert GUI on port 7007

Now start jTracert GUI by executing appropriate shell wrapper (jtracert.cmd on Windows, jtracert.sh on GNU/Linux) or just type java -jar jTracert-gui.jar, and fill in a few fields in connection dialog:
  1. Working folder (in which diagram files will be stored)
  1. Host & port of jTracert agent
  1. A regular expression for filtering classes to be instrumented and analyzed

![http://lh6.ggpht.com/__gdII9J2aOQ/Sa-ii1UXWPI/AAAAAAAAA8A/wBczH1jpLTs/Screenshot-jTracert%20Connection%20Wizard.png](http://lh6.ggpht.com/__gdII9J2aOQ/Sa-ii1UXWPI/AAAAAAAAA8A/wBczH1jpLTs/Screenshot-jTracert%20Connection%20Wizard.png)

Click on "Connect" button. That's it!

![http://lh4.ggpht.com/__gdII9J2aOQ/Sa-iiky-IeI/AAAAAAAAA74/fezar0VkpbY/s720/jT-Screenshot.png](http://lh4.ggpht.com/__gdII9J2aOQ/Sa-iiky-IeI/AAAAAAAAA74/fezar0VkpbY/s720/jT-Screenshot.png)
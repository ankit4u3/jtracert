## 14 September 2009 - jTracert 0.1.3 released ##

A new minor version of jTracert is released - 0.1.3

Changes list:

  * Bugfixing - Issues 16, 17, 19
  * Improved instrumentation of classes, loaded by system class laoder
  * Better large method call trees support

## 11 September 2009 - jTracert recieved SOFTPEDIA "100% FREE" AWARD ##
![http://mac.softpedia.com/base_img/softpedia_free_award_f.gif](http://mac.softpedia.com/base_img/softpedia_free_award_f.gif)

Today jTracert has recieved "100% FREE" award from softpedia.com

Below is an extract from [softpedia web site](http://mac.softpedia.com/progClean/jtracert-Clean-62712.html):

This product was last tested in the Softpedia Labs on 09 September 2009 by Sergiu Gatlan

Softpedia guarantees that jtracert 0.1.2 is 100% Free, which means it does not contain any form of malware, including but not limited to: spyware, viruses, trojans and backdoors.

This software product was tested thoroughly and was found absolutely clean; therefore, it can be installed with no concern by any computer user. However, it should be noted that this product will be retested periodically and the award may be withdrawn, so you should check back occasionally and pay attention to the date of testing shown above.

## 25 May 2009 - jTracert is one year old; new version is released ##

The first version of jTracert was released one year ago, on the 31st of May. We've made a lot of work since this first version, and there're a lot of plans for the next year.

A new version of jTracert is also released.
Changes in release 0.1.2:

  * Bugfixing - [Issue 15](https://code.google.com/p/jtracert/issues/detail?id=15), [Issue 14](https://code.google.com/p/jtracert/issues/detail?id=14) is now fixed completely
  * jTracert GUI now remembers the connection settings
  * Fixed possible issue with jTracert preventing the application under test to stop
  * Show short class name feature improved
  * jTracert now omits the short method call traces (-DminimalTraceLength parameter)
  * Added graphical installer
  * Added [.exe wrapper](http://jtracert.googlecode.com/files/jTracert-0.1.2-setup.exe) for [Microsoft Windows](http://www.microsoft.com/WINDOWS/) (c) platform

## 17 March 2009 - jTracert 0.1.1 released ##

We're glad to announce a new version of jTracert - 0.1.1
Changes since previous version:

  * Added support for OSGi environments ([Issue 14](https://code.google.com/p/jtracert/issues/detail?id=14))
  * Analyze native method calls on JRE 1.6+
  * View dependencies tool added to jTracert GUI (Menu item Extras\View dependencies)
  * Create shell wrappers for convinient executing jTracert GUI on GNU/Linux and Windows operation systems
  * jTracert is now packages as a single zip archive file
  * Fixed integration test on JDK 1.5
  * Some other minor refactorings

## 05 March 2009 - jTracert 0.1.0 released ##

jTracert 0.1.0 is the first version of jTracert which comes with a special GUI frontend.
Now it's much more convenient to use jTracert

Some significant changes since previous version:

  * A new GUI frontend introduced; you can execute it with 'java -jar jTracert-gui-0.1.0.jar'
  * Bugfixing ([Issue 10](https://code.google.com/p/jtracert/issues/detail?id=10), [Issue 11](https://code.google.com/p/jtracert/issues/detail?id=11), [Issue 12](https://code.google.com/p/jtracert/issues/detail?id=12), [Issue 13](https://code.google.com/p/jtracert/issues/detail?id=13))
  * jTracert will try using short class names (without package) if applicable and -DshortenClassNames=false is not specified

## 17 February 2009 - jTracert 0.0.5 released ##

We're glad to announce a new version of jTracert - 0.0.5
Changes since previous version:
  * Bugfixing ([Issue 8](https://code.google.com/p/jtracert/issues/detail?id=8) and [Issue 9](https://code.google.com/p/jtracert/issues/detail?id=9))
  * License switched to [GNU GPL version 3](http://www.gnu.org/licenses/gpl-3.0.txt)

## 27 January 2009 - jTracert 0.0.4 released ##

New version of jTracert is now released.
Changes:
  * Numerous defects fixed ( [Issue 2](https://code.google.com/p/jtracert/issues/detail?id=2), [Issue 3](https://code.google.com/p/jtracert/issues/detail?id=3), [Issue 4](https://code.google.com/p/jtracert/issues/detail?id=4), [Issue 5](https://code.google.com/p/jtracert/issues/detail?id=5), [Issue 6](https://code.google.com/p/jtracert/issues/detail?id=6) )
  * Large projects support improved
  * Implemented small framework for integration tests - users are now welcomed to send their applications in case of having any problems

I've also updated sdedit hosted in this site to version 3.0.5

## 6 January 2009 - happy New Year and Merry Christmas; some news about jTracert ##

Dear jTracert users, happy New Year and Merry Christmas!
I hope that jTracert will become more useful and gain new users in 2009 year.

In spite of a break in releases (last release was in September), I'm still working on this project.
You can the latest version of jTracert from SVN and build it yourself.
See [BuildingJTracertFromSVN](http://code.google.com/p/jtracert/wiki/BuildingJTracertFromSVN) page for details.

jTracert is now presented on ohloh.net: https://www.ohloh.net/p/jTracert

Join our [new discussion group](http://groups.google.com/group/jtracert) and watch the project using [feeds](http://code.google.com/p/jtracert/feeds).

If you want to contribute to this project, do not hesitate to contact me: [Dmitry.Bedrin@gmail.com](mailto:Dmitry.Bedrin@gmail.com) !

## 9 September 2008 - big thanks for your feedback; jTracert 0.0.3 released ##

First of all, I'd like to thank the jTracert users for the feedback. This project is very young, but anyway we already have a couple hundreds of downloads and thousands visitors from the entire world. A first defect ( [Issue 1](https://code.google.com/p/jtracert/issues/detail?id=1) ) was opened in bug tracking system and was successfully closed.

Based on the received feedback, following two features are top priority:
  * A separate specific GUI for jTracert
  * Better perfomance and ability to work on big enterprise applications

I believe that these two features are mandatory for the first beta version release.

New features in jTracert 0.0.3:
  * New experimental support of http://www.websequencediagrams.com/ See WebSequenceDiagrams
  * [Issue 1](https://code.google.com/p/jtracert/issues/detail?id=1) fixed
  * Unit tests fixed
  * Various refactorings

## 9 June 2008 - jTracert 0.0.2 released ##

New features:
  * Removing duplicate method calls from jTracert output
  * Support of nested self-calls
  * [Support of Zanthan SEQUENCE file format](ZanthanSequence.md)
  * Numerous bugfixes and performance improvements
  * CLI interface cleaned and refactored

## 31 May 2008 - jTracert 0.0.1 released ##

We're proud to present you the very first version of jTracert
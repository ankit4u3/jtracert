# 0.1.3 #

Released on 14 September 2009

  * Bugfixing - Issues 16, 17, 19
  * Improved instrumentation of classes, loaded by system class laoder
  * Better large method call trees support

# 0.1.2 #

Released on 25 May 2009

  * Bugfixing - [Issue 15](https://code.google.com/p/jtracert/issues/detail?id=15), [Issue 14](https://code.google.com/p/jtracert/issues/detail?id=14) is now fixed completely
  * jTracert GUI now remembers the connection settings
  * Fixed possible issue with jTracert preventing the application under test to stop
  * Show short class name feature improved
  * jTracert now omits the short method call traces (-DminimalTraceLength parameter)
  * Added graphical installer
  * Added [.exe wrapper](http://jtracert.googlecode.com/files/jTracert-0.1.2-setup.exe) for [Microsoft Windows](http://www.microsoft.com/WINDOWS/) (c) platform

# 0.1.1 #

Released on 17 March 2009

  * Added support for OSGi environments ([Issue 14](https://code.google.com/p/jtracert/issues/detail?id=14))
  * Analyze native method calls on JRE 1.6+
  * View dependencies tool added to jTracert GUI (Menu item Extras\View dependencies)
  * Create shell wrappers for convinient executing jTracert GUI on GNU/Linux and Windows operation systems
  * jTracert is now packages as a single zip archive file
  * Fixed integration test on JDK 1.5
  * Some other minor refactorings

# 0.1.0 #

Released on 5 March 2009

  * A new GUI frontend introduced; you can execute it with 'java -jar jTracert-gui-0.1.0.jar'
  * Bugfixing ([Issue 10](https://code.google.com/p/jtracert/issues/detail?id=10), [Issue 11](https://code.google.com/p/jtracert/issues/detail?id=11), [Issue 12](https://code.google.com/p/jtracert/issues/detail?id=12), [Issue 13](https://code.google.com/p/jtracert/issues/detail?id=13))
  * jTracert will try using short class names (without package) if applicable and -DshortenClassNames=false is not specified

# 0.0.5 #

Released on 17 February 2009
  * Bugfixing ([Issue 8](https://code.google.com/p/jtracert/issues/detail?id=8) and [Issue 9](https://code.google.com/p/jtracert/issues/detail?id=9))
  * License switched to [GNU GPL version 3](http://www.gnu.org/licenses/gpl-3.0.txt)

# 0.0.4 #

Released on 27 January 2009

  * Numerous defects fixed ( [Issue 2](https://code.google.com/p/jtracert/issues/detail?id=2), [Issue 3](https://code.google.com/p/jtracert/issues/detail?id=3), [Issue 4](https://code.google.com/p/jtracert/issues/detail?id=4), [Issue 5](https://code.google.com/p/jtracert/issues/detail?id=5), [Issue 6](https://code.google.com/p/jtracert/issues/detail?id=6) )
  * Large projects support improved
  * Implemented small framework for integration tests - users are now welcomed to send their applications in case of having any problems

# 0.0.3 #

Released on 9 September 2008

  * New experimental support of http://www.websequencediagrams.com/ See WebSequenceDiagrams
  * [Issue 1](https://code.google.com/p/jtracert/issues/detail?id=1) fixed
  * Unit tests fixed
  * Various refactorings

# 0.0.2 #

Released on 9 June 2008

  * Removing duplicate method calls from jTracert output
  * Support of nested self-calls
  * Support of Zanthan SEQUENCE file format
  * Numerous bugfixes and performance improvements
  * CLI interface cleaned and refactored

# 0.0.1 #

Released on 31 May 2008
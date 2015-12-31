# Introduction #

You can get the source code of _jTracert_ and build it yourself.
It will allow you to use the latest version without waiting for the next release.

# Getting the code #

_jTracert_ source code is stored in _SVN_ on _googlecode.com_ server.
See [Source](http://code.google.com/p/jtracert/source/checkout) page for details.

You need to install some _SVN_ client, console or GUI.
See this page for details: http://subversion.tigris.org/links.html#clients

# Building jTracert #

_jTracert_ utilizes [Apache ANT](http://ant.apache.org/) for building.
You can download it here: http://ant.apache.org/bindownload.cgi
Version 1.7 is fine.

In order to build _jTracert_, just go the folder with the sources, and type '_ant_'

That's it! Now you have latest jTracert jar file placed to _deploy_ folder

# Executing integration tests #

jTracert comes with a set of integration tests. In order to execute them just type '_ant_ _integration.test_'
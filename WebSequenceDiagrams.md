# Introduction #

**WARNING** - this feature is discontinued and no longer tested or support. It may work inproperly

websequencediagrams is an online service, providing you with the ability to generate sequence diagrams online without installing any applications on your computer.

You can check it here: http://www.websequencediagrams.com/

![http://lh4.ggpht.com/Dmitry.Bedrin/SMWFzkIEqZI/AAAAAAAAAqk/sRh34kW92Vk/s800/Screenshot.png](http://lh4.ggpht.com/Dmitry.Bedrin/SMWFzkIEqZI/AAAAAAAAAqk/sRh34kW92Vk/s800/Screenshot.png)

# Configuring jTracert #

An example command line for using jTracert together with websequencediagrams is like below:

_java -DanalyzerOutput=**webSequenceDiagramsFileSystem** -DoutputFolder=/home/dmitrybedrin/bla -DclassNameRegEx=net\.sf.`*` -javaagent:jTracert.jar -jar sdedit-3.0.3.jar_
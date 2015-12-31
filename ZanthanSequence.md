# Introduction #

**WARNING** - this feature is discontinued and no longer tested or support. It may work inproperly

Zanthan SEQUENCE is another application for drawing sequence diagrams.
You can download it from following page: http://www.zanthan.com/itymbi/archives/cat_sequence.html

# Generating SEQUENCE files using jTracert #

Start jTracert with following command line parameters:
_java **-DanalyzerOutput=sequenceFileSystem -DoutputFolder=/tmp/sq -javaagent:jTracert.jar** -jar yourApplication.jar_

jTracert will generate SEQUENCE diagrams to the specified folder /tmp

Example file content is like below:
```
net/sf/sdedit/ui/components/configuration/Configurator$1.run {
    net/sf/sdedit/ui/components/configuration/configurators/StringSelectionConfigurator._actionPerformed {
        net/sf/sdedit/ui/components/configuration/configurators/StringSelectionConfigurator.applyValue {
            net/sf/sdedit/ui/components/configuration/Bean.setValue {
                net/sf/sdedit/ui/components/configuration/Bean.getStringsForProperty {
                    net/sf/sdedit/ui/components/configuration/Bean.norm {
                    }
                }
                net/sf/sdedit/ui/components/configuration/Bean.norm {
                }
                net/sf/sdedit/ui/components/configuration/Bean.firePropertyChanged {
                }
            }
        }
    }
}
```

# View sequence diagrams in SEQUENCE #

Open generated diagrams with SEQUENCE and examine your code.

![http://www.zanthan.com/ajm/sequence/sequence_20030813.png](http://www.zanthan.com/ajm/sequence/sequence_20030813.png)

That's it!
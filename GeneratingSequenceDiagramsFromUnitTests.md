# Introduction #

jTracert can be used to generate sequence diagrams from your application being tested by unit tests. It can be even used to do this automatically


# Details #

Below is an example for integrating jTracert with [unit tests](http://en.wikipedia.org/wiki/Unit_testing) run by [Maven](http://maven.apache.org/):

```
<properties>
	<jtraceOutputFolder>target/sequence-diagrams</jtraceOutputFolder>
</properties>
<profiles>
		<profile>
			<id>generate-sequence-diagrams</id>
			<build>
				<plugins>
					<!--
						Generera .sdx-filer (sekvensdiagram i textformat) för alla tester
					-->
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<configuration>
							<argLine>
							<!-- Important, following on one line! -->
-javaagent:${settings.localRepository}com/google/code/jtracert/0.1.1/jTracert-0.1.1.jar 
-DanalyzerOutput=sdEditFileSystem
-DclassNameRegEx=com\.mycompany.* 
-DoutputFolder=${jtraceOutputFolder}
							</argLine>
						</configuration>
					</plugin>

					<plugin>
						<groupId>org.codehaus.groovy.maven</groupId>
						<artifactId>gmaven-plugin</artifactId>
						<executions>
							<execution>
								<phase>package</phase>
								<goals>
									<goal>execute</goal>
								</goals>
								<configuration>
									<source>
										def dir = new File("$project.basedir/${jtraceOutputFolder}")
										dir.eachFileRecurse{
										if(!it.directory){
										println "converting $it to png"
										def fileName = it.path.replace(".sdx", ".png")

										def proc = """java -Xms256M -Xmx1024M -jar
										${settings.localRepository}net/sourceforge/sdedit/3.0.5/sdedit-3.0.5.jar
										-o ${fileName}
										-t png
										$it"""
										.execute()
										proc.waitFor()
										}
										}
					                </source>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
```

You can adopt this example to use jTracert with [ANT](http://ant.apache.org/) as well.
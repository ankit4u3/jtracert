package com.google.code.jtracert.samples;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

public class SimpleApp1Test extends TestCase {

    @Test
    public void testSimpleApp1() throws Exception {

        String[] commands = new String[]{
                "java",
                "-DanalyzerOutput=sdEditRtClient",
                "-DsdEditHost=127.0.0.1",
                "-DsdEditPort=60001",
                /*"-DverboseInstrumentation=true",
                "-DverboseAnalyze=true",*/
                "-javaagent:deploy/jTracert.jar",
                "-jar","deploy/simpleApp1.jar"
        };

        System.out.println(Arrays.toString(commands));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(".").getAbsoluteFile());

        System.out.println("Starting process... ");

        Process process = processBuilder.start();

        System.out.println("Process: " + process);
        
        InputStream is = process.getInputStream();

        int ch;
        while( (ch=is.read()) != -1 )
            System.out.write((char)ch);


        System.out.println("Waiting for process to finish");

        int exitCode = process.waitFor();

        System.out.println("Process finished with exit code " + exitCode);

        assertEquals(0, exitCode);

    }

}

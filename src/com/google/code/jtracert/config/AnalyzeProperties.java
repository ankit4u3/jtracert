package com.google.code.jtracert.config;

import com.google.code.jtracert.util.FileUtils;

import java.io.Serializable;

/**
 * @author Dmitry Bedrin
 */
public class AnalyzeProperties implements Serializable {

    public enum AnalyzerOutput {
        none,
        sdEditOut,
        sdEditRtClient,
        sdEditFileSystem,
        sequenceOut,
        sequenceFileSystem
    }

    public final static String SDEDIT_HOST = "sdEditHost";
    public final static String SDEDIT_PORT = "sdEditPort";
    public final static String ANALYZER_OUTPUT = "analyzerOutput";
    public final static String OUTPUT_FOLDER = "outputFolder";
    public final static String VERBOSE = "verboseAnalyze";

    private String sdEditHost;
    private int sdEditPort;
    private AnalyzerOutput analyzerOutput;
    private String outputFolder;
    private boolean verbose;

    public AnalyzeProperties() {

    }

    public static AnalyzeProperties loadFromSystemProperties() {
        AnalyzeProperties analyzeProperties = new AnalyzeProperties();

        String host = System.getProperty(SDEDIT_HOST);
        if (null != host) {
            analyzeProperties.setSdEditHost(host);
        } else {
            analyzeProperties.setSdEditHost("127.0.0.1");
        }

        String port = System.getProperty(SDEDIT_PORT);
        if (null != port) {
            analyzeProperties.setSdEditPort(Integer.parseInt(port));
        } else {
            analyzeProperties.setSdEditPort(60001);
        }

        String analyzerOutput = System.getProperty(ANALYZER_OUTPUT);
        if (null != analyzerOutput) {
            analyzeProperties.setAnalyzerOutput(AnalyzerOutput.valueOf(analyzerOutput));
        } else {
            analyzeProperties.setAnalyzerOutput(AnalyzerOutput.none);
        }

        String outputFolder = System.getProperty(OUTPUT_FOLDER);
        if (null != outputFolder) {
            analyzeProperties.setOutputFolder(outputFolder);
        } else {
            analyzeProperties.setOutputFolder(FileUtils.TEMP_DIR);
        }

        String verboseAnalyze = System.getProperty(VERBOSE);
        if (null != verboseAnalyze) {
            analyzeProperties.setVerbose(Boolean.valueOf(verboseAnalyze));
        } else {
            analyzeProperties.setVerbose(false);
        }

        return analyzeProperties;
    }

    public String getSdEditHost() {
        return sdEditHost;
    }

    public void setSdEditHost(String sdEditHost) {
        this.sdEditHost = sdEditHost;
    }

    public int getSdEditPort() {
        return sdEditPort;
    }

    public void setSdEditPort(int sdEditPort) {
        this.sdEditPort = sdEditPort;
    }

    public AnalyzerOutput getAnalyzerOutput() {
        return analyzerOutput;
    }

    public void setAnalyzerOutput(AnalyzerOutput analyzerOutput) {
        this.analyzerOutput = analyzerOutput;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
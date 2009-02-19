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
        sequenceFileSystem,
        webSequenceDiagramsOut,
        webSequenceDiagramsFileSystem,
        serializableTcpClient,
        serializableTcpServer
    }

    private final static String SDEDIT_HOST = "sdEditHost";
    private final static String SDEDIT_PORT = "sdEditPort";
    private final static String ANALYZER_OUTPUT = "analyzerOutput";
    private final static String OUTPUT_FOLDER = "outputFolder";
    private final static String VERBOSE = "verboseAnalyze";
    private final static String SERIALIZABLE_TCP_SERVER_HOST = "serializableTcpServerHost";
    private final static String SERIALIZABLE_TCP_SERVER_PORT = "serializableTcpServerPort";

    private String sdEditHost;
    private int sdEditPort;
    private AnalyzerOutput analyzerOutput;
    private String outputFolder;
    private boolean verbose;

    private String serializableTcpServerHost;
    private int serializableTcpServerPort;

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

        String serializableTcpServerHost = System.getProperty(SERIALIZABLE_TCP_SERVER_HOST);
        if (null != serializableTcpServerHost) {
            analyzeProperties.setSerializableTcpServerHost(serializableTcpServerHost);
        } else {
            analyzeProperties.setSerializableTcpServerHost("127.0.0.1");
        }

        String serializableTcpServerPort = System.getProperty(SERIALIZABLE_TCP_SERVER_PORT);
        if (null != serializableTcpServerPort) {
            analyzeProperties.setSerializableTcpServerPort(Integer.parseInt(serializableTcpServerPort));
        } else {
            analyzeProperties.setSerializableTcpServerPort(60002);
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

    public String getSerializableTcpServerHost() {
        return serializableTcpServerHost;
    }

    public void setSerializableTcpServerHost(String serializableTcpServerHost) {
        this.serializableTcpServerHost = serializableTcpServerHost;
    }

    public int getSerializableTcpServerPort() {
        return serializableTcpServerPort;
    }

    public void setSerializableTcpServerPort(int serializableTcpServerPort) {
        this.serializableTcpServerPort = serializableTcpServerPort;
    }

}
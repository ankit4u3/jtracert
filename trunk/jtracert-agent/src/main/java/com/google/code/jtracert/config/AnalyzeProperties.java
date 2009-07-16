package com.google.code.jtracert.config;

import com.google.code.jtracert.util.FileUtils;

import java.io.Serializable;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
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
    private final static String SHORTEN_CLASS_NAMES = "shortenClassNames";
    private final static String VERBOSE = "verboseAnalyze";
    private final static String SERIALIZABLE_TCP_SERVER_HOST = "serializableTcpServerHost";
    private final static String SERIALIZABLE_TCP_SERVER_PORT = "serializableTcpServerPort";
    private final static String MINIMAL_TRACE_LENGTH = "minimalTraceLength";
    private final static String MAXIMAL_TRACE_LENGTH = "maximalTraceLength";

    private String sdEditHost;
    private int sdEditPort;
    private AnalyzerOutput analyzerOutput;
    private String outputFolder;
    private boolean verbose;
    private boolean shortenClassNames;

    private String serializableTcpServerHost;
    private int serializableTcpServerPort;

    private int minimalTraceLength;
    private int maximalTraceLength;

    /**
     *
     */
    public AnalyzeProperties() {

    }

    /**
     * @return
     */
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

        String shortenClassNames = System.getProperty(SHORTEN_CLASS_NAMES);
        if (null != shortenClassNames) {
            analyzeProperties.setShortenClassNames(Boolean.valueOf(shortenClassNames));
        } else {
            analyzeProperties.setShortenClassNames(true);
        }

        String minimalTraceLength = System.getProperty(MINIMAL_TRACE_LENGTH);
        if (null != minimalTraceLength) {
            analyzeProperties.setMinimalTraceLength(Integer.parseInt(minimalTraceLength));
        } else {
            analyzeProperties.setMinimalTraceLength(3);
        }

        String maximalTraceLength = System.getProperty(MAXIMAL_TRACE_LENGTH);
        if (null != maximalTraceLength) {
            analyzeProperties.setMaximalTraceLength(Integer.parseInt(maximalTraceLength));
        } else {
            analyzeProperties.setMaximalTraceLength(1000);
        }

        return analyzeProperties;
    }

    /**
     * @return
     */
    public String getSdEditHost() {
        return sdEditHost;
    }

    /**
     * @param sdEditHost
     */
    public void setSdEditHost(String sdEditHost) {
        this.sdEditHost = sdEditHost;
    }

    /**
     * @return
     */
    public int getSdEditPort() {
        return sdEditPort;
    }

    /**
     * @param sdEditPort
     */
    public void setSdEditPort(int sdEditPort) {
        this.sdEditPort = sdEditPort;
    }

    /**
     * @return
     */
    public AnalyzerOutput getAnalyzerOutput() {
        return analyzerOutput;
    }

    /**
     * @param analyzerOutput
     */
    public void setAnalyzerOutput(AnalyzerOutput analyzerOutput) {
        this.analyzerOutput = analyzerOutput;
    }

    /**
     * @return
     */
    public String getOutputFolder() {
        return outputFolder;
    }

    /**
     * @param outputFolder
     */
    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    /**
     * @todo refactor: use some logging tool with several logging levels for this purpose
     * @return
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * @param verbose
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * @return
     */
    public String getSerializableTcpServerHost() {
        return serializableTcpServerHost;
    }

    /**
     * @param serializableTcpServerHost
     */
    public void setSerializableTcpServerHost(String serializableTcpServerHost) {
        this.serializableTcpServerHost = serializableTcpServerHost;
    }

    /**
     * @return
     */
    public int getSerializableTcpServerPort() {
        return serializableTcpServerPort;
    }

    /**
     * @param serializableTcpServerPort
     */
    public void setSerializableTcpServerPort(int serializableTcpServerPort) {
        this.serializableTcpServerPort = serializableTcpServerPort;
    }

    /**
     * @return
     */
    public boolean isShortenClassNames() {
        return shortenClassNames;
    }

    /**
     * @param shortenClassNames
     */
    public void setShortenClassNames(boolean shortenClassNames) {
        this.shortenClassNames = shortenClassNames;
    }

    /**
     * @return
     */
    public int getMinimalTraceLength() {
        return minimalTraceLength;
    }

    /**
     * @param minimalTraceLength
     */
    public void setMinimalTraceLength(int minimalTraceLength) {
        this.minimalTraceLength = minimalTraceLength;
    }

    /**
     * @return
     */
    public int getMaximalTraceLength() {
        return maximalTraceLength;
    }

    /**
     * @param maximalTraceLength
     */
    public void setMaximalTraceLength(int maximalTraceLength) {
        this.maximalTraceLength = maximalTraceLength;
    }

}
package com.google.code.jtracert.gui;

import java.net.InetAddress;

/**
 *
 */
public class AgentConnectionSettings {

    private InetAddress inetAddress;
    private int port;
    private String classNamePattern;

    /**
     * @param inetAddress
     * @param port
     * @param classNamePattern
     */
    public AgentConnectionSettings(InetAddress inetAddress, int port, String classNamePattern) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.classNamePattern = classNamePattern;
    }

    /**
     * @return
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * @param inetAddress
     */
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return
     */
    public String getClassNamePattern() {
        return classNamePattern;
    }

    /**
     * @param classNamePattern
     */
    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "AgentConnectionSettings{" +
                "inetAddress=" + inetAddress +
                ", port=" + port +
                ", classNamePattern='" + classNamePattern + '\'' +
                '}';
    }

}

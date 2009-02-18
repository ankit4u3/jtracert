package com.google.code.jtracert.gui;

import java.net.InetAddress;

public class AgentConnectionSettings {

    InetAddress inetAddress;
    int port;
    String classNamePattern;

    public AgentConnectionSettings(InetAddress inetAddress, int port, String classNamePattern) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.classNamePattern = classNamePattern;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    @Override
    public String toString() {
        return "AgentConnectionSettings{" +
                "inetAddress=" + inetAddress +
                ", port=" + port +
                ", classNamePattern='" + classNamePattern + '\'' +
                '}';
    }
    
}

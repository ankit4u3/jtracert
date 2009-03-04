package com.google.code.jtracert.gui;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class Node extends DefaultMutableTreeNode {

    private String value;
    private String fileName;

    /**
     *
     */
    public Node() {
    }

    /**
     * @param value
     */
    public Node(String value) {
        this.value = value;
    }

    /**
     * @param value
     * @param fileName
     */
    public Node(String value, String fileName) {
        this.value = value;
        this.fileName = fileName;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return value;
    }

}
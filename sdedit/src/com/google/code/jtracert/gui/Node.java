package com.google.code.jtracert.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class Node extends DefaultMutableTreeNode {

    private String value;
    private String fileName;

    public Node() {
    }

    public Node(String value) {
        this.value = value;
    }

    public Node(String value, String fileName) {
        this.value = value;
        this.fileName = fileName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return value;
    }
    
}

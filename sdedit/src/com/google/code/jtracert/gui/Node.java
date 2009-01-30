package com.google.code.jtracert.gui;

import java.util.List;
import java.util.ArrayList;

public class Node {

    private Node parent;
    private String value;
    private List<Node> children = new ArrayList<Node>();

    private String diagramFileName;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getDiagramFileName() {
        return diagramFileName;
    }

    public void setDiagramFileName(String diagramFileName) {
        this.diagramFileName = diagramFileName;
    }

    @Override
    public String toString() {
        return getValue();
    }
    
}

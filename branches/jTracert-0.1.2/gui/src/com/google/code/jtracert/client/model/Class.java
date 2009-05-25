package com.google.code.jtracert.client.model;

import java.util.Collection;

public class Class {

    private String name;
    private Collection<Method> methods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Method> getMethods() {
        return methods;
    }

    public void setMethods(Collection<Method> methods) {
        this.methods = methods;
    }
    
}

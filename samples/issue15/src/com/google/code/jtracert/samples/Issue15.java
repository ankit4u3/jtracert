package com.google.code.jtracert.samples;

public class Issue15 {

    public static void main(String[] args) throws Exception {
        Class parserClass = Class.forName("Parser");
        parserClass.newInstance();
    }

}

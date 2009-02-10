package com.google.code.jtracert.samples;

public class Issue9 {

    public Issue9() throws Exception {
        this("argument");
    }

    public Issue9(String argument) throws Exception {
        throw new Exception("test exception");
    }

    public Issue9(int i) {
        this(i, i + 1);
    }

    public Issue9(int i, int j) {
        this(i,j,i+j);
    }

    public Issue9(int i, int j, int k) {
        System.out.println(i + j + k);
    }

    public static void main(String[] arguments) throws Exception {
        try {
            Issue9 issue9 = new Issue9();
        } catch (Exception e) {
        }
        //Issue9 issue9 = new Issue9(1);
    }
    
}

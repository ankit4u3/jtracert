package com.google.code.jtracert.client;

import com.google.code.jtracert.sequenceDiagram.DefaultSequenceDiagramModel;
import com.google.code.jtracert.sequenceDiagram.JSequenceDiagram;
import com.google.code.jtracert.client.model.MethodCallImpl;

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String... arguments) {

        JFrame mainFrame = new JFrame();

        mainFrame.setSize(640,480);
        mainFrame.setTitle("jTracert Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container mainFrameContentPane = mainFrame.getContentPane();

        DefaultSequenceDiagramModel sequenceDiagramModel = new DefaultSequenceDiagramModel();

        MethodCallImpl methodCall = new MethodCallImpl("Class1","method1");

        methodCall.addCallee(new MethodCallImpl("Class2","method1"));

        MethodCallImpl mc1 = new MethodCallImpl("Class2", "method2withverylongname");
        methodCall.addCallee(mc1);

        MethodCallImpl mc2 = new MethodCallImpl("Class1", "methodXYZ");
        mc1.addCallee(mc2);

        mc2.addCallee(new MethodCallImpl("Class2","foobar"));

        methodCall.addCallee(new MethodCallImpl("Class2","foo"));
        methodCall.addCallee(new MethodCallImpl("Class2","bar"));
        methodCall.addCallee(new MethodCallImpl("Class1","innerMethod"));
        methodCall.addCallee(new MethodCallImpl("Class2","foo"));
        

        sequenceDiagramModel.setRootMethodCall(methodCall);

        JSequenceDiagram jSequenceDiagram = new JSequenceDiagram(sequenceDiagramModel);

        mainFrameContentPane.add(jSequenceDiagram);

        mainFrame.setVisible(true);

    }

}

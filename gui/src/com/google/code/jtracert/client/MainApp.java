package com.google.code.jtracert.client;

import com.google.code.jtracert.client.gui.JSequenceDiagramPane;
import com.google.code.jtracert.sequenceDiagram.DefaultSequenceDiagramModel;
import com.google.code.jtracert.sequenceDiagram.SequenceDiagramModel;
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
        methodCall.addCallee(new MethodCallImpl("Class2","method2withverylongname"));

        sequenceDiagramModel.setRootMethodCall(methodCall);

        JSequenceDiagram jSequenceDiagram = new JSequenceDiagram(sequenceDiagramModel);

        mainFrameContentPane.add(jSequenceDiagram);

        mainFrame.setVisible(true);

/*        JFrame mainFrame = new JFrame();

        mainFrame.setSize(640,480);
        mainFrame.setTitle("jTracert Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container mainFrameContentPane = mainFrame.getContentPane();

        SequenceDiagramModel sequenceDiagramModel = new DefaultSequenceDiagramModel();


        MethodCallImpl mc1 = new MethodCallImpl("Class1","");
        com.google.code.jtracert.sequenceDiagram.MethodCall methodCall = mc1;


        MethodCallImpl mc2 = new MethodCallImpl("Class2","method2longName");

        mc1.addCallee(mc2);

        mc1 = new MethodCallImpl("Class3","method3");
        mc2.addCallee(mc1);

        mc1 = new MethodCallImpl("Class3","foo");
        mc2.addCallee(mc1);

        mc1 = new MethodCallImpl("Class3","bar");
        mc2.addCallee(mc1);

        sequenceDiagramModel.setRootMethodCall(methodCall);

        JSequenceDiagramPane jSequenceDiagramPane = new JSequenceDiagramPane();
        jSequenceDiagramPane.setModel(sequenceDiagramModel);

        mainFrameContentPane.add(jSequenceDiagramPane);

        mainFrame.setVisible(true);*/

    }

}

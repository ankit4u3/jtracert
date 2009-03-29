package com.google.code.jtracert.client;

import com.google.code.jtracert.client.gui.JSequenceDiagramPane;
import com.google.code.jtracert.client.gui.DefaultSequenceDiagramModel;
import com.google.code.jtracert.client.gui.SequenceDiagramModel;
import com.google.code.jtracert.client.model.MethodCall;

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String... arguments) {

        JFrame mainFrame = new JFrame();

        mainFrame.setSize(640,480);
        mainFrame.setTitle("jTracert Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container mainFrameContentPane = mainFrame.getContentPane();

        SequenceDiagramModel sequenceDiagramModel = new DefaultSequenceDiagramModel();

        /*MethodCall methodCall = new MethodCall("Class1","method1");

        MethodCall mc;
        methodCall.addCallee(mc = new MethodCall("Class1","method2"));
        mc.addCallee(new MethodCall("Class1","method3"));

        methodCall.addCallee(new MethodCall("Class2","method2"));
        methodCall.addCallee(new MethodCall("Class3","method3"));*/

        MethodCall mc1 = new MethodCall("Class1","");
        MethodCall methodCall = mc1;


        MethodCall mc2 = new MethodCall("Class2","method2longName");

        mc1.addCallee(mc2);

        mc1 = new MethodCall("Class3","method3");
        mc2.addCallee(mc1);

        sequenceDiagramModel.setRootMethodCall(methodCall);

        JSequenceDiagramPane jSequenceDiagramPane = new JSequenceDiagramPane();
        jSequenceDiagramPane.setModel(sequenceDiagramModel);

        mainFrameContentPane.add(jSequenceDiagramPane);

        mainFrame.setVisible(true);

    }

}

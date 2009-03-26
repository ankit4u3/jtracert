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

        MethodCall methodCall = new MethodCall("Class1","method1");
        methodCall.addCallee(new MethodCall("Class2","method2"));
        methodCall.addCallee(new MethodCall("Class3","method3"));

        for (int i = 4; i < 10000; i++) {
            methodCall.addCallee(new MethodCall("Class" + i, "method" + i));
        }

        sequenceDiagramModel.setRootMethodCall(methodCall);

        JSequenceDiagramPane jSequenceDiagramPane = new JSequenceDiagramPane();
        jSequenceDiagramPane.setModel(sequenceDiagramModel);

        mainFrameContentPane.add(jSequenceDiagramPane);

        mainFrame.setVisible(true);

    }

}

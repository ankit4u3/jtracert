package com.google.code.jtracert.gui;

import net.sf.sdedit.Main;
import net.sf.sdedit.editor.Editor;
import net.sf.sdedit.ui.UserInterface;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainApp {

    @Deprecated
    private static final String BASE_PATH = "/home/dmitrybedrin/Documents/VeroRace/VeroRaceDiagrams/";

    public static void main(String[] arguments) throws Exception {

        Main.main(arguments);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UserInterface ui = Editor.getEditor().getUI();

                JFrame uiFrame = (JFrame) ui;

                Node root = new Node();
                root.setValue("Root");

                Node child1 = new Node();
                child1.setParent(root);
                child1.setValue("Child1");
                child1.setDiagramFileName(BASE_PATH + "actionRepository.sdx");
                root.getChildren().add(child1);

                Node child2 = new Node();
                child2.setParent(root);
                child2.setValue("Child2");
                root.getChildren().add(child2);

                Node child21 = new Node();
                child21.setParent(child2);
                child21.setValue("Child21");
                child21.setDiagramFileName(BASE_PATH + "gisPanel.sdx");
                child2.getChildren().add(child21);

                Node child22 = new Node();
                child22.setParent(child2);
                child22.setValue("Child22");
                child22.setDiagramFileName(BASE_PATH + "networkTreeSelect.sdx");
                child2.getChildren().add(child22);

                final JTree jtree = new JTree();
                jtree.setModel(new JTracertTreeModel(root));

                jtree.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if ((MouseEvent.BUTTON1 == e.getButton()) && (2 == e.getClickCount())) {

                            TreePath treePath =
                                    jtree.getPathForLocation(e.getX(), e.getY());

                            if (null != treePath) {
                                Node selectedNode = (Node) treePath.getLastPathComponent();
                                if (jtree.getModel().isLeaf(selectedNode)) {
                                    System.out.println(selectedNode);
                                }

                                String fileName = selectedNode.getDiagramFileName();

                                try {
                                    Editor.getEditor().loadCode(new File(fileName));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                        
                    }

                });
                
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setViewportView(jtree);

                JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
                tabbedPane.addTab("jTracert", scrollPane);

                uiFrame.getContentPane().add(tabbedPane, BorderLayout.WEST);
                uiFrame.repaint();


            }
            
        });

    }

}

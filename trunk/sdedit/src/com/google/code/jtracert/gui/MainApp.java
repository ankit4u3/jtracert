package com.google.code.jtracert.gui;

import net.sf.sdedit.Main;
import net.sf.sdedit.editor.Editor;
import net.sf.sdedit.ui.UserInterface;
import net.sf.sdedit.ui.components.buttons.Activator;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainApp {

    @Deprecated
    private static final String BASE_PATH = "/home/dmitrybedrin/Documents/VeroRace/VeroRaceDiagrams/";

    public static void main(String[] arguments) throws Exception {

        AgentConnectionDialog agentConnectionDialog = new AgentConnectionDialog();
        agentConnectionDialog.setVisible(true);

        AgentConnectionSettings agentConnectionSettings =
                agentConnectionDialog.getAgentConnectionSettings();

        System.out.println(agentConnectionSettings);

        AgentConnection agentConnection = new AgentConnection(agentConnectionSettings);
        agentConnection.connect();

        /*Main.main(arguments);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                Editor.getEditor().getUI().addAction("Extras", new Action() {

                    private Map<String,Object> values = new HashMap<String,Object>();

                    {
                        values.put(Action.NAME,"Bla");
                    }
                    
                    public Object getValue(String key) {
                        return values.get(key);
                    }

                    public void putValue(String key, Object value) {
                        values.put(key, value);
                    }

                    public void setEnabled(boolean b) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public boolean isEnabled() {
                        return true;
                    }

                    public void addPropertyChangeListener(PropertyChangeListener listener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public void removePropertyChangeListener(PropertyChangeListener listener) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog((JFrame) Editor.getEditor().getUI(),"Selected");
                    }

                }, new Activator() {

                    public boolean isEnabled() {
                        return true;
                    }
                });

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

                                if (null != fileName) {
                                    try {
                                        Editor.getEditor().loadCode(new File(fileName));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            }
                        }
                        
                    }

                });
                
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setViewportView(jtree);

                JTabbedPane jTracertTabbedPane = new JTabbedPane(JTabbedPane.TOP);
                jTracertTabbedPane.addTab("jTracert", scrollPane);

                Component sdEditTabbedPane = uiFrame.getContentPane().getComponent(0);

                JSplitPane jTracertSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jTracertTabbedPane, sdEditTabbedPane);

                uiFrame.getContentPane().remove(sdEditTabbedPane);
                uiFrame.getContentPane().add(jTracertSplitter, BorderLayout.CENTER);
                //uiFrame.getContentPane().add(jTracertTabbedPane, BorderLayout.WEST);

                uiFrame.repaint();


            }
            
        });*/

    }

}

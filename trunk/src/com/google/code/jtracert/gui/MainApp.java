package com.google.code.jtracert.gui;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditFileClient;
import net.sf.sdedit.Main;
import net.sf.sdedit.editor.Editor;
import net.sf.sdedit.ui.UserInterface;
import net.sf.sdedit.ui.components.buttons.Activator;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import java.beans.PropertyChangeListener;

/**
 * This class provides a GUI frontend for jTracert agent
 * <p/>
 * First of all you should execute your application with jTracert javaagent
 * Just add following parameter to java command:
 * -javaagent:<PATH_TO_J_TRACERT_JAR>/<J_TRACERT_AGENT_JAR>=<J_TRACERT_AGENT_PORT>
 * Where:
 * <PATH_TO_J_TRACERT_JAR> is a path to jTracert agent jar, like C:/jTracert
 * <J_TRACERT_AGENT_JAR is a jTracert agent jar name, like jTracer-0.1.0.jar
 * <J_TRACERT_AGENT_PORT> is port number you want to use for agent-GUI negotiation, like 7007
 * <p/>
 * An example may look like below:
 * -javaagent:../../../deploy/jTracert.jar=7007
 * <p/>
 * Now, execute the GUI and fill in some simple properties in agent connection dialog
 * <p/>
 * That's it!
 *
 * @author Dmitry.Bedrin@gmail.com
 * @todo consider serious refactoring; this is just proof of concept implementation!
 * <p/>
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 */
public class MainApp {

    private static JTree jtree;
    private static JTracertTreeModel jTracertTreeModel;

    protected static Set<String> jarUrls = new HashSet<String>();

    private static void processMethodCall(MethodCall methodCall) {
       String jarUrl = methodCall.getJarUrl();

        if (!jarUrls.contains(jarUrl)) {
            jarUrls.add(jarUrl);
        }

        for (MethodCall callee : methodCall.getCallees()) {
            processMethodCall(callee);
        }

    }

    /**
     * @param arguments
     * @throws Exception
     */
    public static void main(String[] arguments) throws Exception {

        try {

            final AgentConnectionDialog agentConnectionDialog = new AgentConnectionDialog();
            agentConnectionDialog.setVisible(true);

            final AgentConnectionSettings agentConnectionSettings =
                    agentConnectionDialog.getAgentConnectionSettings();

            final AgentConnection agentConnection = new AgentConnection(agentConnectionSettings);
            agentConnection.connect();

            agentConnection.addMethodCallListener(new MethodCallListener() {

                public void onMethodCall(MethodCallEvent methodCallEvent) {

                    try {
                        MethodCall methodCall = methodCallEvent.getMethodCall();

                        processMethodCall(methodCall);

                        SDEditFileClient methodCallProcessor = new SDEditFileClient();

                        AnalyzeProperties analyzeProperties = new AnalyzeProperties();
                        analyzeProperties.setOutputFolder(agentConnectionDialog.getFolder());

                        methodCallProcessor.setAnalyzeProperties(analyzeProperties);

                        File diagramFile = methodCallProcessor.saveMethodCall(methodCall);
                        String diagramFileName = diagramFile.getAbsolutePath();

                        Node rootNode = (Node) jTracertTreeModel.getRoot();

                        String className = methodCall.getRealClassName();
                        String[] packages = className.split("\\.");

                        Node contextNode = rootNode;

                        for (int i = 0; i < packages.length - 1; i++) {

                            String packageName = packages[i];

                            boolean nodeFound = false;

                            Enumeration children = contextNode.children();
                            while (children.hasMoreElements()) {
                                Node node = (Node) children.nextElement();

                                if (node.getAllowsChildren() && (node.getValue().equals(packageName))) {
                                    contextNode = node;
                                    nodeFound = true;
                                    break;
                                }

                            }

                            if (!nodeFound) {
                                Node node = new Node(packageName);
                                node.setAllowsChildren(true);
                                jTracertTreeModel.insertNodeInto(node, contextNode, contextNode.getChildCount());
                                contextNode = node;
                            }

                        }

                        Node node = new Node(diagramFile.getName().substring(0, diagramFile.getName().length() - 4), diagramFileName);
                        node.setAllowsChildren(false);

                        jTracertTreeModel.insertNodeInto(node, contextNode, contextNode.getChildCount());

                    } catch (Exception e) {
                        processException(e);
                    }

                }

            });

            Main.main(arguments);

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    UserInterface ui = Editor.getEditor().getUI();

                    final JFrame uiFrame = (JFrame) ui;

                    uiFrame.setTitle("jTracert GUI version 0.1.0");


                    Editor.getEditor().getUI().addAction("Help", new Action() {

                        private Map<String,Object> values = new HashMap<String,Object>();

                        {
                            values.put(Action.NAME,"About jTracert");
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
                            JTracertAboutDialog aboutDialog = new
                                    JTracertAboutDialog(uiFrame, true);
                            aboutDialog.setVisible(true);
                        }

                    }, new Activator() {

                        public boolean isEnabled() {
                            return true;
                        }
                    });

                    Editor.getEditor().getUI().addAction("Extras", new Action() {

                        private Map<String,Object> values = new HashMap<String,Object>();

                        {
                            values.put(Action.NAME,"View dependencies");
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
                            DependenciesDialog dependenciesDialog =
                                    new DependenciesDialog(uiFrame, true);
                            dependenciesDialog.setVisible(true);
                        }

                    }, new Activator() {

                        public boolean isEnabled() {
                            return true;
                        }
                    });




                    Node root = new Node("Packages");

                    jtree = new JTree();
                    jTracertTreeModel = new JTracertTreeModel(root);
                    jtree.setModel(jTracertTreeModel);

                    jtree.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            if ((MouseEvent.BUTTON1 == e.getButton()) && (2 == e.getClickCount())) {

                                TreePath treePath =
                                        jtree.getPathForLocation(e.getX(), e.getY());

                                if (null != treePath) {
                                    Node selectedNode = (Node) treePath.getLastPathComponent();

                                    if (selectedNode.isLeaf() && !selectedNode.getAllowsChildren()) {

                                        String fileName = selectedNode.getFileName();

                                        if (null != fileName) {
                                            try {
                                                Editor.getEditor().loadCode(new File(fileName));
                                            } catch (Exception ex) {
                                                processException(ex);
                                            }
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

                    uiFrame.repaint();

                    agentConnection.start();

                }

            });

        } catch (Exception e) {
            processException(e);
        }

    }

    /**
     * @todo todo handle exceptions correctly
     * @param e
     */
    @Deprecated
    static void processException(Exception e) {
        // do nothing
    }

}

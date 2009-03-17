package com.google.code.jtracert.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class AgentConnectionDialog extends JDialog implements ActionListener {

    private JTextField folderTextField;
    private JTextField agentAddressTextField;
    private JTextField agentPortTextField;
    private JTextField classNameRegExTextField;

    private AgentConnectionSettings agentConnectionSettings;

    /**
     * @return
     */
    public String getFolder() {
        return folderTextField.getText();
    }

    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        String folder = folderTextField.getText();
        String address = agentAddressTextField.getText();
        String port = agentPortTextField.getText();
        String pattern = classNameRegExTextField.getText();

        InetAddress inetAddress;
        int inetPort;

        if ("Select project folder".equals(folder)) {
            JOptionPane.showMessageDialog(this, "Please select project folder!", "jTracert GUI Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(this, "Unknown address!", "jTracert GUI Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            inetPort = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid port!", "jTracert GUI Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((null != pattern) && (!"".equals(pattern.trim()))) {
            try {
                Pattern.compile(pattern);
            } catch (PatternSyntaxException ex) {
                JOptionPane.showMessageDialog(this, "Invalid class name pattern!", "jTracert GUI Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        agentConnectionSettings = new AgentConnectionSettings(inetAddress, inetPort, pattern);

        dispose();

    }

    /**
     * @return
     */
    public AgentConnectionSettings getAgentConnectionSettings() {
        return agentConnectionSettings;
    }

    /**
     * @throws HeadlessException
     */
    public AgentConnectionDialog() throws HeadlessException {

        super();

        setSize(400, 250);
        setTitle("jTracert Connection Wizard");
        setResizable(false);
        setModal(true);
        setBounds(getFrameBounds());

        createGUI();

    }

    /**
     *
     */
    private void createGUI() {

        Container contentPane = getContentPane();

        contentPane.setLayout(new GridBagLayout());

        JLabel headerLabel = createHeaderLabel();

        JLabel folderLabel = createFolderLabel();
        folderTextField = createFolderTextField();
        JButton browseFolderButton = createBrowseFolderButton(folderTextField);

        folderLabel.setLabelFor(folderTextField);

        JLabel agentAddressLabel = createAgentAddressLabel();
        agentAddressTextField = createAgentAddressTextField();
        agentAddressLabel.setLabelFor(agentAddressTextField);

        JLabel agentPortLabel = createAgentPortLabel();
        agentPortTextField = createAgentPortTextField();
        agentPortLabel.setLabelFor(agentPortTextField);

        JLabel classNameRegExLabel = createClassNameRegExLabel();
        classNameRegExTextField = createClassNameRegExTextField();
        classNameRegExLabel.setLabelFor(classNameRegExTextField);

        // Header

        contentPane.add(headerLabel, new GridBagConstraints(0, 0, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 0, 10), 0, 0
        ));

        // Folder

        contentPane.add(folderLabel, new GridBagConstraints(0, 1, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 0, 5), 0, 0
        ));

        contentPane.add(folderTextField, new GridBagConstraints(0, 2, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 0), 0, 0
        ));

        contentPane.add(browseFolderButton, new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        // Agent address

        contentPane.add(agentAddressLabel, new GridBagConstraints(0, 3, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        contentPane.add(agentAddressTextField, new GridBagConstraints(0, 4, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        // Agent label

        contentPane.add(agentPortLabel, new GridBagConstraints(0, 5, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        contentPane.add(agentPortTextField, new GridBagConstraints(0, 6, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        // Class name pattern

        contentPane.add(classNameRegExLabel, new GridBagConstraints(0, 7, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        contentPane.add(classNameRegExTextField, new GridBagConstraints(0, 8, 2, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(0, 5, 0, 5), 0, 0
        ));

        // Buttons

        JButton cancelButton = createCancelButton();
        JButton connectButton = createConnectButton();

        contentPane.add(cancelButton, new GridBagConstraints(0, 9, 1, 1, 1, 1,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));


        contentPane.add(connectButton, new GridBagConstraints(1, 9, 1, 1, 0, 1,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 5), 0, 0));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    /**
     * @return
     */
    private JButton createConnectButton() {
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        return connectButton;
    }

    /**
     * @return
     */
    private JButton createCancelButton() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        );
        return cancelButton;
    }

    /**
     * @return
     */
    private JTextField createClassNameRegExTextField() {
        JTextField classNameRegExTextField = new JTextField();
        classNameRegExTextField.setText("com\\.mycompany.*");
        return classNameRegExTextField;
    }

    /**
     * @return
     */
    private JLabel createClassNameRegExLabel() {
        JLabel classNameRegExLabel = new JLabel();
        classNameRegExLabel.setText("Class filter (regular expression like \"com\\.mycompany.*\")");
        return classNameRegExLabel;
    }

    /**
     * @return
     */
    private JTextField createAgentPortTextField() {
        JTextField agentPortTextField = new JTextField();
        agentPortTextField.setText("7007");
        return agentPortTextField;
    }

    /**
     * @return
     */
    private JLabel createAgentPortLabel() {
        JLabel agentPortLabel = new JLabel();
        agentPortLabel.setText("jTracert Agent port");
        return agentPortLabel;
    }

    /**
     * @return
     */
    private JTextField createAgentAddressTextField() {
        JTextField agentAddressTextField = new JTextField();
        agentAddressTextField.setText("127.0.0.1");
        return agentAddressTextField;
    }

    /**
     * @return
     */
    private JLabel createAgentAddressLabel() {
        JLabel agentAddressLabel = new JLabel();
        agentAddressLabel.setText("jTracert Agent address");
        return agentAddressLabel;
    }

    /**
     * @param folderTextField
     * @return
     */
    private JButton createBrowseFolderButton(final JTextField folderTextField) {
        JButton browseFolderButton = new JButton();
        browseFolderButton.setText("Browse");

        browseFolderButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        chooseFolder(folderTextField);
                    }
                }
        );

        return browseFolderButton;
    }

    /**
     * @param folderTextField
     */
    private void chooseFolder(JTextField folderTextField) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(self())) {
            File selectedFile = fileChooser.getSelectedFile();
            folderTextField.setText(selectedFile.getAbsolutePath());
        }

    }

    /**
     * @return
     */
    private JTextField createFolderTextField() {
        final JTextField folderTextField = new JTextField();
        folderTextField.setText("Select project folder");
        folderTextField.setEditable(false);
        folderTextField.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        chooseFolder(folderTextField);
                    }
                }
        );
        return folderTextField;
    }

    /**
     * @return
     */
    private JLabel createFolderLabel() {
        JLabel folderLabel = new JLabel();
        folderLabel.setText("Select project folder");
        return folderLabel;
    }

    /**
     * @return
     */
    private JLabel createHeaderLabel() {
        JLabel headerLabel = new JLabel();
        headerLabel.setText("jTracert Agent Connection Wizard");
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        Font labelFont = headerLabel.getFont();
        labelFont = labelFont.deriveFont(Font.BOLD, 18);
        headerLabel.setFont(labelFont);
        return headerLabel;
    }

    /**
     * @return
     */
    private AgentConnectionDialog self() {
        return this;
    }

    /**
     * @return
     */
    private Rectangle getFrameBounds() {

        Dimension wizardDimension = getSize();

        double width = wizardDimension.getWidth();
        double height = wizardDimension.getHeight();

        Rectangle rectangle = new Rectangle(
                new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize()
        );

        double f = 1.0F;
        int i = (int) (f * width);
        int j = (int) (f * height);
        int k = rectangle.width - (i != -1 ? i : getWidth());
        int l = rectangle.height - (j != -1 ? j : getHeight());
        rectangle.x += k / 2;
        rectangle.width -= k;
        rectangle.y += l / 2;
        rectangle.height -= l;
        return rectangle;

    }

}

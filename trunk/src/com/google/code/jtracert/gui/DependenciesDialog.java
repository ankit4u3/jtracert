package com.google.code.jtracert.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DependenciesDialog extends JDialog {

    public DependenciesDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        createGui();
    }

    private void createGui() {

        setTitle("Dependencies");

        JTable dependenciesTable = new JTable();

        String[][] data;

        data = new String[MainApp.jarUrls.size()][1];
        int i = 0;
        for (String jar : MainApp.jarUrls) {
            data[i][0] = jar;
            i++;
        }

        dependenciesTable.setModel(
                new DefaultTableModel(
                        data,
                        new String[]{"Jar URL"}
                ) {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                    
                });

        Container contentPane = getContentPane();

        contentPane.setLayout(new BorderLayout());

        contentPane.add(dependenciesTable, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        contentPane.add(okButton, BorderLayout.SOUTH);

        setSize(600, 440);
        setResizable(true);
        setModal(true);
        setBounds(getFrameBounds());

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

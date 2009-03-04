package com.google.code.jtracert.gui;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @todo consider removing this class and using DefaultTreeModel directly
 *
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class JTracertTreeModel extends DefaultTreeModel {

    /**
     * 
     * @param root
     */
    public JTracertTreeModel(TreeNode root) {
        super(root);
    }

}
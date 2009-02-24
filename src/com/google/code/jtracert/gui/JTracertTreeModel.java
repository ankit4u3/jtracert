package com.google.code.jtracert.gui;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @todo consider removing this class and using DefaultTreeModel directly
 */
public class JTracertTreeModel extends DefaultTreeModel {

    public JTracertTreeModel(TreeNode root) {
        super(root);
    }

}
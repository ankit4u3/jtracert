package com.google.code.jtracert.gui;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author Dmitry.Bedrin@gmail.com
 * @todo consider removing this class and using DefaultTreeModel directly
 * <p/>
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 */
public class JTracertTreeModel extends DefaultTreeModel {

    /**
     * @param root
     */
    public JTracertTreeModel(TreeNode root) {
        super(root);
    }

}
package Server;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class Tree {
    JTree tree;

    public Tree(SticksCollection coll){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
        DefaultMutableTreeNode sticks = new DefaultMutableTreeNode("Sticks");
        root.add(sticks);
        setTree(coll, root);
        tree = new JTree(root);
    }

    public void setTree(SticksCollection coll, DefaultMutableTreeNode root) {
        DefaultMutableTreeNode sticks = (DefaultMutableTreeNode) root.getFirstChild();
        coll.getMyColl().forEach((item)->{
                sticks.add(new DefaultMutableTreeNode(item));
        });
    }

    public JTree getTree(){
        return tree;
    }
}

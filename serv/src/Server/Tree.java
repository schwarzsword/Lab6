package Server;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

    public class Tree {
        JTree tree;

        public Tree(SticksCollection myColl){
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
            DefaultMutableTreeNode stroot = new DefaultMutableTreeNode("Sticks");
            root.add(stroot);
            setTree(myColl, root);
            tree = new JTree(root);
        }

        public void setTree(SticksCollection myColl, DefaultMutableTreeNode root) {
            DefaultMutableTreeNode Sticks = (DefaultMutableTreeNode) root.getFirstChild();
            myColl.getMyColl().forEach((e)->{Sticks.add(new DefaultMutableTreeNode(e));
            });
        }

        public JTree getTree(){
            return tree;
        }
    }



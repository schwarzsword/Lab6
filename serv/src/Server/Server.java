package Server;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

public class Server{
    static int port = 62091;
    static SticksCollection myColl = new SticksCollection();
    static String way= "/home/schwarz/ucheb/prog/l7/serv/myconfig.csv";
    static String pass = "";

    static class ServGUI extends JFrame{
        Font font1 = new Font("Courier", Font.PLAIN, 16);
        Font font2 = new Font("Courier", Font.BOLD,14);

         ServGUI(){
            super("Server");
            UIManager.put("OptionPane.messageFont", font1);
            UIManager.put("OptionPane.buttonFont", font2);
            logIn(this);
        }

        void logIn(JFrame current){
            JFrame login = new JFrame("Sign in");
            login.setDefaultCloseOperation(3);
            login.setFont(font1);
            login.setSize(200, 110);
            login.setResizable(false);
            login.setLocationRelativeTo(null);

            JLabel promt = new JLabel("Enter password");
            promt.setFont(font1);
            promt.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            promt.setAlignmentY(Component.TOP_ALIGNMENT);

            JPasswordField pass = new JPasswordField();
            pass.setFont(font1);
            pass.setAlignmentY(Component.CENTER_ALIGNMENT);
            pass.setAlignmentX(Component.CENTER_ALIGNMENT);
            pass.setEchoChar('*');
            pass.setMaximumSize(new Dimension(150, 25));

            JButton ok = new JButton("Ok");
            ok.setFont(font2);
            ok.setSize(50, 30);
            ok.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            ok.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
            ok.addActionListener((event)->
            {
                if(new String(pass.getPassword()).equals(Server.pass)){
                    login.dispose();
                    collection();
                }
                else {
                    JOptionPane.showMessageDialog(login, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            login.setLayout(new BoxLayout(login.getContentPane(), 1));
            login.add(promt);
            login.add(pass);
            login.add(ok);
            login.getRootPane().setDefaultButton(ok);
            login.setVisible(true);
            current.setEnabled(false);
        }


        static void exit(JFrame frame) {
            if (JOptionPane.showConfirmDialog(frame,"Exit without save??","Exit",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==0)
                System.exit(0);
                //myColl.save(way);
        }


        void settings(Stick stick, JTree jTree, DefaultMutableTreeNode node, boolean add){
            JFrame properties = new JFrame("Properties");
            setEnabled(false);
            properties.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            properties.setFont(font1);
            properties.setSize(300, 400);
            properties.setResizable(false);
            properties.setLocationRelativeTo(null);

            JLabel textName = new JLabel("Name:");
            textName.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel textXB = new JLabel("Coord X Begin:");
            textXB.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel textYB = new JLabel("Coord Y Begin:");
            textYB.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel textXE = new JLabel("Coord X End:");
            textXE.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel textYE = new JLabel("Coord Y End:");
            textYE.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel textMat = new JLabel("Material:");
            textMat.setAlignmentX(Component.RIGHT_ALIGNMENT);

            JTextField name = new JTextField(stick.getStickName());
            name.setAlignmentX(Component.CENTER_ALIGNMENT);
            name.setMaximumSize(new Dimension(300, 70));
            JTextField XB = new JTextField(Integer.toString(stick.getStickCoordBeg().x));
            XB.setAlignmentX(Component.CENTER_ALIGNMENT);
            XB.setMaximumSize(name.getMaximumSize());
            JTextField YB = new JTextField(Integer.toString(stick.getStickCoordBeg().y));
            YB.setAlignmentX(Component.CENTER_ALIGNMENT);
            YB.setMaximumSize(name.getMaximumSize());
            JTextField XE = new JTextField(Integer.toString(stick.getStickCoordEnd().x));
            XE.setAlignmentX(Component.CENTER_ALIGNMENT);
            XE.setMaximumSize(name.getMaximumSize());
            JTextField YE = new JTextField(Integer.toString(stick.getStickCoordEnd().y));
            YE.setAlignmentX(Component.CENTER_ALIGNMENT);
            YE.setMaximumSize(name.getMaximumSize());
            JComboBox material = new JComboBox(Material.values());
            material.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton cancel = new JButton("Cancel");
            cancel.setFont(font2);
            cancel.setPreferredSize(new Dimension(100,20));
            cancel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            cancel.addActionListener((event)-> {
                properties.dispose();
                setVisible(true);
                setEnabled(true);
            });

            JButton ok = new JButton("  Ok  ");
            ok.setFont(font2);
            ok.setAlignmentX(Component.RIGHT_ALIGNMENT);
            ok.addActionListener((event)->
            {
                try {
                    stick.setStickName(name.getText());
                    stick.setStickCoordBeg(Integer.parseInt(XB.getText()), Integer.parseInt(YB.getText()));
                    stick.setStickCoordEnd(Integer.parseInt(XE.getText()), Integer.parseInt(YE.getText()));
                    stick.setMaterial((Material) material.getSelectedItem());

                    if (!add) {
                    } else {
                        myColl.add(stick);
                        myColl.save(way);
                        node.add(new DefaultMutableTreeNode(stick));
                    }
                    jTree.updateUI();
                    properties.dispose();
                    setVisible(true);
                    setEnabled(true);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(properties, "Неверный формат числового поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e){
                    JOptionPane.showMessageDialog(properties, "Невреный формат поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            });

            JPanel space1 = new JPanel();
            space1.setMaximumSize(new Dimension(200, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);
            JPanel space4 = new JPanel();
            space1.setMaximumSize(new Dimension(200, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel space2 = new JPanel();
            space1.setMaximumSize(new Dimension(10, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel space3 = new JPanel();
            space1.setMaximumSize(new Dimension(10, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel butts = new JPanel();
            butts.setLayout(new BoxLayout(butts, 0));
            butts.add(space3);
            butts.add(ok);
            butts.add(cancel);
            butts.add(space2);


            ok.setSize(cancel.getSize());
            properties.setLayout(new BoxLayout(properties.getContentPane(), 1));
            properties.add(textName);
            properties.add(name);
            properties.add(textXB);
            properties.add(XB);
            properties.add(textYB);
            properties.add(YB);
            properties.add(textXE);
            properties.add(XE);
            properties.add(textYE);
            properties.add(YE);
            properties.add(textMat);
            properties.add(material);
            properties.add(space1);
            properties.add(butts);
            properties.add(space4);
            properties.pack();
            properties.setVisible(true);

        }


        void collection() {
            this.setFont(font1);
            this.setSize(new Dimension(700, 520));
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            Tree td = new Tree(myColl);
            JTree MyTree = td.getTree();
            MyTree.setSize(new Dimension(100, 200));
            DefaultTreeModel model = (DefaultTreeModel) MyTree.getModel();

            JScrollPane scrollPane = new JScrollPane(MyTree);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(600, 520));

            JLabel selectedLabel = new JLabel(" ");

            MyTree.getSelectionModel().addTreeSelectionListener((event) -> {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try {
                    selectedLabel.setText(((Stick) selectedNode.getUserObject()).getStickName());
                } catch (NullPointerException e1) {
                    selectedLabel.setText("Tree closed");
                } catch (ClassCastException e2) {
                    selectedLabel.setText(selectedNode.getUserObject().toString());
                }
            });
            {
            JMenuBar menuBar = new JMenuBar();

            JMenu mainMenu = new JMenu("Collection");
            mainMenu.setFont(font2);
            mainMenu.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            JMenuItem loadItem = new JMenuItem("Import");
            loadItem.setFont(font2);
            mainMenu.add(loadItem);
            loadItem.addActionListener((event) -> {
                myColl.collectionImport(way);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                DefaultMutableTreeNode male = (DefaultMutableTreeNode) root.getFirstChild();
                male.removeAllChildren();
                td.setTree(myColl, root);
                MyTree.updateUI();
                scrollPane.setPreferredSize(MyTree.getSize());
                scrollPane.revalidate();
            });

            JMenuItem saveItem = new JMenuItem("Save");
            saveItem.setFont(font2);
            mainMenu.add(saveItem);
            saveItem.addActionListener((event) -> {
                myColl.save(way);
                JOptionPane.showMessageDialog(null, "Collection saved", "Report", JOptionPane.INFORMATION_MESSAGE);
            });

            JMenuItem deleteItem = new JMenuItem("Delete");
            deleteItem.setFont(font2);
            mainMenu.add(deleteItem);
            deleteItem.addActionListener((event) -> {
                myColl.getMyColl().clear();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) model.getRoot();
                DefaultMutableTreeNode stick = (DefaultMutableTreeNode) selectedNode.getChildAt(0);
                if (!stick.isLeaf()) stick.removeAllChildren();
                MyTree.updateUI();
                scrollPane.revalidate();
                JOptionPane.showMessageDialog(null, "Collection is cleared", "Report", JOptionPane.INFORMATION_MESSAGE);
            });

            mainMenu.addSeparator();

            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.setFont(font2);
            mainMenu.add(exitItem);
            exitItem.addActionListener((event) -> exit(this));

            menuBar.add(mainMenu);
            setJMenuBar(menuBar);
        }
            JLabel collSize = new JLabel("Collection size: "+myColl.getMyColl().size());
            collSize.setFont(new Font("Courier",1,14 ));
            collSize.setAlignmentY(Component.CENTER_ALIGNMENT);

            JButton delete = new JButton(" Delete ");
             delete.setAlignmentX(Component.CENTER_ALIGNMENT);
            delete.setFont(font2);
            delete.setSize(new Dimension(70, 30));
            delete.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
                    parent.remove(parent.getIndex(selectedNode));
                    myColl.getMyColl().remove(temp);
                    MyTree.updateUI();
                    scrollPane.revalidate();
                    collSize.setText("Collection size: "+myColl.getMyColl().size());

                } catch (ClassCastException e2) {
                    JOptionPane.showMessageDialog(null,"Выбрана не одежда","Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Элемент не выбран","Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton change = new JButton(" Change ");
            change.setAlignmentX(Component.CENTER_ALIGNMENT);
            change.setFont(font2);
            change.setSize(delete.getSize());
            change.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    settings(temp, MyTree,selectedNode, false);
                    MyTree.updateUI();
                    collSize.setText("Collection size: "+myColl.getMyColl().size());
                } catch (ClassCastException e2) {
                    JOptionPane.showMessageDialog(null,"Can't change folder","Error", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Choose the element","Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton addButton = new JButton("  Add  ");
            addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addButton.setFont(font2);
            addButton.setSize(delete.getSize());
            addButton.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    if (temp != null) {
                        JOptionPane.showMessageDialog(null, "Choose folder", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassCastException e2) {
                    if(!selectedNode.isRoot()) {
                        Stick temp = new Stick("Stick", 70,50,100,100, Material.ELM, "");
                        settings(temp, MyTree, selectedNode, true);
                        scrollPane.revalidate();
                        collSize.setText("Collection size: "+myColl.getMyColl().size());
                    }
                    else JOptionPane.showMessageDialog(null,"Can't change root","Error", JOptionPane.INFORMATION_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Choose element","Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel space1 = new JPanel();
            space1.setMaximumSize(new Dimension(200, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel space2 = new JPanel();
            space2.setMaximumSize(new Dimension(200, 30));
            space2.setAlignmentX(Component.CENTER_ALIGNMENT);

//            JPanel space3 = new JPanel();
//            space2.setMaximumSize(new Dimension(200, 300));
//            space2.setAlignmentX(Component.TOP_ALIGNMENT);
//            JLabel initDate1 = new JLabel("Initialization date:");
//            initDate1.setFont(new Font("Courier",1,14 ));
//            initDate1.setAlignmentY(Component.CENTER_ALIGNMENT);
//
//            JLabel initDate2 = new JLabel(""+myColl.getInitialization()+"  ");
//            initDate2.setFont(new Font("Courier",1,14 ));
//            initDate2.setAlignmentY(Component.CENTER_ALIGNMENT);

            JLabel information = new JLabel("Info: ");
            information.setFont(new Font("Courier",1,18 ));

//            JPanel infoPane = new JPanel();
//            infoPane.setLayout(new BoxLayout(infoPane, 1));
//            //infoPane.add(space3);
//            infoPane.add(information);
//            infoPane.add(collSize);
//            infoPane.add(initDate1);
//            infoPane.add(initDate2);

            JPanel editCollPane = new JPanel();
            BoxLayout b = new BoxLayout(editCollPane, 1);
            editCollPane.setLayout(new BoxLayout(editCollPane, 1));
            editCollPane.add(addButton);
            editCollPane.add(space1);
            editCollPane.add(change);
            editCollPane.add(space2);
            editCollPane.add(delete);
            editCollPane.setPreferredSize(new Dimension(200, 520));


            this.setLayout(new BoxLayout(this.getContentPane(), 0));
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //this.add(infoPane);
            this.add(scrollPane);
            this.add(editCollPane);
            this.setLocationRelativeTo(null);
            this.setEnabled(true);
            this.setVisible(true);
        }

    }

    public static void main(String ... args) {
        Thread t = new Thread(ServGUI::new);
        SwingUtilities.invokeLater(t);
        myColl.collectionImport(way);

        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    myColl.save(way);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Save ", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            ServerSocket serverSock = new ServerSocket(port);
            while (!serverSock.isClosed()) {
                if (t.isInterrupted()) {
                    System.exit(0);
                } else {
                    Socket sock = serverSock.accept();
                    new Thread(new ServerThread(sock, myColl, way)).start();
                }
            }

        } catch (BindException e) {
            System.out.println("Server is already running!");
            System.exit(0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }}
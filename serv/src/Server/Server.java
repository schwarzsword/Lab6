package Server;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

public class Server{
    static int port = 62091;
    static SticksCollection myColl = new SticksCollection();
    static String way= System.getenv("MyPath");
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
            if (JOptionPane.showConfirmDialog(frame,"Close server?","Exit",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==0)
                System.exit(0);
        }


        void settings(Stick stick, JTree jTree, DefaultMutableTreeNode node, boolean add){
            JFrame properties = new JFrame("Properties");
            setEnabled(false);
            properties.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            properties.setFont(font1);
            properties.setSize(285, 360);
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
            JTextField XB = new JTextField(Integer.toString(stick.getStickCoordBeg().x));
            XB.setAlignmentX(Component.CENTER_ALIGNMENT);
            JTextField YB = new JTextField(Integer.toString(stick.getStickCoordBeg().y));
            YB.setAlignmentX(Component.CENTER_ALIGNMENT);
            JTextField XE = new JTextField(Integer.toString(stick.getStickCoordEnd().x));
            XE.setAlignmentX(Component.CENTER_ALIGNMENT);
            JTextField YE = new JTextField(Integer.toString(stick.getStickCoordEnd().y));
            YE.setAlignmentX(Component.CENTER_ALIGNMENT);
            JComboBox material = new JComboBox(Material.values());
            material.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton cancel = new JButton("Cancel");
            cancel.setFont(font2);
            cancel.setPreferredSize(new Dimension(100,20));
            cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancel.addActionListener((event)-> {
                properties.dispose();
                setVisible(true);
                setEnabled(true);
            });

            JButton ok = new JButton("Ok");
            ok.setFont(font2);
            ok.setAlignmentX(Component.CENTER_ALIGNMENT);
            ok.addActionListener((event)->
            {
                try {Stick toAdd = new Stick(name.getText(), Integer.parseInt(XB.getText()), Integer.parseInt(YB.getText()),Integer.parseInt(XE.getText()),Integer.parseInt(YE.getText()),(Material) material.getSelectedItem() );
                    stick.setStickName(name.getText());
                    stick.setStickCoordBeg(Integer.parseInt(XB.getText()), Integer.parseInt(YB.getText()));
                    stick.setStickCoordEnd(Integer.parseInt(XE.getText()), Integer.parseInt(YE.getText()));
                    stick.setMaterial((Material) material.getSelectedItem());

                    if (add) {
                    } else {
                        myColl.add(toAdd);
                        node.add(new DefaultMutableTreeNode(toAdd));
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
            properties.add(ok);
            properties.add(cancel);
            properties.pack();
            properties.setVisible(true);

        }


        void collection(){
            this.setFont(font1);
            this.setSize(new Dimension(520,520));

            Tree td = new Tree(myColl);
            JTree MyTree = td.getTree();
            MyTree.setSize(new Dimension(100, 200));
            DefaultTreeModel model = (DefaultTreeModel)MyTree.getModel();

            JScrollPane scrollPane = new JScrollPane(MyTree);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(100, 200));

            JLabel selectedLabel = new JLabel(" ");

            MyTree.getSelectionModel().addTreeSelectionListener((event)-> {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try {
                    selectedLabel.setText(((Stick) selectedNode.getUserObject()).getStickName());
                } catch (NullPointerException e1) {
                    selectedLabel.setText("Tree closed");
                } catch (ClassCastException e2) {
                    selectedLabel.setText(selectedNode.getUserObject().toString());
                }
            });
            JMenuBar menuBar = new JMenuBar();

            JMenu mainMenu = new JMenu("Collection");
            mainMenu.setFont(font2);

            JMenuItem loadItem = new JMenuItem("Import");
            loadItem.setFont(font2);
            mainMenu.add(loadItem);
            loadItem.addActionListener((event)-> {
                myColl.collectionImport(way);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                DefaultMutableTreeNode male = (DefaultMutableTreeNode) root.getFirstChild();
                DefaultMutableTreeNode female = (DefaultMutableTreeNode) root.getLastChild();
                male.removeAllChildren();
                female.removeAllChildren();
                td.setTree(myColl, root);
                MyTree.updateUI();
                scrollPane.setPreferredSize(MyTree.getSize());
                scrollPane.revalidate();
            });

            JMenuItem saveItem = new JMenuItem("Save");
            saveItem.setFont(font2);
            mainMenu.add(saveItem);
            saveItem.addActionListener((event)->{
                byte[] res = myColl.save(way);
                JOptionPane.showMessageDialog(null, res, "Collection saved", JOptionPane.INFORMATION_MESSAGE);
            });

            JMenuItem deleteItem = new JMenuItem("Delete");
            deleteItem.setFont(font2);
            mainMenu.add(deleteItem);
            deleteItem.addActionListener((event)->{
                myColl.getMyColl().clear();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)model.getRoot();
                DefaultMutableTreeNode male = (DefaultMutableTreeNode)selectedNode.getChildAt(0);
                if(!male.isLeaf())male.removeAllChildren();
                DefaultMutableTreeNode female = (DefaultMutableTreeNode)selectedNode.getChildAt(1);
                if(!female.isLeaf())female.removeAllChildren();
                MyTree.updateUI();
                scrollPane.revalidate();
                JOptionPane.showMessageDialog(null, "Collection is emty", "Done", JOptionPane.INFORMATION_MESSAGE);
            });

            mainMenu.addSeparator();

            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.setFont(font2);
            mainMenu.add(exitItem);
            exitItem.addActionListener((event)->exit(this));

            menuBar.add(mainMenu);
            setJMenuBar(menuBar);

            JButton deleteButton = new JButton("Удалить");
            deleteButton.setFont(font2);
            deleteButton.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
                    parent.remove(parent.getIndex(selectedNode));
                    myColl.getMyColl().remove(temp);
                    MyTree.updateUI();
                    scrollPane.revalidate();
                } catch (ClassCastException e2) {
                    JOptionPane.showMessageDialog(null,"Выбрана не одежда","Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Элемент не выбран","Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton customizeButton = new JButton("Изменить");
            customizeButton.setFont(font2);
            customizeButton.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    settings(temp, MyTree,selectedNode, false);
                    MyTree.updateUI();
                } catch (ClassCastException e2) {
                    JOptionPane.showMessageDialog(null,"Выбрана не одежда","Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Элемент не выбран","Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton createButton = new JButton("Add");
            createButton.setFont(font2);
            createButton.addActionListener((event)->{
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) MyTree.getLastSelectedPathComponent();
                try{
                    Stick temp = (Stick) selectedNode.getUserObject();
                    if (temp != null) {
                        JOptionPane.showMessageDialog(null, "Выбрана одежда", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassCastException e2) {
                    if(!selectedNode.isRoot()) {
                        Stick temp = new Stick("Stick", 70,50,100,100, Material.OAK);
                        settings(temp, MyTree, selectedNode, true);
                        scrollPane.revalidate();
                    }
                    else JOptionPane.showMessageDialog(null,"Элементы нельзя добавлять в корневую папку!","Ошибка", JOptionPane.INFORMATION_MESSAGE);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null,"Элемент не выбран","Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel main_panel = new JPanel();
            GroupLayout gl_main = new GroupLayout(main_panel);
            main_panel.setLayout(gl_main);

            gl_main.setAutoCreateGaps(true);
            gl_main.setAutoCreateContainerGaps(true);
            gl_main.setHorizontalGroup(
                    gl_main.createSequentialGroup()
                            .addGroup(gl_main.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addGroup(gl_main.createSequentialGroup()
                                            .addComponent(createButton)
                                            .addComponent(customizeButton)
                                            .addComponent(deleteButton)
                                    )
                                    .addComponent(selectedLabel, GroupLayout.Alignment.CENTER)

                            )
            );
            gl_main.setVerticalGroup(
                    gl_main.createSequentialGroup()
                            .addGroup(gl_main.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(createButton).addComponent(customizeButton).addComponent(deleteButton)
                            )
                            .addGroup(gl_main.createParallelGroup()
                                    .addComponent(selectedLabel)
                            )
            );

            GroupLayout fin = new GroupLayout(this.getContentPane());
            //this.getContentPane().setBackground(new Color(255, 0, 255));

            fin.setAutoCreateGaps(true);
            fin.setAutoCreateContainerGaps(true);
            fin.setVerticalGroup(
                    fin.createSequentialGroup()
                            .addComponent(scrollPane)
                            .addComponent(main_panel)
            );
            fin.setHorizontalGroup(fin.createSequentialGroup()
                    .addGroup(
                            fin.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(scrollPane, GroupLayout.Alignment.CENTER)
                                    .addComponent(main_panel, GroupLayout.Alignment.CENTER)
                    )
            );
            this.getContentPane().setLayout(fin);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setEnabled(true);
            this.setVisible(true);
        }

    }

    public static void main(String ... args) {




        Thread app = new Thread(ServGUI::new);
        javax.swing.SwingUtilities.invokeLater(app);

        myColl.collectionImport(way);
        Thread t = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Collection saved");
            myColl.save(way);
            System.exit(0);
            try{
                t.join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }));
        try{
            DatagramChannel connection = DatagramChannel.open();
            DatagramChannel dchan = DatagramChannel.open().bind(new InetSocketAddress(port));
            Date d1 = new Date();
            long t1, t2;
            while (connection.isOpen()){
                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                SocketAddress clientadr = dchan.receive(inp);
                String str = new String(bytes);
                t1 = Long.parseLong(str.substring(0,str.indexOf(";")));
                t2 = d1.getTime();
                if (t2-t1 < 5000){new Thread(new ServerThread(clientadr, str.substring(str.indexOf(";")+1), myColl, way)).start();}
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();  }
                } connection.close();
        }catch (IOException ex){ex.printStackTrace();}   }}
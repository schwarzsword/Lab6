package Client;
import Server.Material;
import Server.Stick;
import Server.SticksCollection;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;



public class Client {
    static int port = 62091;
    static String host = "localhost";
    static ArrayList<Stick> sticks;

    static class ClientGUI extends JFrame {
        private Font font1 = new Font("Courier", 0, 16);
        private Font font2 = new Font("Courier", 1,14);
        private List<StickPanel> stickPanels = new ArrayList<>();
        private Timer timer;
        int counter;
        int alp;
        JPanel slidePanel = new JPanel();
        JPanel filterPanel = new JPanel();
        JPanel menu = new JPanel();
        JCheckBox oakBox = new JCheckBox("OAK");
        JCheckBox birchBox = new JCheckBox("BIRCH");
        JCheckBox elmBox = new JCheckBox("ELM");
        JCheckBox lindenBox = new JCheckBox("LINDEN");
        JCheckBox palmBox = new JCheckBox("PALM");
        JCheckBox pineBox = new JCheckBox("PINE");
        JCheckBox redBox = new JCheckBox("REDTREE");
        JCheckBox ironBox = new JCheckBox("IRONTREE");
        JTextField name = new JTextField();
        MySlider lengthSlider = new MySlider("Length", 0,1000,50,200,50);


        ClientGUI(){
            super("Client");
            UIManager.put("OptionPane.messageFont", font1);
            UIManager.put("OptionPane.buttonFont", font2);
            init();
        }

        void initPanels(){
            stickPanels.clear();
            sticks.forEach(e-> stickPanels.add(new StickPanel(e))
            );
        }

        class MySlider extends JSlider {
            private JPanel panel;
            private JLabel label;
            private int myValue = 0;

            private int getMyValue() {
                return myValue;
            }

            private void setMyValue(int myValue) {
                this.myValue = myValue;
            }

            private MySlider(String text, int min, int max, int value, int bigStep, int smallStep) {
                super(JSlider.HORIZONTAL, min, max, value);
                panel = new JPanel();
                label = new JLabel(text);
                label.setFont(new Font("Font", Font.PLAIN, 15));
                panel.add(label);
                this.setMajorTickSpacing(bigStep);
                this.setMinorTickSpacing(smallStep);
                this.setPaintTicks(true);
                this.setPaintLabels(true);
                panel.add(this);
                this.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        JSlider source = (JSlider) e.getSource();
                        setMyValue(source.getValue());
                    }
                });
            }

            JPanel getPanel() {
                return panel;
            }

            JLabel getLabel() {
                return label;
            }
        }

        class StickPanel extends JPanel{
             double size;
             Color color;
             Stick stick;
             boolean animated;
             int r;
             int g;
             int b;
             int a;

            class StickBorder implements Border{
                 int side;
                 StickBorder(int side){                    this.side = side;                }
                public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
                    graphics.fillRect(x , y, width, height);
                    graphics.setColor(new Color(r,g,b,a));
                    graphics.drawRect(x , y, width, height);
                }
                public Insets getBorderInsets(Component c) {
                    return new Insets(this.side + 1, this.side +1, this.side+1, this.side+1);
                }
                public boolean isBorderOpaque() {                    return true;                }
            }
            private StickPanel(Stick stick){
                this.stick = stick;
                this.size = stick.getStickLength();
                this.color= stick.getMaterial().getColor();
                this.r=stick.getMaterial().getColor().getRed();
                this.g=stick.getMaterial().getColor().getGreen();
                this.b=stick.getMaterial().getColor().getBlue();
                this.a=250;
                this.animated = false;
                setBackground(new Color(r,g,b,a));
                setForeground(new Color(r,g,b,a));
                setBounds((int)(stick.getStickCoordBeg().x), (int)(stick.getStickCoordBeg().y), (int)this.size, (int)this.size);
                setBorder(new StickBorder((int)this.size));
                setToolTipText(this.stick.toString());
                setOpaque(false);
                setEnabled(false);
            }

            public void reColor(){
                setForeground(new Color(r,g,b,a));
                setBackground(new Color(r,g,b,a));
            }

            @Override
            public void paintComponent(Graphics graphics) {
                graphics.setColor(new Color(r,g,b,a));
                graphics.fillRect(stick.getStickCoordBeg().x, stick.getStickCoordBeg().y, (int)this.size, (int)this.size);;
            }
            public boolean getAnim(){return this.animated;}
        }
        public static boolean checkFilters(StickPanel sp, JTextField name, JCheckBox ch1,  JCheckBox ch2,  JCheckBox ch3,
                                           JCheckBox ch4,  JCheckBox ch5,  JCheckBox ch6,  JCheckBox ch7,  JCheckBox ch8, MySlider ms){
            if(sp.stick.getStickName().equals(name.getText()) && ((sp.stick.getMaterial().equals(Material.valueOf(ch1.getName()))&&ch1.isSelected())||
                    (sp.stick.getMaterial().equals(Material.valueOf(ch2.getName()))&&ch2.isSelected()) || (sp.stick.getMaterial().equals(Material.valueOf(ch3.getName()))&&ch3.isSelected())
                    || (sp.stick.getMaterial().equals(Material.valueOf(ch4.getName()))&&ch4.isSelected())||(sp.stick.getMaterial().equals(Material.valueOf(ch5.getName()))&&ch5.isSelected())
                    ||(sp.stick.getMaterial().equals(Material.valueOf(ch6.getName()))&&ch6.isSelected())||(sp.stick.getMaterial().equals(Material.valueOf(ch7.getName()))&&ch7.isSelected())
                    ||(sp.stick.getMaterial().equals(Material.valueOf(ch8.getName())))&&ch8.isSelected())&&sp.stick.getStickLength()<=ms.getValue()){
                return true;
            }
            else return false;
        }

        private void init(){
            this.setFont(font1);
            class Field extends JPanel{
                private Graphics2D gr;
                private int size;
                private boolean animDraw;

                private Field(int size){
                    super();
                    this.size = size;
                    this.setLayout(null);
                    this.setNeededSize();
                    setAnimDraw(false);
                }
                public void paintComponent(Graphics g) {
                    gr = (Graphics2D) g;
                    Rectangle2D field = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
                    gr.setPaint(Color.white);
                    gr.fill(field);

                    //сетка
                    gr.setPaint(new Color(0,0,255, 50));
                    gr.setStroke(new BasicStroke(0.2f));
                    for (int i = 0; i <= this.getWidth(); i+=size){
                        gr.draw(new Line2D.Double(0, i, this.getWidth(), i));
                        gr.draw(new Line2D.Double(i, 0, i, this.getHeight()));
                    }
                    if(!animDraw)
                    initPanels();
                    this.removeAll();
                    stickPanels.sort((c1, c2)->(c1.stick.getStickLength() - c2.stick.getStickLength()));
                    stickPanels.forEach(this::add);
                }
                private void setNeededSize(){
                    downloadCollection();
                    int width = sticks.get(sticks.size()-1).getStickCoordBeg().x + sticks.get(sticks.size()-1).getStickLength() + 50;
                    sticks.sort((c1,c2)->{return c1.getStickLength()-c2.getStickLength();});
                    this.setPreferredSize(new Dimension(width, sticks.get(sticks.size()-1).getStickCoordBeg().y + sticks.get(sticks.size()-1).getStickLength() + 50));
                }

                public void setAnimDraw(boolean value){this.animDraw = value;}
                public boolean getAnimDraw(){return this.animDraw;}
                public void stop(){

                }

            }

            Field field = new Field(50);
            field.setNeededSize();

            JScrollPane drawField = new JScrollPane(field);

            this.add(drawField, BorderLayout.CENTER);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createTitledBorder("Menu"));

            JPanel namePanel = new JPanel();
            namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            name.setPreferredSize(new Dimension(100,20));
            namePanel.add(name);
            namePanel.setBorder(BorderFactory.createTitledBorder("Name"));
            namePanel.setMaximumSize(new Dimension(200, 50));

            JPanel chBoxPanel = new JPanel();
            chBoxPanel.setMaximumSize(new Dimension(200, 230));
            chBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chBoxPanel.setLayout(new BoxLayout(chBoxPanel, 1));

            chBoxPanel.add(oakBox);
            oakBox.setName("OAK");
            chBoxPanel.add(birchBox);
            birchBox.setName("BIRCH");
            chBoxPanel.add(elmBox);
            elmBox.setName("ELM");
            chBoxPanel.add(lindenBox);
            lindenBox.setName("LINDEN");
            chBoxPanel.add(palmBox);
            palmBox.setName("PALM");
            chBoxPanel.add(pineBox);
            pineBox.setName("PINE");
            chBoxPanel.add(redBox);
            redBox.setName("REDTREE");
            chBoxPanel.add(ironBox);
            ironBox.setName("IRONTREE");
            chBoxPanel.setBorder(BorderFactory.createTitledBorder("Material"));

            slidePanel.setMaximumSize(new Dimension(200, 100));
            slidePanel.setBorder(BorderFactory.createTitledBorder("Length"));
            slidePanel.add(lengthSlider);

            filterPanel.setLayout(new BoxLayout(filterPanel, 1));
            filterPanel.add(namePanel);
            filterPanel.add(chBoxPanel);
            filterPanel.add(slidePanel);




            JButton updateButton = new JButton(" Update ");
            updateButton.setFont(font2);
            updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            updateButton.addActionListener((event)-> {
                field.setNeededSize();
                field.revalidate();
                field.repaint();
                //lengthSlider.setMaximum(field.getPreferredSize().width);
            });

            JButton animationButton = new JButton(" Start  ");
            animationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            animationButton.setFont(font2);
            animationButton.addActionListener((event)-> {
                if (!field.getAnimDraw()) {
                    if (true) {
                        counter = 0;
                        stickPanels.forEach((e) -> {
                            if(checkFilters(e,name, oakBox,birchBox,palmBox,pineBox,ironBox,redBox,elmBox,lindenBox, lengthSlider)) {
                                e.animated = true;
                                counter++;
                            }
                        });
                            field.setAnimDraw(true);
                            counter = 0;
                            timer = new Timer(16, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (counter < 4000) {
                                        stickPanels.forEach((item) -> {
                                            if (item.getAnim()) {
                                                item.a--;
                                                item.reColor();
                                            }
                                        });
                                        counter += timer.getDelay();
                                    } else if (counter < 6000) {
                                        stickPanels.forEach((item) -> {
                                            if (item.getAnim()) {
                                                item.a+=2;
                                                item.reColor();
                                            }
                                        });
                                        counter += timer.getDelay();
                                    }
                                    else if (counter >= 6000){
                                        timer.stop();
                                        field.setAnimDraw(false);
                                        field.setNeededSize();
                                        field.revalidate();
                                        field.repaint();
                                    }
                                }
                            });
                            timer.start();
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Animation is running", "Error", JOptionPane.ERROR_MESSAGE);
            });

            JButton stopButton = new JButton("  Stop  ");
            stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            stopButton.setFont(font2);
            stopButton.addActionListener((event)-> {
                    timer.stop();
                    field.setAnimDraw(false);
                    field.setNeededSize();
                    field.revalidate();
                    field.repaint();
                });

            buttonPanel.setLayout(new BoxLayout(buttonPanel ,1));
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JPanel space1 = new JPanel();
            space1.setMaximumSize(new Dimension(200, 30));
            space1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel space2 = new JPanel();
            space2.setMaximumSize(new Dimension(200, 30));
            space2.setAlignmentX(Component.CENTER_ALIGNMENT);

            buttonPanel.add(animationButton);
            buttonPanel.add(space1);
            buttonPanel.add(stopButton);
            buttonPanel.add(space2);
            buttonPanel.add(updateButton);
            buttonPanel.setMaximumSize(new Dimension(200,150));


            menu.setLayout(new BoxLayout(menu, 1));
            menu.add(buttonPanel);
            menu.add(filterPanel);
            this.add(menu, BorderLayout.EAST);
            this.setMinimumSize(new Dimension(1000, 500));
            this.setLocationRelativeTo(null);
            menu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            this.setVisible(true);
        }

        void downloadCollection() {
            while (true) {
                try {
                    sticks = connect().getMyColl();
                    break;
                } catch (Exception e) {
                    if (JOptionPane.showConfirmDialog(this, "Connection lost.\nTry again?", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1)
                        System.exit(0);
                }
            }
        }
    }

    public static SticksCollection connect() throws ClassCastException, ClassNotFoundException, IOException{
        SocketAddress sAddr = new InetSocketAddress(host, port);
        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(true);
        if (sChannel.connect(sAddr)){
            ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(sChannel.socket().getOutputStream());
            oos.writeObject("info");
            Object o = ois.readObject();
            return (SticksCollection)o;
        } else throw new ConnectException();
    }

    public static void main(String ... args) {
        SwingUtilities.invokeLater(ClientGUI::new);

    }
}


//    JButton animationButton = new JButton(" Start  ");
//            animationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//                    animationButton.setFont(font2);
//                    animationButton.addActionListener((event)-> {
//                    if(field.animDraw==false){
//                    stickPanels.forEach(e->{
//                    if(checkFilters(e,name, oakBox,birchBox,palmBox,pineBox,ironBox,redBox,elmBox,lindenBox, lengthSlider))
//                    {e.animated=true;}
//                    });
//                    } else {
//                    field.setAnimDraw(true);
//                    counter = 0;
//                    alp = 255;
//                    timer = new Timer(10, new ActionListener() {
//@Override
//public void actionPerformed(ActionEvent e) {
//        if (counter < 4000) {
//        stickPanels.forEach((item) -> {
//        int r = item.stick.getMaterial().getColor().getRed();
//        int g = item.stick.getMaterial().getColor().getGreen();
//        int b = item.stick.getMaterial().getColor().getBlue();
//        item.setBackground(new Color(r, g, b, alp));
//        item.setForeground(new Color(r, g, b, alp));
//        item.revalidate();
//        item.repaint();
//        field.revalidate();
//        field.repaint();
//        drawField.revalidate();
//        drawField.updateUI();
//        });
//        field.revalidate();
//        field.repaint();
//        field.updateUI();
//        counter += 20;
//        alp--;
//        } else if (counter < 6000) {
//        stickPanels.forEach((item) -> {
//        int r = item.stick.getMaterial().getColor().getRed();
//        int g = item.stick.getMaterial().getColor().getGreen();
//        int b = item.stick.getMaterial().getColor().getBlue();
//        item.setBackground(new Color(r, g, b, alp));
//        item.setForeground(new Color(r, g, b, alp));
//        item.revalidate();
//        item.repaint();
//        field.revalidate();
//        field.repaint();
//        drawField.revalidate();
//        drawField.updateUI();
//        });
//        field.revalidate();
//        field.repaint();
//        field.updateUI();
//        counter += 20;
//        alp += 2;
//        } else field.setAnimDraw(false);
//        }
//        }); timer.start();
//        }
//        });
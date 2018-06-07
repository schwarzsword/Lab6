package Client;
import Server.Material;
import Server.Stick;
import Server.SticksCollection;
import javax.swing.*;
import javax.swing.border.Border;
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
import java.util.concurrent.CopyOnWriteArraySet;


public class Client {
    static int port = 62091;
    static String host = "localhost";
    static CopyOnWriteArraySet<Stick> sticks;

    static class ClientGUI extends JFrame {
        private Font font1 = new Font("Courier", 0, 16);
        private Font font2 = new Font("Courier", 1,14);

        private List<StickButton> buttons = new ArrayList<>();
        private Timer timer;
        int counter = 0;

        private ClientGUI(){
            super("Client");
            UIManager.put("OptionPane.messageFont", font1);
            UIManager.put("OptionPane.buttonFont", font2);
            init();
        }

        private void initButtons(){
            buttons.clear();
            sticks.forEach(e->buttons.add(new StickButton(e))
            );
        }

        class MySlider extends JSlider{
             JPanel panel;
             JLabel label;
             int val = 0;

            private MySlider(int min, int max, int val, int big, int small, JLabel lab){
                super(JSlider.HORIZONTAL, min, max, val);
                label = lab;
                label.setFont(font2);
                panel = new JPanel();
                panel.add(label);
                this.setMajorTickSpacing(big);
                this.setMinorTickSpacing(small);
                this.setPaintTicks(true);
                this.setPaintLabels(true);
                panel.add(this);
                this.addChangeListener((event)-> {
                    JSlider source = (JSlider) event.getSource();
                    setVal(source.getValue());
                });
            }

            private void setVal(int v){val = v;}
            private JPanel getPanel() {
                return panel;
            }
            public int getValue(){return val;}
        }

        class StickButton extends JButton {
            int size;
            Stick stick;


            class StickBorder implements Border {
                int side;
                StickBorder(int side){
                    this.side = side;
                }
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    g.fillRect(x , y, width, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(x , y, width, height);
                }
                @Override
                public Insets getBorderInsets(Component c) {
                    return new Insets(this.side + 1, this.side +1, this.side+1, this.side+1);
                }
                @Override
                public boolean isBorderOpaque() {
                    return true;
                }
            }

            private StickButton(Stick tmp){
                super("");
                this.stick = tmp;
                this.size = tmp.getStickLength();
                setBackground(Color.BLACK);
                setForeground(this.stick.getMaterial().getColor());
                setBounds(this.stick.getStickCoordBeg().x - Math.round(this.size/ 2), this.stick.getStickCoordBeg().y - Math.round(this.size/2), this.size, this.size);
                setBorder(new StickButton.StickBorder(this.size));
                setToolTipText(this.stick.toString());
                setOpaque(false);
                setEnabled(false);
            }
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(stick.getMaterial().getColor());
                g.fillRect(stick.getStickCoordBeg().x - Math.round(size/ 2), stick.getStickCoordBeg().y - Math.round(size/2), stick.getStickLength(), stick.getStickLength());
            }
            public Stick getStick(){return this.stick;}
        }


        private void init(){
            this.setFont(font1);
            class Canvas extends JPanel{
                 Graphics2D gr;
                 int size;
                 boolean animDraw;
                 boolean toSmall;

                private Canvas(int size){
                    super();
                    this.size = size;
                    this.setLayout(null);
                    setAnimDraw(false);
                }
                public void paintComponent(Graphics g) {
                    gr = (Graphics2D) g;
                    Rectangle2D field = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
                    gr.setPaint(Color.LIGHT_GRAY);
                    gr.fill(field);

                    //сетка
                    gr.setPaint(Color.DARK_GRAY);
                    gr.setStroke(new BasicStroke(0.2f));
                    for (int i = 0; i <= this.getWidth(); i+=size){
                        gr.draw(new Line2D.Double(0, i, this.getWidth(), i));
                        gr.draw(new Line2D.Double(i, 0, i, this.getHeight()));
                    }

                    //подписи
                    for(int i = 0; i <= this.getWidth(); i+=size)
                        gr.drawString(""+i, i-10, 10);
                    gr.drawString("X", 25,10);
                    for(int i = 0; i <= this.getHeight(); i+=size)
                        gr.drawString(""+i, 4, i-1);
                    gr.drawString("Y", 5,25);
                    downloadCollection();
                    initButtons();
                    this.removeAll();
                    buttons.sort((c1,c2)->(c1.size - c2.size));
                    buttons.forEach(this::add);
                }


                public void setAnimDraw(boolean value){this.animDraw = value;}
                public boolean getAnimDraw(){return this.animDraw;}

            }
            Canvas canvas = new Canvas(50);
            //canvas.setNeededSize();

            JScrollPane panel_ofCanvas = new JScrollPane(canvas);

            this.add(panel_ofCanvas, BorderLayout.CENTER);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createTitledBorder("Меню"));

            JButton updateButton = new JButton("update");
            updateButton.setFont(font2);
            updateButton.addActionListener((event)-> {
               // canvas.setNeededSize();
                canvas.revalidate();
                canvas.repaint();
                panel_ofCanvas.revalidate();
            });

            JButton animationButton = new JButton("Start");
            animationButton.setFont(font2);
            animationButton.addActionListener((event)-> {
                //canvas.setNeededSize();
                canvas.setAnimDraw(true);
                counter = 0;
                timer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (counter < 5000){
                            buttons.forEach((item) -> {
                                item.size += 1;
                            });
                            canvas.revalidate();
                            canvas.repaint();
                            counter += 20;
                        }
                        else if (counter < 8000){
                            buttons.forEach((item) -> {
                                item.size -= 3;
                            });
                            canvas.revalidate();
                            canvas.repaint();
                            counter += 35;
                        } else canvas.setAnimDraw(false);
                    }
                });
                timer.start();
            });

            JButton stopButton = new JButton("Stop");
            stopButton.setFont(font2);
            stopButton.addActionListener((event)-> {
                canvas.setAnimDraw(false);
            });
            buttonPanel.add(animationButton);
            buttonPanel.add(stopButton);
            buttonPanel.add(updateButton);
            //фильтры

            JLabel[] charact = new JLabel[9];
            for(int i = 0; i < 9; i++){
                charact[i] = new JLabel();
                charact[i].setFont(font2);
            }
            charact[6].setText("X");
            charact[7].setText("Y");

            JPanel namePanel = new JPanel();
            JTextField name = new JTextField();
            name.setPreferredSize(new Dimension(100,20));
            namePanel.add(name);
            namePanel.setBorder(BorderFactory.createTitledBorder("Name"));

            JPanel matPanel = new JPanel();
            JComboBox material = new JComboBox(Material.values());
            matPanel.setBorder(BorderFactory.createTitledBorder("Material"));
            matPanel.add(material);


            JPanel sizePanel = new JPanel();
            JSpinner size = new JSpinner();
            size.setPreferredSize(new Dimension(50, 20));
            sizePanel.setBorder(BorderFactory.createTitledBorder("Размер"));
            sizePanel.add(size);


            MySlider xCoord = new MySlider(0, 1400,  0, 400, 100, charact[6]);
            MySlider yCoord = new MySlider(0, 1200,  0, 200, 50, charact[7]);



            JPanel setP = new JPanel();
            GroupLayout settings = new GroupLayout(setP);
            setP.setPreferredSize(new Dimension(900,150));
            settings.setAutoCreateContainerGaps(true);
            settings.setAutoCreateGaps(true);
            settings.setVerticalGroup(
                    settings.createSequentialGroup()
                            .addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonPanel)
                                    .addComponent(namePanel)
                                    .addComponent(matPanel)
                            ).addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(sizePanel)
                            .addComponent(xCoord.getPanel())
                            .addComponent(yCoord.getPanel())
                    )
            );
            settings.setHorizontalGroup(settings.createSequentialGroup()
                    .addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonPanel)
                    ).addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(namePanel)
                            .addComponent(sizePanel)
                    ).addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(matPanel)
                            .addComponent(xCoord.getPanel())
                    ).addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(yCoord.getPanel())
                    ).addGroup(settings.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    )
            );
            this.add(setP, BorderLayout.SOUTH);
            this.setMinimumSize(new Dimension(1000, 600));
            this.setLocationRelativeTo(null);
            setP.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            this.setVisible(true);
        }
        private void downloadCollection() {
            while (true) {
                try {
                    sticks = connect().getMyColl();
                    break;
                } catch (Exception e) {
//                    if (JOptionPane.showConfirmDialog(this, "Не удалось получить коллекцию\nПовторить попытку?", "Ошибка", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1)
//                        System.exit(0);
                    e.printStackTrace();
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

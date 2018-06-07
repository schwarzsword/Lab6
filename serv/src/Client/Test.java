//package Client;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.TimerTask;
//
//public class Test{
//public static void main(String[] args) {
//
//        JFrame frame = new JFrame("Test");
//        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
//
//// Полосатая фоновая панель
//final JPanel contentPane = new JPanel( new BorderLayout() ) {
//@Override
//public void paintComponent(Graphics g) {
//        super.paintComponent( g );
//        g.setColor( Color.RED );
//        for ( int i = 0; i < getWidth(); i += 20 ) {
//        g.fillRect( i, 0, 10, getHeight() );
//        }
//        }
//        };
//        contentPane.setBackground( Color.GREEN );
//        frame.setContentPane( contentPane );
//
//        contentPane.add( new JLabel( "NORTH"), BorderLayout.NORTH );
//        contentPane.add( new JLabel( "SOUTH"), BorderLayout.SOUTH );
//        contentPane.add( new JLabel( "EAST"), BorderLayout.EAST );
//        contentPane.add( new JLabel( "WEST"), BorderLayout.WEST );
//
//        //Полупрозрачная панель
//        JPanel otherPanel = new JPanel( new BorderLayout() ) {
//@Override
//public void paintComponent( Graphics g ) {
//        super.paintComponent( g );
//
//        // Apply our own painting effect
//        Graphics2D g2d = (Graphics2D) g.create();
//        // 50% transparent Alpha
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//
//        g2d.setColor(getBackground());
//        g2d.fillRect( 0, 0, getWidth(), getHeight() );
//
//        g2d.dispose();
//        }
//        };
//        otherPanel.setOpaque( false );
//        contentPane.add( otherPanel, BorderLayout.CENTER );
//
//        // обратите внимание, что альфа-канал цвета - 255 (непрозрачный)
//        // т.к. используется композит в paintComponent
//        // можно сделать полупрозрачный цвет и убрать композит
//        otherPanel.setBackground( new Color( 0, 0, 250, 255 ) );
//
//final JLabel label = new JLabel( "LABEL" );
//
//        otherPanel.add( label );
//        label.setForeground( new Color( 200, 200, 0, 200 ) );
//
//        frame.pack();
//        frame.setVisible( true );
//
//        // изменяем метку
//        Timer t = new Timer();
//        t.scheduleAtFixedRate( new TimerTask() {
//
//@Override
//public void run() {
//        SwingUtilities.invokeLater( () -> {
//        label.setText( label.getText() + " + ");
//        });
//        }
//        }, 1000, 1000 );
//
//        }}
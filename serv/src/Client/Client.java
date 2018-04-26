package Client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import Server.*;


public class Client {
    private static final int port = 1234;
    private static final String host = "localhost";

    public static void main(String ... args){
        Scanner in = new Scanner(System.in);
        HappyPig piglet1 = new HappyPig("Piglet", 5, 5);
        Donkey ia1 = new Donkey("Ia", 1, 1);
        piglet1.cutFlower(Flowerist.Flowers.VIOLET);
        piglet1.toSniff(Flowerist.Flowers.VIOLET);
        ia1.raiseLeg();
        ia1.shakeLeg("to", piglet1);
        ia1.lookAt("sticks");
        SocketAddress adr = new InetSocketAddress(host, port);
        byte[] answer = new byte[1000];
        try{
            DatagramSocket datagramSocket = new DatagramSocket();
            DatagramPacket start = new DatagramPacket( new String("123").getBytes(), in.nextLine().getBytes().length, adr);
            datagramSocket.send(start);
        while (true){
                DatagramPacket outp = new DatagramPacket(in.nextLine().getBytes(), in.nextLine().getBytes().length, adr);
                datagramSocket.send(outp);
                 try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
        }
        } catch (IOException ex){ex.printStackTrace();}
      /**


            try {
                String sendStr = in.nextLine();
                byte[] tosend = sendStr.getBytes();
                SocketAddress adr = new InetSocketAddress("localhost", port);
                DatagramPacket dpack = new DatagramPacket(tosend, tosend.length, adr);
                DatagramSocket dsock = new DatagramSocket();
                dsock.connect(adr);
                dsock.send(dpack);
                dsock.disconnect();
                dsock.close();
                ByteBuffer fromServ = ByteBuffer.wrap(tosend);
                fromServ.clear();
                DatagramChannel dchan = DatagramChannel.open();
                dchan.bind(adr);
                dchan.receive(fromServ);
                String toPrint = new String(fromServ.array());
                System.out.println(toPrint);
                }
            catch (IllegalArgumentException | IOException ex){System.out.println("This command no longer exists");}
        }*/
    }
}

package Client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import Server.*;


public class Client {
    private static final int port = 30499;
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

            DatagramChannel dchan = DatagramChannel.open().connect(adr);
        while (true){
            if(dchan.isConnected()){
                String toSend = in.nextLine();
                DatagramPacket outp = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, adr);
                datagramSocket.send(outp);
                byte[] bytes = new byte[200];
                DatagramPacket inp = new DatagramPacket(bytes, 200);
                datagramSocket.receive(inp);
                String str = "";
                for (byte i : bytes) {
                    str+=(char)i;
                }
                System.out.println(str);
            }
                 try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
        }
        } catch (IOException ex){ex.printStackTrace();}

    }
}

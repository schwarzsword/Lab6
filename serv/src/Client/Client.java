package Client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import Server.*;
import java.util.Date;


public class Client {
    private int port;
    private String host;

    public int getPort() {return port;}
    public void setPort(int port) {this.port = port;}
    public void setHost(String host) { this.host = host; }
    public String getHost() {return host; }

    public Client(){
        setPort(62091);
        setHost("localhost");
    }
    public Client(int port, String host) {
        setHost(host);
        setPort(port);
    }
    public void send(DatagramSocket ds, SocketAddress adr)throws IOException{
        Scanner in = new Scanner(System.in);
        ds.setSoTimeout(5000);
        String toSend = in.nextLine();
        DatagramPacket outp = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, adr);
        ds.send(outp);
    }
    public void receive(DatagramSocket ds){
        byte[] bytes = new byte[1000];
        DatagramPacket inp = new DatagramPacket(bytes, bytes.length);
        try {
            ds.receive(inp);
        }catch (IOException ex){
            System.out.print("Server is irresponsible, try later");
        }
        String str = "";
        for (byte i : bytes) {
            str += (char) i;
        }
        System.out.println(str);
    }
    public static void main(String ... args){
        Client cl = new Client();
        HappyPig piglet1 = new HappyPig("Piglet", 5, 5);
        Donkey ia1 = new Donkey("Ia", 1, 1);
        piglet1.cutFlower(Flowerist.Flowers.VIOLET);
        piglet1.toSniff(Flowerist.Flowers.VIOLET);
        ia1.raiseLeg();
        ia1.shakeLeg("to", piglet1);
        ia1.lookAt("sticks");
        SocketAddress socketAddress = new InetSocketAddress(cl.getHost(), cl.getPort());
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            while (true) {
                cl.send(datagramSocket, socketAddress);
                cl.receive(datagramSocket);
                Thread.sleep(1500);
            }
        } catch (IOException | InterruptedException ex){ex.printStackTrace();}

    }
}

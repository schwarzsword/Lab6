package Server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerThread implements Runnable{
    SocketAddress adr;
    String command;
    SticksCollection collection;
    String path;
    ServerThread(SocketAddress adr, String command, SticksCollection collection, String way){
        this.adr = adr;
        this.command = command;
        this.collection = collection;
        way = path;
    }
    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            while (true){
                    byte[] toSend = collection.startWork(command, path);
                    DatagramPacket outp = new DatagramPacket(toSend, toSend.length, adr);
                    datagramSocket.send(outp);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
    }catch (IOException ex){ex.printStackTrace();}
    }
}

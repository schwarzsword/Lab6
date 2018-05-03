package Server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class ServerThread implements Runnable{
    SocketAddress adr;
    String command;
    SticksCollection collection;
    String path;
    ServerThread(SocketAddress adr, String str, SticksCollection collection, String way){
        this.adr = adr;
        command = str;
        this.collection = collection;
        path = way;
    }
    @Override
    public void run() {
        try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    byte[] toSend = collection.startWork(command, path);
                    DatagramPacket outp = new DatagramPacket(toSend, toSend.length, adr);
                    datagramSocket.send(outp);
    }catch (IOException ex){ex.printStackTrace();}    }}

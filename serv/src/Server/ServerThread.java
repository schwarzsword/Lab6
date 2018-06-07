package Server;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

import static Server.Server.way;

public class ServerThread implements Runnable{
    SocketAddress adr;
    String command;
    SticksCollection collection;
    String path;
    public ServerThread(SocketAddress adr, SticksCollection collection, String way){
        this.adr = adr;
        this.collection = collection;
        path = way;
    }
    @Override
    public void run() {
        try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutput out = new ObjectOutputStream(bos);
                    out.writeObject(collection);
                    out.flush();
                    bos.close();
                    byte[] toSend = bos.toByteArray();
                    DatagramSocket datagramSocket = new DatagramSocket();
                    DatagramPacket outp = new DatagramPacket(toSend, toSend.length, adr);
                    datagramSocket.send(outp);
    }catch (IOException ex){ex.printStackTrace();}    }}


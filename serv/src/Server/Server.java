package Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server{
    private static final int port = 30499;
    private static final String host = "localhost";
    public static void main(String ... args) {
        SticksCollection myColl = new SticksCollection();
        String way= System.getenv("MyPath");
        myColl.collectionImport(way);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{myColl.save(way);}));
        SocketAddress adr = new InetSocketAddress(host,port);
        try{
            DatagramChannel dchan = DatagramChannel.open().bind(adr);
            while (true){

                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                adr=dchan.receive(inp);
                String str = new String();
                for (byte i : bytes) {
                    str+=(char)i;
                }
                System.out.println(str);
                ServerThread serv = new ServerThread(adr, str, myColl, way);
                serv.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }}
        }catch (IOException ex){ex.printStackTrace();}
    }
}
package Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class Server{
    private static final int port = 62091;
    private static final String host = "localhost";
    public static void main(String ... args) {
        SticksCollection myColl = new SticksCollection();
        String way= System.getenv("MyPath");
        myColl.collectionImport(way);
        Thread t = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            myColl.save(way);
            try{
                t.join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }));

        SocketAddress adr = new InetSocketAddress(host,port);
        try{
            DatagramChannel dchan = DatagramChannel.open().bind(adr);
            while (true){
                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                SocketAddress clientadr = dchan.receive(inp);
                String str = new String(bytes);
                //System.out.println(str + dchan.getLocalAddress());
                new Thread(new ServerThread(clientadr, str, myColl, way)).start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                }
        }catch (IOException ex){ex.printStackTrace();}
    }
}
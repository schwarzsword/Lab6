package Server;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server{
    private static final int port = 62091;
    public static void main(String ... args) {
        SticksCollection myColl = new SticksCollection();
        String way= System.getenv("MyPath");
        myColl.collectionImport(way);
        Thread t = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{myColl.save(way);}));
        try{
            DatagramChannel dchan = DatagramChannel.open().bind(new InetSocketAddress(port));
            while (true){
                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                SocketAddress clientadr = dchan.receive(inp);
                String str = new String(bytes);
                new Thread(new ServerThread(clientadr, str, myColl, way)).start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();  }
                }
        }catch (IOException ex){ex.printStackTrace();}    }}
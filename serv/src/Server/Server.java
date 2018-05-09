package Server;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

public class Server{
    private static final int port = 62091;
    public static void main(String ... args) {
        SticksCollection myColl = new SticksCollection();
        String way= System.getenv("MyPath");
        myColl.collectionImport(way);
        Thread t = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Collection saved");
            myColl.save(way);
            try{
                t.join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }));
        try{
            DatagramChannel connection = DatagramChannel.open();
            DatagramChannel dchan = DatagramChannel.open().bind(new InetSocketAddress(port));
            Date d1 = new Date();
            long t1, t2;
            while (connection.isOpen()){
                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                SocketAddress clientadr = dchan.receive(inp);
                String str = new String(bytes);
                t1 = Long.parseLong(str.substring(0,str.indexOf(";")));
                t2 = d1.getTime();
                if (t2-t1 < 5000){new Thread(new ServerThread(clientadr, str.substring(str.indexOf(";")+1), myColl, way)).start();}
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();  }
                } connection.close();
        }catch (IOException ex){ex.printStackTrace();}   }}
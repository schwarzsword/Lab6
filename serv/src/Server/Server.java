package Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server{
    private static final int port = 1234;
    private static final String host = "localhost";
    public static void main(String ... args) {
        SticksCollection myColl = new SticksCollection();
        String way= System.getenv("MyPath");
        myColl.collectionImport(way);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{myColl.save(way);}));
        SocketAddress adr = new InetSocketAddress(host,port);
        try{
            DatagramChannel dchan = DatagramChannel.open();
            dchan.bind(adr);
            while (true){
                byte[] bytes = new byte[100];
                ByteBuffer inp = ByteBuffer.wrap(bytes);
                inp.clear();
                adr=dchan.receive(inp);
                String str = "";
                for (byte i : bytes) {
                    str+=(char)i;
                }
                System.out.println(str);

                }
        }catch (IOException ex){ex.printStackTrace();}
        /** SticksCollection myColl = new SticksCollection();
         String way= System.getenv("MyPath");
         myColl.collectionImport(way);
         Runtime.getRuntime().addShutdownHook(new Thread(()->{myColl.save(way);}));
         int port = 5555;
         byte[] in = new byte[1000];
         try{
         SocketAddress adr = new InetSocketAddress("localhost", port);

         while (dsock.isClosed()!=true){
         try {
         DatagramPacket dpack = new DatagramPacket(in, 1000, adr);
         String command = new String(in);
         dsock.receive(dpack);
         System.out.println(dpack.toString());
         ServerThread serverThread = new ServerThread(adr, command);
         serverThread.run();
         }catch (IllegalArgumentException ex){System.out.println("This command no longer exists");}
         }
         }catch (IOException ex){ex.printStackTrace();}*/

    }
}
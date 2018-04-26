package Server;


import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerThread implements Runnable{
    ServerThread(SocketAddress adr, String command){
        this.adr = adr;
        this.command = command;
    }
    SocketAddress adr;
    String command;
    @Override
    public void run() {
        try {
            SticksCollection myColl = new SticksCollection();
            String way= System.getenv("MyPath");
            myColl.collectionImport(way);
            DatagramChannel dchann = DatagramChannel.open();
            ByteBuffer toClient = myColl.startWork(command,way);
            dchann.send(toClient, adr);
    }catch (IOException ex){ex.printStackTrace();}
    }
}

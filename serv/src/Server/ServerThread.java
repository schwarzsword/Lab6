package Server;
import java.io.*;
import java.net.*;

public class ServerThread implements Runnable {
     Socket client;
     String path;
     SticksCollection processed;
    ServerThread(Socket socket, SticksCollection needToProc, String file){
        this.client = socket;
        processed = needToProc;
        path = file;
    }
    @Override
    public void run() {
        try(ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream())){
            try {
                if (!client.isClosed()) {
                    String order = (String) in.readObject();
                    //processed.menu(order, path);
                    processed.save(path.replaceAll("myconfig.csv", "myconfig2.csv"));
                    SticksCollection toSend = new SticksCollection();
                    toSend.collectionImport(path.replaceAll("myconfig.csv", "myconfig2.csv"));
                    out.writeObject(toSend);
                    out.flush();
                }
            } catch (SocketException e) {
                System.out.println("Client " + client.getInetAddress() + " disconnected");
            } catch (EOFException e) {
                System.out.println("EOF");
            } catch (ClassNotFoundException e) {
                System.out.println("Wrong command.");
            } finally {
                out.flush();
                in.close();
                out.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}

package Server;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import com.google.gson.*;
/**
 * StickCollection, class controls collection.
 * @author Kirill Cherniy.
 * @version 1.0.0alpha.
 */
public class SticksCollection{
    private static Date initialization;
    public static Date getInitialization() {
        return initialization;
    }

    public static void setInitialization(Date initialization) {
        SticksCollection.initialization = initialization;
    }


    private LinkedHashSet<Stick> myColl = new LinkedHashSet<>();
    /**
     * Method allows to import data from file to
     * collection.
     * Format of file should be <i>csv</i>.
     * @param path location of file.
     */
    public ByteBuffer collectionImport(String path){

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
            int EoF;
            char EoS;
            String value = new String();
            String splitBy = ",";
            String[] collectVal = new String[5];
                while (((EoF = reader.read()) != -1)){
                    if((EoS=(char)EoF) != ';'){value+=EoS;}
                    else
                    {collectVal = value.split(splitBy);
                    Stick testStick = new Stick(collectVal[0], Integer.valueOf(collectVal[1]), Integer.valueOf(collectVal[2]),Integer.valueOf(collectVal[3]), Integer.valueOf(collectVal[4]));
                    myColl.add(testStick);
                    value = "";}}
            reader.close();
            setInitialization(new Date());
        }catch (IllegalArgumentException | IOException ex){
            ex.printStackTrace();
        }
        String sendStr = new String("Collection imported");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    /**
     * Method allows to remove elements
     * from collection,
     * if their length smaller than input.
     * @param str element in Json format.
     */
    public ByteBuffer removeLower(String str){
        Gson gson = new Gson();
        Stick testStick = gson.fromJson(str, Stick.class);
        while (myColl.iterator().hasNext()){
            Stick e = myColl.iterator().next();
            if(e.getStickLength() <testStick.getStickLength()){
                myColl.remove(e);
            }
        }
        String sendStr = new String("Element removed");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    /**
     * .Method allows to add element
     * to collection,
     * if it has maximal length
     * @param str element in Json format.
     */
    public ByteBuffer addIfMax(String str){
        Gson gson = new Gson();
        Stick testStick = gson.fromJson(str, Stick.class);
        int counter = 0;
        for (Stick e: myColl){
            if (e.getStickLength()<testStick.getStickLength()){
                counter++;
            }
        }
        if (counter == myColl.size()){
            myColl.add(testStick);
            String sendStr = new String("Stick added successfully");
            byte[] bytes = sendStr.getBytes();
            return ByteBuffer.wrap(bytes);
        }
        else {String sendStr = new String("Stick's length isn't enough");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);}

    }
    /**
     * Method allows to print information
     * about collection.
     */
    public ByteBuffer info(){
        String type = "Stick";
        int size = myColl.size();
        String sendStr = new String("Collection type: "+ type +", Elements in total: " + size + ", Initialization date :" + getInitialization());
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    /**
     * Method allows to import data to file from
     * collection.
     * Format of file should be <i>csv</i>.
     * @param path location of file.
     */
    public ByteBuffer save(String path){
        try {
            FileOutputStream writer = new FileOutputStream(path);
            myColl.forEach(e -> {
                String output = e.getStickName()+","+e.getStickCoordBeg(0)+","+e.getStickCoordBeg(1)+","+e.getStickCoordEnd(0)+","+e.getStickCoordEnd(1)+";";
                char[] outp = output.toCharArray();
                for(int i = 0; i<output.length(); i++ ){
                    try{writer.write((byte)outp[i]);}catch (IOException ex){}
                }
            });
                        writer.close();

        } catch (IOException ex){ex.printStackTrace();}
        String sendStr = new String("Collection saved");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    /**
     * Method allows to print every element
     * from collection in Json format.
     */
  /**  public ByteBuffer print(){
        myColl.forEach(e-> {Gson gson = new Gson();
        System.out.println(gson.toJson(e));
        String sendStr = new String(gson.toJson(e).toString());
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
        });
    }*/
    public ByteBuffer man()
    {
        String sendStr = new String("import - method allows to import data to file from collection\n " +
                "remove_lower - method allows to remove elements from collection, if their length smaller than input\n" +
                "info - method allows to print information about collection\n" +
                "add_if_max - method allows to add element to collection, if it has maximal length\n" +
                "print - method allows to print every element from collection in Json format\n" +
                "save -  method allows to import data to file from collection\n" +
                "man - method allows to show command list or command info\n" +
                "exit - method closes work with collection");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    public ByteBuffer manImport(){
        String sendStr = new String("import - method allows to import data to file from collection");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);

    }
    public ByteBuffer manRemLow(){
        String sendStr = new String("remove_lower - method allows to remove elements from collection, if their length smaller than input\n" +
                "should get Json string as input object\n" +
                "example: remove_lower {\"stickName\":\"Stick6\",\"stickCoordBeg\":[0,0],\"stickCoordEnd\":[10,10],\"stickLength\":14}");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    public ByteBuffer manInfo(){
        String sendStr = new String("import - method allows to import data to file from collection");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    public ByteBuffer manAddIfMax(){
        String sendStr = new String("add_if_max - method allows to add element to collection, if it's length bigger than input\n" +
                "should get Json string as input object\n" +
                "example: add_if_max {\"stickName\":\"Stick6\",\"stickCoordBeg\":[0,0],\"stickCoordEnd\":[10,10],\"stickLength\":14}");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    public ByteBuffer manPrint(){
        String sendStr = new String("print - method allows to print out every element of collection");
        byte[] bytes = sendStr.getBytes();
        return ByteBuffer.wrap(bytes);
    }
    /**
     * Method allows to interact with
     * collection.
     * Format of file should be <i>csv</i>.
     * @param command inputten command.
     * @param path location of file.
     */
    public ByteBuffer startWork(String command, String path){
        ByteBuffer send = ByteBuffer.allocate(1000);
        switch (command.contains(" ")?command.substring(0,command.indexOf(" ")):command){

            case "import":
               send = collectionImport(path);
                break;
            case "remove_lower":
                send = removeLower(command.substring(command.indexOf(" ")+1));
                break;
            case "info":
               send = info();
                break;
            case "add_if_max":
                send = addIfMax(command.substring(command.indexOf(" ")+1));
                break;
            /**case "print":
                print();
                break;*/
            case "save":
                send = save(path);
                break;
            case "man":
                send = man();
                break;
            case "man_import":
                send = manImport();
                break;
            case "man_info":
               send = manInfo();
                break;
            case "man_remove_lower":
                send = manRemLow();
                break;
            case "man_add_if_max":
                send = manAddIfMax();
                break;
            case "man_print":
                send = manPrint();
                break;
            case "exit":
                System.exit(0);
                String toSend = new String("System exit. Code 0");
                byte[] bytes = toSend.getBytes();
                send = ByteBuffer.wrap(bytes);
                break;
            default: throw new IllegalArgumentException();
        }
        return send;
    }
}
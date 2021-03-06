package Server;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * StickCollection, class controls collection.
 * @author Kirill Cherniy.
 * @version 1.0.0alpha.
 */
public class SticksCollection implements Serializable{

    public ArrayList<Stick> getMyColl() {        return myColl;    }

    public void setMyColl(ArrayList<Stick> myColl) {
        this.myColl = myColl;
    }

    private ArrayList<Stick> myColl = new ArrayList<>();

    /**
     * Method allows to import data from file to
     * collection.
     * Format of file should be <i>csv</i>.
     * @param path location of file.
     */

    public void collectionImport(String path){

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
            int EoF;
            char EoS;
            String value = new String();
            String splitBy = ",";
            String[] collectVal = new String[8];
                while (((EoF = reader.read()) != -1)){
                    if((EoS=(char)EoF) != ';'){value+=EoS;}
                    else
                    {collectVal = value.split(splitBy);
                    String s = collectVal[6];
                    if (s.equals(" ")){
                        Stick testStick = new Stick(collectVal[0], Integer.valueOf(collectVal[1]), Integer.valueOf(collectVal[2]),Integer.valueOf(collectVal[3]), Integer.valueOf(collectVal[4]), Material.valueOf(collectVal[5]), s);
                        myColl.add(testStick);}
                    else{
                        Stick testStick = new Stick(collectVal[0], Integer.valueOf(collectVal[1]), Integer.valueOf(collectVal[2]),Integer.valueOf(collectVal[3]), Integer.valueOf(collectVal[4]), Material.valueOf(collectVal[5]), ZonedDateTime.parse(collectVal[6]));
                        myColl.add(testStick);}
                    value = "";}}
            reader.close();
        }catch (IllegalArgumentException | IOException ex){
            ex.printStackTrace();
        }
    }
//    /**
//     * Method allows to remove elements
//     * from collection,
//     * if their length smaller than input.
//     * @param str element in Json format.
//     */
//    public byte[] removeLower(String str){
//        Gson gson = new Gson();
//        Stick testStick = gson.fromJson(str, Stick.class);
//        myColl.forEach(e-> {if(e.getStickLength()<testStick.getStickLength()) {myColl.remove(e);}});
//        String sendStr = new String("Removing completed");
//        return sendStr.getBytes();
//    }
//    /**
//     * .Method allows to add element
//     * to collection,
//     * if it has maximal length
//     * @param str element in Json format.
//     */
//    public byte[] addIfMax(String str){
//        Gson gson = new Gson();
//        Stick testStick = gson.fromJson(str, Stick.class);
//        int counter = 0;
//        for (Stick e: myColl){
//            if (e.getStickLength()<testStick.getStickLength()){
//                counter++;
//            }
//        }
//        if (counter == myColl.size()){
//            myColl.add(testStick);
//            String sendStr = new String("Stick added successfully");
//            return sendStr.getBytes();
//        }
//        else {String sendStr = new String("Stick's length isn't enough");
//        return sendStr.getBytes();}
//
//    }
    /**
     * Method allows to import data to file from
     * collection.
     * Format of file should be <i>csv</i>.
     * @param path location of file.
     */
    public void save(String path){
        try {
            FileOutputStream writer = new FileOutputStream(path);
            myColl.forEach(e -> {
                String output = e.getStickName()+","+e.coorbegx+","+e.coorbegy+","+e.coorendx+","+e.coorendy+","+e.getMaterial()+","+e.initdate +";";
                char[] outp = output.toCharArray();
                for(int i = 0; i<output.length(); i++ ){
                    try{writer.write((byte)outp[i]);}catch (IOException ex){}
                }
            });
                        writer.close();

        } catch (IOException ex){ex.printStackTrace();}
    }
//    /**
//     * Method allows to print every element
//     * from collection in Json format.
//     */
//     public byte[] print() {
//       String str = "";
//       Gson gson = new Gson();
//       myColl.stream().sorted();
//         for (Stick e: myColl){
//             str+=gson.toJson(e)+"\n";
//     }
//     byte[] bytes = str.getBytes();
//         return bytes;
//     }
//    public byte[] man()
//    {
//        String sendStr = new String("import - method allows to import data to file from collection\n " +
//                "remove_lower - method allows to remove elements from collection, if their length smaller than input\n" +
//                "info - method allows to print information about collection\n" +
//                "add_if_max - method allows to add element to collection, if it has maximal length\n" +
//                "print - method allows to print every element from collection in Json format\n" +
//                "save -  method allows to import data to file from collection\n" +
//                "man - method allows to show command list or command info\n" +
//                "exit - method closes work with collection");
//        return sendStr.getBytes();
//    }
//    public byte[] manImport(){
//        String sendStr = new String("import - method allows to import data to file from collection");
//        return sendStr.getBytes();
//
//    }
//    public byte[] manRemLow(){
//        String sendStr = new String("remove_lower - method allows to remove elements from collection, if their length smaller than input\n" +
//                "should get Json string as input object\n" +
//                "example: remove_lower {\"stickName\":\"Stick6\",\"stickCoordBeg\":[0,0],\"stickCoordEnd\":[10,10],\"stickLength\":14}");
//        return sendStr.getBytes();
//    }
//    public byte[] manInfo(){
//        String sendStr = new String("Info - methos allows to show actual infomation about collection");
//        return sendStr.getBytes();
//    }
//    public byte[] manAddIfMax(){
//        String sendStr = new String("add_if_max - method allows to add element to collection, if it's length bigger than input\n" +
//                "should get Json string as input object\n" +
//                "example: add_if_max {\"stickName\":\"Stick6\",\"stickCoordBeg\":[0,0],\"stickCoordEnd\":[10,10],\"stickLength\":14}");
//        return sendStr.getBytes();
//    }
//    public byte[] manPrint(){
//        String sendStr = new String("print - method allows to print out every element of collection");
//        return sendStr.getBytes();
//    }

    public void add(Stick stick){
         myColl.add(stick);
    }
    public void clear(){
        myColl.clear();
    }
//    /**
//     * Method allows to interact with
//     * collection.
//     * Format of file should be <i>csv</i>.
//     * @param command inputten command.
//     * @param path location of file.
//     */

//    public byte[] startWork(String command, String path){
//        byte[] send;
//        switch (command.contains(" ")?command.substring(0,command.indexOf(" ")):command.substring(0,command.indexOf((char)0))){
//            case "import":
//               send = collectionImport(path);
//                break;
////            case "remove_lower":
////                send = removeLower(command.substring(command.indexOf(" ")+1, command.indexOf("}")+1));
////                break;
////            case "add_if_max":
////                send = addIfMax(command.substring(command.indexOf(" ")+1, command.indexOf("}")+1));
////                break;
////            case "print":
////                send = print();
////                break;
//            case "save":
//                send = save(path);
//                break;
////            case "man":
////                send = man();
////                break;
////            case "man_import":
////                send = manImport();
////                break;
////            case "man_info":
////               send = manInfo();
////                break;
////            case "man_remove_lower":
////                send = manRemLow();
////                break;
////            case "man_add_if_max":
////                send = manAddIfMax();
////                break;
////            case "man_print":
////                send = manPrint();
////                break;
//            case "exit":
//                //System.exit(0);
//                String toSend = new String("System exit. Code 0");
//                send = toSend.getBytes();
//                break;
//            default: String sendStr = new String("Invalid command");
//                return sendStr.getBytes();
//        }
//        return send;
//    }
}

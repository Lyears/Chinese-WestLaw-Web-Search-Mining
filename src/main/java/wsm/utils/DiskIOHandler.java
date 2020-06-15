package wsm.utils;

import java.io.*;

public class DiskIOHandler {

    public static void writeObjectToFile(Object obj, String filePath)
    {

        File file =new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("Write object success!");
        } catch (IOException e) {
            System.out.printf("Write object failed, error %s", e);
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String filePath) {
        Object temp=null;
        File file =new File(filePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}

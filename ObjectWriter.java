import java.io.*;

/**
 * Created by xue on 17-1-19.
 */
public class ObjectWriter {
    public static void writeObject(Object obj, OutputStream os) {
        if (obj == null) {
            System.out.println("obj object is null");
            return;
        }
        if (os == null) {
            System.out.println("os is null");
            return;
        }
        try (ObjectOutputStream os = new ObjectOutputStream(os)) {
            os.writeObject(obj);
        } catch (Exception e) {

        }
    }

    public static FileOutputStream createFileOutputStream(String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path + "/" + fileName);
        try {
            return new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}

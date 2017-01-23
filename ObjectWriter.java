import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by xue on 17-1-19.
 */
public class ObjectWriter {

    private static Logger logger = LoggerFactory.getLogger(ObjectWriter.class);

    public static boolean writeObject(Object obj, OutputStream os) {
        if (obj == null) {
            return false;
        }
        if (os == null) {
            return false;
        }
        if (!(obj instanceof Serializable)) {
            return false;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(obj);
            return true;
        } catch (Exception e) {
            logger.error("Object 序列化后写入文件时发生异常.", e);
            return false;
        }
    }

    public static FileOutputStream createFileOutputStream(String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File((path.endsWith(File.separator) ? path : (path + File.separator)) + fileName);
        try {
            return new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            logger.error("创建文件输出流时发生异常.", e);
            return null;
        }
    }

    public static boolean writeObject(String path, String fileName, Object obj) {
        try (OutputStream os = createFileOutputStream(path, fileName)) {
            return writeObject(obj, os);
        } catch (Exception e) {
            logger.error("Object 写入文件时发生异常.", e);
            return false;
        }
    }
}

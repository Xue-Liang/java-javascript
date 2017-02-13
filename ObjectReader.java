package com.gooagoo.container.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by xue on 2017-01-19.
 */
public class ObjectReader<T> {
    public static <T> T readObjectFrom(File f) {
        try (InputStream is = new FileInputStream(f)) {
            return readObjectFrom(is);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T readObjectFrom(InputStream is) {
        if (is == null) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}

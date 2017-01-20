package com.gooagoo.container.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by xue on 2017-01-19.
 */
public class ObjectReader<T> {

    public <T> T readObjectFrom(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("文件" + f.getName() + "不存在,无法读取.");
        }
        InputStream is = new FileInputStream(f);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T readObjectFrom(InputStream is) {
        if (is == null) {
            throw new IllegalArgumentException("输入流为空,无法读取.");
        }

        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}

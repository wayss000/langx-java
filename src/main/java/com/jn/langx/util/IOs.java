package com.jn.langx.util;

import com.jn.langx.util.reflect.Reflects;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class IOs {
    public static void close(Object target) {
        if (target == null) {
            return;
        }
        if (target instanceof Closeable) {
            try {
                ((Closeable) target).close();
            } catch (IOException ex) {
                // ignore it
            }
        }
        Reflects.invokeAnyMethodForcedIfPresent(target, "close", null, null);
    }

    public static String readAsString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        char[] chars = new char[1024];
        int length = -1;
        while ((length = reader.read(chars)) != -1) {
            stringBuilder.append(chars, 0, length);
        }
        return stringBuilder.toString();
    }

    public static String readAsString(InputStream reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        byte[] bytes = new byte[1024];
        int length = -1;
        while ((length = reader.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, length));
        }
        return stringBuilder.toString();
    }

}
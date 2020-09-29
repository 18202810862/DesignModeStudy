package com.chentao.designmodestudy.project1;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {

    public static void closeQuietly(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

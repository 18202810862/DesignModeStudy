package com.chentao.designmodestudy.project1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DiskCache implements ImageCache {

    static String cacheDir = "sdcard/cache/";

    @Override
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir + url);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(cacheDir + url);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(outputStream);
        }
    }
}

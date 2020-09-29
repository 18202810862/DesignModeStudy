package com.chentao.designmodestudy.project1;

import android.graphics.Bitmap;

/**
 * 双缓存。获取图片时先从内存中获取，如果没获取到，然后从SD中获取。
 * 图片在内存和SD中都缓存一份
 */
public class DoubleCache implements ImageCache{
    MemoryCache mMemoryCache = new MemoryCache();
    DiskCache   mDiskCache   = new DiskCache();

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }
}

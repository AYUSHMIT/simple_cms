package com.example.blubirch.myapplication_camera;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.LruCache;
//import static com.example.blubirch.myapplication_camera.cache.getBitmapFromMemCache;
//import static com.example.blubirch.myapplication_camera.cache.mMemoryCache;
//import com.example.android.displayingbitmaps.util;

/**
 * Created by blubirch on 15/2/17.
 */

public class MyApp extends Application {


    public static LruCache<String, Bitmap> mMemoryCache;


    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";



    public void onCreate() {
        super.onCreate();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };




        // Required initialization logic here!
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }


    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }






}
package com.example.gankapp.util;

import android.content.Context;

import com.example.gankapp.ui.bean.MobUserInfo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class ACache {


    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
    private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
    private ACacheManager  mCache;

    public ACache(File file, int maxSize, int maxCount) {
        if (!file.exists() && !file.mkdirs()){
            throw new RuntimeException("can't make dirs in " + file.getAbsolutePath());
        }
        mCache = new ACacheManager(file, maxSize, maxCount);
    }


    public static ACache get(Context context) {
        return get(context, "ACache");
    }

    private static ACache get(Context context, String cacheName) {
        File file = new File(context.getCacheDir(),cacheName);
        return get(file, MAX_SIZE, MAX_COUNT);
    }

    private static ACache get(File file, int maxSize, int maxCount) {
        ACache aCache = mInstanceMap.get(file.getAbsolutePath() + myPid());
        if (aCache == null){
            aCache = new ACache(file, maxSize, maxCount);
            mInstanceMap.put(file.getAbsolutePath() + myPid(), aCache);
        }
        return aCache;
    }

    private static String myPid(){
        return "_" + android.os.Process.myPid();
    }

    /**
     * 读取 Serializable数据
     * @param key
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null){
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;

            try {
                bis = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bis);
                Object reObject = ois.readObject();
                return reObject;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                    try {
                        if (bis != null){
                        bis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    if (ois != null){
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private byte[] getAsBinary(String key) {
        return new byte[0];
    }

    /**
     * @title 缓存管理器
     * @author huchun
     * @version 1.0
     */
    public class ACacheManager{

        private AtomicLong cacheSize;
        private AtomicInteger cacheCount;
        private long sizeLimit;
        private int  countLimit;
        private Map<File, Long> lastUsageDatas = Collections.synchronizedMap(new HashMap<File, Long>());
        private File cacheDir;

        public ACacheManager(File file, int maxSize, int maxCount) {
           this.cacheDir = file;
           this.sizeLimit = maxSize;
           this.countLimit = maxCount;
           cacheSize = new AtomicLong();
           cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        /**
         * 计算 cacheSize和cacheCount
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null){
                        for (File cachedFile : cachedFiles){
                            size += calculateSize(cachedFile);
                            count += 1;
                            lastUsageDatas.put(cachedFile, cachedFile.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }
}

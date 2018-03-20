package com.example.gankapp.util;

import android.content.Context;
import android.content.Entity;
import android.util.Log;

import com.example.gankapp.ui.bean.MobUserInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import jp.wasabeef.glide.transformations.internal.Utils;

import static android.system.Os.posix_fallocate;
import static android.system.Os.remove;
import static java.util.Arrays.copyOfRange;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class ACache {

    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
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

    /**
     * 获取 byte 数据
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        RandomAccessFile raFile = null;
        boolean removeFile = false;
        try {
            File file = mCache.get(key);
            if (!file.exists())
                return null;
            raFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) raFile.length()];
            raFile.read(byteArray);
            if (!Utils.isDue(byteArray)) {
                return Utils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
        return null;
    }

    public boolean remove(String key){
        return mCache.remove(key);
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================
    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     * @param saveTime
     *            保存的时间，单位：秒
     */
    private void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            byte[] data = bos.toByteArray();
            if (saveTime != -1){
                put(key, data, saveTime);
            }else{
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(oos != null && bos != null) {
                    oos.close();
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================
    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的数据
     */
    public void put(String key, byte[] value){
        File file = mCache.newFile(key);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fos != null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
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

        public File get(String key) {
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDatas.put(file,currentTime);
            return file;
        }

        private File newFile(String key) {
            return new File(cacheDir, key.hashCode() + "");
        }

        public boolean remove(String key) {
            File image = get(key);
            return image.delete();
        }

        public void put(File file) {
            int curCacheCount = cacheCount.get();
            while (curCacheCount + 1 > countLimit){
                long freedSize = removeNext();
                cacheSize.addAndGet(-freedSize);
                curCacheCount = cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);

            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            while (curCacheSize + valueSize > sizeLimit){
                long freedSize = removeNext();
                curCacheSize = cacheSize.addAndGet(-freedSize);
            }
            cacheSize.addAndGet(valueSize);

            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDatas.put(file,curCacheSize);
        }

        /**
         * 移除旧的文件
         *
         * @return
         */
        private long removeNext() {
            if (lastUsageDatas.isEmpty()){
                return 0;
            }

            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Map.Entry<File, Long>> entities = lastUsageDatas.entrySet();
            synchronized (lastUsageDatas){
                for (Map.Entry<File,Long> entry : entities){
                    if (mostLongUsedFile == null){
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    }else{
                        Long lastValueUsage = entry.getValue();
                        if(lastValueUsage < oldestUsage){
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }
            long fileSize = calculateSize(mostLongUsedFile);
            if (mostLongUsedFile.delete()){
                lastUsageDatas.remove(mostLongUsedFile);
            }
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    /**
     * @title 时间计算工具类
     * @author huchun
     * @version 1.0
     */
    private static class Utils{

        private static final char mSeparator = ' ';
        /**
         * 判断缓存的String数据是否到期
         *
         * @param str
         * @return true：到期了 false：还没有到期
         */
        public static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2){
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")){
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000){
                    return true;
                }
            }
            return false;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (hasDateInfo(data)){
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        public static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)){
                return copyOfRange(data, indexOf(data, mSeparator) +1, data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == '-' && indexOf(data, mSeparator) > 14;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to){
            int newLength = to - from;
            if (newLength < 0)
                throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static int indexOf(byte[] data, char c) {
            for(int i =0; i < data.length; i++){
                if (data[i] == c){
                    return i;
                }
            }
            return -1;
        }


    }
}

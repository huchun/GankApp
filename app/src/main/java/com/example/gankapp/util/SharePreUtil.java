package com.example.gankapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 存储和去除配置信息的工具类
 * Created by chunchun.hu on 2018/3/12.
 */

public class SharePreUtil {

    private static String CONFIG = "config";
    private static SharedPreferences sp;

    /**
     * 获取String类型配置信息
     * @param context	上下文
     * @param key		键
     * @param defValue		值
     */
    public static String getStringData(Context context, String key, String defValue) {
        if (sp == null){
            sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }

    /**
     * 保存boolean类配置信息
     * @param context	上下文
     * @param key		键
     * @param value		值
     */
    public static boolean saveBooleanData(Context context, String key, boolean value) {
        if (sp == null){
            sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, value);
    }

    /**
     * 保存int类型配置信息
     * @param context	上下文
     * @param key		键
     * @param value		值
     */
    public static void saveIntData(Context context, String key, int value) {
        if (sp == null){
            sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }
}

package com.example.gankapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gankapp.SplashActivity;

/**
 * Created by chunchun.hu on 2018/3/6.
 */

public class SkinManager {

    public final static int THEME_DAY = 0;
    public final static int THEME_NIGHT = 1;

    private static String CONFIG = "config";
    private static String SkinKey = "SkinKey";
    private static SharedPreferences sharedPreferences;

    /**
     * 获取当前主题的Type
     *
     * @param context
     * @return 0：白天主题；1：夜间主题
     */
    public static int getCurrentSkinType(Context context) {
        return getSharePreSkin(context,THEME_DAY);
    }

    private static int getSharePreSkin(Context context, int defValue) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(SkinKey,defValue);
    }
}

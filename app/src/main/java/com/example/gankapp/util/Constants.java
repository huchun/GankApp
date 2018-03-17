package com.example.gankapp.util;

import android.os.Environment;

/**
 * 常量类
 * 一些接口地址等常量
 * Created by chunchun.hu on 2018/3/7.
 */

public class Constants {

    //接口请求的Url
    public static final String BASEURL = "http://gank.io/api/";

    //保存图片的地址
    public static final String BasePath = Environment.getExternalStorageDirectory() + "/GankApp";

    public static final String SPSwitcherDataType = "SPSwitcherDataType"; //保存首页头条

    //Mob官网API
    public static final String URL_Mob = "http://apicloud.mob.com/";
    //Mob ApiKey
    public static final String URL_APP_Key = "1c9dccb9a2434";


    public static final String FlagWelFare = "福利";
}

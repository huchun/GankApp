package com.example.gankapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

/**
 * Created by chunchun.hu on 2018/3/6.
 */

public class MyApplicaiton extends Application {

    private static final String TAG = "MyApplicaiton";

    private static MyApplicaiton applicaiton = null;
    private static Handler mHandler;

    public MyApplicaiton(){

    }

    public static MyApplicaiton getIntstance(){
        synchronized (MyApplicaiton.class) {
            if (null == applicaiton) {
                applicaiton = new MyApplicaiton();
            }
        }
        return applicaiton;
    }

    public static Handler getHandler() {
        if (mHandler == null){
            mHandler = new Handler();
        }
        return mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBase();
        initLog();  //初始化Log
        initJpush();
        initCrash(); //初始化异常捕获
        initUmeng(); //Umeng统计相关

    }

    private void initBase() {
         applicaiton = this;
         mHandler = new Handler();
    }

    private void initLog() {

    }

    private void initJpush() {

    }

    private void initCrash() {

    }

    private void initUmeng() {

    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;

        try {
            PackageManager manager = context.getPackageManager();
            packageInfo = manager.getPackageInfo(context.getPackageName(),PackageManager.GET_CONFIGURATIONS);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}

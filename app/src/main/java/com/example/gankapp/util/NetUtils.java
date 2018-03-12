package com.example.gankapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class NetUtils {

    private static ConnectivityManager connectivityManager = null;

    public static ConnectivityManager getConnectivityManager(Context context){
        if (connectivityManager == null){
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityManager;
    }

    /**
     * 判断是否具有网络连接
     *
     * @return
     */
    public static boolean hasNetWorkConnection(Context context) {
        // 获取连接活动管理器
        NetworkInfo networkInfo = getConnectivityManager(context).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }
}

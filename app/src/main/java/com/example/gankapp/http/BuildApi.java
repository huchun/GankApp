package com.example.gankapp.http;

import android.util.Log;

import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chunchun.hu on 2018/3/12.
 * 获取网络框架类
 */

public class BuildApi {

    private static Retrofit retrofit;

    public static APIService getAPIService() {
        Log.d("TAG","getAPIService");
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                     .baseUrl(Constants.BASEURL) //设置Base的访问路径
                     .addConverterFactory(GsonConverterFactory.create())  //设置默认的解析库：Gson
                     .client(MyApplicaiton.defaultOkHttpClient())
                     .build();
        }
        return retrofit.create(APIService.class);
    }
}

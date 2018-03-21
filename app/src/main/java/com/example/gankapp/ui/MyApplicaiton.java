package com.example.gankapp.ui;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.example.gankapp.BuildConfig;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.util.ACache;
import com.example.gankapp.util.NetUtils;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by chunchun.hu on 2018/3/6.
 */

public class MyApplicaiton extends Application {

    private static final String TAG = "MyApplicaiton";

    private static MyApplicaiton applicaiton = null;
    private static Handler mHandler;
    private static ACache aCache;

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
         aCache = ACache.get(this); //初始化ACache类
    }

    public static ACache getACache() {
        return aCache;
    }

    private void initLog() {
      //  KLog.init(BuildConfig.LOG_DEBUG, "---GankAPP---");
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

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
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

    public static Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long  t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            KLog.i(TAG, "-----LoggingInterceptor----- :\nrequest url:" + request.url() + "\ntime:" + (t2 - t1) / 1e6d + "\nbody:" + content + "\n");
            return response.newBuilder().body(ResponseBody.create(mediaType,content)).build();
        }
    };

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //设置缓存路径
        File httpCacheDirectory = new File(applicaiton.getCacheDir(), "okhttpCache");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        client.cache(cache);
        //设置拦截器
        client.addInterceptor(LoggingInterceptor);
        client.addInterceptor(new ChuckInterceptor(applicaiton));
        client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return client.build();
    }

    private static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //方案一：有网和没有网都是先读缓存
//                Request request = chain.request();
//                Log.i(TAG, "request=" + request);
//                Response response = chain.proceed(request);
//                Log.i(TAG, "response=" + response);
//
//                String cacheControl = request.cacheControl().toString();
//                if (TextUtils.isEmpty(cacheControl)) {
//                    cacheControl = "public, max-age=60";
//                }
//                return response.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();

            //方案二：无网读缓存，有网根据过期时间重新请求
            boolean netWorkConnection = NetUtils.hasNetWorkConnection(MyApplicaiton.getIntstance());
            Request request = chain.request();
            if (!netWorkConnection){
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response response = chain.proceed(request);
            if (netWorkConnection){
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                response.newBuilder()
                        .removeHeader("Pragma")  // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", cacheControl)
                        .build();
            }else {
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };
}

package com.example.gankapp.http;

import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.HttpResult;
import com.example.gankapp.ui.bean.RandomEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by chunchun.hu on 2018/3/12.
 * 接口调用的工具类
 */

public interface APIService {

    //http://gank.io/api/data/Android/3/1
    @Headers("Cache-Control: public, max-age=120")
    @GET("data/{type}/{count}/{pageIndex}")
    Call<HttpResult<List<GankEntity>>> getCommonDateNew(@Path("type") String type,
                                                        @Path("count") int count,
                                                        @Path("pageIndex") int pageIndex);

    //http://gank.io/api/random/data/Android/5 --- 随机数据
    @Headers("Cache-Control: public, max-age=300")
    @GET("random/data/{type}/{count}")
    Call<RandomEntity> getRandomDatas(@Path("type") String type,
                                      @Path("count") int count);
}

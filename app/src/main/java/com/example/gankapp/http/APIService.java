package com.example.gankapp.http;

import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.HttpResult;
import com.example.gankapp.ui.bean.MobBaseEntity;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.ui.bean.RandomEntity;
import com.example.gankapp.ui.bean.SearchBean;
import com.example.gankapp.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chunchun.hu on 2018/3/12.
 * 接口调用的工具类
 */

public interface APIService {

    //http://gank.io/api/data/Android/10/1
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

    //Search
    //http://gank.io/api/search/query/listview/category/Android/count/10/page/1
    @Headers("Cache-Control:public, max-age=120")
    @GET("search/query/{keyword}/category/{type}/count/{count}/page/{pageIndex}")
    Call<HttpResult<List<SearchBean>>> getSearchData(@Path("keyword") String keyWord,
                                                     @Path("type")String type,
                                                     @Path("count")int count,
                                                     @Path("pageIndex")int indexPage);

    /* ----------------------用户系统接口---------------------- */
    //用户注册
    //http://apicloud.mob.com/user/rigister?key=123456&username=tangsir&password=4AC36A18EA02AC6C
    @GET(Constants.URL_Mob + "user/rigister")
    Call<MobBaseEntity> userRegister(@Query("key") String appkey,
                                     @Query("username")String username,
                                     @Query("password")String userpassword,
                                     @Query("email")String useremail);

    //用户登录
    //http://apicloud.mob.com/user/login?key=123456&username=tangsir&password=4AC36A18EA02AC6C
    @GET(Constants.URL_Mob + "user/login")
    Call<MobBaseEntity<MobUserInfo>> userLogin(@Query("key")String key,
                                               @Query("username")String username,
                                               @Query("password")String password);
}

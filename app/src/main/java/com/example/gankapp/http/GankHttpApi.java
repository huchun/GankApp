package com.example.gankapp.http;

import android.util.Log;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.AppUpdateInfo;
import com.example.gankapp.ui.bean.CitysEntity;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.HttpResult;
import com.example.gankapp.ui.bean.RandomEntity;
import com.example.gankapp.ui.bean.SearchBean;
import com.example.gankapp.ui.bean.WeatherBaseEntity;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.UserUtils;
import com.socks.library.KLog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chunchun.hu on 2018/3/12.
 * 所有的网络请求
 */

public class GankHttpApi {

    private static final String TAG = "GankHttpApi";

    public final static String GET_DATA_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_dialog_confirm);
    public final static String NET_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_dialog_confirm);

    public static Call<HttpResult<List<GankEntity>>> getCommonDataNew(String type, int count, int pageIndex, final int what, final MyCallBack httpCallBack) {
        Log.d("TAG","getCommonDataNew");
        Call<HttpResult<List<GankEntity>>> commonDataNew = BuildApi.getAPIService().getCommonDateNew(type, count, pageIndex);

       commonDataNew.enqueue(new Callback<HttpResult<List<GankEntity>>>() {
           @Override
           public void onResponse(Call<HttpResult<List<GankEntity>>> call, Response<HttpResult<List<GankEntity>>> response) {
               Log.d("TAG","onResponse");
               if (response.isSuccessful()){
                   HttpResult<List<GankEntity>> httpResult = response.body();
                   if (httpResult != null){
                       KLog.i(httpResult.toString());
                       if (!httpResult.isError()){
                           List<GankEntity> entityList = httpResult.getResults();
                           httpCallBack.onSuccessList(what,entityList);
                       }else{
                           httpCallBack.onFail(what,GET_DATA_FAIL);
                       }
                   }else{
                       httpCallBack.onFail(what,GET_DATA_FAIL);
                   }
               }else {
                   httpCallBack.onFail(what,GET_DATA_FAIL);
               }
           }

           @Override
           public void onFailure(Call<HttpResult<List<GankEntity>>> call, Throwable t) {
               KLog.e("getCommonDataNew-----onFailure：" + t.toString());
               //数据错误
               httpCallBack.onFail(what,NET_FAIL);
           }
       });
       return commonDataNew;
    }

    public static Call<RandomEntity> getRandomDatas(String type, int count, final int what, final MyCallBack httpCallBack) {
        Log.d("TAG","getRandomDatas"+type.toString());
        Call<RandomEntity> randomEntityCall = BuildApi.getAPIService().getRandomDatas(type,count);

        randomEntityCall.enqueue(new Callback<RandomEntity>() {
            @Override
            public void onResponse(Call<RandomEntity> call, Response<RandomEntity> response) {
                Log.d("TAG","onResponse" + response.toString());
                  if (response.isSuccessful()){
                      RandomEntity randomEntity = response.body();
                      Log.d("TAG","randomEntity" + randomEntity.toString());
                      if (randomEntity != null){
                          if (!randomEntity.isError()){
                              KLog.i("getRandomDatas---success：" + randomEntity.toString());
                              httpCallBack.onSuccessList(what,randomEntity.getResults());
                          }else{
                              httpCallBack.onFail(what,GET_DATA_FAIL);
                          }
                      }else{
                          httpCallBack.onFail(what,GET_DATA_FAIL);
                      }
                  }else{
                      httpCallBack.onFail(what,GET_DATA_FAIL);
                  }
            }

            @Override
            public void onFailure(Call<RandomEntity> call, Throwable t) {
                KLog.e("getRandomDatas-----onFailure：" + t.toString());
                httpCallBack.onFail(what, NET_FAIL); //数据错误
            }
        });
        return randomEntityCall;
    }

    /**
     * 获取搜索结果
     *
     * @param keyWord    关键字
     * @param type       类型  category 后面可接受参数 all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
     * @param count      搜索长度
     * @param indexPage  页码
     * @param what
     * @param httpCallBack
     * @return
     */
    public static Call<HttpResult<List<SearchBean>>> getSearchData(String keyWord, String type, int count, int indexPage, final int what, final MyCallBack httpCallBack) {
          Log.d(TAG, "getSearchData" + keyWord.toString()+type.toString());
          Call<HttpResult<List<SearchBean>>>  searchData = BuildApi.getAPIService().getSearchData(keyWord, type, count, indexPage);

          searchData.enqueue(new Callback<HttpResult<List<SearchBean>>>() {
              @Override
              public void onResponse(Call<HttpResult<List<SearchBean>>> call, Response<HttpResult<List<SearchBean>>> response) {
                  Log.d(TAG, "onResponse");
                  if (response.isSuccessful()){
                      HttpResult<List<SearchBean>> httpResult = response.body();
                      if (httpResult != null){
                          if (!httpResult.isError()){
                              List<SearchBean> entityList = httpResult.getResults();
                              Log.i(TAG,"getHistoryData---success：" + entityList.toString());
                              httpCallBack.onSuccessList(what, entityList);
                          }else{
                              httpCallBack.onFail(what, GET_DATA_FAIL);
                          }
                      }else{
                          httpCallBack.onFail(what, GET_DATA_FAIL);
                      }
                  }else{
                      httpCallBack.onFail(what, GET_DATA_FAIL);
                  }
              }

              @Override
              public void onFailure(Call<HttpResult<List<SearchBean>>> call, Throwable t) {
                  Log.d(TAG, "onFailure" + t.toString());
                  httpCallBack.onFail(what, NET_FAIL); //数据错误
              }
          });
         return searchData;
    }

    public static Call<AppUpdateInfo> getAppUpdateInfo(final int what, final MyCallBack httpCallBack) {
        Log.d(TAG,"getAppUpdateInfo");
        Call<AppUpdateInfo>   updateInfoCall = BuildApi.getAPIService().getTheLastAppInfo();
        updateInfoCall.enqueue(new Callback<AppUpdateInfo>() {
            @Override
            public void onResponse(Call<AppUpdateInfo> call, Response<AppUpdateInfo> response) {
                Log.d(TAG, "onResponse" + response.toString());
                if (response.isSuccessful()){
                    AppUpdateInfo body = response.body();
                    if (body != null){
                        if (body.getName().equals("干货")){
                            httpCallBack.onSuccess(what, body);
                        }else{
                            httpCallBack.onFail(what,GET_DATA_FAIL);
                        }
                    }else{
                        httpCallBack.onFail(what,GET_DATA_FAIL);
                    }
                }else{
                    httpCallBack.onFail(what,GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<AppUpdateInfo> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.toString());
                httpCallBack.onFail(what,NET_FAIL);
            }
        });
        return updateInfoCall;
    }

    /***
     * 获取城市列表
     *
     * @param what
     * @param httpCallBack
     * @return
     */
    public static Call<CitysEntity> getCitys(final int what, final MyCallBack httpCallBack) {
        Log.d(TAG, "getCitys");
        Call<CitysEntity> entityCall = BuildApi.getAPIService().getCitys(Constants.URL_APP_Key);
        entityCall.enqueue(new Callback<CitysEntity>() {
            @Override
            public void onResponse(Call<CitysEntity> call, Response<CitysEntity> response) {
                Log.d(TAG, "onResponse " + response.toString());
                if (response.isSuccessful()){
                    CitysEntity citysEntity = response.body();
                    if (citysEntity != null){
                        UserUtils.saveCitysCache(citysEntity);
                        if (citysEntity.getMsg().equals("success")){
                            Log.d(TAG, "success:" + citysEntity.toString());
                            httpCallBack.onSuccess(what,citysEntity.getResult());
                        }else{
                            httpCallBack.onFail(what, GET_DATA_FAIL);
                        }
                    }else{
                        httpCallBack.onFail(what, GET_DATA_FAIL);
                    }
                }else{
                    httpCallBack.onFail(what, GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<CitysEntity> call, Throwable t) {
                Log.d(TAG, "onFailure " + t.toString());
                httpCallBack.onFail(what, NET_FAIL);
            }
        });
          return entityCall;
    }

    /***
     * 获取城市天气信息
     *
     * @param city
     * @param province
     * @param what
     * @param myCallBack
     * @return
     */
    public static Call<WeatherBaseEntity> getCityWeather(String city, String province, final int what, final MyCallBack myCallBack) {
        Log.d(TAG, "getCityWeather" + city.toString() + province.toString());
        final Call<WeatherBaseEntity> weatherBaseEntityCall = BuildApi.getAPIService().getCityWeather(Constants.URL_APP_Key,city, province);
        weatherBaseEntityCall.enqueue(new Callback<WeatherBaseEntity>() {
            @Override
            public void onResponse(Call<WeatherBaseEntity> call, Response<WeatherBaseEntity> response) {
                 Log.d(TAG, "onResponse" + response.toString());
                if (response.isSuccessful()){
                    WeatherBaseEntity weatherBaseEntity = response.body();
                    if (weatherBaseEntity != null){
                        if (weatherBaseEntity.getMsg().equals("success")){
                            Log.d(TAG, "success" + weatherBaseEntity.toString());
                            myCallBack.onSuccess(what, weatherBaseEntity.getResult());
                        }else {
                            myCallBack.onFail(what,GET_DATA_FAIL);
                        }
                    }else{
                        myCallBack.onFail(what,GET_DATA_FAIL);
                    }
                }else{
                    myCallBack.onFail(what,GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<WeatherBaseEntity> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.toString());
                myCallBack.onFail(what,NET_FAIL);
            }
        });
        return weatherBaseEntityCall;
    }
}

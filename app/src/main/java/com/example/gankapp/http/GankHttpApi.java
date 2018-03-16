package com.example.gankapp.http;

import android.util.Log;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.HttpResult;
import com.example.gankapp.ui.bean.RandomEntity;
import com.example.gankapp.ui.bean.SearchBean;
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
}

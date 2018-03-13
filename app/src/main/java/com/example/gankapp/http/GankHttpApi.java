package com.example.gankapp.http;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.HttpResult;
import com.example.gankapp.ui.bean.RandomEntity;
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

    public final static String GET_DATA_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_dialog_confirm);
    public final static String NET_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_dialog_confirm);

    public static Call<HttpResult<List<GankEntity>>> getCommonDataNew(String type, int count, int pageIndex, final int what, final MyCallBack httpCallBack) {
        Call<HttpResult<List<GankEntity>>> commonDataNew = BuildApi.getAPIService().getCommonDateNew(type, count, pageIndex);

       commonDataNew.enqueue(new Callback<HttpResult<List<GankEntity>>>() {
           @Override
           public void onResponse(Call<HttpResult<List<GankEntity>>> call, Response<HttpResult<List<GankEntity>>> response) {
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
        Call<RandomEntity> randomEntityCall = BuildApi.getAPIService().getRandomDatas(type,count);

        randomEntityCall.enqueue(new Callback<RandomEntity>() {
            @Override
            public void onResponse(Call<RandomEntity> call, Response<RandomEntity> response) {
                  if (response.isSuccessful()){
                      RandomEntity randomEntity = response.body();
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
}

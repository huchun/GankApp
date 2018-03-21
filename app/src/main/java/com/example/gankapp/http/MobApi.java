package com.example.gankapp.http;

import android.util.Log;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.MobBaseEntity;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class MobApi {

    private static final String TAG = "MobApi";
    public final static String GET_DATA_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_get_data_fail);
    public final static String NET_FAIL = MyApplicaiton.getIntstance().getString(R.string.gank_net_fail);

    /* -----start------用户系统------------ */
    public static Call<MobBaseEntity> userRegister(String userName, String userPassword, String userEmail, final int what, final MyCallBack myCallBack) {
            Log.d(TAG, "userRegister" + userName.toString() + userPassword.toString() + userEmail.toString());

        Call<MobBaseEntity> call = BuildApi.getAPIService().userRegister(Constants.URL_APP_Key, userName, userPassword, userEmail);
        call.enqueue(new Callback<MobBaseEntity>() {
            @Override
            public void onResponse(Call<MobBaseEntity> call, Response<MobBaseEntity> response) {
                 Log.d(TAG, "onResponse"+ response.toString());
                 if(response.isSuccessful()){
                      MobBaseEntity body = response.body();
                      if (body != null){
                          if (body.getMsg().equals("success")){
                               Log.d(TAG, "success" + body.toString());
                               myCallBack.onSuccess(what, body.getResult());
                          }else{
                              myCallBack.onFail(what, body.getMsg());
                          }
                      }else{
                          myCallBack.onFail(what, GET_DATA_FAIL);
                      }
                 }else{
                     myCallBack.onFail(what, GET_DATA_FAIL);
                 }
            }

            @Override
            public void onFailure(Call<MobBaseEntity> call, Throwable t) {
                Log.d(TAG, "onFailure"+ t.toString());
                //数据错误
                myCallBack.onFail(what, NET_FAIL);
            }
        });
        return call;
    }

    public static Call<MobBaseEntity<MobUserInfo>> userLogin(String username, String password, final int what, final MyCallBack myCallBack) {
        Log.d(TAG, "userLogin" + username.toString() + password.toString());
         Call<MobBaseEntity<MobUserInfo>> call = BuildApi.getAPIService().userLogin(Constants.URL_APP_Key, username, password);
         call.enqueue(new Callback<MobBaseEntity<MobUserInfo>>() {
             @Override
             public void onResponse(Call<MobBaseEntity<MobUserInfo>> call, Response<MobBaseEntity<MobUserInfo>> response) {
                  Log.d(TAG, "onResponse" + response.toString());
                  if (response.isSuccessful()){
                      MobBaseEntity body = response.body();
                      if (body != null){
                          if (body.getMsg().equals("success")){
                              Log.d(TAG, "success" + body.toString());
                              myCallBack.onSuccess(what, body.getResult());
                          }else{
                              myCallBack.onFail(what, body.getMsg());
                          }
                      }else{
                          myCallBack.onFail(what, GET_DATA_FAIL);
                      }
                  }else{
                      myCallBack.onFail(what, GET_DATA_FAIL);
                  }
             }

             @Override
             public void onFailure(Call<MobBaseEntity<MobUserInfo>> call, Throwable t) {
                 Log.d(TAG, "onFailure" + t.toString());
                 myCallBack.onFail(what,GET_DATA_FAIL);   //数据错误
             }
         });

        return call;
    }

    public static Call<MobBaseEntity> userGetCodeVerification(String userName, final int what, final MyCallBack httpCallBack) {
        Log.d(TAG, "userGetCodeVerification" + userName.toString());
         Call<MobBaseEntity> call = BuildApi.getAPIService().userGetCodeVerification(Constants.URL_APP_Key, userName);

         call.enqueue(new Callback<MobBaseEntity>() {
             @Override
             public void onResponse(Call<MobBaseEntity> call, Response<MobBaseEntity> response) {
                 Log.d(TAG, "onResponse" + response.toString());
                 if (response.isSuccessful()){
                     MobBaseEntity entity = response.body();
                     if (entity != null){
                         if (entity.getRetCode().equals("200")){
                              Log.d(TAG, "success：" + entity.toString());
                             httpCallBack.onSuccess(what, entity.getMsg());
                         }else{
                              httpCallBack.onFail(what, entity.getMsg());
                         }
                     }else{
                         httpCallBack.onFail(what, GET_DATA_FAIL);
                     }
                 }else{
                     httpCallBack.onFail(what, GET_DATA_FAIL);
                 }
             }

             @Override
             public void onFailure(Call<MobBaseEntity> call, Throwable t) {
                 Log.d(TAG, "onFailure" + t.toString());
                 httpCallBack.onFail(what, NET_FAIL);  //数据错误
             }
         });
         return call;
    }

    public static Call<MobBaseEntity> userModifyPsd(String userName, String oldPsd, String newPsd, String mode, final int what, final MyCallBack httpCallBack) {
        Log.d(TAG, "userModifyPsd" + userName.toString() + oldPsd.toString() + newPsd.toString()+mode.toString());
        Call<MobBaseEntity>  call = BuildApi.getAPIService().userModifyPsd(Constants.URL_APP_Key, userName, oldPsd, newPsd, mode);
        call.enqueue(new Callback<MobBaseEntity>() {
            @Override
            public void onResponse(Call<MobBaseEntity> call, Response<MobBaseEntity> response) {
                Log.d(TAG, "onResponse" + response.toString());
                if (response.isSuccessful()){
                    MobBaseEntity body = response.body();
                    if (body != null){
                        if (body.getMsg().equals("200")){
                            Log.d(TAG, "success：" + body.toString());
                            Object result = body.getResult();
                            httpCallBack.onSuccess(what, result);
                        }else{
                            httpCallBack.onFail(what, body.getMsg());
                        }
                    }else{
                          httpCallBack.onFail(what, GET_DATA_FAIL);
                    }
                }else{
                    httpCallBack.onFail(what, GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<MobBaseEntity> call, Throwable t) {
                Log.d(TAG, "onFailure" + t.toString());
                httpCallBack.onFail(what, NET_FAIL);  //数据错误
            }
        });

        return call;
    }
}

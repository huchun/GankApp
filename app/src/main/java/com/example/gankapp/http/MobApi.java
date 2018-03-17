package com.example.gankapp.http;

import android.util.Log;

import com.example.gankapp.ui.bean.MobBaseEntity;
import com.example.gankapp.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class MobApi {

    private static final String TAG = "MobApi";

    /* -----start------用户系统------------ */
    public static Call<MobBaseEntity> userRegister(String userName, String userPassword, String userEmail, int what, MyCallBack myCallBack) {
            Log.d(TAG, "userRegister" + userName.toString() + userPassword.toString() + userEmail.toString());

        Call<MobBaseEntity> call = BuildApi.getAPIService().userRegister(Constants.URL_APP_Key, userName, userPassword, userEmail);
        call.enqueue(new Callback<MobBaseEntity>() {
            @Override
            public void onResponse(Call<MobBaseEntity> call, Response<MobBaseEntity> response) {
                 Log.d(TAG, "onResponse"+ response.toString());


            }

            @Override
            public void onFailure(Call<MobBaseEntity> call, Throwable t) {
                Log.d(TAG, "onFailure"+ t.toString());


            }
        });

        return null;
    }
}

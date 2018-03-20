package com.example.gankapp.ui.activity.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.gankapp.R;
import com.example.gankapp.http.MobApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.util.AppValidationMgr;
import com.example.gankapp.util.KeyboardUtils;
import com.example.gankapp.util.MySnackbar;
import com.example.gankapp.util.SkinManager;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    private LinearLayout ll_bg = null;
    private Toolbar   mToolbar = null;
    private EditText  mEtUserName = null;
    private EditText  mEtUserPassword = null;
    private EditText  mEtEmail = null;
    private Button    mBtnRegister = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        ll_bg =  findViewById(R.id.ll_bg);
        mToolbar = findViewById(R.id.toolbar);
        mEtUserName = findViewById(R.id.et_user_name);
        mEtUserPassword = findViewById(R.id.et_user_password);
        mEtEmail = findViewById(R.id.et_email);
        mBtnRegister = findViewById(R.id.btn_register);

        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            initBaseToolBar(mToolbar, "注册", R.drawable.gank_ic_back_white);
        }else{
            initBaseToolBar(mToolbar, "注册", R.drawable.gank_ic_back_night);
        }

        initListener();
    }

    private void initListener() {
        ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(RegisterActivity.this); //隐藏输入法
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(RegisterActivity.this); //隐藏输入法

                String userName = mEtUserName.getText().toString();
                String userPassword = mEtUserPassword.getText().toString();
                String userEmail = mEtEmail.getText().toString();

                //获取数据
                if (TextUtils.isEmpty(userName)){
                    MySnackbar.makeSnackBarRed(mToolbar, "用户名不可为空");
                    return;
                }
                if (TextUtils.isEmpty(userPassword)){
                    MySnackbar.makeSnackBarRed(mToolbar, "密码不可为空");
                    return;
                }
                if (TextUtils.isEmpty(userEmail)){
                    MySnackbar.makeSnackBarRed(mToolbar, "邮箱不可为空");
                    return;
                }

                if (userPassword.length() < 6){
                    MySnackbar.makeSnackBarRed(mToolbar, "密码不能少于6位");
                    return;
                }
                if (!AppValidationMgr.isEmail(userEmail)){
                    MySnackbar.makeSnackBarRed(mToolbar, "邮箱格式不正确");
                    return;
                }
                showProgressDialog("注册中...");
                MobApi.userRegister(userName, userPassword, userEmail, 0x001, myCallBack);
            }
        });
    }

   private MyCallBack myCallBack = new MyCallBack() {
       @Override
       public void onSuccess(int what, Object result) {
           Log.d(TAG, "onSuccess" + result.toString());
           dismissProgressDialog();
           showProgressSuccess("注册成功,即将关闭页面!");

           //关闭页面
           MyApplicaiton.getHandler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   closeActivity();
               }
           }, 1000);
       }

       @Override
       public void onSuccessList(int what, List results) {
           Log.d(TAG, "onSuccessList" + results.toString());
       }

       @Override
       public void onFail(int what, String result) {
           Log.d(TAG, "onFail" + result.toString());
           dismissProgressDialog();
           MySnackbar.makeSnackBarRed(mToolbar, result);
       }
   };

    private void closeActivity() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

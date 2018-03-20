package com.example.gankapp.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.http.MobApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.util.KeyboardUtils;
import com.example.gankapp.util.MySnackbar;
import com.example.gankapp.util.SkinManager;
import com.example.gankapp.util.UserUtils;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/17.
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private LinearLayout ll_bg = null;
    private Toolbar mToolbar = null;
    private EditText mEtUserName = null;
    private EditText mEtPassword = null;
    private TextView mTvForgetPwd = null;
    private Button   mBtnLogin = null;
    private Button   mBtnRegister = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        ll_bg = findViewById(R.id.ll_bg);
        mToolbar = findViewById(R.id.toolbar);
        mEtUserName = findViewById(R.id.et_user_name);
        mEtPassword = findViewById(R.id.et_user_password);
        mTvForgetPwd = findViewById(R.id.btn_forget);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);

        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            initBaseToolBar(mToolbar, "登录", R.drawable.gank_ic_back_white);
        }else{
            initBaseToolBar(mToolbar, "登录", R.drawable.gank_ic_back_night);
        }

        initListener();
    }

    private void initListener() {
        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(LoginActivity.this);  //隐藏输入法

                //获取数据
                final String username = mEtUserName.getText().toString();
                final String password = mEtPassword.getText().toString();

                // 判空
                if (TextUtils.isEmpty(username)){
                    MySnackbar.makeSnackBarRed(mToolbar, "用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    MySnackbar.makeSnackBarRed(mToolbar, "密码不能为空");
                    return;
                }
                showProgressDialog("正在登录...");
                MobApi.userLogin(username, password, 0x001, new MyCallBack(){

                    @Override
                    public void onSuccess(int what, Object result) {
                         Log.d(TAG, "onSuccess" + result.toString());
                         dismissProgressDialog();
                         showProgressDialog("登录成功!");
                        MobUserInfo userInfo = (MobUserInfo) result;
                        userInfo.setUserName(username);
                        userInfo.setUserPsd(password);

                        //保存用户信息
                        UserUtils.saveUserCache(userInfo);

                        //关闭当前页面
                        closeActivity();
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
                });
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mTvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPsdActivity.class);
                intent.putExtra(ForgetPsdActivity.IntentKey_Mode, 2);
                startActivity(intent);
            }
        });
    }

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

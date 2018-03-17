package com.example.gankapp.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.util.KeyboardUtils;
import com.example.gankapp.util.SkinManager;

/**
 * Created by chunchun.hu on 2018/3/17.
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private RelativeLayout ll_bg = null;
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

            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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

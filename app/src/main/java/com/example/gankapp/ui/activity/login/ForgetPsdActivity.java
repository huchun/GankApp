package com.example.gankapp.ui.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gankapp.R;
import com.example.gankapp.http.MobApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.util.KeyboardUtils;
import com.example.gankapp.util.MySnackbar;
import com.example.gankapp.util.SkinManager;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/20.
 */

public class ForgetPsdActivity extends BaseActivity {

    public static final String TAG = "ForgetPsdActivity";
    public static final String IntentKey_Mode = "IntentKey_Mode";

    private int mMode;
    private MyCountDownTimer mMyCountDownTimer;

    private Toolbar mToolbar = null;
    private EditText mEtUserName = null;
    private EditText mEtOldPassword = null;
    private Button   mBtnGetCode = null;
    private EditText mEtNewPassword = null;
    private Button   mBtnOk = null;
    private ImageView mIvYzm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd);

        mMode = getIntent().getIntExtra(IntentKey_Mode, 1);
        initViews();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        mEtUserName = findViewById(R.id.et_user_name);
        mBtnGetCode = findViewById(R.id.btn_get_code);
        mEtOldPassword = findViewById(R.id.et_old_yzm);
        mEtNewPassword = findViewById(R.id.et_new_password);
        mBtnOk = findViewById(R.id.btn_ok);
        mIvYzm = findViewById(R.id.iv_yzm);

        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            initBaseToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_white);
        }else{
            initBaseToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_night);
        }

        initListener();
    }

    private void initListener() {
        if (mMode == 1){
            mBtnGetCode.setVisibility(View.GONE);
            mEtOldPassword.setHint("请输入旧密码");
            mIvYzm.setBackgroundResource(R.mipmap.icon_login_pw);
        }else{
            mIvYzm.setBackgroundResource(R.mipmap.icon_user_yzm);
            mEtOldPassword.setHint("请输入验证码");
            mBtnGetCode.setVisibility(View.VISIBLE);
            mMyCountDownTimer = new MyCountDownTimer(60000, 1000);
        }

        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(ForgetPsdActivity.this); //隐藏键盘
                String userName = mEtUserName.getText().toString();  //获取用户名
                if (TextUtils.isEmpty(userName)){
                    MySnackbar.makeSnackBarRed(mToolbar, "用户名不能为空");
                    return;
                }
                //获取验证码
                mMyCountDownTimer.start();
                mBtnGetCode.setClickable(true);
                mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.gray_light));
                mBtnGetCode.setText("60s 后再次发送");

                MobApi.userGetCodeVerification(userName, 0x001, httpCallBack);  //发送
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(ForgetPsdActivity.this); //隐藏键盘

                String userName = mEtUserName.getText().toString();  //获取用户名
                String oldPsd = mEtOldPassword.getText().toString();  //获取旧密码
                String newPsd = mEtNewPassword.getText().toString();  //获取新密码

                if (TextUtils.isEmpty(userName)){
                     MySnackbar.makeSnackBarRed(mToolbar, "用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(oldPsd)){
                    if (mMode == 1){
                        MySnackbar.makeSnackBarRed(mToolbar, "用户名不能为空");
                    }else{
                        MySnackbar.makeSnackBarRed(mToolbar, "验证码不能为空");
                    }
                    return;
                }
                if (TextUtils.isEmpty(newPsd)){
                    MySnackbar.makeSnackBarRed(mToolbar, "新密码不能为空");
                    return;
                }
                if (newPsd.length() < 6){
                    MySnackbar.makeSnackBarRed(mToolbar, "新密码不能少于6位");
                    return;
                }
                //修改密码
                MobApi.userModifyPsd(userName, oldPsd, newPsd, String.valueOf(mMode), 0x002, httpCallBack);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private class MyCountDownTimer extends CountDownTimer{

        public MyCountDownTimer(int millisInFuture, int countDownInterval) {
            super(millisInFuture, countDownInterval);
            Log.d(TAG, "MyCountDownTimer" );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(TAG, "onTick" );
            mBtnGetCode.setText(millisUntilFinished / 1000 + "s 后再次发送");
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "onFinish" );
            resetBtn();
        }
    }

    private void resetBtn() {
        Log.d(TAG, "resetBtn" );
        mBtnGetCode.setText("重新获取验证码");
        mBtnGetCode.setClickable(true); //设置可点击
        mBtnGetCode.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
    }

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Object result) {
            Log.d(TAG, "onSuccess");
            dismissProgressDialog();
            if (what == 0x001){
                MySnackbar.makeSnackBarGreen(mToolbar, "密码追回的验证码已发送到您的邮箱,请到邮箱查看!");
            }else{
                showProgressSuccess("密码修改成功,请返回重新登录!");
                MyApplicaiton.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 500);
            }
        }

        @Override
        public void onSuccessList(int what, List results) {
            Log.d(TAG, "onSuccessList");
        }

        @Override
        public void onFail(int what, String result) {
            Log.d(TAG, "onFail");
            dismissProgressDialog();
            MySnackbar.makeSnackBarRed(mToolbar, result);
            if (what == 0x001){
                //重置按钮
                mMyCountDownTimer.cancel();
                mMyCountDownTimer.onFinish();
            }
        }
    };
}

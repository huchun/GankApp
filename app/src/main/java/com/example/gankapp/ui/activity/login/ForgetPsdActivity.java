package com.example.gankapp.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gankapp.R;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.util.SkinManager;

/**
 * Created by chunchun.hu on 2018/3/20.
 */

public class ForgetPsdActivity extends BaseActivity {

    public static final String TAG = "ForgetPsdActivity";
    public static final String IntentKey_Mode = "IntentKey_Mode";

    private int mMode;

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
        initToolbar();
        initViews();
    }

    private void initToolbar() {
         int currentSkinType = SkinManager.getCurrentSkinType(this);
         if (SkinManager.THEME_DAY == currentSkinType){
             initBaseToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_white);
         }else{
             initBaseToolBar(mToolbar, "修改密码", R.drawable.gank_ic_back_night);
         }
    }

    private void initViews() {
        mEtUserName = findViewById(R.id.et_user_name);
        mBtnGetCode = findViewById(R.id.btn_get_code);
        mEtOldPassword = findViewById(R.id.et_old_yzm);
        mEtNewPassword = findViewById(R.id.et_new_password);
        mBtnOk = findViewById(R.id.btn_ok);
        mIvYzm = findViewById(R.id.iv_yzm);

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
}

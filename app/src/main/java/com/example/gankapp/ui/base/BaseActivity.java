package com.example.gankapp.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.example.gankapp.R;
import com.example.gankapp.util.SkinManager;
import com.jaeger.library.StatusBarUtil;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by chunchun.hu on 2018/3/7.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    public Context mContext;
    private MStatusDialog mStatusDialog;
    private MProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        //设置主题
        SkinManager.onActivityCreateSetSkin(this);
        super.onCreate(savedInstanceState);
        mContext = this;

        initStatus();
        initDialog();
    }

    private void initStatus() {
        Log.d(TAG, "initStatus");
        //设置状态栏的颜色
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color));
        }else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color_night));
        }
    }

    private void initDialog() {
        mProgressDialog = new MProgressDialog.Builder(this).build();
        mStatusDialog = new MStatusDialog(this);
    }

    public void initBaseToolBar(Toolbar toolbar, String title, int icon) {
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(icon);
        setSupportActionBar(toolbar);
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color));
            toolbar.setPopupTheme(R.style.Widget_AppCompat_Toolbar);
        }else{
            toolbar.setTitleTextColor(getResources().getColor(R.color.gank_text1_color_night));
            toolbar.setPopupTheme(R.style.Widget_AppCompat_Toolbar);
        }
    }

    public void showProgressDialog() {
         dismissProgressDialog();
         mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public void showProgressDialog(String msg) {
       if (TextUtils.isEmpty(msg)){
           showProgressDialog();
       }else{
           dismissProgressDialog();
           mProgressDialog.show(msg);
       }
    }

    public void showProgressSuccess(String msg) {
        mStatusDialog.show(msg, getResources().getDrawable(R.mipmap.mn_icon_dialog_success));
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        // MainActivity 内嵌套Fragment，应在Fragment中统计页面
        String className = this.getClass().getSimpleName();
        if (!"MainActivity".equals(className)){
            MobclickAgent.onPageStart(className);  // 统计页面
        }
        MobclickAgent.onResume(this);  //统计时长
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        // MainActivity 内嵌套Fragment，应在Fragment中统计页面
        String className = this.getClass().getSimpleName();
        if (!"MainActivity".equals(className)){
            MobclickAgent.onPageStart(className);  // 统计页面
        }
        MobclickAgent.onPause(this);  //统计时长
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "keyCode"+"event");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        dismissProgressDialog();
        Glide.with(this.getApplicationContext()).pauseRequests();
        super.onDestroy();
    }
}

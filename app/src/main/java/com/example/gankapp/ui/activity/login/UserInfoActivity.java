package com.example.gankapp.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gankapp.R;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.ui.view.MySettingItemView;
import com.example.gankapp.util.DialogUtils;
import com.example.gankapp.util.UserUtils;
import com.jaeger.library.StatusBarUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chunchun.hu on 2018/3/20.
 */

public class UserInfoActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";

    private ImageView   mImageBack = null;
    private LinearLayout mllContent = null;
    private TextView     mTvUserName = null;
    private CircleImageView mAvatarCircle = null;
    private TextView        mTvEditInfo = null;
    private MySettingItemView mItemAppCollect = null;
    private MySettingItemView mItemAppSetting = null;
    private MySettingItemView mItemAppPsd = null;
    private MySettingItemView mItemAppMarket = null;
    private MySettingItemView mItemAppSupport = null;
    private Button  mBtnQuitLogin = null;

    private MobUserInfo mUserCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        StatusBarUtil.setTranslucentForImageView(this, 20, mllContent);
        initView();
    }

    private void initView() {
        mImageBack = findViewById(R.id.btn_back);
        mTvUserName = findViewById(R.id.tv_user_name);
        mAvatarCircle = findViewById(R.id.avatar);
        mTvEditInfo = findViewById(R.id.btn_edit_info);
        mllContent = findViewById(R.id.ll_content);
        mItemAppCollect = findViewById(R.id.item_app_collect);
        mItemAppSetting = findViewById(R.id.item_app_setting);
        mItemAppPsd = findViewById(R.id.item_app_psd);
        mItemAppMarket = findViewById(R.id.item_app_market);
        mItemAppSupport  = findViewById(R.id.item_app_support);
        mBtnQuitLogin  = findViewById(R.id.btn_quit_login);

        initListener();
    }

    private void initListener() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       mTvEditInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
      mItemAppCollect.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
      mItemAppSetting.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
      mItemAppPsd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(UserInfoActivity.this, ForgetPsdActivity.class);
              intent.putExtra(ForgetPsdActivity.IntentKey_Mode, 1);
              startActivity(intent);
          }
      });
      mItemAppMarket.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
      mItemAppSupport.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
      mBtnQuitLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // exit login
              DialogUtils.showMyDialog(mContext, "退出提示", "确定要退出当前用户吗?", "退出", "取消", new DialogUtils.OnDialogClickListener() {
                  @Override
                  public void onConfirm() {
                      quitLogin();
                  }

                  @Override
                  public void onCancel() {   }
              });
          }
      });
    }

    private void quitLogin() {
        // clean login info
        UserUtils.quitLogin();
        this.finish();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        refreshUserInfo();
    }

    private void refreshUserInfo() {
        //刷新数据
        mUserCache = UserUtils.getUserCache();
        //设置头像
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.icon_default_avatar);
        options.error(R.mipmap.icon_default_avatar);
        Glide.with(mContext).load(mUserCache.getAvatarLocal()).apply(options).into(mAvatarCircle);
        mTvUserName.setText(mUserCache.getUserName());
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}

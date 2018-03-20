package com.example.gankapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gankapp.R;

/**
 * Created by chunchun.hu on 2018/3/20.
 * 设置界面的组合控件
 */

public class MySettingItemView extends FrameLayout {

    private Context context;

    //声明的控件
    private TextView tv_title = null;  //标题
    private TextView tv_right = null; //右侧文字
    private ImageView iv_red_dot = null;  //小红点

    private String title;
    private String rightText;
    private boolean isShowRedDot;
    private boolean redDot;

    public MySettingItemView(@NonNull Context context) {
        this(context, null);
    }

    public MySettingItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySettingItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingView);
        //遍历拿到自定义属性
        for (int i =0; i < typedArray.getIndexCount(); i++){
             int index = typedArray.getIndex(i);
             switch (index){
                 case R.styleable.SettingView_setting_title:
                    title = typedArray.getString(R.styleable.SettingView_setting_title);
                     break;
                 case R.styleable.SettingView_setting_red_dot:
                     isShowRedDot = typedArray.getBoolean(R.styleable.SettingView_setting_red_dot, false);
                     break;
                 case R.styleable.SettingView_setting_right_text:
                     rightText = typedArray.getString(R.styleable.SettingView_setting_right_text);
                     break;
             }
        }
        typedArray.recycle();  //销毁
        initView(); //初始化View
    }

    private void initView() {
        View.inflate(context, R.layout.setting_item_view, this);
        tv_title = findViewById(R.id.setting_tv_title);
        tv_right = findViewById(R.id.setting_tv_right);
        iv_red_dot = findViewById(R.id.setting_iv_red);

        tv_title.setText(title);
        setRedDot(isShowRedDot);
        setRightText(rightText);
    }

    private void setRedDot(boolean flag) {
        if (flag){
            iv_red_dot.setVisibility(VISIBLE);
        }else{
            iv_red_dot.setVisibility(GONE);
        }
    }

    private void setRightText(String rightText) {
        if (TextUtils.isEmpty(rightText)){
            tv_right.setVisibility(GONE);
        }else{
            tv_right.setVisibility(VISIBLE);
            tv_right.setText(rightText);
        }
    }
}

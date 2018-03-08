package com.example.gankapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.example.gankapp.R;
import com.example.gankapp.util.SkinManager;

/**
 * Created by chunchun.hu on 2018/3/7.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

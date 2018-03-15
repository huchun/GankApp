package com.example.gankapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.util.SkinManager;

/**
 * Created by chunchun.hu on 2018/3/6.
 */
public class SplashActivity extends AppCompatActivity {

    TextView app_version;
    TextView shadeBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);

        app_version = (TextView) findViewById(R.id.tv_app_version);
        shadeBg = (TextView) findViewById(R.id.shade_bg);

        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (currentSkinType == SkinManager.THEME_NIGHT){
            shadeBg.setVisibility(View.VISIBLE);
        }

        MyApplicaiton.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

        app_version.setText(String.valueOf("V" + MyApplicaiton.getVersionName(SplashActivity.this)));
    }
}

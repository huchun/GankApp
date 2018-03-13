package com.example.gankapp.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.gankapp.ui.MyApplicaiton;

/**
 * Created by chunchun.hu on 2018/3/13.
 * 提示框
 */

public class MySnackbar {


    public static void makeSnackBarBlack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        int currentSkinType = SkinManager.getCurrentSkinType(MyApplicaiton.getIntstance().getApplicationContext());
        if (SkinManager.THEME_DAY == currentSkinType){
            ColoredSnackbar.defaultInfo(snackbar).show();
        }else{
            ColoredSnackbar.defaultInfoNight(snackbar).show();
        }
    }
}

package com.example.gankapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;

import com.example.gankapp.R;
import com.example.gankapp.ui.MyApplicaiton;

/**
 * Created by chunchun.hu on 2018/3/6.
 */

public class SkinManager {

    public final static int THEME_DAY = 0;
    public final static int THEME_NIGHT = 1;

    private static String CONFIG = "config";
    private static String SkinKey = "SkinKey";
    private static SharedPreferences sharedPreferences;

    /**
     * 获取当前主题的Type
     *
     * @param context
     * @return 0：白天主题；1：夜间主题
     */
    public static int getCurrentSkinType(Context context) {
        return getSharePreSkin(context,THEME_DAY);
    }

    private static int getSharePreSkin(Context context, int defValue) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(SkinKey,defValue);
    }

    /*--------------------WebView 夜间模式-----------------*/
    public static void setupWebView(WebView webView, String backgroudColor, String fontColor, String urlColor) {
          if (webView != null){
              webView.setBackgroundColor(0);
              if (getCurrentSkinType(MyApplicaiton.getIntstance()) == THEME_NIGHT){
                  String js = String.format(jsStyle, backgroudColor, fontColor, urlColor, backgroudColor);
                  webView.loadUrl(js);
              }
          }
    }

    public static void onActivityCreateSetSkin(Activity activity) {
         int currentSkinTheme = getCurrentSkinTheme(activity.getApplicationContext());
         activity.setTheme(currentSkinTheme);
    }

    /**
     * 获取当前主题
     * @param context
     * @return
     */
    private static int getCurrentSkinTheme(Context context) {
        int saveSkinType = getCurrentSkinType(context);
        int currentTheme;
        switch (saveSkinType){
            default:
            case THEME_DAY:
                currentTheme = R.style.DayTheme;
                break;
           // case THEME_NIGHT:
              //  currentTheme = R.style.NightTheme;
            //    break;
        }
        return currentTheme;
    }

    private static String jsStyle ="https://github.com/huchun"; /**"javascript:(function(){\n" +
            "\t\t   document.body.style.backgroundColor=\"%s\";\n" +
            "\t\t    document.body.style.color=\"%s\";\n" +
            "\t\t\tvar as = document.getElementsByTagName(\"a\");\n" +
            "\t\tfor(var i=0;i<as.length;i++){\n" +
            "\t\t\tas[i].style.color = \"%s\";\n" +
            "\t\t}\n" +
            "\t\tvar divs = document.getElementsByTagName(\"div\");\n" +
            "\t\tfor(var i=0;i<divs.length;i++){\n" +
            "\t\t\tdivs[i].style.backgroundColor = \"%s\";\n" +
            "\t\t}\n" +
            "\t\t})()";*/
}

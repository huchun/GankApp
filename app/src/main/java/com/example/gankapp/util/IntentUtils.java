package com.example.gankapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.gankapp.ui.activity.WebActivity;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.imagebrowser.MNImageBrowser;

import java.util.ArrayList;

/**
 * Created by chunchun.hu on 2018/3/8.
 * 页面跳转
 */

public class IntentUtils {


    public static final String WebTitleFlag = "WebTitleFlag";
    public static final String WebTitle = "WebTitle";
    public static final String WebUrl = "WebUrl";

    public static final String PushMessage = "PushMessage";

    public static void startToImageShow(Context context, ArrayList<String> mDatas, ArrayList<GankEntity> gankEntityList, int position, View view) {
        MNImageBrowser.showImageBrowser(context, view, position, mDatas, gankEntityList);
    }

    public static void startAppShareText(Context context, String shareTitle, String shareText) {
        Log.d("TAG", "shareTitle"+shareTitle.toString() + "shareText"+shareText.toString());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain"); // 纯文本
        shareIntent.putExtra(Intent.EXTRA_TITLE, shareTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    public static void startToWebActivity(Context context, String titleFlag, String title, String url) {
        Log.d("TAG", "titleFlag"+titleFlag.toString() + "title"+title.toString() + "url"+url.toString());
        Intent intent = new Intent(context.getApplicationContext(), WebActivity.class);
        intent.putExtra(WebTitleFlag, titleFlag);
        intent.putExtra(WebTitle, title);
        intent.putExtra(WebUrl, url);
        context.startActivity(intent);
    }
}

package com.example.gankapp.util;

import android.content.Context;
import android.view.View;

import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.imagebrowser.MNImageBrowser;

import java.util.ArrayList;

/**
 * Created by chunchun.hu on 2018/3/8.
 * 页面跳转
 */

public class IntentUtils {


    public static final String PushMessage = "PushMessage";

    public static void startToImageShow(Context context, ArrayList<String> mDatas, ArrayList<GankEntity> gankEntityList, int position, View view) {
        MNImageBrowser.showImageBrowser(context, view, position, mDatas, gankEntityList);
    }
}

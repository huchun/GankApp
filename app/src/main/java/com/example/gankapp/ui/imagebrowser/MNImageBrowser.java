package com.example.gankapp.ui.imagebrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.example.gankapp.R;
import com.example.gankapp.ui.bean.GankEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class MNImageBrowser {

    /**
     * 打开浏览页面
     *
     * @param context   上下文
     * @param view      点击的当前View
     * @param position  默认打开第几个
     * @param imageList 数据源ArrayList<String>
     */
    public static void showImageBrowser(Context context, View view, int position, ArrayList<String> imageList, ArrayList<GankEntity> gankEntityList) {
        Intent intent = new Intent(context, MNImageBrowserActivity.class);
        intent.putExtra(MNImageBrowserActivity.IntentKey_ImageList,imageList);
        intent.putExtra(MNImageBrowserActivity.IntentKey_CurrentPosition,position);
        intent.putExtra(MNImageBrowserActivity.IntentKey_GankEntityList, gankEntityList);

        //android V4包的类,用于两个activity转场时的缩放效果实现
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);

        try {
            ActivityCompat.startActivity(context,intent,optionsCompat.toBundle());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.browser_enter_anim,0);
        }
    }
}

package com.example.gankapp.util;

import android.content.Context;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class DensityUtil {


    /**
     * 获取屏幕宽度
     *
     * @date 2017年7月23日
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dip2Px(Context context, int dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

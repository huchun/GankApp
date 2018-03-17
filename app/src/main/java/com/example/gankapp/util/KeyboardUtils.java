package com.example.gankapp.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by chunchun.hu on 2018/3/16.
 * desc  : 键盘相关工具类
 */

public class KeyboardUtils {

    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 动态隐藏软键盘
     * @param context 上下文
     * @param view    视图
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    /**
     * 动态隐藏软键盘
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

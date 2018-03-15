package com.example.gankapp.ui.presenter.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.gankapp.ui.iview.IWebView;

/**
 * Created by chunchun.hu on 2018/3/15.
 */

public class WebPresenterImpl extends BasePresenterImpl<IWebView> implements IWebPresenter {

    private Context context;

    public WebPresenterImpl(Context mContext, IWebView iWebView) {
        this.context = mContext;
        attachView(iWebView);
    }

    @Override
    public void copy(String string) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        ClipData clipData = ClipData.newPlainText("text", string);
        clipboardManager.setPrimaryClip(clipData);
        if (mView != null){
            mView.showToast("复制成功!");
        }
    }

    @Override
    public void openBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}

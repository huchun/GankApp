package com.example.gankapp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.gankapp.R;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.iview.IWebView;
import com.example.gankapp.ui.presenter.impl.WebPresenterImpl;
import com.example.gankapp.util.IntentUtils;
import com.example.gankapp.util.MySnackbar;
import com.example.gankapp.util.NetUtils;
import com.example.gankapp.util.SkinManager;

/**
 * Created by chunchun.hu on 2018/3/15.
 * 展示网页的
 */

public class WebActivity extends BaseActivity implements IWebView{

    public static final String TAG = "WebActivity";

    private Toolbar mToolbar = null;
    private LinearLayout rootView = null;
    private ProgressBar  progressBar = null;
    private WebView      webView = null;

    //标题
    private String  flagTitle;
    private String  titleContent;
    private String  url;

    private WebPresenterImpl webPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webPresenter = new WebPresenterImpl(this, (IWebView) this);

        rootView = findViewById(R.id.rootView);
        mToolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(android.R.id.progress);
        webView = findViewById(R.id.webView);

        flagTitle = getIntent().getStringExtra(IntentUtils.WebTitleFlag);
        titleContent = getIntent().getStringExtra(IntentUtils.WebTitle);
        url = getIntent().getStringExtra(IntentUtils.WebUrl);

        initMyToolbar();
        initWebView();
    }

    private void initMyToolbar() {
        Log.d(TAG, "initMyToolbar");
       String title;
       if (TextUtils.isEmpty(flagTitle)){
           title =  titleContent;
       }else {
           title = flagTitle + "+" + titleContent;
       }
       int currentSkinType = SkinManager.getCurrentSkinType(this);
       if (SkinManager.THEME_DAY == currentSkinType){
           initBaseToolBar(mToolbar, title, R.drawable.gank_ic_back_white);
       }else {
           initBaseToolBar(mToolbar, title, R.drawable.gank_ic_back_night);
       }
    }

    private void initWebView() {
        Log.d(TAG, "initWebView");
          webView.setBackgroundColor(0);  //设置背景色
          webView.getSettings().setJavaScriptEnabled(true); //设置WebView属性，能够执行Javascript脚本
          webView.getSettings().setAllowFileAccess(true);

          webView.getSettings().setDomStorageEnabled(true);  // 开启DOM storage API 功能
          webView.getSettings().setDatabaseEnabled(true);  // 开启database storage API功能
          webView.getSettings().setDefaultTextEncodingName("UTF-8");

          webView.getSettings().setUseWideViewPort(true);  //自适应屏幕
          webView.getSettings().setLoadWithOverviewMode(true);

          webView.getSettings().setSupportZoom(true);  // 设置可以支持缩放
          webView.getSettings().setBuiltInZoomControls(true);

          webView.getSettings().setDisplayZoomControls(false); //不显示webview缩放按钮
          webView.setInitialScale(100); //设置缩放比例：最小25

        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        if (NetUtils.hasNetWorkConnection(this)){
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  // 根据cache-control决定是否从网络上取数据。
        }else{
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //优先加载缓存
        }

        webView.loadUrl(url);
        //设置了默认在本应用打开，不设置会用浏览器打开的
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading");
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished");
                super.onPageFinished(view, url);
                //设置webView
                String backgroudColor = "#232736";
                String fontColor = "#626f9b";
                String urlColor = "#9AACEC";
                SkinManager.setupWebView(webView, backgroudColor, fontColor, urlColor);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted");
                super.onPageStarted(view, url, favicon);
               webView.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100){
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE);
                }else{
                    // 加载中
                    if (progressBar.getVisibility() == View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        webView.onPause();
        webView.reload(); //保证了webView退出后不再有声音
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (webView.canGoBack()){
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        webPresenter.detachView();
        if (webView != null){
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            rootView.removeView(webView);
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, 1, "复制链接");
        menu.add(Menu.NONE, Menu.FIRST+1, 2, "浏览器打开");
        menu.add(Menu.NONE, Menu.FIRST+2, 3, "分享链接");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case 1:
                webPresenter.copy(url);
                break;
            case 2:
                webPresenter.openBrowser(url);
                break;
            case 3:
                IntentUtils.startAppShareText(WebActivity.this, "GankApp链接分享", "GankApp链接分享:" + url);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBaseProgressDialog(String msg) {
        Log.d(TAG, "showBaseProgressDialog" + msg.toString());
          showProgressDialog();
    }

    @Override
    public void hideBaseProgressDialog() {
        Log.d(TAG, "hideBaseProgressDialog");
          dismissProgressDialog();
    }

    @Override
    public void showToast(String msg) {
        Log.d(TAG, "showToast" + msg.toString());
        MySnackbar.makeSnackBarBlack(mToolbar,msg);
    }
}

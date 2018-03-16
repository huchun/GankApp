package com.example.gankapp.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.gankapp.http.GankHttpApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.bean.SearchBean;
import com.example.gankapp.ui.iview.ISearchView;
import com.example.gankapp.ui.presenter.ISearchPresenter;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/16.
 */

public class SearchPresenterImpl extends BasePresenterImpl<ISearchView> implements ISearchPresenter {

    private static final String TAG = "SearchPresenterImpl";

    private Context mContext;
    private int pageSize = 20;
    private int pageIndex = 1;
    private String keyWord;
    private List<SearchBean>  searchList;

    public SearchPresenterImpl(Context context, ISearchView iSearchView) {
        this.mContext = context;
        attachView(iSearchView);
    }

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Object result) {
            Log.d(TAG, "onSuccess"+result.toString());
        }

        @Override
        public void onSuccessList(int what, List results) {
            Log.d(TAG, "onSuccessList");
              if (mView == null){
                  return;
              }
              if (what == 0x001){
                  mView.hideBaseProgressDialog();
                  searchList = results;
                  if (searchList != null && searchList.size() > 0){
                      Log.d(TAG, "searchList");
                      mView.setSearchList(searchList);
                  }
                  //判断是不是可以使用上啦加载更多功能
                  if (searchList == null || searchList.size() == 0 || searchList.size() < pageIndex * pageSize){
                      mView.setLoadMoreEnabled(false);
                  }else{
                      mView.setLoadMoreEnabled(true);
                  }
                  pageIndex = 1;
                  pageIndex++;
                  mView.overRefresh();
              }else if (what == 0x002){
                   searchList.addAll(results);
                   if (searchList != null && searchList.size() > 0){
                       Log.d(TAG, "searchList");
                       mView.setSearchList(searchList);
                   }
                   pageIndex++;
                   mView.overRefresh();
              }
        }

        @Override
        public void onFail(int what, String result) {
            Log.d(TAG, "onFail" + result.toString());
              if (mView == null){
                  return;
              }
              mView.hideBaseProgressDialog();
              mView.overRefresh();
              if (!TextUtils.isEmpty(result)){
                  mView.showToast(result);
              }
        }
    };

    @Override
    public void searchDatas(String keyWords) {
        Log.d(TAG, "searchDatas" + keyWords.toString());
          if (mView == null){
              return;
          }

        //开始搜索
        if (TextUtils.isEmpty(keyWords)){
              mView.showToast("关键字不能为空");
              return;
        }
        keyWord = keyWords;
        //获取数据
        mView.showBaseProgressDialog("搜索中...");
        GankHttpApi.getSearchData(keyWord, "all", pageSize, pageIndex, 0x001, httpCallBack);
    }

    @Override
    public void loadMoreDatas() {
        Log.d(TAG, "loadMoreDatas");
        GankHttpApi.getSearchData(keyWord, "all", pageSize, pageIndex, 0x002, httpCallBack);
    }

    @Override
    public void itemClick(int position) {

    }
}

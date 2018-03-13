package com.example.gankapp.ui.presenter.impl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.gankapp.http.GankHttpApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.fragment.WelFareFragment;
import com.example.gankapp.ui.iview.IWelFareView;
import com.example.gankapp.ui.presenter.IWelFarePresenter;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.IntentUtils;
import com.example.gankapp.util.SharePreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class WelFarePresenterImpl extends BasePresenterImpl<IWelFareView> implements IWelFarePresenter {

    private Context mContext;

    private List<GankEntity> welFareLists;
    private List<GankEntity> randommLists;
    private int pageSize = 20;
    private int pageIndex = 1;
    private ArrayList<String>  imageList = new ArrayList<>();

    public WelFarePresenterImpl(Context context, IWelFareView iWelFareView) {
          this.mContext = context;
          attachView(iWelFareView);
    }

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Object result) {

        }

        @Override
        public void onSuccessList(int what, List results) {
               if (mView == null){
                   return;
               }
               switch (what){
                   case 0x001:
                       if (results == null){
                           mView.overRefresh();;
                           return;
                       }
                       if (welFareLists == null){
                           welFareLists = new ArrayList<>();
                       }
                       if (pageIndex == 1 && welFareLists.size() > 0){
                           welFareLists.clear();
                       }
                       welFareLists.addAll(results);
                       mView.setWelFareList(welFareLists);
                       if (welFareLists == null || welFareLists.size() == 0 || welFareLists.size() < pageIndex * pageSize){
                            mView.setLoadMoreEnabled(false);
                       }else{
                           mView.setLoadMoreEnabled(true);
                       }
                       pageIndex++;
                       mView.overRefresh();
                       break;
                   case 0x002://下拉刷新
                        if (results == null){
                          mView.overRefresh();
                          return;
                        }
                        pageIndex = 1;
                        pageIndex++;
                        welFareLists = results;
                        if (welFareLists.size()>0){
                            mView.setWelFareList(welFareLists);
                        }
                        mView.overRefresh();
                       break;
                   case 0x003://头条，10条随机数
                       randommLists = results;
                      mView.setRandomList(randommLists);
                       break;
               }
        }

        @Override
        public void onFail(int what, String result) {
               if (mView == null){
                   return;
               }
               mView.overRefresh();
               if (!TextUtils.isEmpty(result)){
                   mView.showToast(result);
               }
        }
    };

    @Override
    public void getNewDatas() {
        GankHttpApi.getCommonDataNew(Constants.FlagWelFare, pageSize, 1, 0x002, httpCallBack);
        getRandomDatas();
    }

    @Override
    public void getMoreDatas() {
        GankHttpApi.getCommonDataNew(Constants.FlagWelFare, pageSize, 1, 0x001, httpCallBack);
    }

    @Override
    public void getRandomDatas() {
     //查看配置的干活类型:默认Android
      String headLineType = SharePreUtil.getStringData(mContext, Constants.SPSwitcherDataType, "Android");
      GankHttpApi.getRandomDatas(headLineType,10,0x003,httpCallBack);
    }

    @Override
    public void itemClick(View view, int position) {
        imageList.clear();
        for (int i = 0; i < welFareLists.size(); i++){
            imageList.add(welFareLists.get(i).getUrl());
        }
        IntentUtils.startToImageShow(mContext, imageList, (ArrayList<GankEntity>)welFareLists, position, view);
    }
}

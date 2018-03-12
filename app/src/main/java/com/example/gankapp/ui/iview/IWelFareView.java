package com.example.gankapp.ui.iview;

import com.example.gankapp.ui.bean.GankEntity;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/9.
 */

public interface IWelFareView {

    void setWelFareList(List<GankEntity> welFareList);

    void setRandomList(List<GankEntity> randomList);

    void showToast(String msg);

    void overRefresh();

    void setLoadMoreEnabled(boolean flag);
}

package com.example.gankapp.ui.iview;

import com.example.gankapp.ui.bean.SearchBean;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/16.
 */

public interface ISearchView extends IBaseView{

    void setSearchList(List<SearchBean> resultList);

    void showToast(String msg);

    void overRefresh();

    void setLoadMoreEnabled(boolean flag);
}

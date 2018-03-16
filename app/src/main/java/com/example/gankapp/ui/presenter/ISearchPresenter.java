package com.example.gankapp.ui.presenter;

/**
 * Created by chunchun.hu on 2018/3/16.
 * 搜索
 */

public interface ISearchPresenter {

    void searchDatas(String keyWords);

    void loadMoreDatas();

    void itemClick(int position);
}

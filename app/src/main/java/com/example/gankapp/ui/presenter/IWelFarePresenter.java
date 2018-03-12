package com.example.gankapp.ui.presenter;

import android.view.View;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public interface IWelFarePresenter {

    void getNewDatas();

    void getMoreDatas();

    void getRandomDatas();

    void itemClick(View view, int position);
}

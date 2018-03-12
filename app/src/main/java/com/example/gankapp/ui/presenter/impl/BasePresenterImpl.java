package com.example.gankapp.ui.presenter.impl;

import com.example.gankapp.ui.presenter.IBasePresenter;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class BasePresenterImpl<T> implements IBasePresenter{

    public T mView;

    protected void attachView(T mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
       this.mView = null;
    }
}

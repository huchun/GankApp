package com.example.gankapp.ui.presenter;

/**
 * Created by chunchun.hu on 2018/3/21.
 */

public interface IMainPresenter {

    void initFeedBack();

    void initAppUpdate();

    void getLocationInfo();

    void getCitys();

    void destroyLocation();

    void initDatas();

    void getCityWeather(String provinceName, String cityName);
}

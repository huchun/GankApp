package com.example.gankapp.ui.iview;

import com.example.gankapp.ui.bean.AppUpdateInfo;
import com.example.gankapp.ui.bean.WeatherBaseEntity;

/**
 * Created by chunchun.hu on 2018/3/21.
 */

public interface IMainView {

    void showToast(String msg);

    void showAppUpdateDialog(AppUpdateInfo appUpdateInfo);

    void initWeatherInfo(WeatherBaseEntity.WeatherBean weatherEntity);

    void updateLocationInfo(String provinceName, String cityName);
}

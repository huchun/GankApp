package com.example.gankapp.ui.iview;

import com.example.gankapp.ui.bean.CalendarInfoEntity;
import com.example.gankapp.ui.bean.WeatherBaseEntity;

/**
 * Created by chunchun.hu on 2018/3/23.
 */

public interface IWeatherView {

    void showToast(String msg);

    void initWeatherInfo(WeatherBaseEntity.WeatherBean weatherEntity);

    void overRefresh();

    void updateCalendarInfo(CalendarInfoEntity calendarInfoEntity);
}

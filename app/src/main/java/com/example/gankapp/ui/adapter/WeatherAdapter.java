package com.example.gankapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.gankapp.ui.activity.WeatherActivity;
import com.example.gankapp.ui.bean.CalendarInfoEntity;
import com.example.gankapp.ui.bean.WeatherBaseEntity;

/**
 * Created by chunchun.hu on 2018/3/23.
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public WeatherAdapter(WeatherActivity weatherActivity, WeatherBaseEntity.WeatherBean weatherEntity, CalendarInfoEntity calendarInfoEntity) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void updateDatas(WeatherBaseEntity.WeatherBean weatherEntity, CalendarInfoEntity calendarInfoEntity) {

    }
}

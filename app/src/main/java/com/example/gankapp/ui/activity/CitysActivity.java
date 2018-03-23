package com.example.gankapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.gankapp.R;
import com.example.gankapp.ui.adapter.RecycleCitysAdapter;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.CitysEntity;
import com.example.gankapp.util.SkinManager;
import com.example.gankapp.util.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chunchun.hu on 2018/3/22.
 */

public class CitysActivity extends BaseActivity {
    public static final String TAG = "CitysActivity";

    private Toolbar mToolbar = null;
    private RecyclerView mRecycler_province = null;
    private RecyclerView mRecycler_city = null;
    private RecycleCitysAdapter  mRecycle_province_Adapter;
    private RecycleCitysAdapter  mRecycle_city_Adapter;

    private List<String>  provinceList = new ArrayList<>();
    private List<String>  citysList = new ArrayList<>();
    private Map<String, List<String>>  citysMap = new HashMap<>();
    private String  chooseProvinceName;
    private String  chooseCityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citys);

        initView();
        initToolBar();
        initAdapter();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mRecycler_province = findViewById(R.id.recycle_province);
        mRecycler_city = findViewById(R.id.recycle_city);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler_province.setLayoutManager(linearLayoutManager);
        mRecycler_province.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler_city.setLayoutManager(linearLayoutManager2);
        mRecycler_city.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (currentSkinType == SkinManager.THEME_DAY){
            initBaseToolBar(mToolbar, "城市选择", R.drawable.gank_ic_back_white);
        }else{
            initBaseToolBar(mToolbar, "城市选择", R.drawable.gank_ic_back_night);
        }
    }

    private void initAdapter() {
        CitysEntity citysCache = UserUtils.getCitysCache();
        if (citysCache != null){
            final List<CitysEntity.ResultBean>  cityList = citysCache.getResult();
            for (int i =0; i < cityList.size(); i++){
                String province = cityList.get(i).getProvince();
                provinceList.add(province);
                List<String>  cityNameList = new ArrayList<>();
                List<CitysEntity.ResultBean.CityBean>  city = cityList.get(i).getCity();
                if (city != null){
                    for (int j = 0; j< city.size(); j++){
                        String cityName = city.get(j).getCity();
                        cityNameList.add(cityName);
                    }
                    citysMap.put(province, cityNameList);
                }
            }
          mRecycle_province_Adapter = new RecycleCitysAdapter(this, provinceList);
          mRecycler_province.setAdapter(mRecycle_province_Adapter);
          mRecycle_province_Adapter.setOnItemClickListener(new RecycleCitysAdapter.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                  chooseProvinceName = provinceList.get(position);
                  citysList = citysMap.get(chooseProvinceName);
                  if (mRecycle_city_Adapter != null){
                      mRecycle_city_Adapter.updateDatas(citysList);
                  }else{
                      mRecycle_city_Adapter = new RecycleCitysAdapter(CitysActivity.this, citysList);
                      mRecycler_city.setAdapter(mRecycle_city_Adapter);
                      mRecycle_city_Adapter.setOnItemClickListener(new RecycleCitysAdapter.OnItemClickListener() {
                          @Override
                          public void onItemClick(View view, int position) {
                              chooseCityName = mRecycle_city_Adapter.getPositionValue(position);
                              //关闭界面
                              Intent intent = new Intent();
                              intent.putExtra("provinceName",chooseProvinceName);
                              intent.putExtra("cityName",chooseCityName);
                              setResult(100, intent);
                              finish();
                          }
                      });
                  }
              }
          });
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.gankapp.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gankapp.R;
import com.example.gankapp.ui.adapter.WeatherAdapter;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.CalendarInfoEntity;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.WeatherBaseEntity;
import com.example.gankapp.ui.iview.IWeatherView;
import com.example.gankapp.util.MySnackbar;

import java.util.List;
import java.util.Random;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by chunchun.hu on 2018/3/23.
 */

public class WeatherActivity extends BaseActivity implements IWeatherView{
    public static final String TAG = "WeatherActivity";

    public static final String intentKey_weatherBean = "intentKey_weatherBean";
    public static final String intentKey_weatherProvinceName = "intentKey_weatherProvinceName";
    public static final String intentKey_weatherCityName = "intentKey_weatherCityName";
    public static final String intentKey_bg_url = "intentKey_bg_url";
    private static final float maxAlpha = 1.0f;
    private static final float defaultAlpha = 0.0f;

    private String provinceName;
    private String cityName;
    private String bgPicUrl;
    private List<GankEntity> welFareList;

    private CalendarInfoEntity  calendarInfoEntity;
    private WeatherBaseEntity.WeatherBean  weatherEntity;
    private WeatherAdapter  weatherAdapter;

    private TextView tvTitle = null;
    private RecyclerView swipeTarget = null;
    private ImageView  ivBg = null;
    private ImageView  ivBg2 = null;
    private RelativeLayout llBack = null;
    private LinearLayout llContent = null;
    private LinearLayout llBgBlur = null;
    private SwipeToLoadLayout swipeToLoadLayout = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherEntity = (WeatherBaseEntity.WeatherBean) getIntent().getSerializableExtra(intentKey_weatherBean);
        welFareList = (List<GankEntity>) getIntent().getSerializableExtra(intentKey_bg_url);
        provinceName = getIntent().getStringExtra(intentKey_weatherProvinceName);
        cityName = getIntent().getStringExtra(intentKey_weatherCityName);

        initView();
        initAdapter();
        initBackgroundPic();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        swipeTarget = findViewById(R.id.swipe_target);
        ivBg = findViewById(R.id.iv_bg);
        ivBg2 = findViewById(R.id.iv_bg2);
        llBack = findViewById(R.id.rl_back);
        llContent = findViewById(R.id.ll_content);
        llBgBlur = findViewById(R.id.ll_bg_blur);
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (weatherEntity != null){
            tvTitle.setText(weatherEntity.getCity());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.setItemAnimator(new DefaultItemAnimator());
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        swipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initAdapter() {
        if (weatherAdapter == null){
            weatherAdapter = new WeatherAdapter(this, weatherEntity, calendarInfoEntity);
            swipeTarget.setAdapter(weatherAdapter);
        }else{
            weatherAdapter.updateDatas(weatherEntity, calendarInfoEntity);
        }
    }

    private void initBackgroundPic() {
        Log.d(TAG, "initBackgroundPic");
        if (welFareList != null && welFareList.size() > 0){
            Random random = new Random();
            int randomIndex = random.nextInt(welFareList.size() - 1);
            bgPicUrl = welFareList.get(randomIndex).getUrl();
            if (!TextUtils.isEmpty(bgPicUrl)){
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Glide.with(mContext)
                     .asBitmap()
                     .load(bgPicUrl)
                     .apply(options)
                     .into(new SimpleTarget<Bitmap>() {
                         @Override
                         public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                              ivBg.setImageBitmap(resource);
                              ivBg2.setImageBitmap(resource);
                             Blurry.with(WeatherActivity.this)
                                     .radius(25)
                                     .sampling(3)
                                     .async()
                                     .capture(ivBg2)
                                     .into(ivBg2);
                             llBgBlur.setAlpha(defaultAlpha);
                         }
                     });
            }
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
    public void showToast(String msg) {
        Log.d(TAG, "showToast" + msg.toString());
        MySnackbar.makeSnackBarBlack(ivBg,msg);
    }

    @Override
    public void initWeatherInfo(WeatherBaseEntity.WeatherBean weatherEntity) {
        Log.d(TAG, "initWeatherInfo");
        this.weatherEntity = weatherEntity;
        initAdapter();
        initBackgroundPic();
    }

    @Override
    public void overRefresh() {
        Log.d(TAG, "overRefresh");
        swipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void updateCalendarInfo(CalendarInfoEntity calendarInfoEntity) {
        Log.d(TAG, "updateCalendarInfo");
        this.calendarInfoEntity = calendarInfoEntity;
        initAdapter();
    }
}

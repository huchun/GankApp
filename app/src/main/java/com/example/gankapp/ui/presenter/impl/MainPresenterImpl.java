package com.example.gankapp.ui.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.gankapp.R;
import com.example.gankapp.http.GankHttpApi;
import com.example.gankapp.http.MyCallBack;
import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.AppUpdateInfo;
import com.example.gankapp.ui.bean.WeatherBaseEntity;
import com.example.gankapp.ui.iview.IMainView;
import com.example.gankapp.ui.presenter.IMainPresenter;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.NetUtils;
import com.example.gankapp.util.SharePreUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/21.
 */

public class MainPresenterImpl extends BasePresenterImpl<IMainView> implements IMainPresenter, AMapLocationListener {
    private static final String TAG = "MainPresenterImpl";

    private Context context;

    public AMapLocationClientOption mLocationClientOption = null;
    private AMapLocationClient mlocationClient;
    private String cityName;
    private String provinceName;

    public MainPresenterImpl(Context context, IMainView iMainView) {
          this.context = context;
          attachView(iMainView);
    }

    private MyCallBack httpCallBack = new MyCallBack() {
        @Override
        public void onSuccess(int what, Object result) {
            Log.d(TAG, "onSuccess" + result.toString());
            if (mView == null){
                return;
            }
            switch (what){
                case 0x001:
                    if (result == null){
                        return;
                    }
                    //获取当前APP的版本号
                    int newVersion;
                    AppUpdateInfo appUpdateInfo;

                    try {
                        appUpdateInfo = (AppUpdateInfo) result;
                        newVersion = Integer.parseInt(appUpdateInfo.getBuild());
                    }catch (Exception e){
                        e.printStackTrace();
                        newVersion = 1;
                        appUpdateInfo = new AppUpdateInfo();
                    }
                    if (MyApplicaiton.getVersionCode(context) < newVersion){
                        SharePreUtil.saveBooleanData(context, Constants.BASEURL + MyApplicaiton.getVersionCode(context), true);
                        //需要版本更新
                        if (mView != null){
                            mView.showAppUpdateDialog(appUpdateInfo);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onSuccessList(int what, List results) {
            Log.d(TAG, "onSuccessList");
            if (mView == null){
                return;
            }
            if (results == null){
                return;
            }
            switch (what){
                case 0x003:
                    List<WeatherBaseEntity.WeatherBean> weathers = results;
                    if (weathers.size() > 0){
                        WeatherBaseEntity.WeatherBean resultBean = weathers.get(0);
                        if (resultBean != null){
                            mView.initWeatherInfo(resultBean);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, String result) {
            Log.d(TAG, "onFail" + result.toString());
            if (!TextUtils.isEmpty(result)){
                mView.showToast(result);
            }
        }
    };

    @Override
    public void initFeedBack() {
        Log.d(TAG, "initFeedBack");
    }

    @Override
    public void initAppUpdate() {
        Log.d(TAG, "initAppUpdate");
        if (NetUtils.hasNetWorkConnection(context)){
            GankHttpApi.getAppUpdateInfo(0x001,httpCallBack);  //版本更新
        }
    }

    /**
     * get gao de location info
     */
    @Override
    public void getLocationInfo() {
        Log.d(TAG, "getLocationInfo");
        requestLocationInfo();
    }

    @Override
    public void getCitys() {
        Log.d(TAG, "gitCitys");
        GankHttpApi.getCitys(0x002,httpCallBack);
    }

    /**
     * 销毁定位客户端，同时销毁本地定位服务。
     */
    @Override
    public void destroyLocation() {
        Log.d(TAG, "destroyLocation");
        if (mlocationClient != null){
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void initDatas() {
        Log.d(TAG, "initDatas");
        SharePreUtil.saveIntData(context, "未知", R.mipmap.icon_weather_none);
        SharePreUtil.saveIntData(context, "晴", R.mipmap.icon_weather_sunny);
        SharePreUtil.saveIntData(context, "阴", R.mipmap.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "多云", R.mipmap.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "少云", R.mipmap.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "晴间多云", R.mipmap.icon_weather_cloudytosunny);
        SharePreUtil.saveIntData(context, "局部多云", R.mipmap.icon_weather_cloudy);
        SharePreUtil.saveIntData(context, "雨", R.mipmap.icon_weather_rain);
        SharePreUtil.saveIntData(context, "小雨", R.mipmap.icon_weather_rain);
        SharePreUtil.saveIntData(context, "中雨", R.mipmap.icon_weather_rain);
        SharePreUtil.saveIntData(context, "大雨", R.mipmap.icon_weather_rain);
        SharePreUtil.saveIntData(context, "阵雨", R.mipmap.icon_weather_rain);
        SharePreUtil.saveIntData(context, "雷阵雨", R.mipmap.icon_weather_thunderstorm);
        SharePreUtil.saveIntData(context, "霾", R.mipmap.icon_weather_haze);
        SharePreUtil.saveIntData(context, "雾", R.mipmap.icon_weather_fog);
        SharePreUtil.saveIntData(context, "雨夹雪", R.mipmap.icon_weather_snowrain);
    }

    @Override
    public void getCityWeather(String provinceName, String cityName) {
        Log.d(TAG, "getCityWeather");
        mView.updateLocationInfo(provinceName, cityName);
        GankHttpApi.getCityWeather(cityName, provinceName, 0x003, httpCallBack);
    }

    private void requestLocationInfo() {
        mlocationClient = new AMapLocationClient(context);
        mLocationClientOption = new AMapLocationClientOption(); //初始化定位参数
        mlocationClient.setLocationListener(this);  //设置定位监听
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationClientOption.setInterval(2000); //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClientOption.setHttpTimeOut(20000); //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationClientOption.setNeedAddress(true);  //设置是否返回地址信息（默认返回地址信息）
        mLocationClientOption.setOnceLocation(true); //获取一次定位结果：该方法默认为false。
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationClientOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationClientOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.d(TAG, "onLocationChanged");
        if (amapLocation != null){
            if (amapLocation.getErrorCode() == 0){
                //停止定位后，本地定位服务并不会被销毁
                if (mlocationClient != null){
                    mlocationClient.startLocation();
                }
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType(); //获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude(); //获取纬度
                amapLocation.getLongitude(); //获取经度
                amapLocation.getAccuracy(); //获取精度信息
                amapLocation.getAddress();  //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry(); //国家信息
                amapLocation.getProvince(); //省信息
                amapLocation.getCity(); //城市信息
                amapLocation.getDistrict(); //城区信息
                amapLocation.getStreet(); //街道信息
                amapLocation.getStreetNum(); //街道门牌号信息
                amapLocation.getCityCode(); //城市编码
                amapLocation.getAdCode(); //地区编码
                amapLocation.getAoiName(); //获取当前定位点的AOI信息
                amapLocation.getBuildingId(); //获取当前室内定位的建筑物Id
                amapLocation.getFloor(); //获取当前室内定位的楼层

                //获取定位时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                simpleDateFormat.format(date);

                Log.d(TAG, "定位成功:" + amapLocation.toString());
                //获取城市
                cityName = amapLocation.getCity();
                if (cityName.endsWith("市")){
                    cityName = cityName.substring(0, cityName.length() - 1);
                }
                provinceName = amapLocation.getProvince();
                if (provinceName.endsWith("省") || provinceName.endsWith("市")){
                    provinceName = provinceName.substring(0, provinceName.length() - 1);
                }
                getCityWeather(provinceName, cityName);
            }else{
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.d(TAG, "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}

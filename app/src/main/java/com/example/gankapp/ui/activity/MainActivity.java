package com.example.gankapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gankapp.R;
import com.example.gankapp.ui.activity.login.LoginActivity;
import com.example.gankapp.ui.activity.login.UserInfoActivity;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.AppUpdateInfo;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.bean.MobUserInfo;
import com.example.gankapp.ui.bean.WeatherBaseEntity;
import com.example.gankapp.ui.fragment.CategoryFragment;
import com.example.gankapp.ui.fragment.CollectFragment;
import com.example.gankapp.ui.fragment.HistoryFragment;
import com.example.gankapp.ui.fragment.WelFareFragment;
import com.example.gankapp.ui.iview.IMainView;
import com.example.gankapp.ui.presenter.impl.MainPresenterImpl;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.DialogUtils;
import com.example.gankapp.util.IntentUtils;
import com.example.gankapp.util.MySnackbar;
import com.example.gankapp.util.SharePreUtil;
import com.example.gankapp.util.SkinManager;
import com.example.gankapp.util.UserUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener , IMainView{

    private static final String TAG = "MainActivity";

    private DrawerLayout   mDrawerLayout = null;
    private NavigationView mNavigationView = null;
    private Toolbar  mToolbar = null;
    private RelativeLayout mLayoutWeather;
    private TextView  mHeader_tv_temperature = null;
    private TextView  mHeader_tv_other = null;
    private ImageView mHeader_iv_weather = null;
    private TextView  mHeader_tv_city_name = null;
    private LinearLayout mHeader_ll_choose_city;

    private Context mContext;
    private WelFareFragment welFareFragment;
    private HistoryFragment historyFragment;
    private CategoryFragment categoryFragment;
    private CollectFragment collectFragment;

    private int navigationCheckedItemId = R.id.nav_fuli;
    private String navigationCheckedTitle = "福利";
    private static final String savedInstanceStateItemId = "navigationCheckedItemId";
    private static final String savedInstanceStateTitle = "navigationCheckedTitle";

    private MainPresenterImpl mainPresenter;

    private List<GankEntity>  welFareList;
    private WeatherBaseEntity.WeatherBean  weatherEntity;
    private static final int citysChooseRequestCode = 10001;
    private String provinceName;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mainPresenter = new MainPresenterImpl(this, this);
        mainPresenter.initDatas();
        mainPresenter.initAppUpdate();
        mainPresenter.initFeedBack();
        mainPresenter.getLocationInfo();
        mainPresenter.getCitys();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initToolBar();
        initNavigationView();
        initIntent();
        initOtherDatas(savedInstanceState);
        setDefaultFragment();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType){
            initBaseToolBar(mToolbar, Constants.FlagWelFare, R.drawable.gank_icon_menu_white);
        }else{
            initBaseToolBar(mToolbar, Constants.FlagWelFare, R.drawable.gank_icon_menu_night);
        }
    }

    private void initNavigationView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        mLayoutWeather = headerLayout.findViewById(R.id.layout_weather);
        mHeader_tv_temperature = headerLayout.findViewById(R.id.header_tv_temperature);
        mHeader_tv_other = headerLayout.findViewById(R.id.header_tv_other);
        mHeader_iv_weather =headerLayout.findViewById(R.id.imageView);
        mHeader_ll_choose_city = headerLayout.findViewById(R.id.header_layout_choose_city);
        mHeader_tv_city_name = headerLayout.findViewById(R.id.header_tv_city_name);

        mLayoutWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 切换天气
               if (weatherEntity != null){
                   Intent intent_weather = new Intent(MainActivity.this, WeatherActivity.class);
                   intent_weather.putExtra(WeatherActivity.intentKey_weatherBean, weatherEntity);
                   intent_weather.putExtra(WeatherActivity.intentKey_weatherProvinceName, provinceName);
                   intent_weather.putExtra(WeatherActivity.intentKey_weatherCityName, cityName);
                   if (welFareList != null && welFareList.size() > 0){
                       intent_weather.putStringArrayListExtra(WeatherActivity.intentKey_bg_url, (ArrayList) welFareList);
                   }
                   startActivity(intent_weather);
               }
            }
        });
        mHeader_ll_choose_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换城市
                Intent intent = new Intent(MainActivity.this, CitysActivity.class);
                startActivityForResult(intent, citysChooseRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == citysChooseRequestCode){
            if (data != null){
                provinceName = data.getStringExtra("provinceName");
                cityName = data.getStringExtra("cityName");
                mainPresenter.getCityWeather(provinceName, cityName);
            }
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        String pushMessage = intent.getStringExtra(IntentUtils.PushMessage);
        if (!TextUtils.isEmpty(pushMessage)){
            DialogUtils.showMyDialog(this,
                    getString(R.string.gank_dialog_title_notify),
                    pushMessage,
                    getString(R.string.gank_dialog_confirm),
                    "",
                    null);
        }
    }

    private void initOtherDatas(Bundle savedInstanceState) {
        Log.d(TAG, "initOtherDatas");
         if (savedInstanceState != null && savedInstanceState.getInt(savedInstanceStateItemId) != 0){
               navigationCheckedItemId = savedInstanceState.getInt(savedInstanceStateItemId);
               navigationCheckedTitle = savedInstanceState.getString(savedInstanceStateTitle);
         }
    }

    /**
     * 设置默认的Fragment显示：如果savedInstanceState不是空，证明activity被后台销毁重建了，之前有fragment，就不再创建了
     */
    private void setDefaultFragment() {
        setMenuSelection(navigationCheckedItemId);
    }

    private void setMenuSelection(int flag) {
        mToolbar.setTitle(navigationCheckedTitle);

        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        switch (flag){
            case R.id.nav_fuli:
                if (welFareFragment == null){
                   welFareFragment = WelFareFragment.newInstance();
                   fragmentTransaction.add(R.id.frame_content,welFareFragment);
                }else{
                    fragmentTransaction.show(welFareFragment);
                }
                break;
            case R.id.nav_history:
                if (historyFragment == null){
                    historyFragment = HistoryFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content,historyFragment);
                }else{
                    fragmentTransaction.show(historyFragment);
                }
                break;
            case R.id.nav_category:
                if (categoryFragment == null){
                    categoryFragment = CategoryFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content,categoryFragment);
                }else{
                    fragmentTransaction.show(categoryFragment);
                }
                break;
            case R.id.nav_collect:
                if (collectFragment == null){
                    collectFragment = CollectFragment.newInstance();
                    fragmentTransaction.add(R.id.frame_content,collectFragment);
                }else{
                    fragmentTransaction.show(collectFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
           if (welFareFragment != null){
               transaction.hide(welFareFragment);
           }
           if (collectFragment != null){
               transaction.hide(collectFragment);
           }
           if (historyFragment != null){
               transaction.hide(historyFragment);
           }
           if (categoryFragment != null){
               transaction.hide(categoryFragment);
           }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState" + outState.toString());
        outState.putInt(savedInstanceStateItemId, navigationCheckedItemId);
        outState.putString(savedInstanceStateTitle,navigationCheckedTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mainPresenter.destroyLocation();
        mainPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_login:
                //判断是不是登录了
                MobUserInfo userInfo = UserUtils.getUserCache();
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getUid())){
                    //跳转资料页面
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mDrawerLayout.closeDrawers();
        if (id == R.id.nav_fuli) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
        } else if (id == R.id.nav_category) {
        } else if (id == R.id.nav_collect) {
             navigationCheckedItemId = item.getItemId();
             navigationCheckedTitle = item.getTitle().toString();
        } else if (id == R.id.nav_codes) {

        }else if (id == R.id.nav_cocoa_china){

        }else if (id == R.id.nav_more){

        }else if (id == R.id.setting){

        }else if (id == R.id.nav_share){

        }else if (id == R.id.nav_send){

        }else if (id == R.id.my_support_pay){

        }else if (id == R.id.about){

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setPicList(List<GankEntity> welFareList) {
         this.welFareList = welFareList;
    }

    @Override
    public void showToast(String msg) {
         MySnackbar.makeSnackBarBlack(mNavigationView, msg);
    }

    @Override
    public void showAppUpdateDialog(AppUpdateInfo appUpdateInfo) {

    }

    @Override
    public void initWeatherInfo(WeatherBaseEntity.WeatherBean weatherEntity) {
         this.weatherEntity = weatherEntity;  //初始化天气信息
         String temperature = weatherEntity.getTemperature(); //当前温度
         String airCondition = weatherEntity.getAirCondition(); //空气
         String weather = weatherEntity.getWeather(); //天气
         String cityName = weatherEntity.getCity(); //城市

        mHeader_tv_temperature.setText(temperature);
        mHeader_tv_other.setText(weather + "空气" + airCondition);
        mHeader_iv_weather.setImageDrawable(getResources().getDrawable(SharePreUtil.getIntData(mContext, weather, R.mipmap.icon_weather_none)));
        mHeader_tv_city_name.setText(cityName);
    }

    @Override
    public void updateLocationInfo(String provinceName, String cityName) {
        this.provinceName = provinceName;
        this.cityName = cityName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions,grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
             Log.d(TAG, "onSucceed" + grantPermissions.toString());
             MySnackbar.makeSnackBarBlack(mToolbar, "权限申请成功");
             if (grantPermissions.contains("android.permission.ACCESS_FINE_LOCATION")){
                 Log.d(TAG, "定位权限申请成功");
                 mainPresenter.getLocationInfo();
             }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            Log.d(TAG, "onFailed" + deniedPermissions.toString());
            // 权限申请失败回调。
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)){
                // 权限申请失败回调。
                // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                AndPermission.defaultSettingDialog(MainActivity.this, 300)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };
}

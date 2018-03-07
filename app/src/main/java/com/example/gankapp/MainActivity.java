package com.example.gankapp;

import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gankapp.base.BaseActivity;
import com.example.gankapp.util.Constants;
import com.example.gankapp.util.SkinManager;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
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
        mLayoutWeather = (RelativeLayout) headerLayout.findViewById(R.id.layout_weather);
        mHeader_tv_temperature = headerLayout.findViewById(R.id.header_tv_temperature);
        mHeader_tv_other = headerLayout.findViewById(R.id.header_tv_other);
        mHeader_iv_weather = headerLayout.findViewById(R.id.imageView);
        mHeader_ll_choose_city = (LinearLayout) headerLayout.findViewById(R.id.header_layout_choose_city);
        mHeader_tv_city_name = headerLayout.findViewById(R.id.header_tv_city_name);

        mLayoutWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mHeader_ll_choose_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initOtherDatas(Bundle savedInstanceState) {

    }

    private void setDefaultFragment() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

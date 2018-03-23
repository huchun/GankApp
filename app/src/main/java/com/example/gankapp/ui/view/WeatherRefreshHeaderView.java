package com.example.gankapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.example.gankapp.R;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class WeatherRefreshHeaderView extends SwipeRefreshHeaderLayout {


    private int mHeaderHeight;

    private Animation rotateAnimation;

    private boolean rotated = false;

    private RelativeLayout iv_sun = null;
    private TextView  tv_Refresh = null;

    public WeatherRefreshHeaderView(Context context) {
        this(context, null);
    }

    public WeatherRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_weather);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_sun);
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_Refresh = findViewById(R.id.tvRefresh);
        iv_sun = findViewById(R.id.iv_sun);
    }

    @Override
    public void onRefresh() {
         tv_Refresh.setText("正在刷新...");
    }

    @Override
    public void onPrepare() {
        Log.d("WeatherRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete){
            if ( y > mHeaderHeight){
                tv_Refresh.setText("释放刷新");
                if (!rotated){
                    iv_sun.clearAnimation();
                    iv_sun.startAnimation(rotateAnimation);
                    rotated = true;
                }
            }else if(y < mHeaderHeight){
                if (rotated){
                    rotated = false;
                }
                tv_Refresh.setText("下拉刷新");
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("WeatherRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        tv_Refresh.setText("刷新完成");
        iv_sun.clearAnimation();
    }

    @Override
    public void onReset() {
        rotated = false;
    }

}

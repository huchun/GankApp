package com.example.gankapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.example.gankapp.R;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class TwitterRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private ImageView  ivArrow;
    private ImageView  ivSuccess;
    private TextView   tvRefresh;

    private ProgressWheel progressBar;
    private Animation rotateUp;
    private Animation rotateDown;

    private int mHeaderHeight;
    private boolean rotated = false;

    public TwitterRefreshHeaderView(Context context) {
        super(context, null);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rorate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rorate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvRefresh = findViewById(R.id.tvRefresh);
        ivArrow = findViewById(R.id.ivArrow);
        ivSuccess = findViewById(R.id.ivSuccess);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("正在刷新...");
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
        Log.d("TwitterRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete){
            ivArrow.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            ivSuccess.setVisibility(GONE);
            if (y > mHeaderHeight){
                  tvRefresh.setText("释放刷新");
                  if (!rotated){
                      ivArrow.clearAnimation();
                      ivArrow.startAnimation(rotateUp);
                      rotated = true;
                  }
            }else if (y < mHeaderHeight){
                  if (rotated){
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                 }
                 tvRefresh.setText("下拉刷新");
            }
        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        super.onComplete();
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText("刷新完成");
    }

    @Override
    public void onReset() {
        super.onReset();
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }
}

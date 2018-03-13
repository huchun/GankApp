package com.example.gankapp.ui.imagebrowser;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Created by chunchun.hu on 2018/3/13.
 */

public class MNGestureView extends RelativeLayout {

    private static final String TAG = "MNGestureView";

    private static final int mHeight = 500;
    private float mDisplacementX;
    private float mDisplacementY;
    private float mInitialTx;
    private float mInitialTy;
    private boolean mTracking;

    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public MNGestureView(Context context) {
        super(context,null);
    }

    public MNGestureView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MNGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mDisplacementX = ev.getX();
                mDisplacementY = ev.getY();

                mInitialTx = getTranslationX();
                mInitialTy = getTranslationY();
                break;
            case MotionEvent.ACTION_MOVE:
                // get the delta distance in X and Y direction
                float deltaX = ev.getRawX() - mDisplacementX;
                float deltaY = ev.getRawY() - mDisplacementY;

                // set the touch and cancel event
                if (deltaY > 0 && (Math.abs(deltaY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2 && Math.abs(deltaX) < Math.abs(deltaY) / 2)
                        || mTracking){
                    onSwipeListener.onSwiping(deltaY);

                    setBackgroundColor(Color.TRANSPARENT);

                    mTracking = true;

                    setTranslationX(mInitialTx + deltaX);
                    setTranslationY(mInitialTy + deltaY);

                    float mScale = 1 -deltaY / mHeight;
                    if (mScale < 0.3){
                        mScale = 0.3f;
                    }
                    setScaleX(mScale);
                    setScaleY(mScale);
                }
                if (deltaY < 0){
                    setViewDefault();
                }
                break;
            case MotionEvent.ACTION_UP:
                 if (mTracking){
                     mTracking = false;
                     float currentTranslateY = getTranslationY();

                     if (currentTranslateY > mHeight / 3){
                         onSwipeListener.downSwipe();
                         break;
                     }
                 }
                 setViewDefault();

                 onSwipeListener.overSwipe();
                break;
        }
        return false;
    }

    private void setViewDefault() {
        //恢复默认
        setAlpha(1);
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1);
        setScaleY(1);
        setBackgroundColor(Color.BLACK);
    }

    public interface OnSwipeListener{

        void downSwipe(); //向下滑动

        void overSwipe(); //结束

        void onSwiping(float y); //正在滑动
    }
}

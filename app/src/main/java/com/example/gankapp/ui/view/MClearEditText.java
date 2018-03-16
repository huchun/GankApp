package com.example.gankapp.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.gankapp.R;
import com.example.gankapp.util.DensityUtil;

/**
 * Created by chunchun.hu on 2018/3/16.
 * 有清除功能的EditText：只有当有焦点和文本不为空的时候才显示
 */

@SuppressLint("AppCompatCustomView")
public class MClearEditText extends EditText implements TextWatcher, View.OnFocusChangeListener {

    private Context mContext;

    private Drawable mClearDrawable;
    private Drawable mLeftDrawable;

    private Paint mPaint;
    private int color;

    private static final int DRAWABLE_LEFT = 0; //左边图标
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;

    private boolean hasFoucs;  // 控件是否有焦点

    public MClearEditText(Context context) {
        super(context);
        init(context);
    }

    public MClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        drawLine();//画线
        setDrawable(context); // 设置图标
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
        updateDrawable(true); //默认状态
    }

    private void drawLine() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3.0f);
        color = Color.parseColor("#00a9b7b7");
    }

    private void setDrawable(Context context) {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片:左上右下（0123）
        mClearDrawable = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (mClearDrawable == null){
            mClearDrawable = getResources().getDrawable(R.drawable.gank_icon_clean_edit); //获取默认图标
        }
        mClearDrawable.setBounds(0, 0, DensityUtil.dip2Px(context, 25),  DensityUtil.dip2Px(context, 25));
        mLeftDrawable = getCompoundDrawables()[DRAWABLE_LEFT];
        if (mLeftDrawable != null){
            mLeftDrawable.setBounds(0, 0, DensityUtil.dip2Px(context, 25), DensityUtil.dip2Px(context, 25));
        }
    }

    // 更新删除图片状态: 当内容不为空，而且获得焦点，才显示右侧删除按钮
    private void updateDrawable(boolean hasFocus) {
         if (length() > 0 && hasFocus){
             setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], mClearDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
         }else {
             setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
         }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(color);
        canvas.drawLine(DensityUtil.dip2Px(mContext, 2),
                this.getHeight() - 1,
                this.getWidth() - DensityUtil.dip2Px(mContext, 2),
                this.getHeight() -1 ,
                 mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //可以获得上下左右四个drawable，右侧排第二。图标没有设置则为空。
        Drawable rightIcon = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (rightIcon != null && event.getAction() == MotionEvent.ACTION_UP){
            //检查点击的位置是否是右侧的删除图标
            //注意，使用getRwwX()是获取相对屏幕的位置，getX()可能获取相对父组件的位置
            int leftEdgeOfRightDrawable = getRight() - getPaddingRight() - rightIcon.getBounds().width();
            if (event.getRawX() >= leftEdgeOfRightDrawable){
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateDrawable(true); //更新状态
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        updateDrawable(hasFoucs);
    }
}

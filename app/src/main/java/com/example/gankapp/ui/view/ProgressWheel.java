package com.example.gankapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.gankapp.R;
import com.maning.mndialoglibrary.MProgressWheel;

/**
 * Created by chunchun.hu on 2018/3/8.
 * A Material style progress wheel, compatible up to 2.2.
 * Todd Davies' Progress Wheel https://github.com/Todd-Davies/ProgressWheel
 */

public class ProgressWheel extends View {

    private final int barLength = 16;
    private final int barMaxLength = 270;
    private final long pauseGrowingTime = 200;

    //Sizes (with defaults in DP)
    private int circleRadius = 28;
    private int barWidth = 4;
    private int rimWidth = 4;
    private boolean fillRadius = false;
    private double timeStartGrowing = 0;
    private double barSpinCycleTime = 40;
    private float  barExtraLength = 0;
    private boolean barGrowingFromFront = true;
    private long  pausedTimeWithoutGrowing = 0;
    private int barColor = 0xAA000000;
    private int rimColor = 0x00FFFFFF;

    //Paints
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();

    //Rectangles
    private RectF circleBounds = new RectF();

    //Animation
    //The amount of degrees per second
    private float spinSpeed = 230.0f;
    //private float spinSpeed = 120.0f;
    // The last time the spinner was animated
    private long lastTimeAnimated = 0;

    private boolean linearProgress;

    private float mProgress = 0.0f;
    private float mTargetProgress = 0.0f;
    private boolean isSpinning;

    private MProgressWheel.ProgressCallback callback;
    private boolean shouldAnimate;

    /**
     * The constructor for the ProgressWheel
     */
    public ProgressWheel(Context context) {
        super(context);
        setAnimationEnabled();
    }

    /**
     * The constructor for the ProgressWheel
     */
    public ProgressWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
        setAnimationEnabled();
    }

    private void setAnimationEnabled() {
        int currentApiVersion = Build.VERSION.SDK_INT;

        float animationValue;
        if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            animationValue = Settings.Global.getFloat(getContext().getContentResolver(),Settings.Global.ANIMATOR_DURATION_SCALE,1);
        }else {
            animationValue = Settings.System.getFloat(getContext().getContentResolver(), Settings.System.ANIMATOR_DURATION_SCALE,1);
        }
        shouldAnimate = animationValue != 0;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param typedArray the attributes to parse
     */
    private void parseAttributes(TypedArray typedArray) {
        // We transform the default values from DIP to pixels
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, metrics);
        rimWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth, metrics);
        circleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, circleRadius, metrics);

        circleRadius = (int) typedArray.getDimension(R.styleable.ProgressWheel_matProg_circleRadius,circleRadius);
        fillRadius = typedArray.getBoolean(R.styleable.ProgressWheel_matProg_fillRadius, false);

        barWidth = (int) typedArray.getDimension(R.styleable.ProgressWheel_matProg_barWidth,barWidth);
        rimWidth = (int) typedArray.getDimension(R.styleable.ProgressWheel_matProg_rimWidth,rimWidth);

        float baseSpinSpeed = typedArray.getFloat(R.styleable.ProgressWheel_matProg_spinSpeed, spinSpeed/360.0f);
        spinSpeed = baseSpinSpeed * 360;

        barSpinCycleTime = typedArray.getInt(R.styleable.ProgressWheel_matProg_barSpinCycleTime, (int) barSpinCycleTime);
        barColor = typedArray.getColor(R.styleable.ProgressWheel_matProg_barColor,barColor);
        rimColor = typedArray.getColor(R.styleable.ProgressWheel_matProg_rimColor,rimColor);

        linearProgress = typedArray.getBoolean(R.styleable.ProgressWheel_matProg_linearProgress, false);
        if (typedArray.getBoolean(R.styleable.ProgressWheel_matProg_progressIndeterminate, false)){
            spin();
        }
        // Recycle
        typedArray.recycle();
    }

    /**
     * Puts the view on spin mode
     */
    private void spin() {
        lastTimeAnimated = SystemClock.uptimeMillis();
        isSpinning = true;
        invalidate();
    }

    // Great way to save a view's state http://stackoverflow.com/a/7089687/1991053
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        WheelSavedState savedState = new WheelSavedState(superState);

        // We save everything that can be changed at runtime
        savedState.mProgress = this.mProgress;
        savedState.mTargetProgress = this.mTargetProgress;
        savedState.isSpinning = this.isSpinning;
        savedState.spinSpeed = this.spinSpeed;
        savedState.barWidth = this.barWidth;
        savedState.barColor = this.barColor;
        savedState.rimWidth = this.rimWidth;
        savedState.rimColor = this.rimColor;
        savedState.circleRadius = this.circleRadius;
        savedState.linearProgress = this.linearProgress;
        savedState.fillRadius = this.fillRadius;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof WheelSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        WheelSavedState savedState = new WheelSavedState(state);
        super.onRestoreInstanceState(savedState.getSuperState());

        this.mProgress = savedState.mProgress;
        this.mTargetProgress = savedState.mTargetProgress;
        this.isSpinning = savedState.isSpinning;
        this.spinSpeed = savedState.spinSpeed;
        this.barWidth = savedState.barWidth;
        this.barColor = savedState.barColor;
        this.rimWidth = savedState.rimWidth;
        this.rimColor = savedState.rimColor;
        this.circleRadius = savedState.circleRadius;
        this.linearProgress = savedState.linearProgress;
        this.fillRadius = savedState.fillRadius;

        this.lastTimeAnimated = SystemClock.uptimeMillis();
    }

    /**
     * @return the current progress between 0.0 and 1.0,
     * if the wheel is indeterminate, then the result is -1
     */
    public float getProgress() {
        return isSpinning ? -1 : mProgress / 360.0f;
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the progress to a specific value,
     * the bar will smoothly animate until that value
     *
     * @param progress the progress between 0 and 1
     */
    public void setProgress(float progress) {
        if (isSpinning){
            mProgress = 0.0f;
            isSpinning = false;
            runCallback();
        }
        if (progress > 1.0f){
            progress -= 1.0f;
        }else if (progress < 0){
            progress = 0;
        }
        if (progress == mTargetProgress){
            return;
        }

        // If we are currently in the right position
        // we set again the last time animated so the
        // animation starts smooth from here
        if (mProgress == mTargetProgress){
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
        mTargetProgress = Math.min(progress * 360.0f, 360.0f);

        invalidate();
    }

    /**
     * Sets the determinate progress mode
     *
     * @param linearProgress if the progress should increase linearly
     */
    public void setLinearProgress(boolean linearProgress) {
        this.linearProgress = linearProgress;
        if (!isSpinning){
            invalidate();
        }
    }

    /**
     * @return the radius of the wheel in pixels
     */
    public int getCircleRadius() {
        return circleRadius;
    }

    /**
     * Sets the radius of the wheel
     *
     * @param circleRadius the expected radius, in pixels
     */
    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        if (!isSpinning){
            invalidate();
        }
    }

    /**
     * @return the width of the spinning bar
     */
    public int getBarWidth() {
        return barWidth;
    }

    /**
     * Sets the width of the spinning bar
     *
     * @param barWidth the spinning bar width in pixels
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (!isSpinning){
            invalidate();
        }
    }

    /**
     * @return the color of the spinning bar
     */
    public int getBarColor() {
        return barColor;
    }

    /**
     * Sets the color of the spinning bar
     *
     * @param barColor The spinning bar color
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
        setupPaints();
        if (!isSpinning){
            invalidate();
        }
    }

    /**
     * @return the color of the wheel's contour
     */
    public int getRimColor() {
        return rimColor;
    }

    /**
     * Sets the color of the wheel's contour
     *
     * @param rimColor the color for the wheel
     */
    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        setupPaints();
        if (!isSpinning){
            invalidate();
        }
    }

    /**
     * @return the base spinning speed, in full circle turns per second
     * (1.0 equals on full turn in one second), this value also is applied for
     * the smoothness when setting a progress
     */
    public float getSpinSpeed() {
        return spinSpeed / 360.0f;
    }

    /**
     * Sets the base spinning speed, in full circle turns per second
     * (1.0 equals on full turn in one second), this value also is applied for
     * the smoothness when setting a progress
     *
     * @param spinSpeed the desired base speed in full turns per second
     */
    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed * 360.0f;
    }

    /**
     * @return the width of the wheel's contour in pixels
     */
    public int getRimWidth() {
        return rimWidth;
    }

    /**
     * Sets the width of the wheel's contour
     *
     * @param rimWidth the width in pixels
     */
    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        if (!isSpinning){
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = circleRadius + this.getPaddingLeft() + this.getPaddingRight();
        int viewHeight = circleRadius + this.getPaddingTop() + this.getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY){
            //Must be this size
            width = widthSize;
        }else if (widthMode == MeasureSpec.AT_MOST){
            //Can't be bigger than..
            width = Math.min(viewWidth,widthSize);
        }else{
            //Be whatever you want
            width = viewWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY){
            //Must be this size
            height = heightSize;
        }else if (heightMode == MeasureSpec.AT_MOST){
            //Can't be bigger than...
            height = Math.min(viewHeight,heightSize);
        }else{
            //Be whatever you want
            height = viewHeight;
        }
        setMeasuredDimension(width,height);
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setupBounds(w,h);
        setupPaints();
        invalidate();
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (!fillRadius){
            // Width should equal to Height, find the min value to setup the circle
            int minValue = Math.min(layout_width - paddingLeft - paddingRight, layout_height - paddingBottom - paddingTop);

            int circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth * 2);

            // Calc the Offset if needed for centering the wheel in the available space
            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter)/2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter)/2 + paddingTop;

            circleBounds = new RectF(xOffset + barWidth, yOffset + barWidth, xOffset + circleDiameter - barWidth, yOffset + circleDiameter - barWidth);
        }else{
            circleBounds = new RectF(paddingLeft + barWidth, paddingTop + barWidth, layout_width - paddingRight - barWidth, layout_height - paddingBottom - barWidth);
        }
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
         barPaint.setColor(barColor);
         barPaint.setAntiAlias(true);
         barPaint.setStyle(Paint.Style.STROKE);
         barPaint.setStrokeWidth(barWidth);

         rimPaint.setColor(rimColor);
         rimPaint.setAntiAlias(true);
         rimPaint.setStyle(Paint.Style.STROKE);
         rimPaint.setStrokeWidth(rimWidth);
    }

    public void setCallback(MProgressWheel.ProgressCallback callback) {
        this.callback = callback;

        if (!isSpinning){
            runCallback();
        }
    }

    private void runCallback(float value) {
        if (callback != null){
            callback.onProgressUpdate(value);
        }
    }

    private void runCallback() {
        if (callback != null){
            float normalizedProgress = Math.round(mProgress * 100 / 360.0f) / 100;
            callback.onProgressUpdate(normalizedProgress);
        }
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

        boolean mustInvalidate = false;

        if (!shouldAnimate){
            return;
        }

        if (isSpinning){
            mustInvalidate = true; //Draw the spinning bar

            long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
            float deltaNormalized = deltaTime * spinSpeed / 1000.0f;

            updateBarLength(deltaTime);

            mProgress += deltaNormalized;
            if (mProgress > 360){
                mProgress -= 360f;

                // A full turn has been completed
                // we run the callback with -1 in case we want to
                // do something, like changing the color
                runCallback(-1.0f);
            }
            lastTimeAnimated = SystemClock.uptimeMillis();

            float from = mProgress - 90;
            float length = barLength + barExtraLength;

            if (isInEditMode()){
                from = 0;
                length = 135;
            }
            canvas.drawArc(circleBounds, from, length, false, barPaint);
        }else{
            float oldProgress = mProgress;

            if (mProgress != mTargetProgress){
                //We smoothly increase the progress bar
                mustInvalidate = true;

                float deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated)/1000;
                float deltaNormalized = deltaTime * spinSpeed;

                mProgress = Math.min(mProgress + deltaNormalized, mTargetProgress);
                lastTimeAnimated = SystemClock.uptimeMillis();
            }
            if (oldProgress != mProgress){
                runCallback();
            }

            float offset = 0.0f;
            float progress = mProgress;
            if (!linearProgress){
                float factor = 2.0f;
                offset = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f, 2.0f * factor)) * 360.0f;
                progress = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f, factor)) * 360.0f;
            }
            if (isInEditMode()){
                progress = 360;
            }
            canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint);
        }
        if (mustInvalidate){
            invalidate();
        }
    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
         if (pausedTimeWithoutGrowing >= pauseGrowingTime){
             timeStartGrowing += deltaTimeInMilliSeconds;

             if (timeStartGrowing > barSpinCycleTime){
                 // We completed a size change cycle
                 // (growing or shrinking)
                 timeStartGrowing -= barSpinCycleTime;
                 //if(barGrowingFromFront) {
                 pausedTimeWithoutGrowing = 0;

                 barGrowingFromFront = !barGrowingFromFront;
             }

             float distance = (float) (Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI) / 2 + 0.5f);
             float destLength = (barMaxLength - barLength);

             if (barGrowingFromFront){
                 barExtraLength = distance * destLength;
             }else{
                 float newLength = destLength * (1 - distance);
                 mProgress += (barExtraLength - newLength);
                 barExtraLength = newLength;
             }
         }else{
             pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
         }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE){
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    public interface ProgressCallback{
        /**
         * Method to call when the progress reaches a value
         * in order to avoid float precision issues, the progress
         * is rounded to a float with two decimals.
         *
         * In indeterminate mode, the callback is called each time
         * the wheel completes an animation cycle, with, the progress value is -1.0f
         *
         * @param progress a double value between 0.00 and 1.00 both included
         */
        public void onProgressUpdate(float progress);
    }

    public static class WheelSavedState extends BaseSavedState{
        //required field that makes Parcelables from a Parcel
        public static final Creator<WheelSavedState> CREATOR = new Creator<WheelSavedState>() {
            @Override
            public WheelSavedState createFromParcel(Parcel source) {
                return new WheelSavedState(source);
            }

            @Override
            public WheelSavedState[] newArray(int size) {
                return new WheelSavedState[size];
            }
        };

        float mProgress;
        float mTargetProgress;
        boolean isSpinning;
        float spinSpeed;
        int  barWidth;
        int  barColor;
        int  rimWidth;
        int  rimColor;
        int  circleRadius;
        boolean linearProgress;
        boolean fillRadius;

        public WheelSavedState(Parcelable superState){
            super(superState);
        }

        public WheelSavedState(Parcel source) {
            super(source);
            this.mProgress = source.readFloat();
            this.mTargetProgress = source.readFloat();
            this.isSpinning = source.readByte() != 0;
            this.spinSpeed = source.readFloat();
            this.barWidth = source.readInt();
            this.barColor = source.readInt();
            this.rimWidth = source.readInt();
            this.rimColor = source.readInt();
            this.circleRadius = source.readInt();
            this.linearProgress = source.readByte() != 0;
            this.fillRadius = source.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.mProgress);
            out.writeFloat(mTargetProgress);
            out.writeByte((byte) (isSpinning ? 1 : 0));
            out.writeFloat(spinSpeed);
            out.writeFloat(barWidth);
            out.writeFloat(barColor);
            out.writeFloat(rimWidth);
            out.writeFloat(rimColor);
            out.writeFloat(circleRadius);
            out.writeByte((byte) (linearProgress ? 1 : 0));
            out.writeByte((byte) (fillRadius ? 1 : 0));
        }
    }
}

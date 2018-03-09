package com.example.gankapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.example.gankapp.R;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class ClassicLoadMoreFooterView extends SwipeLoadMoreFooterLayout {


    private TextView tvLoadMore;
    private ImageView ivSuccess;
    private ProgressWheel progressBar;
    private int mFooterHeight;

    public ClassicLoadMoreFooterView(Context context) {
        super(context, null);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_twitter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = findViewById(R.id.tvLoadMore);
        ivSuccess = findViewById(R.id.ivSuccess);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
        ivSuccess.setVisibility(GONE);
    }

    @Override
    public void onRelease() {
        super.onRelease();
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        super.onMove(y, isComplete, automatic);
        if (!isComplete){
            ivSuccess.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            if (-y >= mFooterHeight){
                tvLoadMore.setText("释放加载更多");
            }else{
                tvLoadMore.setText("上拉加载更多");
            }
        }
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        tvLoadMore.setText("正在加载...");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        super.onReset();
        ivSuccess.setVisibility(GONE);
    }
}

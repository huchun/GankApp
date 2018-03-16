package com.example.gankapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.gankapp.R;
import com.example.gankapp.ui.adapter.RecyclerSearchAdapter;
import com.example.gankapp.ui.base.BaseActivity;
import com.example.gankapp.ui.bean.SearchBean;
import com.example.gankapp.ui.iview.ISearchView;
import com.example.gankapp.ui.presenter.impl.SearchPresenterImpl;
import com.example.gankapp.ui.view.MClearEditText;
import com.example.gankapp.ui.view.ProgressWheel;
import com.example.gankapp.util.IntentUtils;
import com.example.gankapp.util.KeyboardUtils;
import com.example.gankapp.util.MySnackbar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/16.
 */

public class SearchActivity extends BaseActivity implements ISearchView, OnLoadMoreListener {

    private static final String TAG = "SearchActivity";

    private Button  mBtnback, mBtnSearch;
    private MClearEditText mClearEditText = null;
    private ProgressWheel  mProgressWheel = null;
    private SwipeToLoadLayout mSwipeToLoadLayout = null;
    private RecyclerView      mRecyclerView = null;

    private List<SearchBean>   searchBeanList;
    private RecyclerSearchAdapter  mSearchAdapter;
    private SearchPresenterImpl searchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBtnback = findViewById(R.id.btn_back);
        mBtnSearch = findViewById(R.id.btn_search);
        mClearEditText = findViewById(R.id.editTextSearch);
        mProgressWheel = findViewById(R.id.progressWheel);
        mSwipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = findViewById(R.id.swipe_target);

        searchPresenter = new SearchPresenterImpl(this ,this);

        initView();
        initSearchAdapter();
    }

    private void initView() {
        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startSearch();
            }
        });

        mClearEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH){
                   startSearch();
               }
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.LTGRAY).build());
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //关闭键盘
                KeyboardUtils.hideSoftInput(SearchActivity.this, mClearEditText);
            }
        });
    }

    private void startSearch() {
         String result = mClearEditText.getText().toString();
         searchPresenter.searchDatas(result);
        //关闭键盘
        KeyboardUtils.hideSoftInput(SearchActivity.this, mClearEditText);
    }

    private void initSearchAdapter() {
         if (mSearchAdapter == null){
             mSearchAdapter = new RecyclerSearchAdapter(this, searchBeanList);
             mRecyclerView.setAdapter(mSearchAdapter);
             //点击事件
             mSearchAdapter.setOnItemClickListener(new RecyclerSearchAdapter.OnItemClickListener() {
                 @Override
                 public void onItemClick(View view, int position) {
                     //跳转页面
                     SearchBean searchBean = searchBeanList.get(position);
                     IntentUtils.startToWebActivity(SearchActivity.this, searchBean.getType(), searchBean.getDesc(), searchBean.getUrl());
                 }
             });
         }else{
            mSearchAdapter.setNewDatas(searchBeanList);
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
        searchPresenter.detachView();
    }

    @Override
    public void showBaseProgressDialog(String msg) {
        Log.d(TAG, "showBaseProgressDialog" + msg.toString());
        mProgressWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBaseProgressDialog() {
        Log.d(TAG, "hideBaseProgressDialog");
        mProgressWheel.setVisibility(View.GONE);
    }

    @Override
    public void setSearchList(List<SearchBean> resultList) {
        Log.d(TAG, "setSearchList");
        this.searchBeanList = resultList;
        initSearchAdapter();
    }

    @Override
    public void showToast(String msg) {
        Log.d(TAG, "showToast" + msg.toString());
        MySnackbar.makeSnackBarBlack(mBtnback, msg);
    }

    @Override
    public void overRefresh() {
        Log.d(TAG, "overRefresh");
        if (mSwipeToLoadLayout == null){
            return;
        }
        if (mSwipeToLoadLayout.isRefreshing()){
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()){
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }

    @Override
    public void setLoadMoreEnabled(boolean flag) {
        Log.d(TAG, "setLoadMoreEnabled");
        mSwipeToLoadLayout.setLoadMoreEnabled(flag);
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        searchPresenter.loadMoreDatas();
    }
}

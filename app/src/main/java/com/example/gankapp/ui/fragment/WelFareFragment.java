package com.example.gankapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.gankapp.R;
import com.example.gankapp.ui.MainActivity;
import com.example.gankapp.ui.adapter.RecyclePicAdapter;
import com.example.gankapp.ui.base.BaseFragment;
import com.example.gankapp.ui.bean.GankEntity;
import com.example.gankapp.ui.iview.IWelFareView;
import com.example.gankapp.ui.presenter.impl.WelFarePresenterImpl;

import java.util.List;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class WelFareFragment extends BaseFragment implements IWelFareView, OnLoadMoreListener, OnRefreshListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipeTarget;

    private RecyclePicAdapter mAdapter;
    private WelFarePresenterImpl welFarePresenter;

    public static WelFareFragment newInstance() {
        return new WelFareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wel_fare,  container,false);

        initView(view);
        initRefresh();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welFarePresenter = new WelFarePresenterImpl(getActivity(), this);
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        }, 100);
    }

    private void initView(View view) {
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        swipeTarget = view.findViewById(R.id.swipe_target);
    }

    private void initRefresh() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        swipeTarget.setLayoutManager(layoutManager);
        swipeTarget.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setWelFareList(List<GankEntity> welFareList) {
          initRecycleView(welFareList);
    }

    private void initRecycleView(List<GankEntity> welFareList) {
        ((MainActivity)getActivity()).setPicList(welFareList);

        if (mAdapter == null){
            mAdapter = new RecyclePicAdapter(context,welFareList);
            swipeTarget.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new RecyclePicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    welFarePresenter.itemClick(view,position);
                }
            });
            welFarePresenter.getRandomDatas(); //获取头条随机
        }else{
            mAdapter.updateDatas(welFareList);
        }
    }

    @Override
    public void setRandomList(List<GankEntity> randomList) {
         if (mAdapter != null){
             mAdapter.updateHeadLindes(randomList);
         }
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void overRefresh() {
         swipeToLoadLayout.setRefreshing(false);
         swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void setLoadMoreEnabled(boolean flag) {
         swipeToLoadLayout.setLoadMoreEnabled(flag);
    }

    @Override
    public void onLoadMore() {
        welFarePresenter.getMoreDatas();
    }

    @Override
    public void onRefresh() {
        // welFarePresenter.getNewDatas();
    }

    @Override
    public void onDestroyView() {
        if (mAdapter != null){
            mAdapter.destroyList();
        }
        welFarePresenter.detachView();
        super.onDestroyView();
    }
}

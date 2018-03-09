package com.example.gankapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.gankapp.R;
import com.example.gankapp.ui.base.BaseFragment;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class WelFareFragment extends BaseFragment {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView swipeTarget;

    public static WelFareFragment newInstance() {
        return new WelFareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wel_fare,  container,false);

      //  initView(view);
        return view;
    }

    private void initView(View view) {
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        swipeTarget = view.findViewById(R.id.swipe_target);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

package com.example.gankapp.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class BaseFragment extends Fragment {

    public static String TAG = "BaseFragment";

    public String  className;
    public Context context;
    private MStatusDialog   mStatusDialog;
    private MProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        initDialog();
    }

    private void initDialog() {
         mProgressDialog = new MProgressDialog.Builder(context).build();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

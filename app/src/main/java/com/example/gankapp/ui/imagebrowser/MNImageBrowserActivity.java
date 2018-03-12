package com.example.gankapp.ui.imagebrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chunchun.hu on 2018/3/12.
 */

public class MNImageBrowserActivity extends AppCompatActivity {

    public final static String TAG = "MNImageBrowserActivity";

    public final static String IntentKey_ImageList = "IntentKey_ImageList";
    public final static String IntentKey_GankEntityList = "IntentKey_GankEntityList";
    public final static String IntentKey_CurrentPosition = "IntentKey_CurrentPosition";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

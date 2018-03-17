package com.example.gankapp.util;

import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.MobUserInfo;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class UserUtils {


    private static final String cache_UserLogin = "cache_UserLogin";

    //获取用户信息
    public static MobUserInfo getUserCache() {
        MobUserInfo userInfo = (MobUserInfo) MyApplicaiton.getACache().getAsObject(cache_UserLogin);
        if (userInfo == null){
            userInfo = new MobUserInfo();
        }
        return userInfo;
    }
}

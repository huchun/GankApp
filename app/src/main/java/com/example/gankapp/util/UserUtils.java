package com.example.gankapp.util;

import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.CitysEntity;
import com.example.gankapp.ui.bean.MobUserInfo;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class UserUtils {


    private static final String cache_UserLogin = "cache_UserLogin";
    private static final String cache_citys = "Cache_Citys";
    /**
     * get userInfo
     * @return
     */
    public static MobUserInfo getUserCache() {
        MobUserInfo userInfo = (MobUserInfo) MyApplicaiton.getACache().getAsObject(cache_UserLogin);
        if (userInfo == null){
            userInfo = new MobUserInfo();
        }
        return userInfo;
    }

    public static void saveCitysCache(CitysEntity citysEntity) {
          if (citysEntity != null){
              MyApplicaiton.getACache().put(cache_citys,citysEntity);
          }
    }

    /**
     *  user login info
     * @param userInfo
     */
    public static void saveUserCache(MobUserInfo userInfo) {
         if (userInfo != null){
             MyApplicaiton.getACache().put(cache_UserLogin, userInfo);
         }
    }

    /**
     * exit login
      */
    public static void quitLogin() {
        MyApplicaiton.getACache().put(cache_UserLogin, new MobUserInfo());
    }
}

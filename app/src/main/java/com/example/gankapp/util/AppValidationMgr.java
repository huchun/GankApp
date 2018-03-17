package com.example.gankapp.util;

import java.util.regex.Pattern;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class AppValidationMgr {

    //邮箱表达式
    private final static Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");


    /**
     * 验证邮箱是否正确
     * @param email  邮箱地址
     * @return boolean
     */
    public static boolean isEmail(String email) {
        return email_pattern.matcher(email).matches();
    }
}

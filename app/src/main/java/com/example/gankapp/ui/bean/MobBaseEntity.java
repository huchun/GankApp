package com.example.gankapp.ui.bean;

import java.io.Serializable;

/**
 * Created by chunchun.hu on 2018/3/17.
 */

public class MobBaseEntity<T> implements Serializable {

    private static final long serialVersionUID = -4553802208756427393L;

    private T result;
    private String msg;
    private String retCode;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    @Override
    public String toString() {
        return "MobBaseEntity{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", retCode='" + retCode + '\'' +
                '}';
    }
}

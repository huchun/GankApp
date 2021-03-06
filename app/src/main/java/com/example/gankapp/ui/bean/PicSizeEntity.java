package com.example.gankapp.ui.bean;

/**
 * Created by chunchun.hu on 2018/3/12.
 * 首页加载图片缓存宽高，防止出现滑动图片改变高度问题
 */

public class PicSizeEntity {

    private String picUrl;
    private int picWidth;
    private int picHeight;

    public PicSizeEntity(int picWidth, int picHeight) {
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    @Override
    public String toString() {
        return "PicSizeEntity{" +
                "picUrl='" + picUrl + '\'' +
                ", picWidth=" + picWidth +
                ", picHeight=" + picHeight +
                '}';
    }

    public boolean isNull() {
        return picHeight == 0 || picWidth == 0;
    }
}

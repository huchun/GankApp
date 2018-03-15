package com.example.gankapp.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gankapp.ui.MyApplicaiton;
import com.example.gankapp.ui.bean.GankEntity;

/**
 * Created by chunchun.hu on 2018/3/14.
 * 收藏表的操作
 */

public class CollectDao {

     private SQLiteDatabase db;

     private GankAppHelper helper;

     public CollectDao(){
         helper = new GankAppHelper(MyApplicaiton.getIntstance());
     }

    /**
     * 往数据库中插入一条收藏数据
     * @param gankResult
     * @return 是否插入成功
     */
    public boolean insertOneCollect(GankEntity gankResult) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GankAppHelper.GankID, gankResult.get_id());
        values.put(GankAppHelper.createdAt, gankResult.getCreatedAt());
        values.put(GankAppHelper.desc, gankResult.getDesc());
        values.put(GankAppHelper.publishedAt, gankResult.getPublishedAt());
        values.put(GankAppHelper.source, gankResult.getSource());
        values.put(GankAppHelper.type, gankResult.getType());
        values.put(GankAppHelper.url, gankResult.getUrl());
        values.put(GankAppHelper.who, gankResult.getWho());
        if (gankResult.isUsed()){
            values.put(GankAppHelper.used, "true");
        }else{
            values.put(GankAppHelper.used, "false");
        }
        String imageUrl = "";
        if (gankResult.getImages() != null && gankResult.getImages().size() > 0){
            imageUrl = gankResult.getImages().get(0);
        }
        values.put(GankAppHelper.imageUrl, imageUrl);
        long insert = db.insert(GankAppHelper.TABLE_NAME_COLLECT, null, values);
        db.close();
        return insert != (-1);
    }

    /**
     * 查询是否存在
     * @param GankID
     * @return
     */
    public synchronized boolean queryOneCollectByID(String GankID) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(GankAppHelper.TABLE_NAME_COLLECT, null, GankAppHelper.GankID + "=?",new String[]{GankID}, null, null,null);
        boolean result = false;
        if (cursor.moveToNext()){
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 删除一条数据
     * @param ganID
     * @return
     */
    public boolean deleteOneCollect(String ganID) {
        db = helper.getWritableDatabase();
        int i = db.delete(GankAppHelper.TABLE_NAME_COLLECT, GankAppHelper.GankID + "=?", new String[]{ganID});
        db.close();
        return i > 0;
    }
}

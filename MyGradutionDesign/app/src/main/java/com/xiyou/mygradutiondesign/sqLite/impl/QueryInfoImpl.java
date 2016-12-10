package com.xiyou.mygradutiondesign.sqLite.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.TablesName;
import com.xiyou.mygradutiondesign.sqLite.TypeAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.TypeInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyi on 16/3/16.
 */
public class QueryInfoImpl {

    public static List<PictureInfoBean> queryPicInfo(SQLiteDatabase db) {
        List<PictureInfoBean> saves = null;
        Cursor cursor = db.query(TablesName.PIC_TABLE_NAME, null, null, null, null,
                null, null);
        if (cursor != null) {
            saves = new ArrayList<PictureInfoBean>();
            while (cursor.moveToNext()) {
                PictureInfoBean pictureInfoBean = new PictureInfoBean();
                pictureInfoBean.setPicId(cursor.getInt(cursor
                        .getColumnIndex(PictureAttributesBean.PIC_ID)));
                pictureInfoBean.setPicName(cursor.getString(cursor
                        .getColumnIndex(PictureAttributesBean.PIC_NAME)));
                pictureInfoBean.setPicStorePosition(cursor.getString(cursor
                        .getColumnIndex(PictureAttributesBean.PIC_STORE_POSITION)));
                pictureInfoBean.setPicJPEGName(cursor.getString(cursor
                        .getColumnIndex(PictureAttributesBean.PIC_JPEG_NAME)));
                pictureInfoBean.setDescribe(cursor.getString(cursor
                        .getColumnIndex(PictureAttributesBean.DESCRIBE)));
                pictureInfoBean.setBelongType(cursor.getString(cursor
                        .getColumnIndex(PictureAttributesBean.BELONG_TYPE)));
                saves.add(pictureInfoBean);
            }
        }
        closeSursor(cursor);

        return saves;
    }

    public static PictureInfoBean queryPicInfoByID(SQLiteDatabase db, int id) {
        Cursor cursor = db.query(TablesName.PIC_TABLE_NAME, null, PictureAttributesBean.PIC_ID
                + "=?", new String[] { String.valueOf(id) }, null, null, null);

        PictureInfoBean pictureInfoBean = null;
        if (cursor != null && cursor.moveToFirst()) {
            pictureInfoBean = new PictureInfoBean();
            pictureInfoBean.setPicId(cursor.getInt(cursor
                    .getColumnIndex(PictureAttributesBean.PIC_ID)));
            pictureInfoBean.setPicName(cursor.getString(cursor
                    .getColumnIndex(PictureAttributesBean.PIC_NAME)));
            pictureInfoBean.setPicStorePosition(cursor.getString(cursor
                    .getColumnIndex(PictureAttributesBean.PIC_STORE_POSITION)));
            pictureInfoBean.setPicJPEGName(cursor.getString(cursor
                    .getColumnIndex(PictureAttributesBean.PIC_JPEG_NAME)));
            pictureInfoBean.setDescribe(cursor.getString(cursor
                    .getColumnIndex(PictureAttributesBean.DESCRIBE)));
            pictureInfoBean.setBelongType(cursor.getString(cursor
                    .getColumnIndex(PictureAttributesBean.BELONG_TYPE)));
        }

        closeSursor(cursor);

        return pictureInfoBean;
    }

    public static List<TypeInfoBean> queryTypeInfo(SQLiteDatabase db) {
        List<TypeInfoBean> saves = null;
        Cursor cursor = db.query(TablesName.TYPE_TABLE_NAME, null, null, null, null,
                null, null);
        if (cursor != null) {
            saves = new ArrayList<>();
            while (cursor.moveToNext()) {
                TypeInfoBean pictureInfoBean = new TypeInfoBean();
                pictureInfoBean.setTypeId(cursor.getInt(cursor
                        .getColumnIndex(TypeAttributesBean.TYPE_ID)));
                pictureInfoBean.setTypeName(cursor.getString(cursor
                        .getColumnIndex(TypeAttributesBean.TYPE_NAME)));
                saves.add(pictureInfoBean);
            }
        }
        closeSursor(cursor);

        return saves;
    }

    public static TypeInfoBean queryTypeInfoByID(SQLiteDatabase db, int id) {
        Cursor cursor = db.query(TablesName.TYPE_TABLE_NAME, null, TypeAttributesBean.TYPE_ID
                + "=?", new String[] { String.valueOf(id) }, null, null, null);

        TypeInfoBean typeInfoBean = null;
        if (cursor != null && cursor.moveToFirst()) {
            typeInfoBean = new TypeInfoBean();
            typeInfoBean.setTypeId(cursor.getInt(cursor
                    .getColumnIndex(TypeAttributesBean.TYPE_ID)));
            typeInfoBean.setTypeName(cursor.getString(cursor
                    .getColumnIndex(TypeAttributesBean.TYPE_NAME)));
        }

        closeSursor(cursor);

        return typeInfoBean;
    }


    private static void closeSursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

}

package com.xiyou.mygradutiondesign.sqLite.impl;

import android.database.sqlite.SQLiteDatabase;

import com.xiyou.mygradutiondesign.bean.TrafficSignBean;
import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.TablesName;
import com.xiyou.mygradutiondesign.sqLite.TypeAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.TypeInfoBean;

/**
 * Created by fengyi on 16/3/15.
 */
public class AddInfoImpl {

    public static void addPicInfo(SQLiteDatabase db, PictureInfoBean pictureInfoBean) {
        System.out.println("picInfoBean=" + pictureInfoBean);
        String sql = "replace into " + TablesName.PIC_TABLE_NAME + "("
                + PictureAttributesBean.PIC_NAME + "," + PictureAttributesBean.PIC_STORE_POSITION
                + ","
                + PictureAttributesBean.PIC_JPEG_NAME + "," + PictureAttributesBean.DESCRIBE + ","
                + PictureAttributesBean.BELONG_TYPE
                + ") values(?,?,?,?,?)";
        System.out.println("sql:"+sql);
        db.execSQL(
                sql,
                new Object[] {
                        pictureInfoBean.getPicName(),
                        pictureInfoBean.getPicStorePosition(),
                        pictureInfoBean.getPicJPEGName(),
                        pictureInfoBean.getDescribe(),
                        pictureInfoBean.getBelongType()
                });
    }

    public static void addTypeInfo(SQLiteDatabase db, TypeInfoBean typeInfoBean) {
        String sql = "replace into " + TablesName.TYPE_TABLE_NAME + "("
                + TypeAttributesBean.TYPE_NAME + ") values(?)";
        db.execSQL(sql, new Object[] {
                typeInfoBean.getTypeName()
        });
    }

}

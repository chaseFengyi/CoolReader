package com.xiyou.mygradutiondesign.sqLite.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.TablesName;
import com.xiyou.mygradutiondesign.sqLite.TypeAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.TypeInfoBean;

/**
 * Created by fengyi on 16/3/16.
 */
public class UpdateInfoImpl {

    public static int updatePicInfo(SQLiteDatabase db,
            PictureInfoBean pictureInfoBean) {
        ContentValues values = new ContentValues();
        values.put(PictureAttributesBean.PIC_NAME, pictureInfoBean.getPicName());
        values.put(PictureAttributesBean.PIC_STORE_POSITION, pictureInfoBean.getPicStorePosition());
        values.put(PictureAttributesBean.PIC_JPEG_NAME, pictureInfoBean.getPicJPEGName());
        values.put(PictureAttributesBean.DESCRIBE, pictureInfoBean.getDescribe());
        values.put(PictureAttributesBean.BELONG_TYPE, pictureInfoBean.getBelongType());
        return db.update(TablesName.PIC_TABLE_NAME, values, PictureAttributesBean.PIC_ID
                + "=?",
                new String[] {
                    String.valueOf(pictureInfoBean.getPicId())
                });
    }

    public static int updateTypeInfo(SQLiteDatabase db,
            TypeInfoBean typeInfoBean) {
        ContentValues values = new ContentValues();
        values.put(TypeAttributesBean.TYPE_NAME, typeInfoBean.getTypeName());
        return db.update(TablesName.TYPE_TABLE_NAME, values, TypeAttributesBean.TYPE_ID
                + "=?",
                new String[] {
                    String.valueOf(typeInfoBean.getTypeId())
                });
    }

}

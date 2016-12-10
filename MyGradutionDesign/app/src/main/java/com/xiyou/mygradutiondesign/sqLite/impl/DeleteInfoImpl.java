package com.xiyou.mygradutiondesign.sqLite.impl;

import android.database.sqlite.SQLiteDatabase;

import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.TablesName;
import com.xiyou.mygradutiondesign.sqLite.TypeAttributesBean;

/**
 * Created by fengyi on 16/3/16.
 */
public class DeleteInfoImpl {

    public static int deletePicInfo(SQLiteDatabase db, int picId) {
        return db.delete(TablesName.PIC_TABLE_NAME, PictureAttributesBean.PIC_ID + "=?",
                new String[] {
                    String.valueOf(picId)
                });
    }

    public static int deleteTypeInfo(SQLiteDatabase db, int typeId) {
        return db.delete(TablesName.TYPE_TABLE_NAME, TypeAttributesBean.TYPE_ID + "=?",
                new String[] {
                    String.valueOf(typeId)
                });
    }

}

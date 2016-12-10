package com.xiyou.mygradutiondesign.sqLite.dao;

import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.TablesName;
import com.xiyou.mygradutiondesign.sqLite.TypeAttributesBean;

/**
 * Created by fengyi on 16/3/15.
 */
public class CreateWordDao {

    // 创建图片表
    public final static String sql_picturetable = "create table " +
            TablesName.PIC_TABLE_NAME + "(" + PictureAttributesBean.PIC_ID +
            " INTEGER primary key autoincrement," + PictureAttributesBean.PIC_NAME +
            " varchar(32)," + PictureAttributesBean.PIC_STORE_POSITION +
            " varchar(100),picJPEGName varchar(32)," + PictureAttributesBean.DESCRIBE +
            " varchar(100)," + PictureAttributesBean.BELONG_TYPE +
            " varchar(10)";
    // 创建类别表
    public final static String sql_typetable = "create table " +
            TablesName.TYPE_TABLE_NAME + "(" + TypeAttributesBean.TYPE_ID +
            " INTEGER primary key autoincrement," + TypeAttributesBean.TYPE_NAME +
            " varchar(20) not null)";
    // 删除图片表
    public final static String drop_picturetable = "drop table if exists picturetable";
    // 删除类别表
    public final static String drop_typetable = "drop table if exists typetable";
}

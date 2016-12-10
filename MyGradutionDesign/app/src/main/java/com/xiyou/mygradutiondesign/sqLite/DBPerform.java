package com.xiyou.mygradutiondesign.sqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.sqLite.bean.TypeInfoBean;
import com.xiyou.mygradutiondesign.sqLite.dao.CreateWordDao;
import com.xiyou.mygradutiondesign.sqLite.helper.BelongTypeDBHelper;
import com.xiyou.mygradutiondesign.sqLite.helper.PictureDBHelper;
import com.xiyou.mygradutiondesign.sqLite.impl.AddInfoImpl;
import com.xiyou.mygradutiondesign.sqLite.impl.DeleteInfoImpl;
import com.xiyou.mygradutiondesign.sqLite.impl.QueryInfoImpl;
import com.xiyou.mygradutiondesign.sqLite.impl.UpdateInfoImpl;

import java.util.List;

/**
 * Created by fengyi on 16/3/16.
 */
public class DBPerform {

    private static SQLiteDatabase db = null;
    private static PictureDBHelper pictureDBHelper;
    private static BelongTypeDBHelper belongTypeDBHelper;

    // 创建表
    private static void createPicDBHelper(Context context) {
        pictureDBHelper = PictureDBHelper.getInstance(context,
                TablesName.PIC_TABLE_NAME);
    }

    private static void createTypeDBHelper(Context context) {
        /*
         * urldbHelper = new URLDBHelper(context, TablesName.DATABASE_NAME, null,
         * TablesName.VERSION);
         */
        belongTypeDBHelper = BelongTypeDBHelper.getInstance(context,
                TablesName.TYPE_TABLE_NAME);
    }

    public static void createPicTable(Context context) {
        createPicDBHelper(context);
        db = pictureDBHelper.getWritableDatabase();
        db.execSQL(CreateWordDao.sql_picturetable);
    }

    public static void createTypeTable(Context context) {
        createTypeDBHelper(context);
        db = belongTypeDBHelper.getWritableDatabase();
        db.execSQL(CreateWordDao.sql_typetable);
    }

    // 用户表的添加操作
    /**
     * @return
     */
    public static void insertPicInfo(Context context, PictureInfoBean pictureInfoBean) {

        createPicTable(context);
        AddInfoImpl.addPicInfo(db, pictureInfoBean);
        if (db != null) {
            db.close();
        }

    }

    // 用户表的删除
    /**
     * @param context
     * @return <0 删除失败
     */
    public static int deletePicInfoById(Context context, int picId) {

        createPicTable(context);
        int ret = DeleteInfoImpl.deletePicInfo(db, picId);
        if (db != null) {
            db.close();
        }
        return ret;
    }

    // 用户表的更新
    /**
     * @return <0 更新失败
     */
    public static int updatePicInfo(Context context, PictureInfoBean pictureInfoBean) {

        createPicTable(context);
        int ret = UpdateInfoImpl.updatePicInfo(db, pictureInfoBean);
        if (db != null) {
            db.close();
        }
        return ret;
    }

    // 查找表中所有数据
    /**
     * @param context
     * @return
     */
    public static List<PictureInfoBean> queryPicInfo(Context context) {

        createPicTable(context);
        List<PictureInfoBean> ret = QueryInfoImpl.queryPicInfo(db);
        if (db != null) {
            db.close();
        }
        return ret;
    }

    // 查找指定email的数据
    public static PictureInfoBean queryUserInfoById(Context context,
            int id) {

        createPicTable(context);
        PictureInfoBean ret = QueryInfoImpl.queryPicInfoByID(db, id);
        if (db != null) {
            db.close();
        }
        return ret;

    }

    // URL的添加操作
    /**
     */
    public static void insertTypeInfo(Context context, TypeInfoBean typeInfoBean) {

        createTypeTable(context);
        AddInfoImpl.addTypeInfo(db, typeInfoBean);
        if (db != null) {
            db.close();
        }
    }

    // URL的删除操作
    /**
     * @param context
     * @return <0删除失败
     */
    public static int deleteTypeInfoById(Context context, int id) {

        createTypeTable(context);
        int ret = DeleteInfoImpl.deleteTypeInfo(db, id);
        if (db != null) {
            db.close();
        }
        return ret;

    }

    // URL的修改操作
    /**
     * @param context
     * @return <0 修改失败
     */
    public static int updateTypeInfo(Context context, TypeInfoBean typeInfoBean) {

        createTypeTable(context);
        int ret = UpdateInfoImpl.updateTypeInfo(db, typeInfoBean);
        if (db != null) {
            db.close();
        }
        return ret;
    }

    // 查找URL表中所有数据
    /**
     * @param context
     * @return
     */
    public static List<TypeInfoBean> queryTypeInfo(Context context) {

        createTypeTable(context);
        List<TypeInfoBean> ret = QueryInfoImpl.queryTypeInfo(db);
        if (db != null) {
            db.close();
        }
        return ret;
    }

    // 查找指定url的数据(URL)
    public static TypeInfoBean queryTypeInfoById(Context context, int id) {

        createTypeTable(context);
        return QueryInfoImpl.queryTypeInfoByID(db, id);
    }

}

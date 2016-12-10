package com.xiyou.mygradutiondesign.sqLite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiyou.mygradutiondesign.sqLite.dao.CreateWordDao;

/**
 * Created by fengyi on 16/3/15.
 */
public class BelongTypeDBHelper extends SQLiteOpenHelper {

    public BelongTypeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try{
            db.execSQL(CreateWordDao.sql_typetable);
        }finally{
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CreateWordDao.drop_typetable);
        onCreate(db);
    }

    private static BelongTypeDBHelper mInstance = null;

    public synchronized static BelongTypeDBHelper getInstance(Context context, String name){
        if(mInstance == null){
            mInstance = new BelongTypeDBHelper(context, name, null, 1);
        }

        return mInstance;
    }
}
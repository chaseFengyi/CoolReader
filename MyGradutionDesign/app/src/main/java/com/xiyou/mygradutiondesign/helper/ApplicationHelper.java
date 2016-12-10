package com.xiyou.mygradutiondesign.helper;

import android.app.Application;

/**
 * Created by fengyi on 16/3/13.
 */
public class ApplicationHelper {
    private static Application me = null;

    public static void init(Application application){
        me = application;
    }

    public static Application getApplication(){
        if (me == null) {
            throw new NullPointerException("init first");
        }
        return me;
    }
}

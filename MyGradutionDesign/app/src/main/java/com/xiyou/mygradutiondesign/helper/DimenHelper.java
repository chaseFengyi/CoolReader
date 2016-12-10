package com.xiyou.mygradutiondesign.helper;

import android.app.Application;

/**
 * Created by fengyi on 16/3/11.
 */
public class DimenHelper {



    public static int getScreenWidth(){
        return ApplicationHelper.getApplication().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return ApplicationHelper.getApplication().getResources().getDisplayMetrics().heightPixels;
    }

}

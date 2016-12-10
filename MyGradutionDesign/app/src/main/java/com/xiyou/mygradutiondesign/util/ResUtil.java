package com.xiyou.mygradutiondesign.util;

import com.xiyou.mygradutiondesign.helper.ApplicationHelper;

/**
 * Created by fengyi on 16/3/13.
 */
public class ResUtil {

    public static String getString(int id) {
        return ApplicationHelper.getApplication().getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs){
        return ApplicationHelper.getApplication().getResources().getString(id, formatArgs);
    }

}

package com.xiyou.mygradutiondesign.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by fengyi on 16/3/15.
 */
public class ToastUtil {

    public static void toast(Context context, int resId) {
        toast(context, ResUtil.getString(resId));
    }

    public static void toast(Context context, String message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String message, int time) {
        if (context == null || message.isEmpty() || time < 0) {
            return;
        }
        Toast.makeText(context, message, time).show();
    }

}

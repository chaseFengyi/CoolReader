package com.xiyou.mygradutiondesign.helper;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by fengyi on 16/3/13.
 */
public class IntentHelper {

    public static Bundle getBundle(Intent data){
        if (data == null) {
            return null;
        }
        return data.getExtras();
    }

    public static String getString(Bundle bundle, String args) {
        if (bundle == null) {
            return "";
        }
        return bundle.getString(args);
    }

}

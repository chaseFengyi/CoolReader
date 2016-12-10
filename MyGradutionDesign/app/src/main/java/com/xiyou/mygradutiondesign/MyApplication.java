package com.xiyou.mygradutiondesign;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiyou.mygradutiondesign.helper.ApplicationHelper;
import com.xiyou.mygradutiondesign.helper.DimenHelper;

/**
 * Created by fengyi on 16/3/11.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.init(this);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.
                Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(configuration);
        System.load("libopencv_java3.so");
    }
}

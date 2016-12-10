package com.xiyou.mygradutiondesign.helper;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiyou.mygradutiondesign.R;

/**
 * Created by fengyi on 16/3/19.
 */
public class ImageLoaderHelper {

    public static void loadBitmap(String url, ImageView imageView){
        loadBitmap(url, imageView, null);
    }

    public static void loadBitmap(String url, ImageView imageView, ImageLoadingListener listener) {
        url = PictureHelper.addScheme(url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(url, imageView, options, listener);

    }

}

package com.xiyou.mygradutiondesign.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import com.squareup.picasso.UrlConnectionDownloader;
import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.util.BitmapUtils;

import java.io.File;

/**
 * Created by fengyi on 16/3/13.
 */
public class PictureHelper {

    /**
     * 根据id获取bitmap对象
     *
     * @param context
     * @param id
     * @return
     */
    public static Bitmap getBtmapByID(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    public static final int MAX_IMAGE_SIZE = 1024;

    public static void loadBitmap(String path, ImageView imageView) {
        loadBitmap(path, imageView, 0);
    }

    public static void loadBitmap(String path, ImageView imageView, int maxSize) {
        loadBitmap(path, imageView, maxSize, new Callback.EmptyCallback());
    }

    public static void loadBitmap(String path, ImageView imageView, Callback callback) {
        loadBitmap(path, imageView, 0, callback);
    }

    public static void loadBitmap(String path, ImageView imageView, int maxSize, Callback callback) {

        if (TextUtils.isEmpty(path) && imageView == null) {
            return;
        }

        path = addScheme(path);
        Picasso picasso = picasso();
        RequestCreator requestCreator = picasso.load(path).error(R.drawable.ic_launcher);
        if (maxSize > 0) {
            requestCreator.resize(maxSize, maxSize).centerInside();
        }
        requestCreator.into(imageView, callback);

    }

    public static void loadBitmap(File path, ImageView imageView, int maxSize, Callback callback) {

        if (path == null && imageView == null) {
            return;
        }

        Picasso picasso = picasso();
        RequestCreator requestCreator = picasso.load(path);
        if (maxSize > 0) {
            requestCreator.resize(maxSize, maxSize).centerInside();
        }
        requestCreator.into(imageView, callback);

    }

    public static void loadBlurBitmap(final Context context, String path, ImageView imageView,
            final int radius) {
        if (TextUtils.isEmpty(path) || imageView == null) {
            return;
        }
        path = addScheme(path); // Load local image
        int blurSize = (int) (50 * ApplicationHelper.getApplication().getResources()
                .getDisplayMetrics().density);
        Picasso picasso = picasso();
        picasso.load(path)
                .resize(blurSize, blurSize)
                .centerCrop()
                .error(R.drawable.ic_launcher)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Bitmap result = BitmapUtils.fastblur(context, source, radius);
                        if (result != source) {
                            source.recycle();
                        }
                        return result;
                    }

                    @Override
                    public String key() {
                        return "blur_avatar";
                    }
                })
                .into(imageView);
    }

    /**
     * 检查地址是否为指定格式地址 制定格式：file:///android_asset/DvpvklR.png
     *
     * @param path
     * @return
     */
    public static String addScheme(String path) {
        if (TextUtils.isEmpty(path)) {
            return path;
        }
        if (path.startsWith("/")) {
            return String.format("file://%s", path);
        } else {
            return path;
        }
    }

    private static Picasso picassoInstance;

    public static Picasso picasso() {
        if (picassoInstance == null) {
            synchronized (PictureHelper.class) {
                if (picassoInstance == null) {
                    Picasso.Builder builder = new Picasso.Builder(
                            ApplicationHelper.getApplication());
                    Downloader downloader = new UrlConnectionDownloader(
                            ApplicationHelper.getApplication());
                    builder.downloader(downloader);
                    picassoInstance = builder.build();
                }
            }
        }
        return picassoInstance;
    }

    public static File uriToFile(Uri uri) {
        try {
            String filePath = null;
            String[] filePathColumn = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = ApplicationHelper.getApplication().getContentResolver()
                    .query(uri, filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
            if (!TextUtils.isEmpty(filePath)) {
                return new File(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

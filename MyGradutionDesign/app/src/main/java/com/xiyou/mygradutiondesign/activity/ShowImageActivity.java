package com.xiyou.mygradutiondesign.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.IntentHelper;
import com.xiyou.mygradutiondesign.helper.PictureHelper;

/**
 * Created by fengyi on 16/3/16.
 */
public class ShowImageActivity extends BaseNavbarActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        super.setupView(savedInstance);
        String avatarUrl = IntentHelper.getBundle(getIntent()).getString(ArgumentConst.ARG_PIC_PATH);
        ImageView imageView = (ImageView) findViewById(R.id.iv_show_image);
        PictureHelper.loadBitmap(avatarUrl, imageView);
    }
}

package com.xiyou.mygradutiondesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;
import com.xiyou.mygradutiondesign.presenter.MainPresenter;

import java.io.File;

public class MainActivity extends BaseNavbarActivity {

    private ViewAccessor viewAccessor;
    private MainPresenter mainPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        mainPresenter = new MainPresenter(this);
        mainPresenter.storeDestImageInfo();
        super.setupView(savedInstance);
        viewAccessor = ViewAccessor.create(findViewById(R.id.main));
        renderTakePhoto();
        renderGallery();
        renderStoreHouse();
    }

    /**
     * 拍照
     */
    private void renderTakePhoto() {
        viewAccessor.setOnClickListener(R.id.btn_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.takePhoto();
            }
        });
    }

    /**
     * 从图库获取
     */
    private void renderGallery() {
        viewAccessor.setOnClickListener(R.id.btn_gallery, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.takeGallery();
            }
        });
    }

    /**
     * 我的仓库
     */
    private void renderStoreHouse() {
        viewAccessor.setOnClickListener(R.id.btn_storeHouse, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(StoreHouseActivity.class, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File photo = mainPresenter.parseResultOnActivityResult(requestCode, resultCode, data);
        if (photo != null) {
            //跳转到详情界面
            Bundle bundle = new Bundle();
            bundle.putString(ArgumentConst.ARG_PIC_STORE_POSITION, photo.toString());
            launch(TrafficDetailActivity.class, bundle);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.xiyou.mygradutiondesign.activity;

import android.os.Bundle;
import android.view.View;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.helper.DialogHelper;
import com.xiyou.mygradutiondesign.helper.ViewHelper;
import com.xiyou.mygradutiondesign.presenter.BasePresenter;

/**
 * Created by fengyi on 16/3/13.
 */
public abstract class BaseNavbarActivity extends BaseActivity {

    protected void setTitle(String title) {
        setTextString(R.id.navbar_title, title);
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        super.setupView(savedInstance);
        ViewHelper.setOnClickListenerOnViewWithId(findViewById(R.id.navbar),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNavbarItemClicked(v);
                    }
                });
    }

    protected void onNavbarItemClicked(View v) {
        int i = v.getId();
        if (i == R.id.navbar_left) {
            if (!interceptOnBackPressed()){
                super.interceptOnBackPressed();
            }
        } else if (i == R.id.navbar_right) {

        }
    }

}

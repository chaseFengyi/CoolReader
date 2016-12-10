package com.xiyou.mygradutiondesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.helper.ApplicationHelper;

/**
 * Created by fengyi on 16/3/13.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        setupView(savedInstanceState);
    }

    public void launch(Class<? extends Activity> activity,
            Bundle args4activity) {
        launch(activity, args4activity, 0);
    }

    public void launch(Class<? extends Activity> activity,
            Bundle args4activity, int reqCode) {
        Intent intent = new Intent(this, activity);
        if (args4activity != null) {
            intent.putExtras(args4activity);
        }
        if (reqCode != 0) {
            startActivityForResult(intent, reqCode);
        } else {
            startActivity(intent);
        }
    }

    protected boolean interceptOnBackPressed(){
        finish();
        return true;
    }

    protected void setupView(Bundle savedInstance){}

    protected abstract int getLayoutResId();

    protected TextView setTextString(int id, String title){
        TextView textView = (TextView)findViewById(id);
        if (textView != null){
            textView.setText(title);
        }
        return textView;
    }

}

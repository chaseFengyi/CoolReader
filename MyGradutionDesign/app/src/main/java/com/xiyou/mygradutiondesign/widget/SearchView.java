package com.xiyou.mygradutiondesign.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;

/**
 * Created by fengyi on 16/3/23.
 */
public class SearchView extends LinearLayout {

    private OnQueryTextListener onQueryTextListener;

    ViewAccessor accessor;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_search_layout, this);
        initViews(context);
    }

    private void initViews(final Context context) {
        accessor = ViewAccessor.create(findViewById(R.id.view_search));
        renderBack(context);
        renderOK();
        renderEditText();
    }

    private void renderEditText() {
        accessor.addTextChangedListener(R.id.et_view_search, new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onQueryTextListener.onQueryTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void renderBack(final Context context) {
        accessor.setOnClickListener(R.id.iv_view_left, new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
    }

    private void renderOK() {
        accessor.setOnClickListener(R.id.tv_view_right, new OnClickListener() {
            @Override
            public void onClick(View v) {
                onQueryTextListener.onQueryOK();
            }
        });
    }

    public interface OnQueryTextListener {

        /**
         * 在输入框输入内容触发事件
         *
         * @param newText
         */
        void onQueryTextChanged(String newText);

        /**
         * 点击确定触发事件
         */
        void onQueryOK();

    }

}

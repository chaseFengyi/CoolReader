package com.xiyou.mygradutiondesign.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fengyi on 16/3/13.
 */
public class ViewHelper {

    public static void setOnClickListenerOnViewWithId(View view, View.OnClickListener listener) {
        if (view == null) {
            return;
        }

        if (view.getId() != View.NO_ID) {
            view.setOnClickListener(listener);
        }
        if (view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++){
                setOnClickListenerOnViewWithId(group.getChildAt(i), listener);
            }
        }
    }

    public static TextView setTextView(View parent, int id, CharSequence string) {
        View view = findChildById(parent, id);
        if (view != null && view instanceof TextView) {
            TextView t = (TextView) view;
            t.setText(string);
            return t;
        }
        return null;
    }

    public static String getText(View parent, int id) {
        View view = findChildById(parent, id);
        if (view != null && view instanceof TextView) {
            return ((TextView) view).getText().toString().trim();
        }
        return "";
    }

    public static View showView(View view){
        if (view != null){
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static View goneView(View view){
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        return view;
    }

    private static View findChildById(View parent, int id) {
        if (parent == null) {
            return null;
        }
        return parent.findViewById(id);
    }
}

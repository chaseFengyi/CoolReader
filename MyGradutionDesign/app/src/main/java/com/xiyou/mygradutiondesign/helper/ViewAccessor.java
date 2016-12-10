package com.xiyou.mygradutiondesign.helper;

import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.util.ResUtil;
import com.xiyou.mygradutiondesign.widget.SearchView;

/**
 * Created by fengyi on 16/3/13.
 */
public class ViewAccessor {

    private View wrapperView;

    public static ViewAccessor create(View view) {
        return new ViewAccessor(view);
    }

    private ViewAccessor(View wrapperView) {
        this.wrapperView = wrapperView;
    }

    public ViewAccessor setText(int id, int resId) {
        return setText(id, ResUtil.getString(resId));
    }

    public ViewAccessor setText(int id, CharSequence string) {
        View childView = findChildById(id);
        if (childView != null && childView instanceof TextView) {
            TextView textView = (TextView) childView;
            textView.setText(string);
            return this;
        }
        return null;
    }

    public ViewAccessor setOnClickListener(int id, View.OnClickListener l) {
        View view = findChildById(id);
        if (view != null) {
            view.setOnClickListener(l);
        }
        return this;
    }

    public ViewAccessor addTextChangedListener(int id, TextWatcher textWatcher) {
        View view = findChildById(id);
        if (view != null && view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.addTextChangedListener(textWatcher);
        }
        return this;
    }

    public ViewAccessor setOnQueryTextChangeListener(int id, SearchView.OnQueryTextListener listener) {
        View view = findChildById(id);
        if (view != null && view instanceof SearchView) {
            SearchView searchView = (SearchView) view;
            searchView.setOnQueryTextListener(listener);
        }
        return this;
    }

    public ViewAccessor setOnItemClickListener(int id, AdapterView.OnItemClickListener l) {
        AdapterView view = (AdapterView) findChildById(id);
        if (view != null) {
            view.setOnItemClickListener(l);
        }
        return this;
    }

    public ViewAccessor setOnItemLongClickListener(int id,
            AdapterView.OnItemLongClickListener listener) {
        AdapterView view = (AdapterView) findChildById(id);
        if (view != null) {
            view.setOnItemLongClickListener(listener);
        }
        return this;
    }

    public ViewAccessor setOnLongClickListener(int id,
            View.OnLongClickListener listener) {
        View view = findChildById(id);
        if (view != null) {
            view.setOnLongClickListener(listener);
        }
        return this;
    }

    public ViewAccessor setVisibility(int id, int visibility) {
        View view = findChildById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    private View findChildById(int id) {
        if (wrapperView == null) {
            return null;
        }
        return wrapperView.findViewById(id);
    }

}

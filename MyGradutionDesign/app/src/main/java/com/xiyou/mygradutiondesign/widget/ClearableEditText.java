package com.xiyou.mygradutiondesign.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.xiyou.mygradutiondesign.R;

/**
 * Created by fengyi on 16/3/2.
 */

public class ClearableEditText extends EditText implements OnFocusChangeListener,
        TextWatcher, View.OnTouchListener {

    private Drawable mClearIcon;// 清除按钮图标

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 获取drawableRight图片(左，上，右，下)
        mClearIcon = getCompoundDrawables()[2];
        if (mClearIcon == null) {
            mClearIcon = context.getResources().getDrawable(R.drawable.selector_login_delete_btn);
        }
        mClearIcon
                .setBounds(0, 0, mClearIcon.getIntrinsicHeight(), mClearIcon.getIntrinsicHeight());
        setClearIconVisable(false);
        setOnFocusChangeListener(this);
        setOnTouchListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 如果EditText输入框被点击了，并且处于可编辑状态，判断是否有输入，如果没有输入，仍然不显示删除按钮
        if (hasFocus  && isFocusable() && isFocusableInTouchMode()) {
            setClearIconVisable(getText().toString().trim().length() > 0);
        } else {
            setClearIconVisable(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisable(text.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 根据用户点击的位子判断是否点击在删除按钮上，如果是，点击删除按钮并抬起后，删除内容
        // 获取点击后的坐标
        int x = (int) event.getX();
        if (mClearIcon.isVisible()
                && (x > (getWidth() - mClearIcon.getIntrinsicWidth() - getPaddingRight()))) {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置删除按钮是否显示
     *
     * @param visable
     */
    private void setClearIconVisable(boolean visable) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                visable ? mClearIcon : null, getCompoundDrawables()[3]);
    }

    /**
     * 设置是否为可编辑状态
     *
     * @param edittable
     */
    public void setEdittable(boolean edittable){
        if (edittable) {//设置为可编辑状态
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
            setSelection(getText().toString().length());
        }else {
            setFocusable(false);
            setFocusableInTouchMode(false);
        }
    }

}

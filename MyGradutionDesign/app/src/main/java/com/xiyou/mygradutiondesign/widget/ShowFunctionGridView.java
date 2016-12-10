package com.xiyou.mygradutiondesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.xiyou.mygradutiondesign.helper.DimenHelper;

/**
 * Created by fengyi on 16/3/11.
 */
public class ShowFunctionGridView extends GridView {

    private final int numColumns = 4;

    public ShowFunctionGridView(Context context) {
        super(context);
        init(context);
    }

    public ShowFunctionGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowFunctionGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 获取GridView里面item的个数
        int itemsNum = getNumColumns();
        if (itemsNum <= 0) {
            return;
        }
        // 设置GridView每一行显示的item的个数
        if (itemsNum % numColumns != 0) {// 最后一行item个数不够numcolumns
            int row = itemsNum / numColumns + 1;
            for (int i = 0; i < row; i++) {
                if (i == row - 1) {
                    //最后一行
                    setLastItemsState(itemsNum % numColumns);
                } else {
                    setNumColumns(numColumns);
                }
            }
        }
    }

    /**
     * 设置最后一行显示方式是自适应屏幕宽度
     *
     * @param nums
     */
    private void setLastItemsState(int nums) {
        setNumColumns(nums);
    }

}

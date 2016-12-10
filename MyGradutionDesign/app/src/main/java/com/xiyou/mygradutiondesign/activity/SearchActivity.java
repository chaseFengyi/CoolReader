package com.xiyou.mygradutiondesign.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.adapter.SearchAdapter;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.IntentHelper;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;
import com.xiyou.mygradutiondesign.helper.ViewHelper;
import com.xiyou.mygradutiondesign.sqLite.PictureAttributesBean;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;
import com.xiyou.mygradutiondesign.util.ToastUtil;
import com.xiyou.mygradutiondesign.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/20.
 */
public class SearchActivity extends BaseNavbarActivity implements SearchView.OnQueryTextListener {

    private SearchAdapter adapter;

    private List<PictureInfoBean> pics;

    private ListView listView;

    /**
     * 根据所属类别进行相应的模糊搜索
     */
    private String belongType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        super.setupView(savedInstance);
        Bundle bundle = IntentHelper.getBundle(getIntent());
        belongType = bundle.getString(ArgumentConst.ARG_BELONG_SEARCH);
        if (bundle == null) {
            return;
        }
        renderViews();
        clickItem();
    }

    private void clickItem() {
        ViewAccessor.create(findViewById(R.id.list_search)).setOnItemClickListener(
                R.id.list_search, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ArgumentConst.ARG_PIC_STORE_POSITION,
                                ((PictureInfoBean) adapter.getItem(position)).getPicStorePosition());
                        launch(TrafficDetailActivity.class, bundle);
                    }
                }
                );
    }

    private void renderViews() {
        ViewAccessor.create(findViewById(R.id.search)).setOnQueryTextChangeListener(R.id.search,
                this);
        listView = (ListView) findViewById(R.id.list_search);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            renderListView();
        }
    }

    private void renderListView() {
        if (pics == null || pics.size() == 0) {
            renderNoItem();
        } else {
            renderShowItem();
            adapter = new SearchAdapter(pics, this);
            listView.setAdapter(adapter);
        }
    }

    private void renderNoItem() {
        ViewHelper.goneView(findViewById(R.id.list_search));
    }

    private void renderShowItem() {
        ViewHelper.showView(findViewById(R.id.list_search));
        ViewHelper.goneView(findViewById(R.id.iv_no_search_info));
    }

    @Override
    public void onQueryTextChanged(String newText) {
        if (judgeInputStrIsEmpty()) {
            listView.clearTextFilter();
            renderNoItem();
            return;
        }
        // 模糊搜索
        search();
        if (adapter != null) {
            renderListView();
            adapter.notifyDataSetChanged();
        } else {
            renderListView();
        }
    }

    private void clear() {
        pics = null;
    }

    @Override
    public void onQueryOK() {
        if (judgeInputStrIsEmpty()) {
            listView.clearTextFilter();
            renderNoItem();
            return;
        }
        // 模糊搜索
        search();

        if (pics == null || pics.size() == 0) {
            ToastUtil.toast(this, String.format(ResUtil.getString(R.string.search_failed),
                    ((EditText) findViewById(R.id.et_view_search))
                            .getText().toString().trim()));
            return;
        }

        if (adapter != null) {
            renderListView();
            adapter.notifyDataSetChanged();
        } else {
            renderListView();
        }
    }

    /**
     * 判断输入框输入的内容是否符合要求
     *
     * @return
     */
    private boolean judgeInputStrIsEmpty() {
        String inputStr = ((EditText) findViewById(R.id.et_view_search))
                .getText().toString().trim();
        if (TextUtils.isEmpty(inputStr)) {
            ToastUtil.toast(this, ResUtil.getString(R.string.input_content_empty));
            return true;
        }
        return false;
    }

    private void search() {
        clear();
        String text = ((EditText) findViewById(R.id.et_view_search))
                .getText().toString().trim();
        if (belongType.equals(ArgumentConst.ARG_BELONG_ALL)) {
            pics = FinalDBHelper.findAllByWhereGenerally(PictureInfoBean.class,
                    PictureAttributesBean.PIC_NAME, text);
        } else {
            List<Map<String, Object>> type = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put(PictureAttributesBean.PIC_NAME, text);
            type.add(map);
            map.clear();
            map.put(PictureAttributesBean.BELONG_TYPE, belongType);
            type.add(map);
            pics = FinalDBHelper.findAllByWhereGenerally(PictureInfoBean.class, type);
        }

    }

}

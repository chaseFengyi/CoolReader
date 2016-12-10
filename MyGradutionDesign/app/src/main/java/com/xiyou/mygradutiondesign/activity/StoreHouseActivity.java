package com.xiyou.mygradutiondesign.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.adapter.StoreHouseAdapter;
import com.xiyou.mygradutiondesign.adapter.StoreHouseGridAdapter;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.DialogHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.PrefHelper;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;
import com.xiyou.mygradutiondesign.helper.ViewHelper;
import com.xiyou.mygradutiondesign.presenter.StoreHousePresenter;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;
import com.xiyou.mygradutiondesign.util.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/11.
 */
public class StoreHouseActivity extends BaseNavbarActivity implements StoreHousePresenter.IView {

    private StoreHousePresenter storeHousePresenter;
    StoreHouseAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_house;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        if (storeHousePresenter == null) {
            storeHousePresenter = new StoreHousePresenter(this);
        }
        super.setupView(savedInstance);
        renderGridView();
        renderListView();
        ImageView searchButton = (ImageView) findViewById(R.id.navbar_image_right);
        initSearchButton(searchButton);
        onClick();
    }

    int itemPosition;

    private void onClick() {
        final DialogHelper.ConfirmCallback callback = new DialogHelper.BaseConfirmCallback() {
            @Override
            public void onPositive(DialogInterface dialog) {
                super.onPositive(dialog);
                PictureInfoBean pictureInfoBean = (PictureInfoBean) adapter.getItem(itemPosition);
                String picName = pictureInfoBean.getPicName();
                if (TextUtils.isEmpty(picName)) {
                    picName = "图像识别";
                }
                FinalDBHelper.delatePicInfoById(PictureInfoBean.class, pictureInfoBean.getPicId());
                ToastUtil.toast(StoreHouseActivity.this, String.format(
                        ResUtil.getString(R.string.success_delete_info),
                        picName));
                renderListView();
            }
        };
        ViewAccessor
                .create(findViewById(R.id.store_house_list))
                .setOnItemClickListener(
                        R.id.store_house_list, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                                PictureInfoBean bean = getPicDetail(position);
                                Bundle bundle = new Bundle();
                                bundle.putString(ArgumentConst.ARG_PIC_STORE_POSITION,
                                        bean.getPicStorePosition());
                                launch(TrafficDetailActivity.class, bundle);
                            }
                        })
                .setOnItemLongClickListener(R.id.store_house_list,
                        new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                                itemPosition = position;
                                String picName = ((PictureInfoBean) adapter.getItem(position))
                                        .getPicName();
                                if (TextUtils.isEmpty(picName)) {
                                    picName = "图像识别";
                                }
                                View views = LayoutInflater.from(StoreHouseActivity.this).inflate(
                                        R.layout.view_delete_item_dialog, null);
                                DialogHelper.showListItemDeleteDialod(StoreHouseActivity.this,
                                        views,
                                        String.format(ResUtil.getString(R.string.will_delete_info),
                                                picName),
                                        callback);
                                return true;
                            }
                        });
    }

    private void initSearchButton(ImageView searchButton) {
        searchButton.setImageResource(R.drawable.selector_bar_item_search);
    }

    @Override
    protected void onNavbarItemClicked(View v) {
        super.onNavbarItemClicked(v);
        int i = v.getId();
        if (i == R.id.navbar_left) {

        } else if (i == R.id.navbar_image_right) {
            Bundle bundle = new Bundle();
            bundle.putString(ArgumentConst.ARG_BELONG_SEARCH, ArgumentConst.ARG_BELONG_ALL);
            launch(SearchActivity.class, bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PrefHelper.getBoolean(ArgumentConst.PIC_REFRESH, false)) {
            renderListView();
        }
    }

    private void renderGridView() {
        GridView gridView = (GridView) findViewById(R.id.store_house_grid);
        // 取消GridView默认的点击效果
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        List<Map<String, Object>> items = storeHousePresenter.getGridData();
        if (items == null || items.size() == 0) {
            return;
        }
        final StoreHouseGridAdapter adapter = new StoreHouseGridAdapter(items, this);
        gridView.setAdapter(adapter);

        ViewAccessor.create(findViewById(R.id.store_house_grid)).setOnItemClickListener(
                R.id.store_house_grid, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ArgumentConst.ARG_TYPE,
                                (String) ((Map<String, Object>) adapter.getItem(position))
                                        .get("describe"));
                        launch(TrafficTypeActivity.class, bundle);
                    }
                });
    }

    private void renderListView() {
        // 获取数据并显示
        List<PictureInfoBean> items = storeHousePresenter.getTrafficData();
        if (items == null || items.size() == 0) {
            ToastUtil.toast(this, ResUtil.getString(R.string.no_data));
            ViewHelper.goneView(findViewById(R.id.store_house_list));
        } else {
            ViewHelper.showView(findViewById(R.id.store_house_list));
            ListView listView = (ListView) findViewById(R.id.store_house_list);
            adapter = new StoreHouseAdapter(items, this);
            listView.setAdapter(adapter);
        }
    }

    private PictureInfoBean getPicDetail(int position) {
        if (position < 0) {
            return null;
        }
        return (PictureInfoBean) adapter.getItem(position);
    }


    @Override
    public void showProgress() {
//        DialogHelper.showProgressDialog(this, R.string.);
    }

    @Override
    public void dismissProgress() {
//        DialogHelper.dismissProgressDialog();
    }


}

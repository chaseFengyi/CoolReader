package com.xiyou.mygradutiondesign.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.adapter.TrafficTypeAdapter;
import com.xiyou.mygradutiondesign.bean.TrafficSignBean;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.DialogHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.IntentHelper;
import com.xiyou.mygradutiondesign.helper.PrefHelper;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;
import com.xiyou.mygradutiondesign.helper.ViewHelper;
import com.xiyou.mygradutiondesign.presenter.BasePresenter;
import com.xiyou.mygradutiondesign.presenter.TrafficTypePresenter;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;
import com.xiyou.mygradutiondesign.util.ToastUtil;

import java.util.List;

/**
 * Created by fengyi on 16/3/13.
 */
public class TrafficTypeActivity extends BaseNavbarActivity implements TrafficTypePresenter.IView {

    private TrafficTypePresenter trafficTypePresenter;
    private TrafficTypeAdapter adapter;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (trafficTypePresenter == null) {
            trafficTypePresenter = new TrafficTypePresenter(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_traffic_type;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        super.setupView(savedInstance);
        title = IntentHelper.getBundle(getIntent()).getString(ArgumentConst.ARG_TYPE);
        setTitle(title);
        renderData();
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
                ToastUtil.toast(TrafficTypeActivity.this, String.format(
                        ResUtil.getString(R.string.success_delete_info),
                        picName));
                renderData();
            }
        };
        ViewAccessor.create(findViewById(R.id.list_traffic_type)).setOnItemClickListener(
                R.id.list_traffic_type, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ArgumentConst.ARG_PIC_STORE_POSITION,
                                ((PictureInfoBean) adapter.getItem(position)).getPicStorePosition());
                        launch(TrafficDetailActivity.class, bundle);
                    }
                }).setOnItemLongClickListener(R.id.list_traffic_type,
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
                        View views = LayoutInflater.from(TrafficTypeActivity.this).inflate(
                                R.layout.view_delete_item_dialog, null);
                        DialogHelper.showListItemDeleteDialod(TrafficTypeActivity.this,
                                views,
                                String.format(ResUtil.getString(R.string.will_delete_info),
                                        picName),
                                callback);
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PrefHelper.getBoolean(ArgumentConst.PIC_REFRESH, false)) {
            renderData();
        }
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
            bundle.putString(ArgumentConst.ARG_BELONG_SEARCH, title);
            launch(SearchActivity.class, bundle);
        }
    }

    private void renderData() {
        // 获取数据并显示
        List<PictureInfoBean> items = trafficTypePresenter.getTrafficData(title);
        if (items == null || items.size() == 0) {
            ToastUtil.toast(this, ResUtil.getString(R.string.no_data));
            ViewHelper.goneView(findViewById(R.id.list_traffic_type));
        } else {
            ViewHelper.showView(findViewById(R.id.list_traffic_type));
            adapter = new TrafficTypeAdapter(items, this);
            ListView listView = (ListView) findViewById(R.id.list_traffic_type);
            listView.setAdapter(adapter);
        }

    }

    private TrafficSignBean getTrafficDetail(int position) {
        if (position < 0) {
            return null;
        }
        return (TrafficSignBean) adapter.getItem(position);
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

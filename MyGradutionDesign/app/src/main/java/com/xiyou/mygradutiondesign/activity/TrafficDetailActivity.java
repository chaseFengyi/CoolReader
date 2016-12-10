package com.xiyou.mygradutiondesign.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.adapter.DialogBelongTypeAdapter;
import com.xiyou.mygradutiondesign.constant.ArgumentConst;
import com.xiyou.mygradutiondesign.helper.DialogHelper;
import com.xiyou.mygradutiondesign.helper.FinalDBHelper;
import com.xiyou.mygradutiondesign.helper.IntentHelper;
import com.xiyou.mygradutiondesign.helper.PictureHelper;
import com.xiyou.mygradutiondesign.helper.PrefHelper;
import com.xiyou.mygradutiondesign.helper.ViewAccessor;
import com.xiyou.mygradutiondesign.helper.ViewHelper;
import com.xiyou.mygradutiondesign.presenter.TrafficDetailPresenter;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;
import com.xiyou.mygradutiondesign.util.ToastUtil;
import com.xiyou.mygradutiondesign.widget.ClearableEditText;

import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/13.
 */
public class TrafficDetailActivity extends BaseNavbarActivity implements TrafficDetailPresenter.IView {

    private String dataUrl;// 从前一页面获取的图片存储地址

    private String oldPicName;
    private String oldPicBelongType;
    private View parent;
    private ViewAccessor navbarViewAccessor;

    private DialogBelongTypeAdapter adapter;
    private ClearableEditText editText;

    private TrafficDetailPresenter presenter;

    private PictureInfoBean pictureInfoBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_traffic_detail;
    }

    @Override
    protected void setupView(Bundle savedInstance) {
        presenter = new TrafficDetailPresenter(this);
        setTextString(R.id.navbar_title, ResUtil.getString(R.string.traffic_details));
        Bundle bundle = IntentHelper.getBundle(getIntent());
        dataUrl = IntentHelper.getString(bundle, ArgumentConst.ARG_PIC_STORE_POSITION);
        pictureInfoBean = presenter.getPicInfoByAddress(dataUrl);
        super.setupView(savedInstance);
        renderTrafficInfo();
    }

    /**
     * 填充数据
     */
    private void renderTrafficInfo() {
        if (dataUrl == null || dataUrl.isEmpty()) {
            return;
        }

        if (pictureInfoBean == null) {
            return;
        }

        parent = findViewById(R.id.linear_traffic_details);
        oldPicName = ViewHelper.getText(parent, R.id.tv_traffic_details_pic_name);
        oldPicBelongType = ViewHelper.getText(parent, R.id.tv_traffic_details_pic_belong_type);
        // 显示右上角为“修改”
        navbarViewAccessor = ViewAccessor.create(findViewById(R.id.navbar));
        navbarViewAccessor.setVisibility(R.id.navbar_right, View.VISIBLE)
                .setText(R.id.navbar_right, R.string.navbar_text_update);
        editText = (ClearableEditText) parent.findViewById(R.id.tv_traffic_details_pic_name);
        renderPicName();
        renderPicBelongType();
        renderPicStorePosition();
        renderPicIntroduce();
        renderPic();
    }

    @Override
    protected void onNavbarItemClicked(View v) {
        super.onNavbarItemClicked(v);
        int id = v.getId();
        if (id == R.id.navbar_right) {
            renderNavbarRight();
        }
    }

    private void renderNavbarRight() {
        String navbarRightText = ViewHelper.getText(findViewById(R.id.navbar), R.id.navbar_right);
        if (!navbarRightText.isEmpty()
                && navbarRightText.equals(ResUtil.getString(R.string.navbar_text_update))) {
            // 用户点击修改按钮，图片名称和所属类别变为可修改状态
            setNavbarRightText(ResUtil.getString(R.string.navbar_text_ok));
            editText.setEdittable(true);
            clickBelongTypeButton();
        } else if (!navbarRightText.isEmpty()
                && navbarRightText.equals(ResUtil.getString(R.string.navbar_text_ok))) {
            editText.setEdittable(false);
            // 判断当前文本是否有变化，如果有，上传服务器从而更改信息，否则不做处理，是文本显示为“修改”而已
            int i = judgeTextsChanged();
            if (i == 0) {// 文本没有改变
                setNavbarRightText(ResUtil.getString(R.string.navbar_text_update));
                PrefHelper.put(ArgumentConst.PIC_REFRESH, false);
            } else if (i == 1) {// 文本改变了
                setNavbarRightText(ResUtil.getString(R.string.navbar_text_update));
                // 上传服务器
                String curPicName = ViewHelper.getText(parent, R.id.tv_traffic_details_pic_name);
                String curPicBelongType = ViewHelper.getText(parent,
                        R.id.tv_traffic_details_pic_belong_type);
                pictureInfoBean.setPicName(curPicName);
                pictureInfoBean.setBelongType(curPicBelongType);
                FinalDBHelper.update(pictureInfoBean);
                ToastUtil.toast(this, "修改成功");
                PrefHelper.put(ArgumentConst.PIC_REFRESH, true);
            }
        }
    }

    /**
     * 设置右上角文本
     * 
     * @param text
     */
    private void setNavbarRightText(String text) {
        if (navbarViewAccessor == null) {
            return;
        }
        navbarViewAccessor.setText(R.id.navbar_right, text);
    }

    /**
     * @return 1:文本改变了 0:文本没有改变 -1:输入错误
     */
    private int judgeTextsChanged() {
        String curPicName = ViewHelper.getText(parent, R.id.tv_traffic_details_pic_name);
        String curPicBelongType = ViewHelper.getText(parent,
                R.id.tv_traffic_details_pic_belong_type);
        if (curPicName.isEmpty()) {
            ToastUtil.toast(this, "输入内容不能为空");
            return -1;
        }
        if (curPicBelongType.equals(oldPicBelongType) && curPicName.equals(oldPicName)) {
            return 0;
        }
        return 1;
    }

    private void renderPicName() {
        String picName = pictureInfoBean.getPicName();
        ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_name),
                R.id.tv_traffic_details_pic_name,
                (picName == null || picName.isEmpty())
                        ? ResUtil.getString(R.string.app_name) : picName);
        editText.setEdittable(false);
    }

    View appMarketView;

    private void showBelongTypeItems() {
        List<Map<String, String>> items = presenter.getItems();
        adapter = new DialogBelongTypeAdapter(this, items);
        appMarketView = LayoutInflater.from(this).inflate(
                R.layout.view_belong_type_dialog,
                null);
        ListView listView = (ListView) appMarketView.findViewById(R.id.list_dialog_belong_type);
        listView.setAdapter(adapter);

    }

    private void renderPicBelongType() {
        ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_belong_type),
                R.id.tv_traffic_details_pic_belong_type,
                pictureInfoBean.getBelongType());
    }

    private void clickBelongTypeButton() {
        ViewAccessor viewAccessor = ViewAccessor.create(parent);
        final DialogHelper.ConfirmCallback callback = new DialogHelper.BaseConfirmCallback() {
            @Override
            public void onItemPositive(DialogInterface dialog, AdapterView<?> parent, View view,
                    int position, long id) {
                super.onItemPositive(dialog, parent, view, position, id);
                ViewHelper.setTextView(TrafficDetailActivity.this.parent,
                        R.id.tv_traffic_details_pic_belong_type,
                        ((Map<String, String>) (adapter.getItem(position))).get("belongType"));
            }
        };
        viewAccessor.setOnClickListener(R.id.tv_traffic_details_pic_belong_type,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBelongTypeItems();
                        DialogHelper.showBelongTypesDialog(TrafficDetailActivity.this,
                                appMarketView, R.string.choice_belong_types, callback);
                    }
                });

    }

    private void renderPicStorePosition() {
        ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_store_position),
                R.id.tv_traffic_details_pic_store_position,
                pictureInfoBean.getPicStorePosition());
    }

    /**
     * 如果还没有对该图片的详细介绍，则先获取图片信息，然后对该图片进行分析，获取对该图片的介绍
     */
    int picId = 0;
    private void renderPicIntroduce() {
        picId = pictureInfoBean.getPicId();
        String describe = pictureInfoBean.getDescribe();
        if (TextUtils.isEmpty(describe)) {//没有存储该图片的介绍，通过图像分析方法来对图片进行处理
            presenter.getPicDesc(pictureInfoBean.getPicStorePosition());
        }
        ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_introduce),
                R.id.tv_traffic_details_pic_introduce,
                (TextUtils.isEmpty(describe))
                        ? ResUtil.getString(R.string.no_describe) : describe);
    }

    private void renderPic() {

        View headerView = findViewById(R.id.header_view);
        final String avatarUrl = pictureInfoBean.getPicStorePosition();
        headerView.getRootView().measure(0, 0);

        ImageView avatarBgView = (ImageView) headerView.findViewById(R.id.avatar_background);
        ViewGroup.LayoutParams params = avatarBgView.getLayoutParams();
        params.height = headerView.getMeasuredHeight();
        avatarBgView.setLayoutParams(params);
        PictureHelper.loadBlurBitmap(headerView.getContext(), avatarUrl, avatarBgView, 25);// 注意blur半径最大25
        headerView.findViewById(R.id.mask).setLayoutParams(params);

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        presenter.loadImage(avatarUrl, avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ArgumentConst.ARG_PIC_PATH, avatarUrl);
                launch(ShowImageActivity.class, bundle);
            }
        });
    }

    @Override
    public void showProgress() {
        DialogHelper.showProgressDialog(this, R.string.loading);
    }

    @Override
    public void dismissProgress() {
        DialogHelper.dismissProgressDialog();
    }

    @Override
    public void setPicDescribe(String describe) {
        if (!TextUtils.isEmpty(describe)) {
            //将图片的介绍存储在本地
            pictureInfoBean.setDescribe(describe);
            presenter.updatePicInfo(pictureInfoBean);
        }
        ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_introduce),
                R.id.tv_traffic_details_pic_introduce,
                (TextUtils.isEmpty(describe))
                        ? ResUtil.getString(R.string.no_describe) : describe);
    }

    @Override
    public void checkBelongType(final String belongType) {
        if (TextUtils.isEmpty(belongType)){
            FinalDBHelper.delatePicInfoById(PictureInfoBean.class, picId);
            PrefHelper.put(ArgumentConst.PIC_REFRESH, true);
            return;
        }
        if (pictureInfoBean.getBelongType().equals(belongType)){
            return;
        }
        //该图标本身所属类别与用户存储的所属类别不同，询问用户是否需要更改该图标存储的类别位置
        DialogHelper.ConfirmCallback callback = new DialogHelper.BaseConfirmCallback(){
            @Override
            public void onPositive(DialogInterface dialog) {
                super.onPositive(dialog);
                pictureInfoBean.setBelongType(belongType);
                FinalDBHelper.update(pictureInfoBean);
                ViewHelper.setTextView(findViewById(R.id.tv_traffic_details_pic_belong_type),
                        R.id.tv_traffic_details_pic_belong_type,
                        belongType);
                ToastUtil.toast(TrafficDetailActivity.this, "修改成功");
                PrefHelper.put(ArgumentConst.PIC_REFRESH, true);
            }
        };
        View views = LayoutInflater.from(TrafficDetailActivity.this).inflate(
                R.layout.view_delete_item_dialog, null);
        DialogHelper.showListItemDeleteDialod(TrafficDetailActivity.this,
                views,
                R.string.update_save_position_of_sign,
                callback);
    }

}

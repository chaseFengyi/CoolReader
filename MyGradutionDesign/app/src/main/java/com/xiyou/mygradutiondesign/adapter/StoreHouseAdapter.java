package com.xiyou.mygradutiondesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.bean.TrafficSignBean;
import com.xiyou.mygradutiondesign.helper.ImageLoaderHelper;
import com.xiyou.mygradutiondesign.helper.PictureHelper;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/12/12.
 */
public class StoreHouseAdapter extends BaseAdapter {

    List<PictureInfoBean> signs;
    Context context;

    public StoreHouseAdapter(List<PictureInfoBean> signs, Context context) {
        this.signs = signs;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (signs != null || signs.size() > 0) {
            return signs.size();
        } else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return signs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.sample_store_house, null);
            viewHolder.iv_sign_picture = (ImageView) view
                    .findViewById(R.id.iv_simple_store_house_picture);
            viewHolder.tv_sign_title = (TextView) view
                    .findViewById(R.id.tv_simple_store_house_title);
            viewHolder.iv_enter = (ImageView) view.findViewById(R.id.iv_simple_store_house_enter);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 逻辑处理
        /*PictureHelper.loadBitmap(signs.get(position).getPicStorePosition(),
                viewHolder.iv_sign_picture);*/
        ImageLoaderHelper.loadBitmap(signs.get(position).getPicStorePosition(),
                viewHolder.iv_sign_picture);
        String picName = signs.get(position).getPicName();
        if (picName == null) {
            viewHolder.tv_sign_title.setText(ResUtil.getString(R.string.app_name));
        } else {
            viewHolder.tv_sign_title.setText(picName);
        }

        return view;

    }

    class ViewHolder {
        private ImageView iv_sign_picture;
        private TextView tv_sign_title;
        private ImageView iv_enter;
    }

}

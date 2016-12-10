package com.xiyou.mygradutiondesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.helper.ImageLoaderHelper;
import com.xiyou.mygradutiondesign.sqLite.bean.PictureInfoBean;
import com.xiyou.mygradutiondesign.util.ResUtil;

import java.util.List;

/**
 * Created by fengyi on 16/3/23.
 */
public class SearchAdapter extends BaseAdapter {


    List<PictureInfoBean> signs;
    Context context;

    public SearchAdapter(List<PictureInfoBean> signs, Context context) {
        this.signs = signs;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(signs == null || signs.size() <= 0){
            return 0;
        }else
            return signs.size();
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

        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.sample_store_house,null);
            viewHolder.iv_signPic = (ImageView)view.findViewById(R.id.iv_simple_store_house_picture);
            viewHolder.tv_signTitle = (TextView)view.findViewById(R.id.tv_simple_store_house_title);
            viewHolder.iv_enter = (ImageView)view.findViewById(R.id.iv_simple_store_house_enter);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        /*PictureHelper.loadBitmap(signs.get(position).getPicStorePosition(),
                viewHolder.iv_signPic);*/
        ImageLoaderHelper.loadBitmap(signs.get(position).getPicStorePosition(),
                viewHolder.iv_signPic);
        String picName = signs.get(position).getPicName();
        if (picName == null) {
            viewHolder.tv_signTitle.setText(ResUtil.getString(R.string.app_name));
        } else {
            viewHolder.tv_signTitle.setText(picName);
        }

        return view;
    }

    class ViewHolder{
        ImageView iv_signPic;
        TextView tv_signTitle;
        ImageView iv_enter;
    }

}

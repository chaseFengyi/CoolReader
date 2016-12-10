package com.xiyou.mygradutiondesign.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.helper.DimenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/11.
 */
public class StoreHouseGridAdapter extends BaseAdapter {

    private List<Map<String, Object>> items;
    private Context context;

    public StoreHouseGridAdapter(List<Map<String, Object>> items,Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sample_store_house_grid, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_sample_grid_pic);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_sample_grid_describe);
            holder.view = convertView.findViewById(R.id.view_sample_grid);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap((Bitmap) items.get(position).get("icon"));
        holder.textView.setText((CharSequence) items.get(position).get("describe"));
        setItemSize(holder);

        return convertView;
    }

    private void setItemSize(ViewHolder holder){
        int width = DimenHelper.getScreenWidth();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
        params.width = width / 7;
        params.height = width / 7;
        holder.imageView.setLayoutParams(params);
        holder.view.setLayoutParams(params);
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
        private View view;
    }
}

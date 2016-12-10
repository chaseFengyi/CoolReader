package com.xiyou.mygradutiondesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiyou.mygradutiondesign.R;

import java.util.List;
import java.util.Map;

/**
 * Created by fengyi on 16/3/16.
 */
public class DialogBelongTypeAdapter extends BaseAdapter {

    private List<Map<String, String>> items;

    private Context context;

    public DialogBelongTypeAdapter(Context context, List<Map<String, String>> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.view_belong_type_dialog_detail, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_dialog_belong_detail);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(items.get(position).get("belongType"));

        return convertView;
    }

    private class ViewHolder{
        TextView textView;
    }
}

package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */

public class StringListAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Context context;

    private int currCheck = -1;

    public void setCurrCheck(int pos) {
        this.currCheck = pos;
    }

    public int getCurrCheck() {
        return currCheck;
    }

    public StringListAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_string_list, viewGroup, false);
            holder.tv_string_name = convertView.findViewById(R.id.tv_string_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_string_name.setText((String) list.get(i).get("name"));
        if (currCheck == i) {
            holder.tv_string_name.setTextColor(ContextCompat.getColor(context, R.color.main_yellow));
        } else {
            holder.tv_string_name.setTextColor(ContextCompat.getColor(context, R.color.nomalText));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_string_name;
    }


}

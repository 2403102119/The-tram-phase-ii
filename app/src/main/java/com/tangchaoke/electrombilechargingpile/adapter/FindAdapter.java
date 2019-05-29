package com.tangchaoke.electrombilechargingpile.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.util.List;
import java.util.Map;

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public FindAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if (StringUtil.isSpace((String) list.get(position).get("getPhoto"))) {
            ImageLoadUtil.showImage((Activity) context, "http://store.is.autonavi.com/showpic/0214dd40b2286e66c37b2cef86c8555b", holder.img_find_icon);
        }else {
            ImageLoadUtil.showImage((Activity) context, (String) list.get(position).get("getPhoto"), holder.img_find_icon);
        }
        holder.tv_find_name.setText((String)list.get(position).get("getTitle"));
        holder.tv_find_distance.setText(AMapUtil.getFriendlyLength((int)list.get(position).get("getDistance")));
        holder.tv_find_address.setText((String)list.get(position).get("getCityName")+(String)list.get(position).get("getAdName")+(String)list.get(position).get("getSnippet"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.itemListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == list)
        return 0;
        else
            return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private ImageView img_find_icon;
        private TextView tv_find_name,tv_find_address,tv_find_distance;

        public MyHolder(View itemView) {
            super(itemView);

            img_find_icon = itemView.findViewById(R.id.img_find_icon);
            tv_find_name = itemView.findViewById(R.id.tv_find_name);
            tv_find_address = itemView.findViewById(R.id.tv_find_address);
            tv_find_distance = itemView.findViewById(R.id.tv_find_distance);
        }
    }

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemListener(int position);
    }
}

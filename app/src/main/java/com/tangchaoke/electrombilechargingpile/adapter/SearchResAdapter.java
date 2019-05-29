package com.tangchaoke.electrombilechargingpile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tangchaoke.electrombilechargingpile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2017/7/25.
 */

public class SearchResAdapter extends RecyclerView.Adapter<SearchResAdapter.ViewHolder>{
    private int resourceId;
    private List<Map<String,Object>> data = new ArrayList<>();

    public interface onItemClickListener{
        void onClick(View view, int position);
    }

    private onItemClickListener onItemClickListener;

    public  void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener  = listener;
    }

    public SearchResAdapter(int resourceId, List<Map<String,Object>> data) {
        this.resourceId = resourceId;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.str1.setText((String)data.get(position).get("title"));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(holder.item,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView str1;//,str2
        public View item;

        public ViewHolder(View itemView) {
            super(itemView);
//            item=itemView;
            str1 = itemView.findViewById(R.id.str1);
//            str2 = (TextView)itemView.findViewById(R.id.str2);
            item = itemView.findViewById(R.id.item);
        }
    }

}

package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;

import java.util.List;
import java.util.Map;

public class FindMapAdapter extends RecyclerView.Adapter<FindMapAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public FindMapAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find_search, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_find_map_distance.setText(AMapUtil.getFriendlyLength((int)list.get(position).get("getDistance")));
        holder.tv_find_map_name.setText((String)list.get(position).get("getTitle"));
        holder.tv_find_map_address.setText((String)list.get(position).get("getCityName")+(String)list.get(position).get("getAdName")+(String)list.get(position).get("getSnippet"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
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
        private TextView tv_find_map_distance,tv_find_map_name,tv_find_map_address;

        public MyHolder(View itemView) {
            super(itemView);
            tv_find_map_distance = itemView.findViewById(R.id.tv_find_map_distance);
            tv_find_map_name = itemView.findViewById(R.id.tv_find_map_name);
            tv_find_map_address = itemView.findViewById(R.id.tv_find_map_address);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}

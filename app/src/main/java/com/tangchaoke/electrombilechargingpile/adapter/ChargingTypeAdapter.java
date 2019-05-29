package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.util.List;
import java.util.Map;

public class ChargingTypeAdapter extends RecyclerView.Adapter<ChargingTypeAdapter.MyHolder>{
    private List<Map<String, Object>> list;
    private Context context;

    public ChargingTypeAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charging_type, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.cb_auto.setText((String)list.get(position).get("chargingmode"));
        if ((boolean)list.get(position).get("isClick")){
            holder.cb_auto.setBackgroundResource(R.drawable.shape_padding_yellow_5);
            holder.cb_auto.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cb_auto.setBackgroundResource(R.drawable.rect_stroke_gray_5);
            holder.cb_auto.setTextColor(context.getResources().getColor(R.color.gray));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.itemClick(position);
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

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView cb_auto;

        public MyHolder(View itemView) {
            super(itemView);

            cb_auto = itemView.findViewById(R.id.cb_auto);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemClick(int position);
    }
}

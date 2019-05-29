package com.tangchaoke.electrombilechargingpile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * kylin on 2018/5/8.
 */

public class ChargingPileListAdapter extends RecyclerView.Adapter<ChargingPileListAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public ChargingPileListAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charging_pile_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.identity.setText(StringUtil.deleteSpace((String) list.get(position).get("identity"))+"号");
//        holder.guntype.setText("0".equals(list.get(position).get("guntype"))?"空闲":"使用中");

        /*if ("0".equals(list.get(position).get("guntype"))||"".equals(list.get(position).get("guntype"))){
            holder.guntype.setVisibility(View.VISIBLE);
        }else{
            holder.guntype.setVisibility(View.GONE);
        }*/

        if ("2".equals((String)list.get(position).get("location"))){
            //充电类型
            //
            holder.type.setText("外");
            holder.type.setBackgroundResource(R.drawable.shape_padding_green_10);
        }else{
            holder.type.setText("内");
            holder.type.setBackgroundResource(R.drawable.shape_padding_red_10);
        }
        holder.tv_charging_leisure.setText(list.get(position).get("socketLeisurenum")+"空闲");
        holder.tv_charging_used.setText(list.get(position).get("socketUsenum")+"使用中");
        holder.tv_charging_abnormal.setText(list.get(position).get("socketAbnormalnum")+"异常");
        holder.rechargePort.setText(list.get(position).get("rechargePort")+"");
        holder.power.setText((String)list.get(position).get("power"));
        holder.voltage.setText((String)list.get(position).get("voltage"));
        holder.sourceType.setText(list.get(position).get("sourceType")+"");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListener.onItemClick(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView identity;
        private TextView guntype;
        private TextView type;
        private TextView tv_charging_leisure;
        private TextView rechargePort;
        private TextView power;
        private TextView voltage;
        private TextView sourceType;
        private TextView tv_charging_used;
        private TextView tv_charging_abnormal;
        MyViewHolder(View itemView) {
            super(itemView);
            identity = itemView.findViewById(R.id.identity);
            guntype = itemView.findViewById(R.id.guntype);
            type = itemView.findViewById(R.id.type);
            tv_charging_leisure = itemView.findViewById(R.id.tv_charging_leisure);
            tv_charging_used = itemView.findViewById(R.id.tv_charging_used);
            tv_charging_abnormal = itemView.findViewById(R.id.tv_charging_abnormal);
            rechargePort = itemView.findViewById(R.id.rechargePort);
            power = itemView.findViewById(R.id.power);
            voltage = itemView.findViewById(R.id.voltage);
            sourceType = itemView.findViewById(R.id.sourceType);
        }
    }

//    private ChargingPileListAdapter.OnItemClickListener onItemClickListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener(ChargingPileListAdapter.OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
}

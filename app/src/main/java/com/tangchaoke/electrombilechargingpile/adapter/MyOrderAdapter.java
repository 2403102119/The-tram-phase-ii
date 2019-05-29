package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public MyOrderAdapter(List<Map<String, Object>> list, Context context) {
        Log.i("1111", "CouponAdapter: " + context.getClass().getSimpleName());
        Log.i("1111", "CouponAdapter: " + context.getClass().getName());
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {//展示值

        String type = (String) list.get(position).get("oederState");

        if ("1".equals(type)) {
            holder.tv_zhuangtai.setText("已完成");
        }else if ("0".equals(type)){
            holder.tv_zhuangtai.setText("充电中");
        }else {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.shijian.setText((String)list.get(position).get("orderTime"));
        holder.mingcheng.setText((String)list.get(position).get("chargePlace"));
        holder.power_du.setText(StringUtil.deleteSpace(list.get(position).get("identity")+ "")+"号电桩"+ list.get(position).get("socket")+"号位");
        holder.tv_item_money.setText(new DecimalFormat("0.00").format((double)list.get(position).get("cost"))+"元\t");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClickListener(position);
                }
            }
        });
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
        TextView tv_zhuangtai;
        TextView shijian;
        TextView mingcheng;
        TextView power_du;
        TextView tv_item_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_zhuangtai = itemView.findViewById(R.id.tv_zhuangtai);
            shijian = itemView.findViewById(R.id.shijian);
            mingcheng = itemView.findViewById(R.id.mingcheng);
            power_du = itemView.findViewById(R.id.power_du);
            tv_item_money = itemView.findViewById(R.id.tv_item_money);


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

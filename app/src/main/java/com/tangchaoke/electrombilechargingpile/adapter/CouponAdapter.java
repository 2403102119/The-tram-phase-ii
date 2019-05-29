package com.tangchaoke.electrombilechargingpile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;


    public CouponAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {//展示值
        String state = (String) list.get(position).get("state");

        holder.tv_coupon_name.setText((String)list.get(position).get("text"));
        holder.tv_coupon_date.setText("有效期至"+ list.get(position).get("cutOffTime"));
        holder.tv_item_money.setText((double)list.get(position).get("subtractMoney")+"");


        if (state.equals("0")) {//可用
            holder.ll_coupon.setBackgroundResource(R.drawable.shape_padding_white_10);
            holder.biaoshi.setVisibility(View.GONE);
            //判断点击事件，添加点击效果
            if ((Boolean) list.get(position).get("click")) {
                holder.ll_coupon.setBackgroundResource(R.drawable.shape_padding_green_stroke_10);
            } else {
                holder.ll_coupon.setBackgroundResource(R.color.white);
            }
        } else {
            holder.ll_coupon.setBackgroundResource(R.drawable.shape_padding_gray_10);
            if (state.equals("1")) {         //已使用
                holder.biaoshi.setImageResource(R.mipmap.img_used);
            } else {                         //已过期
                holder.biaoshi.setImageResource(R.drawable.jiaobiao);
            }
        }






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
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

    class MyViewHolder extends RecyclerView.ViewHolder {        //声明控件
        RelativeLayout ll_coupon;
        private ImageView biaoshi;
        TextView tv_coupon_name,tv_coupon_date,tv_item_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            biaoshi = itemView.findViewById(R.id.biaoshi);
            ll_coupon = itemView.findViewById(R.id.ll_coupon);
            tv_coupon_name = itemView.findViewById(R.id.tv_coupon_name);
            tv_coupon_date = itemView.findViewById(R.id.tv_coupon_date);
            tv_item_money = itemView.findViewById(R.id.tv_item_money);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}

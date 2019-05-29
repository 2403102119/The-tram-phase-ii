package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

public class AccountDetailsAdapter extends RecyclerView.Adapter<AccountDetailsAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;


    public AccountDetailsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_details, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if ("1".equals(list.get(position).get("type"))) {
            holder.tv_item_money.setText("-"+new DecimalFormat("0.00").format(Double.parseDouble((String) list.get(position).get("money")))+"");
            holder.tv_item_money.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.tv_item_money.setText("+"+new DecimalFormat("0.00").format(Double.parseDouble((String) list.get(position).get("money")))+"");
            holder.tv_item_money.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.mingcheng_DZ.setText((String)list.get(position).get("chargePlace"));
        holder.time_acc.setText((String)list.get(position).get("time"));

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
        TextView tv_item_money,mingcheng_DZ,time_acc;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_money = itemView.findViewById(R.id.tv_item_money);
            mingcheng_DZ=itemView.findViewById(R.id.mingcheng_DZ);
            time_acc=itemView.findViewById(R.id.time_acc);
        }
    }
}

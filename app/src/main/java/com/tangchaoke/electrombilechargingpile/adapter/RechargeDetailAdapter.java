package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RechargeDetailAdapter extends RecyclerView.Adapter<RechargeDetailAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public RechargeDetailAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rechage_detail, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if ("1".equals(list.get(position).get("type"))){        //充值
            holder.tv_rechage_money.setText("+"+list.get(position).get("money"));
            holder.tv_rechage_money.setTextColor(context.getResources().getColor(R.color.green2));
            if ("2".equals(list.get(position).get("paymentMethod"))){  //微信充值
                holder.img_recharge_icon.setImageResource(R.mipmap.ic_payment_wiexin);
                holder.tv_recharge_title.setText("微信充值");
            }else if ("1".equals(list.get(position).get("paymentMethod"))){   //支付宝充值
                holder.img_recharge_icon.setImageResource(R.mipmap.ic_recharge_alipay);
                holder.tv_recharge_title.setText("支付宝充值");
            }
        }else {                                                 //消费
            holder.tv_rechage_money.setText("-"+list.get(position).get("money"));
            holder.tv_rechage_money.setTextColor(context.getResources().getColor(R.color.red));
            holder.img_recharge_icon.setImageResource(R.mipmap.ic_me_wallet);
            holder.tv_recharge_title.setText("余额支付");
        }
        long adoptTimeStr=(long)list.get(position).get("time");
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        long  l = Long.valueOf(adoptTimeStr);
        timeString = sdf.format(new Date(l));//单位秒
        holder.tv_recharge_time.setText(timeString);
    }

    @Override
    public int getItemCount() {
        if (null == list){
            return 0;
        }else {
            return list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private ImageView img_recharge_icon;
        private TextView tv_recharge_title,tv_recharge_time,tv_rechage_money;

        public MyHolder(View itemView) {
            super(itemView);

            img_recharge_icon = itemView.findViewById(R.id.img_recharge_icon);
            tv_recharge_title = itemView.findViewById(R.id.tv_recharge_title);
            tv_recharge_time = itemView.findViewById(R.id.tv_recharge_time);
            tv_rechage_money = itemView.findViewById(R.id.tv_rechage_money);
        }
    }
}

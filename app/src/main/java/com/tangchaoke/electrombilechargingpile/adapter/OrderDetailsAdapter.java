package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/4.
 */

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public OrderDetailsAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.shijian.setText((String)list.get(position).get("type"));
        holder.zhuangtai.setText((String)list.get(position).get("ctime"));
        holder.zhanming.setText((String)list.get(position).get("context"));
        holder.diandu.setText((String)list.get(position).get("ctime"));
        holder.qian.setText((String)list.get(position).get("context"));


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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shijian,zhuangtai,zhanming,diandu,qian;
        public MyViewHolder(View itemView) {
            super(itemView);
            shijian=itemView.findViewById(R.id.shijian);
            zhuangtai=itemView.findViewById(R.id.zhuangtai);
            zhanming=itemView.findViewById(R.id.zhanming);
            diandu=itemView.findViewById(R.id.diandu);
            qian=itemView.findViewById(R.id.qian);



        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}

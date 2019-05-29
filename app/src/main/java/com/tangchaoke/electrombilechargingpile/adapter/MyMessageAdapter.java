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

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public MyMessageAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        //0：系统消息            1：其他消息
        if ("0".equals((String)list.get(position).get("type"))) {
            holder.type1.setText("系统消息");
        }else {
            holder.type1.setText("其他消息");
        }
        holder.ctime1.setText((String)list.get(position).get("ctime"));
        holder.context1.setText((String)list.get(position).get("context"));


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
        TextView type1,context1,ctime1;
        public MyViewHolder(View itemView) {
            super(itemView);
            type1=itemView.findViewById(R.id.type1);
            context1=itemView.findViewById(R.id.context1);
            ctime1=itemView.findViewById(R.id.ctime1);


        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}

package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public MessageAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(FindAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_message, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.tv_message_name.setText((String)list.get(position).get("title"));
        holder.tv_message_content.setText((String)list.get(position).get("context"));

        long adoptTimeStr=(long)list.get(position).get("ctime");
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        long  l = Long.valueOf(adoptTimeStr);
        timeString = sdf.format(new Date(l));//单位秒

        holder.tv_message_time.setText(timeString);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.itemListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        } else {
            return list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_message_type,tv_message_name,tv_message_content,tv_message_time;

        public MyHolder(View itemView) {
            super(itemView);
            tv_message_type = itemView.findViewById(R.id.tv_message_type);
            tv_message_name = itemView.findViewById(R.id.tv_message_name);
            tv_message_content = itemView.findViewById(R.id.tv_message_content);
            tv_message_time = itemView.findViewById(R.id.tv_message_time);
        }
    }

    public FindAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemListener(int position);
    }
}

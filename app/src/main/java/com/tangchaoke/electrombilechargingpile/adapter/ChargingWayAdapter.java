package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/22.
 */

public class ChargingWayAdapter extends RecyclerView.Adapter<ChargingWayAdapter.Charway> {

    private List<Map<String, Object>> list;
    private Context context;

    public ChargingWayAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Charway onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charway, parent, false);
        return new Charway(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Charway holder, final int position) {
        String state = (String) list.get(position).get("state");

        holder.way_pw.setText((String)list.get(position).get("chargingmode"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemListener(position);
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

    class Charway extends RecyclerView.ViewHolder{

        private TextView way_pw;
        LinearLayout powway;

        public Charway(View itemView) {
            super(itemView);
            way_pw=itemView.findViewById(R.id.way_pw);
            powway=itemView.findViewById(R.id.powway);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemListener(int position);
    }
}

package com.tangchaoke.electrombilechargingpile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.view.RatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2018/5/8.
 */

public class ChargingEvaluationListAdapter extends RecyclerView.Adapter<ChargingEvaluationListAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;
//    private List<String> listImg = new ArrayList<>();

    public ChargingEvaluationListAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charging_evaluation_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.ic_figure_head)
                .placeholder(R.mipmap.ic_figure_head))
                .load(UriUtil.ip+list.get(position).get("headimage")+"")
                .into(holder.head);

        holder.name.setText(list.get(position).get("nickname")==null?"":list.get(position).get("nickname").toString());
        holder.stars.setStarCount(list.get(position).get("stars")==null?5:(int)list.get(position).get("stars"));
        holder.stars.setStar(list.get(position).get("stars")==null?5:(int)list.get(position).get("stars"));
        holder.ctime.setText(list.get(position).get("ctime")+"");
        holder.content.setText(list.get(position).get("content")+"");

        EvaluationImgAdapter adapter = new EvaluationImgAdapter((ArrayList<String>)list.get(position).get("imageUrl"), context);
        holder.rv_item_evaluation_img.setAdapter(adapter);
        adapter.setOnImgItemClickListener(new EvaluationImgAdapter.OnImgItemClickListener() {
            @Override
            public void onImgItemClick(int positions) {
                onImgItemClickListener.onImgItemClick(position,positions);
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
        NiceRecyclerView rv_item_evaluation_img;
        ImageView head;
        RatingBar stars;
        TextView name;
        TextView ctime;
        TextView content;

        MyViewHolder(View itemView) {
            super(itemView);
            rv_item_evaluation_img = itemView.findViewById(R.id.rv_item_evaluation_img);
            head = itemView.findViewById(R.id.head);
            stars = itemView.findViewById(R.id.stars);
            name = itemView.findViewById(R.id.name);
            ctime = itemView.findViewById(R.id.ctime);
            content = itemView.findViewById(R.id.content);
        }
    }

    private OnImgItemClickListener onImgItemClickListener;

    public interface OnImgItemClickListener {
        void onImgItemClick(int position,int positions);
    }

    public void setOnImgItemClickListener(OnImgItemClickListener onImgItemClickListener) {
        this.onImgItemClickListener = onImgItemClickListener;
    }
}

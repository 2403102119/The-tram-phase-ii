package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;

import java.util.List;

/**
 * kylin on 2018/5/8.
 */

public class EvaluationImgAdapter extends RecyclerView.Adapter<EvaluationImgAdapter.MyViewHolder> {
    private List<String> list;
    private Context context;

    public EvaluationImgAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluation_img, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String url = UriUtil.ip+list.get(position);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.ic_figure_evaluation)
                .placeholder(R.mipmap.ic_figure_evaluation))
                .load(url)
                .into(holder.item_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImgItemClickListener.onImgItemClick(position);
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
        ImageView item_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
        }
    }

    private OnImgItemClickListener onImgItemClickListener;

    public interface OnImgItemClickListener {
        void onImgItemClick(int position);
    }

    public void setOnImgItemClickListener(OnImgItemClickListener onImgItemClickListener) {
        this.onImgItemClickListener = onImgItemClickListener;
    }
}

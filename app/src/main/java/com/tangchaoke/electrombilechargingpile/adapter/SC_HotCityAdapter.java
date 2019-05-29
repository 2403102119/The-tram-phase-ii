package com.tangchaoke.electrombilechargingpile.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.bean.City;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * kylin on 16/9/24.
 */
public class SC_HotCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<City> mCitys;
    private SC_CityRecyclerAdapter.OnCityClickListener onCityClickListener;

    public SC_HotCityAdapter(Context mContext, List<City> mCitys, SC_CityRecyclerAdapter.OnCityClickListener listener) {
        this.mContext = mContext;
        this.mCitys = mCitys == null ? new ArrayList<City>() : mCitys;
        this.onCityClickListener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_sc_hot_city_gridview, parent, false);
        return new HotCityItemHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((HotCityItemHolder) holder).mTvHotCityName.setText(StringUtil.cutStr(mCitys.get(position).name,5));

        ((HotCityItemHolder) holder).mTvHotCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回定位城市
                if (onCityClickListener != null){
                    onCityClickListener.onCityClick(((TextView)view).getText().toString(),mCitys.get(position).lat,mCitys.get(position).lng);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCitys.size();
    }

    class HotCityItemHolder extends RecyclerView.ViewHolder {


        public TextView mTvHotCityName;

        public HotCityItemHolder(View itemView) {
            super(itemView);
            mTvHotCityName = (TextView) itemView.findViewById(R.id.tv_hot_city_name);

        }
    }


}

package com.tangchaoke.electrombilechargingpile.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2018/5/4.
 */

public class IndexListAdapter extends RecyclerView.Adapter<IndexListAdapter.MyViewHolder> {
    private List<Map<String, Object>> list;
    private Activity context;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double latitude,longitude;

    public IndexListAdapter(List<Map<String, Object>> list, Activity context, double latitude, double longitude) {
        this.list = list;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_index_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n,RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        DecimalFormat fnum = new DecimalFormat("##0.00");

        String url = list.get(position).get("image")+"";
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.ic_figure_index)
                .placeholder(R.mipmap.ic_figure_index))
                .load(url)
                .into(holder.item_img);
        holder.title.setText(list.get(position).get("title")+"");
        holder.address.setText(list.get(position).get("address")+"");
//        holder.juli.setText(AMapUtil.getFriendlyLength(Integer.parseInt(list.get(position).get("title")+"")));

        /*DistanceSearch distanceSearch = new DistanceSearch(context);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                int length;
                if (i==1000&&distanceResult.getDistanceResults().size()>0){
                    length=(int)(distanceResult.getDistanceResults().get(0).getDistance());
                }else {
                    //toToast("没有计算出驾车距离,已使用直线距离");
                    length =list.get(position).get("distance")!=null?((int)list.get(position).get("distance")):0;
                }
                holder.juli.setText(AMapUtil.getFriendlyLength(length));
            }
        });*/
        holder.juli.setText(AMapUtil.getFriendlyLength((int)((double)list.get(position).get("distance"))));
        LatLonPoint start = new LatLonPoint(latitude, longitude);
        double dlat=list.get(position).get("lat")!=null?(Double.parseDouble((String)list.get(position).get("lat"))):0;
        double dlng=list.get(position).get("lng")!=null?(Double.parseDouble((String)list.get(position).get("lng"))):0;
        LatLonPoint start1 = new LatLonPoint(dlat, dlng);
        //设置起点和终点，其中起点支持多个
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        latLonPoints.add(start);
        DistanceSearch.DistanceQuery distanceQuery=new DistanceSearch.DistanceQuery();
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(start1);
        //设置测量方式，支持直线和驾车
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
//        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
//        int length;
//        try {
//            if (distanceResult!=null&&distanceResult.getDistanceResults()!=null&&distanceResult.getDistanceResults().size()>0){
//
//            }else {
//                //toToast("没有计算出驾车距离,已使用直线距离");
//                length =list.get(position).get("distance")!=null?((int)list.get(position).get("distance")):0;
//            }
//            holder.juli.setText(AMapUtil.getFriendlyLength(length));
//        } catch (AMapException e) {
//            e.printStackTrace();
//        }


        holder.price.setText("充电单价："+fnum.format((double)list.get(position).get("powerRange"))+"元/时");
//        holder.parking.setText("车位空闲状态："+list.get(position).get("parkNum")+"");
//        holder.parkdingPrice.setText("停车费："+list.get(position).get("ParkAmount")+"");
//        holder.parkingTime.setText("开放时间段："+list.get(position).get("businessHours")+"小时");
        holder.parking.setText("开放时间段："+list.get(position).get("businessHours")+"小时");
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
        TextView title;
        TextView address;
        TextView juli;
        TextView price;
        TextView parking;
        TextView parkdingPrice;
        TextView parkingTime;

        MyViewHolder(View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            juli = itemView.findViewById(R.id.juli);
            price = itemView.findViewById(R.id.price);
            parking = itemView.findViewById(R.id.parking);
            parkdingPrice = itemView.findViewById(R.id.parkdingPrice);
            parkingTime = itemView.findViewById(R.id.parkingTime);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

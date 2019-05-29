package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.ChargingEvaluationListAdapter;
import com.tangchaoke.electrombilechargingpile.adapter.ChargingPileListAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.PakageUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.view.RatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 电桩详情
 */
public class ChargingPileDetailsActivity extends BaseActivity {
    private Bean.PileDetails model;

    private NiceRecyclerView rv_charging_pile_list, rv_charging_evaluation_list;
    private List<Map<String, Object>> pileList = new ArrayList<>();
    private List<Map<String, Object>> evaluationList = new ArrayList<>();
    private ImageView iv_details_img;
    private LinearLayout ll_evaluation;
    private LinearLayout ll_canyin, ll_zhusu, ll_charging_cost;

    private TextView titleName;
    private RatingBar xingji;
    private TextView countState;
    private TextView address;
    private TextView juli;
    private LinearLayout navigat;
    private TextView price;
    private TextView openTime;
    private TextView phoneStr;
    private TextView allCount;
    private TextView evaCount;
    private ChargingPileListAdapter adapterPileList;
    private ChargingEvaluationListAdapter adapterEvaluationList;

    private String oid;
    private double lat;
    private double lng;

    View item_dialog_map;
    TextView canclePic;
    Dialog showMapDialog;
    private TextView gaode_map,baidu_map,guge_map;

    @SuppressLint("InflateParams")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_chargingpile_details);
        setTopTitle("电站详情");

        iv_details_img = findViewById(R.id.iv_details_img);
        ll_canyin = findViewById(R.id.ll_canyin);
        ll_zhusu = findViewById(R.id.ll_zhusu);
        ll_charging_cost = findViewById(R.id.ll_charging_cost);

        titleName = findViewById(R.id.titleName);
        xingji = findViewById(R.id.xingji);
        countState = findViewById(R.id.countState);
        address = findViewById(R.id.address);
        juli = findViewById(R.id.juli);
        navigat = findViewById(R.id.navigat);
        price = findViewById(R.id.price);
        openTime = findViewById(R.id.OpenTime);
        phoneStr = findViewById(R.id.phoneStr);
        allCount = findViewById(R.id.allCount);
        ll_evaluation = findViewById(R.id.ll_evaluation);
        evaCount = findViewById(R.id.evaCount);

        rv_charging_pile_list = findViewById(R.id.rv_charging_pile_list);
        adapterPileList = new ChargingPileListAdapter(pileList, this);
        rv_charging_pile_list.setAdapter(adapterPileList);

        rv_charging_evaluation_list = findViewById(R.id.rv_charging_evaluation_list);
        adapterEvaluationList = new ChargingEvaluationListAdapter(evaluationList, this);
        rv_charging_evaluation_list.setAdapter(adapterEvaluationList);

        //地图弹窗开始
        item_dialog_map = LayoutInflater.from(this).inflate(R.layout.item_dialog_map, null);
        canclePic = item_dialog_map.findViewById(R.id.canclePic);

        guge_map = item_dialog_map.findViewById(R.id.guge_map);
        gaode_map = item_dialog_map.findViewById(R.id.gaode_map);
        baidu_map = item_dialog_map.findViewById(R.id.baidu_map);
        checkMaps();

        showMapDialog = new Dialog(this, R.style.processDialog);
        showMapDialog.setContentView(item_dialog_map);
        showMapDialog.setCanceledOnTouchOutside(true);
        Window window = showMapDialog.getWindow();
        //底部弹出
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //地图弹窗结束
    }

    @Override
    protected void initListener() {
        ll_evaluation.setOnClickListener(this);
        ll_canyin.setOnClickListener(this);
        ll_zhusu.setOnClickListener(this);
        ll_charging_cost.setOnClickListener(this);
        navigat.setOnClickListener(this);
        guge_map.setOnClickListener(this);
        gaode_map.setOnClickListener(this);
        baidu_map.setOnClickListener(this);
        canclePic.setOnClickListener(this);

        adapterEvaluationList.setOnImgItemClickListener(new ChargingEvaluationListAdapter.OnImgItemClickListener() {
            @Override
            public void onImgItemClick(int position,int positions) {
                /*Intent intent=new Intent(ChargingPileDetailsActivity.this,SBI_ViewPagerSampleActivity.class);
                intent.putStringArrayListExtra("listImg",model.evaluateList.get(position).imageUrl);
                intent.putExtra("position",positions);
                startActivity(intent);*/

            }
        });
//        adapterPileList.setOnItemClickListener(new ChargingPileListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent=new Intent(ChargingPileDetailsActivity.this,ChargingDetailsActivity.class);
//                intent.putExtra("toFragment",pileList.get(position).get("identity")+"");
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void initData() {
        oid=getIntent().getStringExtra("oid");
        lat=getIntent().getDoubleExtra("lat",0);
        lng=getIntent().getDoubleExtra("lng",0);
        getElectricPileDetail(lat,lng,oid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_evaluation://查看用户评价
                Intent intent=new Intent(ChargingPileDetailsActivity.this, AllEvaluationActivity.class);
                intent.putExtra("oid",oid);
                startActivity(intent);
                break;
            case R.id.ll_canyin://周边餐饮
                startActivity(new Intent(ChargingPileDetailsActivity.this, DevelopingActivity.class));
                break;
            case R.id.ll_zhusu://周边住宿
                startActivity(new Intent(ChargingPileDetailsActivity.this, DevelopingActivity.class));
                break;
            case R.id.navigat://地图导航
                checkMaps();
                showMapDialog.show();
                break;
            case R.id.guge_map://谷歌地图
                showMapDialog.dismiss();
                Uri gmmIntentUri = Uri.parse("google.navigation:q="
                        + model.lat + "," + model.lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                        gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.gaode_map://高德地图
                showMapDialog.dismiss();
                intent=new Intent();
                try {
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    gmmIntentUri = Uri.parse("amapuri://route/plan/?slat="+ App.lat +"&slon="+ App.lng +"&sname=我的位置"+
                            "&dlat="+model.lat+"&dlon="+model.lng+"&dname="+model.title+"&dev=0&t=0");
                    intent.setData(gmmIntentUri);
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("地图启动失败");
                }
                break;
            case R.id.baidu_map://百度地图
                showMapDialog.dismiss();
                Intent i1 = new Intent();
                // 驾车导航
                i1.setData(Uri.parse("baidumap://map/navi?location="+model.lat+","+model.lng));
                startActivity(i1);
                break;
            case R.id.canclePic://取消地图导航
                showMapDialog.dismiss();
                break;
            case R.id.ll_charging_cost:
                if (App.islogin) {
                    Intent intent1 = new Intent(this, ChargingStandardActivity.class);
                    intent1.putExtra("oid", oid);
                    startActivity(intent1);
                }else {
                    startActivity(new Intent(ChargingPileDetailsActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    /**搜索充电桩列表*/
    private void getElectricPileDetail(final double lat, final double lng,final String oid){
        if (NetUtil.isNetWorking(ChargingPileDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getElectricPileDetail(lat+"",lng+"",oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.PileDetailsModel pileDetailsModel =new Gson().fromJson(result, Bean.PileDetailsModel.class);
                            if (pileDetailsModel.status==1){
                                //基本数据
                                model=pileDetailsModel.model;
                                Glide.with(ChargingPileDetailsActivity.this).applyDefaultRequestOptions(new RequestOptions()
                                        .error(R.mipmap.ic_figure_details)
                                        .placeholder(R.mipmap.ic_figure_details))
                                        .load(pileDetailsModel.model.chargeimageUrl+"")
                                        .into(iv_details_img);
                                titleName.setText(pileDetailsModel.model.title+"");
//                                xingji.setStar(pileDetailsModel.model.allstar);
                                countState.setText("空闲"+pileDetailsModel.model.socketLeisurenum+"/共"+pileDetailsModel.model.socketNum);
                                address.setText(pileDetailsModel.model.address+"");
                                openTime.setText(model.businessStartDays+"至"+model.businessEndDays+" "+model.businessHours);
                                juli.setText("距离我"+AMapUtil.getFriendlyLength((int)pileDetailsModel.model.distance));
//                                AMapUtil.calculateLength("距离我",ChargingPileDetailsActivity.this,0,lat,lng,pileDetailsModel.model.lat,pileDetailsModel.model.lng,juli);
                                price.setText(StringUtil.doubleToString(pileDetailsModel.model.powerRange)+"元/时");
                                phoneStr.setText(pileDetailsModel.model.servicePhone+"");
                                allCount.setText("充电桩（"+pileDetailsModel.model.PowerInfoList.size()+"）");
//                                evaCount.setText("用户评价（"+pileDetailsModel.model.num+"）");
                                //充电桩列表数据
                                for (int i = 0; i < pileDetailsModel.model.PowerInfoList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid",pileDetailsModel.model.PowerInfoList.get(i).oid);
                                    map.put("identity",pileDetailsModel.model.PowerInfoList.get(i).identity);
                                    map.put("socketNum",pileDetailsModel.model.PowerInfoList.get(i).socketNum);
                                    map.put("rechargePort",pileDetailsModel.model.PowerInfoList.get(i).rechargePort);
                                    map.put("power",pileDetailsModel.model.PowerInfoList.get(i).power);
                                    map.put("voltage",pileDetailsModel.model.PowerInfoList.get(i).voltage);
                                    map.put("socketUsenum",pileDetailsModel.model.PowerInfoList.get(i).socketUsenum);
                                    map.put("location",pileDetailsModel.model.PowerInfoList.get(i).location);
                                    map.put("socketLeisurenum",pileDetailsModel.model.PowerInfoList.get(i).socketLeisurenum);
                                    map.put("socketAbnormalnum",pileDetailsModel.model.PowerInfoList.get(i).socketAbnormalnum);
                                    pileList.add(map);
                                }
                                adapterPileList.notifyDataSetChanged();

                                //评价数据
                              /*  for (int i = 0; i < pileDetailsModel.model.evaluateList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", pileDetailsModel.model.evaluateList.get(i).oid);
                                    map.put("headimage", pileDetailsModel.model.evaluateList.get(i).headimage);
                                    map.put("nickname", pileDetailsModel.model.evaluateList.get(i).nickname);
                                    map.put("ctime", pileDetailsModel.model.evaluateList.get(i).ctime);
                                    map.put("stars", pileDetailsModel.model.evaluateList.get(i).stars);
                                    map.put("content", pileDetailsModel.model.evaluateList.get(i).content);
                                    map.put("imageUrl", pileDetailsModel.model.evaluateList.get(i).imageUrl);
                                    evaluationList.add(map);
                                }
                                adapterEvaluationList.notifyDataSetChanged();*/
                            }else {
                                toast(pileDetailsModel.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }



    private void checkMaps() {
        //判断地图是否存在
        if (PakageUtil.isAvilible(this,"com.autonavi.minimap")){
            gaode_map.setVisibility(View.VISIBLE);
        }else {
            gaode_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(this,"com.baidu.BaiduMap")){
            baidu_map.setVisibility(View.VISIBLE);
        }else {
            baidu_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(this,"com.google.android.apps.maps")){
            guge_map.setVisibility(View.VISIBLE);
        }else {
            guge_map.setVisibility(View.GONE);
        }
    }
}

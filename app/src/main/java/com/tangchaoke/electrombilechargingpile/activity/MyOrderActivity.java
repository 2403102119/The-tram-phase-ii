package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.MyOrderAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.Intents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity {
    private NiceRecyclerView rv_myOrder;
    private List<Map<String, Object>> list = new ArrayList<>();
    private LinearLayout ll_empty;
    private MyOrderAdapter adapter;
    private int index=0,index2=15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_order);
        setTopTitle("我的订单");

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);

        rv_myOrder = findViewById(R.id.rv_myOrder);
        ll_empty = findViewById(R.id.ll_empty);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        adapter = new MyOrderAdapter(list, this);
        rv_myOrder.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyOrderAdapter.OnItemClickListener(){
            @Override
            public void OnItemClickListener(int position) {
                if ("0".equals(list.get(position).get("oederState"))){//
                    Intent intent=new Intent(MyOrderActivity.this,ChargingConductActivity.class);
                    intent.putExtra("oid",list.get(position).get("oid")+"");
                    intent.putExtra("type","");
                    intent.putExtra("chargingmode","");
                    startActivityForResult(intent, 0x90);
                }else if ("1".equals(list.get(position).get("oederState"))||
                        "2".equals(list.get(position).get("oederState"))||
                        "3".equals(list.get(position).get("oederState"))){
                    Intent intent=new Intent(MyOrderActivity.this,OrderDetailActivity.class);
                    intent.putExtra("oid",(String) list.get(position).get("oid"));
                    startActivityForResult(intent, 11111);
                }
            }
        });
        Map<String,Object> map=new HashMap<>();
        map.put("page",index);
        map.put("size",index2);
        String json=StringUtil.map2Json(map);
        getOrderList(json);

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index=0;
                index2=15;

                Map<String,Object> map=new HashMap<>();
                map.put("page",index);
                map.put("size",index2);
                String json=StringUtil.map2Json(map);
                getOrderList(json);
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index=index+15;
                index2=index2+15;
                Map<String,Object> map=new HashMap<>();
                map.put("page",index);
                map.put("size",index2);
                String json=StringUtil.map2Json(map);
                getOrderList(json);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        index=0;
        index2=15;
        Map<String,Object> map=new HashMap<>();
        map.put("page",index);
        map.put("size",index2);
        String json=StringUtil.map2Json(map);
        getOrderList(json);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (11111 == requestCode && 22222 == resultCode){
            index = 0;
            index2 = 15;
            Map<String,Object> map=new HashMap<>();
            map.put("page",index);
            map.put("size",index2);
            String json=StringUtil.map2Json(map);
            getOrderList(json);
        }

        if (0x90 == requestCode && 225 == resultCode){
            index = 0;
            index2 = 15;
            Map<String,Object>map=new HashMap<>();
            map.put("page",index);
            map.put("size",index2);
            String json= StringUtil.map2Json(map);
            getOrderList(json);
        }
    }

    /*
    获取订单列表
    */
    private void getOrderList(final String json) {
        if (NetUtil.isNetWorking(MyOrderActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getOrderList(json, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            Bean.getOrderList data=new Gson().fromJson(result,Bean.getOrderList.class);
                            if (1==data.status){
                                List<Bean.Order> Order=data.list;
                                for (int i=0;i<Order.size();i++){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", Order.get(i).oid);
                                    map.put("orderTime", Order.get(i).orderTime);
                                    map.put("chargePlace", Order.get(i).chargePlace);
                                    map.put("identity", Order.get(i).identity);
                                    map.put("socket", Order.get(i).socket);
                                    map.put("oederState", Order.get(i).oederState);
                                    map.put("cost", Order.get(i).cost);
                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();
                            }else {
                                toast(data.message);
                            }
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }
    }
}

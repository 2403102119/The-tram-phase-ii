package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.CouponAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 优惠券
 */
public class CouponActivity extends BaseActivity {
    private NiceRecyclerView rv_coupon;
    private List<Map<String, Object>> list = new ArrayList<>();
    private LinearLayout ll_empty;

    private ImageView biaoshi;
    private CouponAdapter adapter;
    private TextView no_use;
    private String selectOid ="";
    private boolean isFromPay = false;

    private int index1 = 0,index2 = 15, num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_coupon);
        title.setText("优惠券");

        rv_coupon = findViewById(R.id.rv_coupon);
        biaoshi=findViewById(R.id.biaoshi);
        no_use=findViewById(R.id.no_use);
        ll_empty = findViewById(R.id.ll_empty);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        if(null!=getIntent()){
            selectOid =getIntent().getStringExtra("where");
            if ("mefragment".equals(getIntent().getStringExtra("from_activity"))){
                isFromPay = false;
                no_use.setVisibility(View.GONE);
            }else if ("pay_ac".equals(getIntent().getStringExtra("from_activity"))){
                isFromPay = true;
                no_use.setVisibility(View.VISIBLE);
            }
//            if ("1".equals(selectOid)){
//                no_use.setVisibility(View.GONE);
//            }else {
//                no_use.setVisibility(View.VISIBLE);
//            }
        }


        adapter = new CouponAdapter(list, this);
        rv_coupon.setAdapter(adapter);


        adapter.setOnItemClickListener(new CouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isFromPay) {
                    if ("0".equals(list.get(position).get("state"))) {
                        Intent intent = getIntent();
                        intent.putExtra("coupon_oid", (String) list.get(position).get("oid"));
                        intent.putExtra("coupon_cost", list.get(position).get("subtractMoney") + "");
                        setResult(10, intent);
                        finish();
                    }
                }
            }
        });

        no_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                intent.putExtra("oid2","");
                setResult(10,intent);
                finish();
            }
        });

        if (App.islogin) {
            getCheck(App.token, index1 + "", index2 + "");
        }else {
            startActivity(new Intent(CouponActivity.this, LoginActivity.class));
        }

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index1 = 0;
                index2 = 15;
                if (App.islogin) {
                    getCheck(App.token, index1 + "", index2 + "");
                }else {
                    startActivity(new Intent(CouponActivity.this, LoginActivity.class));
                }
            }
        });
        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index1 += num;
                index2 += num;
                if (App.islogin) {
                    getCheck(App.token, index1 + "", index2 + "");
                }else {
                    startActivity(new Intent(CouponActivity.this, LoginActivity.class));
                }
            }
        });
    }


    /*
    获取优惠券列表
    */
    private void getCheck(final String token, final String index,final String num){
        if (NetUtil.isNetWorking(CouponActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.Coupon(token, index,num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("ok",result);

                            Bean.CouponAll data = new Gson().fromJson(result, Bean.CouponAll.class);
                            if (1 == data.status){
                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                List<Bean.Coupon> couponList = data.list;
                                for (int i = 0; i < couponList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid",couponList.get(i).oid);
                                    map.put("cutOffTime", couponList.get(i).cutOffTime);
                                    map.put("state", couponList.get(i).state);
                                    map.put("subtractMoney", couponList.get(i).subtractMoney);
                                    map.put("text", couponList.get(i).text);
                                    if (selectOid!=null&&selectOid.equals(couponList.get(i).oid)){
                                        map.put("click", true);
                                    }else {
                                        map.put("click", false);
                                    }


                                    list.add(map);
                                }
                                if (0 == list.size()){
                                    no_use.setVisibility(View.GONE);
                                    ll_empty.setVisibility(View.VISIBLE);
                                }else {
                                    ll_empty.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                            }else {
                                toast(data.message);
                            }
                            finishRefresh();

                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                        }
                    });
                }
            });
        }
    }
}

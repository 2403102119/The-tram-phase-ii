package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.RechargeDetailAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.bean.Defian;
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
 * Title：账户明细页面
 * Author:李迪迦
 * Date：2018.5.17
 */
public class AccountDetailsActivity extends BaseActivity {
    private NiceRecyclerView rv_account_details;
    private List<Map<String, Object>> list = new ArrayList<>();
    private RechargeDetailAdapter adapter;
    private LinearLayout ll_empty;

    private int index1 = 0, index2 = 15, num = 15;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_account_details);
        setTopTitle("账户明细");

        rv_account_details = findViewById(R.id.rv_account_details);
        ll_empty = findViewById(R.id.ll_empty);

        mySmart.setEnableLoadmore(true);
        mySmart.setEnableRefresh(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        adapter = new RechargeDetailAdapter(this, list);
        rv_account_details.setAdapter(adapter);

        getCheck( String.valueOf(index1), index2 + "");

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index1 = 0;
                index2 = 15;
                getCheck(String.valueOf(index1), index2 + "");
            }
        });
        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index1 += num;
                index2 += num;
                getCheck( String.valueOf(index1), index2 + "");
            }
        });
    }


    /*
     App14<<账户明细 >
     */
    private void getCheck( final String index, final String num){
        if (NetUtil.isNetWorking(AccountDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.zhanghumingxi(index, num, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);

                            Defian.AccountAll data=new Gson().fromJson(result,Defian.AccountAll.class);
                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                List<Defian.content> accounts=data.content;
                                for (int i=0;i<accounts.size();i++){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("type", accounts.get(i).type);
                                    map.put("money", accounts.get(i).money);
                                    map.put("time", accounts.get(i).paymentTime);
                                    map.put("paymentMethod", accounts.get(i).paymentMethod);
                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();

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

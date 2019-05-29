package com.tangchaoke.electrombilechargingpile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.RechargeDetailAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReChargeDetailActivity extends BaseActivity {
    private NiceRecyclerView nrv_recharge_detail;
    private List<Map<String, Object>> list = new ArrayList<>();
    private RechargeDetailAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_re_charge_detail);

        title.setText("充值明细");
        nrv_recharge_detail = findViewById(R.id.nrv_recharge_detail);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        list.add(new HashMap<String, Object>());
        list.add(new HashMap<String, Object>());
        list.add(new HashMap<String, Object>());

        adapter = new RechargeDetailAdapter(ReChargeDetailActivity.this, list);
        nrv_recharge_detail.setAdapter(adapter);
    }
}

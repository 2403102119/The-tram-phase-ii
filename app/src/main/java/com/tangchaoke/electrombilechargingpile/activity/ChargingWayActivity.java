package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.ChargingWayAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 充电方式
 */
public class ChargingWayActivity extends BaseActivity {

    private NiceRecyclerView wa_power;
    private List<Map<String, Object>> list = new ArrayList<>();
    private ChargingWayAdapter adapter;
    private String identity;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_charging_way);
        setTopTitle("充电方式");

        wa_power=findViewById(R.id.wa_power);


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        if (null != getIntent()) {

            identity=(getIntent().getStringExtra("identity"));//充电编号
            list= (List<Map<String, Object>>) getIntent().getSerializableExtra("tonum");
        }

        adapter = new ChargingWayAdapter(list, this);
        wa_power.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChargingWayAdapter.OnItemClickListener() {
            @Override
            public void onItemListener(int position) {
                Intent intent=new Intent();
                intent.putExtra("way",(String) list.get(position).get("chargingmode"));
                intent.putExtra("toFragment",identity);
                setResult(5,intent);
                finish();
            }
        });

    }




}

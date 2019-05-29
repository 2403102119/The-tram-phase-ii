package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;

public class ChargingEndActivity extends BaseActivity {
    private TextView balance_pay,sta_time,end_time,dushu;
    private String startDate,endDate;
    private float electric,cost;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_payend);
        title.setText("充电完成");

        balance_pay=findViewById(R.id.balance_pay);
        sta_time=findViewById(R.id.sta_time);
        end_time=findViewById(R.id.end_time);
        dushu=findViewById(R.id.dushu);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        if (null!=getIntent()){
            startDate=getIntent().getStringExtra("startDate");
            endDate=getIntent().getStringExtra("endDate");
            electric=getIntent().getFloatExtra("electric",0);
            cost=getIntent().getFloatExtra("cost",0);
        }

        balance_pay.setText(cost+"元");
        sta_time.setText("开始时间："+startDate);
        end_time.setText("结束时间："+endDate);
        dushu.setText("充电时长："+electric+"度");

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
        }
}

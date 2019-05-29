package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;

/**
 * 立即充值
 */
public class RechargeImmediatelyActivity extends BaseActivity {
    private Button but_ok;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_recharge_immediately);
        setTopTitle("立即充值");

        but_ok = findViewById(R.id.but_ok);
    }

    @Override
    protected void initListener() {
        but_ok.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_ok://确定
                finish();
                break;
        }
    }
}

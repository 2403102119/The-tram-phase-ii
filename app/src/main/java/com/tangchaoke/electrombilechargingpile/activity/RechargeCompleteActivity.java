package com.tangchaoke.electrombilechargingpile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
/*
充值完成界面
 */
public class RechargeCompleteActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_recharge_complete);

        title.setText("充值成功");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}

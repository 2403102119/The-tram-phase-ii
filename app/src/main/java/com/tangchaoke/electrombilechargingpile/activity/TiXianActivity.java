package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;

/**
 * 提现
 */
public class TiXianActivity extends BaseActivity {
    private CheckBox cb_weixin, cb_zhifubao, cb_dianebao;
    private LinearLayout ll_weixin, ll_zhifubao, ll_dianebao;
    private Button but_submit;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_ti_xian);
        setTopTitle("提现");

        ll_weixin = findViewById(R.id.ll_weixin);
        ll_zhifubao = findViewById(R.id.ll_zhifubao);
        ll_dianebao = findViewById(R.id.ll_dianebao);
        cb_weixin = findViewById(R.id.cb_weixin);
        cb_zhifubao = findViewById(R.id.cb_zhifubao);
        cb_dianebao = findViewById(R.id.cb_dianebao);
        but_submit = findViewById(R.id.but_submit);
    }

    @Override
    protected void initListener() {
        ll_weixin.setOnClickListener(this);
        ll_zhifubao.setOnClickListener(this);
        ll_dianebao.setOnClickListener(this);
        but_submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_weixin:
                cb_weixin.setChecked(true);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_zhifubao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(true);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_dianebao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(true);
                break;
            case R.id.but_submit://提现
                startActivity(new Intent(TiXianActivity.this, TiXianSubmitActivity.class));
                finish();
                break;
        }
    }
}

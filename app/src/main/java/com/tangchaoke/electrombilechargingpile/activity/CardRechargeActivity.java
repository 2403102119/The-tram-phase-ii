package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;

/**
 * 充值
 */
public class CardRechargeActivity extends BaseActivity {
    private CheckBox cb_100, cb_200, cb_300, cb_500, cb_800, cb_1000;
    private CheckBox cb_weixin, cb_zhifubao, cb_dianebao;
    private LinearLayout ll_weixin, ll_zhifubao, ll_dianebao;
    private ClearEditText et_money_num;
    private Button but_ok;
    private String money;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_card_recharge);
        setTopTitle("卡充值");

        cb_100 = findViewById(R.id.cb_100);
        cb_200 = findViewById(R.id.cb_200);
        cb_300 = findViewById(R.id.cb_300);
        cb_500 = findViewById(R.id.cb_500);
        cb_800 = findViewById(R.id.cb_800);
        cb_1000 = findViewById(R.id.cb_1000);
        ll_weixin = findViewById(R.id.ll_weixin);
        ll_zhifubao = findViewById(R.id.ll_zhifubao);
        ll_dianebao = findViewById(R.id.ll_dianebao);
        et_money_num = findViewById(R.id.et_money_num);
        cb_weixin = findViewById(R.id.cb_weixin);
        cb_zhifubao = findViewById(R.id.cb_zhifubao);
        cb_dianebao = findViewById(R.id.cb_dianebao);
        but_ok = findViewById(R.id.but_ok);

        cb_weixin.setChecked(true);
    }

    @Override
    protected void initListener() {
        cb_100.setOnClickListener(this);
        cb_200.setOnClickListener(this);
        cb_300.setOnClickListener(this);
        cb_500.setOnClickListener(this);
        cb_800.setOnClickListener(this);
        cb_1000.setOnClickListener(this);
        ll_weixin.setOnClickListener(this);
        ll_zhifubao.setOnClickListener(this);
        ll_dianebao.setOnClickListener(this);
        but_ok.setOnClickListener(this);
        back.setOnClickListener(this);
        cb_weixin.setOnClickListener(this);
        cb_zhifubao.setOnClickListener(this);
        cb_dianebao.setOnClickListener(this);

        et_money_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money = "";
                shaxin();
                et_money_num.setFocusable(true);
                et_money_num.setFocusableInTouchMode(true);
                et_money_num.requestFocus();
                et_money_num.findFocus();
                InputMethodManager inputManager = (InputMethodManager)et_money_num.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_money_num, 0);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_100:
                shaxin();
//                money = cb_100.getText().toString();
                money = "100";
                cb_100.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_200:
                shaxin();
//                money = cb_200.getText().toString();
                money = "200";
                cb_200.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_300:
                shaxin();
//                money = cb_300.getText().toString();
                money = "300";
                cb_300.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_500:
                shaxin();
//                money = cb_500.getText().toString();
                money = "500";
                cb_500.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_800:
                shaxin();
//                money = cb_800.getText().toString();
                money = "800";
                cb_800.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_1000:
                shaxin();
//                money = cb_1000.getText().toString();
                money = "1000";
                cb_1000.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.ll_weixin:
            case R.id.cb_weixin:
                cb_weixin.setChecked(true);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_zhifubao:
            case R.id.cb_zhifubao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(true);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_dianebao:
            case R.id.cb_dianebao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(true);
                break;
            case R.id.but_ok://立即充值
                if (StringUtil.isSpace(money)){
                    money = et_money_num.getText().toString().trim();
                    if (StringUtil.isSpace(money)){
                        toast("请输入充值的金额");
                        break;
                    }
                }

                toast(money);

//                startActivity(new Intent(RechargeActivity.this, RechargeImmediatelyActivity.class));
//                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void shaxin() {
        cb_100.setChecked(false);
        cb_200.setChecked(false);
        cb_300.setChecked(false);
        cb_500.setChecked(false);
        cb_800.setChecked(false);
        cb_1000.setChecked(false);
        et_money_num.setFocusable(false);
    }
}

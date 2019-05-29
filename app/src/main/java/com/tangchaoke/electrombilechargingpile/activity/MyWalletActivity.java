package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import okhttp3.Call;

/*
* Title:余额充值界面
* Author：李迪迦
* Date：2019.5.16
* */
public class MyWalletActivity extends BaseActivity {
    private TextView tv_wallet_withdraw,tv_my_wallet_money;
    private Button btn_wallet_recharge;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_wallet);

        title.setText("钱包");
        rightTxt.setText("明细");
        rightTxt.setVisibility(View.VISIBLE);

        btn_wallet_recharge = findViewById(R.id.btn_wallet_recharge);
        tv_wallet_withdraw = findViewById(R.id.tv_wallet_withdraw);
        tv_my_wallet_money = findViewById(R.id.tv_my_wallet_money);

        tv_my_wallet_money.setText(StringUtil.doubleToString(App.balance));
    }

    @Override
    protected void initListener() {
        rightTxt.setOnClickListener(this);
        btn_wallet_recharge.setOnClickListener(this);
        tv_wallet_withdraw.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.islogin){
            getbalance();
        }else {
            startActivity(new Intent(MyWalletActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightTxt:
                if (App.islogin){
                    startActivity(new Intent(MyWalletActivity.this, AccountDetailsActivity.class));
                }else {
                    startActivity(new Intent(MyWalletActivity.this, LoginActivity.class));
                }
                break;
            case R.id.btn_wallet_recharge:
                if (App.islogin){
                    startActivity(new Intent(MyWalletActivity.this, RechargeActivity.class));
                }else {
                    startActivity(new Intent(MyWalletActivity.this, LoginActivity.class));
                }
                break;
            case R.id.tv_wallet_withdraw:
                if (App.islogin){
                    startActivity(new Intent(MyWalletActivity.this, TiXianActivity.class));
                }else {
                    startActivity(new Intent(MyWalletActivity.this, LoginActivity.class));
                }
                break;
        }
    }


    //获取用户余额
    private void getbalance(){
        if (NetUtil.isNetWorking(MyWalletActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getbalance(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.Init_data data=new Gson().fromJson(result,Bean.Init_data.class);

                                App.balance=data.balance;
                                SPUtil.saveData(MyWalletActivity.this,"balance",data.balance);

                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

}

package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.NetUtil;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;

import okhttp3.Call;

/*
* 计费标准
* */
public class ChargingStandardActivity extends BaseActivity {
    private TextView tv_level_one, tv_level_two, tv_level_three, tv_level_four, tv_level_five;
    private String oid;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_charging_standard);

        title.setText("计费标准");

        tv_level_one = findViewById(R.id.tv_level_one);
        tv_level_two = findViewById(R.id.tv_level_two);
        tv_level_three = findViewById(R.id.tv_level_three);
        tv_level_four = findViewById(R.id.tv_level_four);
        tv_level_five = findViewById(R.id.tv_level_five);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            oid = getIntent().getStringExtra("oid");
            getRates(oid);
        }
    }

    private void getRates(final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getRates(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.ChargingStandard data = new Gson().fromJson(result, Bean.ChargingStandard.class);

                            if (1 == data.status){
                                tv_level_one.setText(data.list.first);
                                tv_level_two.setText(data.list.second);
                                tv_level_three.setText(data.list.third);
                                tv_level_four.setText(data.list.fourth);
                                tv_level_five.setText(data.list.fifth);
                            }else {
                                toast(data.message);
                            }
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

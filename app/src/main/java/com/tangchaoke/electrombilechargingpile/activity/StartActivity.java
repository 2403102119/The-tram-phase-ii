package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tangchaoke.electrombilechargingpile.MainActivity;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;

public class StartActivity extends BaseActivity{
    private Handler handler = new Handler();


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_start);

        top.setVisibility(View.GONE);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            }
        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}

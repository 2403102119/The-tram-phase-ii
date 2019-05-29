package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
/*
重置密码界面
 */
public class ResetPasswordActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_reset_password);

        title.setText("重置密码");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}

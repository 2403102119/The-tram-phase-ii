package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.MainActivity;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private Button but_register, but_log;
    private TextView tv_ForgotPassword;
    private ClearEditText phone1;
    private ClearEditText psw1;

    private int type;


    private static final String TAG = "获取.成功";
    /*
    给用户设置别名
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    Log.i("11111111111", "gotResult: " + new JPushMessage().toString());
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    SPUtil.saveData(LoginActivity.this,"isBindAlias",true);

                    App.isBindAlias = true;

                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            toast(logs);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_login);
        title.setText("登录");

        but_register = findViewById(R.id.but_register);
        but_log = findViewById(R.id.but_log);
        tv_ForgotPassword = findViewById(R.id.tv_ForgotPassword);
        phone1 = findViewById(R.id.phone1);
        psw1=findViewById(R.id.psw1);

    }

    @Override
    protected void initListener() {
        but_register.setOnClickListener(this);
        but_log.setOnClickListener(this);
        tv_ForgotPassword.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()) {
            type = getIntent().getIntExtra("toFragment", 0);
        }
        phone1.setText(App.account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_register://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.but_log://登录
                String phoneStr = phone1.getText().toString();
                String password = psw1.getText().toString();

                if (StringUtil.isSpace(phoneStr)){
                    toast("请输入您的手机号");
                    break;
                }
                if (!StringUtil.isPhone(phoneStr)){
                    toast("手机号格式不正确");
                    break;
                }
                if (StringUtil.isSpace(password)){
                    toast("请输入密码");
                    break;
                }
                Map<String,Object> map=new HashMap<>();
                map.put("username",phoneStr);
                map.put("password",password);
                String json=StringUtil.map2Json(map);
                login(json);
                break;
            case R.id.tv_ForgotPassword://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.back:
                if (3 == type){
                    this.setResult(22222);
                }
                finish();
                break;
        }
    }


    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (3 == type){
            this.setResult(22222);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("login-onDestroy","login-onDestroy");
    }
    /*
     登录
     */
    private void login(final String json){
        if (NetUtil.isNetWorking(LoginActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.LoginMember(json, true, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.e("获取成功",result);


                            App.token = result;
                            //TODO
                            App.islogin = true;




                            SPUtil.saveData(LoginActivity.this, "Authorization", result);
                            SPUtil.saveData(LoginActivity.this, "islogin", true);
                            init_data();


                            //判断登录后页面显示

                            if (3 == type){
                                LoginActivity.this.setResult(33333);
                                LoginActivity.this.finish();
                            }
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            App.finishAllActivity();


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

    /*
    初始化数据
    */
    private void init_data() {
        if (NetUtil.isNetWorking(LoginActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUser(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Log.e("获取成功", result);

                            Bean.Init_data data = new Gson().fromJson(result, Bean.Init_data.class);

                                App.islogin = true;
                                App.loginMsg = data;
                                App.account = data.account;

                                // 调用 Handler 来异步设置别名
                                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, data.id));

                                SPUtil.saveData(LoginActivity.this, "account", data.account);
                                SPUtil.saveData(LoginActivity.this, "islogin", true);

                                SPUtil.saveBean2Sp(LoginActivity.this, data, "loginMsg", "loginMsg");







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

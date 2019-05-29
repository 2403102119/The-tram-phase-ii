package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.tangchaoke.electrombilechargingpile.util.SharedPreferencesUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 忘记密码
 */
public class ForgotPasswordActivity extends BaseActivity {

    private Button but_log;
    private ClearEditText phone2;
    private ClearEditText yzm2;
    private ClearEditText pws2;
    private TextView hqyzm;
    private String sign="花花猫";
    private String key;
    private String verificationCode1 = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_forgot_password);
        if (null != getIntent()){
            if ("me".equals(getIntent().getStringExtra("activity"))){
                title.setText("重置密码");
            }else {
                title.setText("找回密码");
            }
        }

        but_log = findViewById(R.id.but_log);
        phone2 = findViewById(R.id.phone2);
        yzm2 = findViewById(R.id.yzm2);
        pws2 = findViewById(R.id.pws2);
        hqyzm=findViewById(R.id.hqyzm);
    }

    @Override
    protected void initListener() {

        hqyzm.setOnClickListener(this);
        but_log.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if (App.islogin) {
            phone2.setText(App.account);
        }

    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.but_log:
                String yzmStr = yzm2.getText().toString().trim();
                String phoneStr = phone2.getText().toString().trim();
                String passwordStr = pws2.getText().toString();
                if (StringUtil.isSpace(phoneStr)) {
                    toast("请输入您的手机号");
                    return;
                }
                if (!StringUtil.isPhone(phoneStr)) {      //判断手机号格式是否正确
                    toast("手机号格式不正确");
                    break;
                }
                if (StringUtil.isSpace(passwordStr)) {
                    toast("密码未输入");
                    return;
                }
                if (StringUtil.isSpace(yzmStr)) {
                    toast("验证码未输入");
                    return;
                }
//                if (!yzmStr.equals(verificationCode1)) {
//                    toast("验证码输入错误");
//                    return;
//                }
                Map<String,Object> map=new HashMap<>();
                map.put("key",key);
                map.put("code",yzmStr);
                String json=StringUtil.map2Json(map);
                verification_code(json,phoneStr,passwordStr);
//                ModifyPassword(verificationCode1, phoneStr, passwordStr);
                break;

            case R.id.hqyzm:
                String phones=phone2.getText().toString().trim();
                if (StringUtil.isSpace(phones)) {
                    toast("请输入您的手机号");
                    return;
                }
                if (!StringUtil.isPhone(phones)) {      //判断手机号格式是否正确
                    toast("手机号格式不正确");
                    break;
                }
                Map<String,Object> map1=new HashMap<>();
                map1.put("sign",sign);
                String json1=StringUtil.map2Json(map1);
                sendYzmMember(json1,phones);
                break;
            case R.id.back:
                finish();
                break;
        }


    }


    /*
    注册获取验证码
     */
    public void sendYzmMember(final String json,final String phonestr){
        if (NetUtil.isNetWorking(ForgotPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.sendYzmMember(json,phonestr, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);
                            Bean.ssid ssid = new Gson().fromJson(result, Bean.ssid.class);
                            key=ssid.key;
                            handler.sendEmptyMessage(0);
                            toast("验证码已发送，请注意查收！");



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
  验证验证码
   */
    public void verification_code(final String json,final String phonestr,final String passwordstr){
        if (NetUtil.isNetWorking(ForgotPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.ip_Verification(json,phonestr, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);
                            Bean.ssid ssid = new Gson().fromJson(result, Bean.ssid.class);
                            key=ssid.key;
                            App.token = result;
                            SharedPreferencesUtil.saveData(getApplicationContext(), "Authorization",App.token);
                            Map<String,Object>map2=new HashMap<>();
                            map2.put("password",passwordstr);
                            map2.put("username",phonestr);
                            String json1=StringUtil.map2Json(map2);
                          registone(json1);


                        }

                        @Override
                        public void onFail(String response) {
                            toast(response);
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
 保存密码
  */
    public void registone(final String json){
        if (NetUtil.isNetWorking(ForgotPasswordActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.registone(json, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);
                            Bean.ssid ssid = new Gson().fromJson(result, Bean.ssid.class);
                            key=ssid.key;



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





    private int time=60;
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (time>0){
                        time--;
                        hqyzm.setText(time+"秒后重发");
                        hqyzm.setTextColor(getResources().getColor(R.color.hintColor));
                        hqyzm.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0,1000);

                    }else {
                        handler.sendEmptyMessage(1);
                    }
                    break;
                case 1:
                    time=60;
                    hqyzm.setText("获取验证码");
                    hqyzm.setTextColor(getResources().getColor(R.color.green));
                    hqyzm.setEnabled(true);
                    break;
            }
        }
    };
}
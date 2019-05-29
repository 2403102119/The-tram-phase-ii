package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.tangchaoke.electrombilechargingpile.util.UriUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.Intents;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private Button but_register;
    private ClearEditText yzm;
    private ClearEditText phone;
    private ClearEditText pws;
    private TextView tv_get_verification_code,click_look;
    private String sign="花花猫";
    private String verificationCode = "";
    private String key;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_register);
        setTopTitle("注册");

        but_register = findViewById(R.id.but_register);
        yzm = findViewById(R.id.yzm);
        phone = findViewById(R.id.phone);
        pws = findViewById(R.id.pws);
        tv_get_verification_code = findView(R.id.tv_get_verification_code);
        click_look=findViewById(R.id.click_look);
    }

    @Override
    protected void initListener() {
        but_register.setOnClickListener(this);
        tv_get_verification_code.setOnClickListener(this);
        click_look.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_register:
                String yzmStr = yzm.getText().toString().trim();
                String phoneStr = phone.getText().toString().trim();
                String passwordStr = pws.getText().toString();

                if (StringUtil.isSpace(phoneStr)){
                    toast("请输入您的手机号");
                    return;
                }
                if (!StringUtil.isPhone(phoneStr)){      //判断手机号格式是否正确
                    toast("手机号格式不正确");
                    break;
                }
                if (StringUtil.isSpace(passwordStr)){
                    toast("请输入密码");
                    return;
                }
                if (StringUtil.isSpace(yzmStr)){        //判断是否为空
                    toast("请输入验证码");
                    return;
                }
               /* if (!yzmStr.equals(verificationCode)) {
                    toast("验证码输入错误");
                    return;
                }*/
                Map<String,Object> map=new HashMap<>();
                map.put("key",key);
                map.put("code",yzmStr);
                String json=StringUtil.map2Json(map);
                verification_code(json,phoneStr,passwordStr);

                break;
            case R.id.tv_get_verification_code:
                String phoneS = phone.getText().toString().trim();
                if (StringUtil.isSpace(phoneS)){
                    toast("请输入您的手机号");
                    break;
                }
                if (!StringUtil.isPhone(phoneS)){
                    toast("手机号格式不正确");
                    break;
                }
                Map<String,Object> map1=new HashMap<>();
                map1.put("sign",sign);
                String json1=StringUtil.map2Json(map1);
                sendYzmMember(json1,phoneS);
                break;
            case R.id.click_look:
                Intent intent=new Intent(RegisterActivity.this,PrivacyActivity.class);
                intent.putExtra("url", UriUtil.ip+"platformRules.html");
                startActivity(intent);
                break;
        }
    }






    /*
    注册获取验证码
     */
    public void sendYzmMember(final String json,final String phonestr){
        if (NetUtil.isNetWorking(RegisterActivity.this)){
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
        if (NetUtil.isNetWorking(RegisterActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.ip_Verification(json,phonestr, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);
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
        if (NetUtil.isNetWorking(RegisterActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.registone(json, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);

                            addMember();

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
   注册
    */
    public void addMember(){
        if (NetUtil.isNetWorking(RegisterActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.addMember( new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                                finish();

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
                        tv_get_verification_code.setText(time+"秒后重发");
                        tv_get_verification_code.setTextColor(getResources().getColor(R.color.hintColor));
                        tv_get_verification_code.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0,1000);

                    }else {
                        handler.sendEmptyMessage(1);
                    }
                    break;
                case 1:
                    time=60;
                    tv_get_verification_code.setText("获取验证码");
                    tv_get_verification_code.setTextColor(getResources().getColor(R.color.green));
                    tv_get_verification_code.setEnabled(true);
                    break;
            }
        }
    };
}

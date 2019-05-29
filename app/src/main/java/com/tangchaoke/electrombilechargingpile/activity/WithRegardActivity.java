package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.PakageUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;

/**
 * 关于我们
 */
public class WithRegardActivity extends BaseActivity {
    private TextView click_look,company;
    private String corporateName;
    public ArrayList<Bean.Conpany> compay=new ArrayList<>();


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_with_regard);
        setTopTitle("关于我们");

        TextView appMsg = findViewById(R.id.appMsg);
         company = findViewById(R.id.company);
        TextView copyright = findViewById(R.id.copyright);
        click_look = findViewById(R.id.click_look);

        appMsg.setText("充电桩" + " v"
                + PakageUtil.getVersionName(WithRegardActivity.this));
        company.setText(corporateName);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        copyright.setText("Copyright©" + year + "福建省有度投资有限公司" + "\tall right reserved");
    }

    @Override
    protected void initListener() {
        click_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithRegardActivity.this, PrivacyActivity.class);
                intent.putExtra("url", UriUtil.ip + "platformRules.html");
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initData() {
        getUser();
    }

    /*
   获取公司信息
    */
    private void getUser() {
        if (NetUtil.isNetWorking(this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getCompany(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);
                            JSONArray jsonArray = JSONArray.parseArray(result);
                            for (int i = 0; i <jsonArray.size() ; i++) {
                                Bean.Conpany list = new Gson().fromJson(jsonArray.getString(i), Bean.Conpany.class);
                                compay.add(list);

                            }
                            company.setText(compay.get(0).corporateName);





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

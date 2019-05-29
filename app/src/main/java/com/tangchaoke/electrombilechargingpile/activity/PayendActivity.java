package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.DialogUtils;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;

import java.text.DecimalFormat;

import okhttp3.Call;

public class PayendActivity extends BaseActivity {
    private TextView balance_pay,sta_time,end_time,dushu;
    private String startDate,endDate;
    private float electric,cost;
    private int duration;
    private int flag=0;
    private String oid;
    private DialogUtils dialogUtils4;
    private SmartRefreshLayout mySmart;
    private DecimalFormat df;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_payend);

        balance_pay=findViewById(R.id.balance_pay);
        sta_time=findViewById(R.id.sta_time);
        end_time=findViewById(R.id.end_time);
        dushu=findViewById(R.id.dushu);
        mySmart = findViewById(R.id.mySmart);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(false);
        mySmart.setEnableAutoLoadmore(false);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(226);
                finish();
            }
        });

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (null != oid){
                    getUserBill(oid);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(226);
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        if (null!=getIntent()){
            startDate=getIntent().getStringExtra("startDate");
            endDate=getIntent().getStringExtra("endDate");
            electric=getIntent().getFloatExtra("electric",0);
            duration=getIntent().getIntExtra("duration",0);
            cost=getIntent().getFloatExtra("cost",0);
            flag=getIntent().getIntExtra("flag",0);
            oid = getIntent().getStringExtra("oid");
        }
        df = new DecimalFormat("0.00");
        balance_pay.setText(df.format(cost) + "元");
        sta_time.setText("开始时间：" + startDate);
        end_time.setText("结束时间：" + endDate);
//        dushu.setText("充电度数：" + df.format(electric)+"kwh");
        dushu.setText("充电时长：" + duration + "分钟");
        if (flag!=0){
            setTopTitle("充电完成");
        }else {
            setTopTitle("支付完成");
        }
    }


    private void getUserBill(final String oid) {
        if (NetUtil.isNetWorking(PayendActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUserBill(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.getpayFinish getPay=new Gson().fromJson(result,Bean.getpayFinish.class);
                            if (1==getPay.status){
                                balance_pay.setText(df.format(getPay.list.cost) + "元");
                                sta_time.setText("开始时间：" + getPay.list.startDate);
                                end_time.setText("结束时间：" + getPay.list.endDate);
//        dushu.setText("充电度数：" + df.format(electric)+"kwh");
                                dushu.setText("充电时长：" + getPay.list.duration + "分钟");
                            }else if (3 == getPay.status){
                                dialogUtils4 = new DialogUtils(PayendActivity.this, true, "订单信息获取超时，是否继续获取？", "", "取消", "继续", true, false);
                                dialogUtils4.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
                                    @Override
                                    public void onClick() {
                                        dialogUtils4.hide();
                                    }
                                });
                                dialogUtils4.setOnTwoBtnClickListener(new DialogUtils.OnTwoBtnClickListener() {
                                    @Override
                                    public void onClick() {
                                        dialogUtils4.hide();
                                        getUserBill(oid);
                                    }
                                });
                                dialogUtils4.show();
                            }else {
                                toast(getPay.message);
                            }
                            mySmart.finishRefresh();
                        }

                        @Override
                        public void onFail(String response) {
                            mySmart.finishRefresh();
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            mySmart.finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {
                            mySmart.finishRefresh();
                        }
                    });
                }
            });
        }
    }

}

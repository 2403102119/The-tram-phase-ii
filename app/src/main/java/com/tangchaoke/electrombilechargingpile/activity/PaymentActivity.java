package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * 支付
 */
public class PaymentActivity extends BaseActivity {
//    private List<Map<String, Object>> list = new ArrayList<>();
    private Button but_submit;
    private LinearLayout coupon_xu;
    private TextView balance_ok,cost,coupon;
    private String oid,oid2="";//,startDate,endDate;
    private double allcost;
    private String coupon_cost=null;
    private DecimalFormat df=new DecimalFormat("0.00");
    private LinearLayout ll_payment_yue,ll_payment_weixin,ll_payment_alipay;
    private CheckBox cb_payment_yue,cb_payment_weixin,cb_payment_alipay;
//    private int status;
//    private float electric,cost1;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_payment);
        setTopTitle("支付");

        but_submit=findViewById(R.id.but_submit);
        coupon_xu=findViewById(R.id.coupon_xu);
        balance_ok=findViewById(R.id.balance_ok);
        cost=findViewById(R.id.cost);
        coupon=findViewById(R.id.coupon);
        ll_payment_yue=findViewById(R.id.ll_payment_yue);
        ll_payment_weixin=findViewById(R.id.ll_payment_weixin);
        ll_payment_alipay=findViewById(R.id.ll_payment_alipay);
        cb_payment_yue=findViewById(R.id.cb_payment_yue);
        cb_payment_weixin=findViewById(R.id.cb_payment_weixin);
        cb_payment_alipay=findViewById(R.id.cb_payment_alipay);

        cb_payment_yue.setChecked(true);
    }

    @Override
    protected void initListener() {
        but_submit.setOnClickListener(this);
        coupon_xu.setOnClickListener(this);
        ll_payment_yue.setOnClickListener(this);
        ll_payment_weixin.setOnClickListener(this);
        ll_payment_alipay.setOnClickListener(this);
        cb_payment_yue.setOnClickListener(this);
        cb_payment_weixin.setOnClickListener(this);
        cb_payment_alipay.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        if (null!=getIntent()){
            oid=getIntent().getStringExtra("oid");
            allcost=getIntent().getDoubleExtra("allcost",0);
        }

        balance_ok.setText("余额支付（可用"+ App.balance+"元）");

        cost.setText(df.format(allcost)+"");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_submit:
               getpayFinish(App.token,oid,oid2);
//               if (status==1){
//
//               }
                break;
            case R.id.coupon_xu:
                Intent intent1=new Intent(PaymentActivity.this,CouponActivity.class);
                intent1.putExtra("where",oid2);
                intent1.putExtra("from_activity","pay_ac");
                startActivityForResult(intent1,10);
                break;
            case R.id.ll_payment_yue:
            case R.id.cb_payment_yue:
                cb_payment_yue.setChecked(true);
                cb_payment_weixin.setChecked(false);
                cb_payment_alipay.setChecked(false);
                break;
            case R.id.ll_payment_weixin:
            case R.id.cb_payment_weixin:
                cb_payment_yue.setChecked(false);
                cb_payment_weixin.setChecked(true);
                cb_payment_alipay.setChecked(false);
                break;
            case R.id.ll_payment_alipay:
            case R.id.cb_payment_alipay:
                cb_payment_yue.setChecked(false);
                cb_payment_weixin.setChecked(false);
                cb_payment_alipay.setChecked(true);
                break;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10){
            if (null!=data){
                oid2=data.getStringExtra("coupon_oid");
                coupon_cost=data.getStringExtra("coupon_cost");

                if (coupon_cost==null){
                    coupon.setText("优惠券抵扣：请选择");
                }else {
                    coupon.setText("优惠券抵扣:"+coupon_cost+"元");
                }
            }
        }
    }

    /*
    判断是否联网，在新线程下执行下面方法
    */
    private void getpayFinish(final String token, final String oid,final String oid2) {//余额支付
        if (NetUtil.isNetWorking(PaymentActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getpayFinish(token, oid, oid2, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {

                            Bean.getpayFinish data=new Gson().fromJson(result,Bean.getpayFinish.class);
                            if (1==data.status){
                                Intent intent=new Intent(PaymentActivity.this,PayendActivity.class);
                                intent.putExtra("startDate",data.list.startDate);
                                intent.putExtra("endDate",data.list.endDate);
                                intent.putExtra("electric", data.list.electric);
                                intent.putExtra("cost",data.list.cost);
                                startActivity(intent);
                                finish();
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

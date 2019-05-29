package com.tangchaoke.electrombilechargingpile.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * 订单详情界面
 */
public class OrderDetailActivity extends BaseActivity {
    private TextView tv_charging_pile_name,tv_pay_money,tv_order_status,tv_order_number_card,
            tv_pay_type,tv_begin_time,tv_end_time,tv_charging_time,tv_charging_detail,
            tv_dianzhuang_number,tv_dianwei_number;
    private Button btn_delete_order;
    private String oid;
    private String chargePlace = "",chargeOid = "";
    private String socketOid;

    private DecimalFormat df=new DecimalFormat("0.00");
    private DecimalFormat df2=new DecimalFormat("00");

    private Dialog dialog;
    private View view;

    private TextView ok_back,no_back;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_order_detail);

        title.setText("订单详情");
        rightTxt.setText("故障报错");
        rightTxt.setVisibility(View.VISIBLE);

        tv_charging_pile_name = findViewById(R.id.tv_charging_pile_name);
        tv_pay_money = findViewById(R.id.tv_pay_money);
        tv_order_status = findViewById(R.id.tv_order_status);
        tv_order_number_card = findViewById(R.id.tv_order_number_card);
        tv_pay_type = findViewById(R.id.tv_pay_type);
        tv_begin_time = findViewById(R.id.tv_begin_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_charging_time = findViewById(R.id.tv_charging_time);
        tv_charging_detail = findViewById(R.id.tv_charging_detail);
        tv_dianzhuang_number = findViewById(R.id.tv_dianzhuang_number);
        tv_dianwei_number = findViewById(R.id.tv_dianwei_number);
        btn_delete_order = findViewById(R.id.btn_delete_order);

        dialog=new Dialog(OrderDetailActivity.this,R.style.processDialog);
        view= LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.item_dialog_shanchu,null);

        ok_back=view.findViewById(R.id.ok_back);
        no_back=view.findViewById(R.id.no_back);
    }

    @Override
    protected void initListener() {
        btn_delete_order.setOnClickListener(this);
        no_back.setOnClickListener(this);
        ok_back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null!=getIntent()){
            oid = getIntent().getStringExtra("oid");
            Log.i("qqqqqqqqqqqq", "initData: "+oid);
        }
    }

    protected void onResume() {
        super.onResume();
        if (App.islogin) {
            getWaitOrderDetail(App.token,oid);
        } else {
            startActivity(new Intent(OrderDetailActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete_order:
//                startActivity(new Intent(OrderDetailActivity.this, PaymentActivity.class));
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                //底部弹出
                window.setGravity(Gravity.CENTER);
                //弹出动画
                window.setWindowAnimations(R.style.bottomDialog);
                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;
            case R.id.no_back:
                dialog.dismiss();
                break;
            case R.id.ok_back:
                getdeletOrder(App.token,oid);
                break;
            case R.id.rightTxt:
                if (null != socketOid){
                    Intent faultIntent = new Intent(this, MalfunctionRepairActivity.class);
                    faultIntent.putExtra("socketOid", socketOid);
                    startActivity(faultIntent);
                }
                break;
        }
    }


    //订单详情
    public void getWaitOrderDetail(final String token, final String oid){
        if (NetUtil.isNetWorking(OrderDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getWaitOrderDetai(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.getWaitOrderDetaiAll data = new Gson().fromJson(result,Bean.getWaitOrderDetaiAll.class);
                            if (1==data.status){
                                socketOid = data.model.soketOid;
                                chargeOid = data.model.oid;
                                tv_charging_pile_name.setText(data.model.chargePlace);//电站名称
                                tv_dianzhuang_number.setText(StringUtil.deleteSpace(data.model.identity));
                                tv_dianwei_number.setText(StringUtil.deleteSpace(data.model.soketIdentity));
                                chargePlace=data.model.chargePlace;
                                tv_pay_money.setText("-" + df.format(data.model.cost));
                                tv_pay_type.setText(data.model.paytype);
                                tv_order_number_card.setText(data.model.orderNumber);//订单编号
                                tv_begin_time.setText(data.model.startDate);
                                tv_end_time.setText(data.model.endDate);
//                                tv_charging_time.setText(data.model.chargingmode);
                                tv_charging_time.setText(data.model.duration + "分钟");
                                tv_charging_detail.setText(df.format(data.model.cost) + "元");
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



    //App24<<删除订单 >
    private void getdeletOrder(final String token,final String oid){
        if (NetUtil.isNetWorking(OrderDetailActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getdeletOrder(token, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data=new Gson().fromJson(result,Bean.class);
                            if (1==data.status){
                                toast("删除成功");
                                setResult(22222, getIntent());
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

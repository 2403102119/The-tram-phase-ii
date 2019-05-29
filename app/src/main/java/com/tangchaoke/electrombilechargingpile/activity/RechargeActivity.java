package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.NetUtil;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.PayResult;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

/**
 * Title：充值页面
 * Author：李迪迦
 * Date：2019.5.16
 * Description ：第三方充值
 */
public class RechargeActivity extends BaseActivity {
    private CheckBox cb_100, cb_200, cb_300, cb_500, cb_800, cb_1000;
    private CheckBox cb_weixin, cb_zhifubao, cb_dianebao;
    private LinearLayout ll_weixin, ll_zhifubao, ll_dianebao;
    private ClearEditText et_money_num;
    private Button but_ok;
    private String money;

    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (SDK_PAY_FLAG == msg.what) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    RechargeActivity.this.finish();
//                    et_chongzhi_number.setText("");
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_recharge);
        setTopTitle("充值");

        rightTxt.setText("卡充值");

        cb_100 = findViewById(R.id.cb_100);
        cb_200 = findViewById(R.id.cb_200);
        cb_300 = findViewById(R.id.cb_300);
        cb_500 = findViewById(R.id.cb_500);
        cb_800 = findViewById(R.id.cb_800);
        cb_1000 = findViewById(R.id.cb_1000);
        ll_weixin = findViewById(R.id.ll_weixin);
        ll_zhifubao = findViewById(R.id.ll_zhifubao);
        ll_dianebao = findViewById(R.id.ll_dianebao);
        et_money_num = findViewById(R.id.et_money_num);
        cb_weixin = findViewById(R.id.cb_weixin);
        cb_zhifubao = findViewById(R.id.cb_zhifubao);
        cb_dianebao = findViewById(R.id.cb_dianebao);
        but_ok = findViewById(R.id.but_ok);

        cb_weixin.setChecked(true);
    }

    @Override
    protected void initListener() {
        cb_100.setOnClickListener(this);
        cb_200.setOnClickListener(this);
        cb_300.setOnClickListener(this);
        cb_500.setOnClickListener(this);
        cb_800.setOnClickListener(this);
        cb_1000.setOnClickListener(this);
        cb_zhifubao.setOnClickListener(this);
        cb_weixin.setOnClickListener(this);
        ll_weixin.setOnClickListener(this);
        ll_zhifubao.setOnClickListener(this);
        ll_dianebao.setOnClickListener(this);
        but_ok.setOnClickListener(this);
        back.setOnClickListener(this);
        rightTxt.setOnClickListener(this);

        et_money_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money = "";
                shaxin();
                et_money_num.setFocusable(true);
                et_money_num.setFocusableInTouchMode(true);
                et_money_num.requestFocus();
                et_money_num.findFocus();
                InputMethodManager inputManager = (InputMethodManager)et_money_num.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_money_num, 0);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_100:
                shaxin();
//                money = cb_100.getText().toString();
                money = "100";
                cb_100.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_200:
                shaxin();
//                money = cb_200.getText().toString();
                money = "200";
                cb_200.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_300:
                shaxin();
//                money = cb_300.getText().toString();
                money = "300";
                cb_300.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_500:
                shaxin();
//                money = cb_500.getText().toString();
                money = "500";
                cb_500.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_800:
                shaxin();
//                money = cb_800.getText().toString();
                money = "800";
                cb_800.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.cb_1000:
                shaxin();
//                money = cb_1000.getText().toString();
                money = "1000";
                cb_1000.setChecked(true);
                et_money_num.setText("");
                break;
            case R.id.ll_weixin:
            case R.id.cb_weixin:
                cb_weixin.setChecked(true);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_zhifubao:
            case R.id.cb_zhifubao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(true);
                cb_dianebao.setChecked(false);
                break;
            case R.id.ll_dianebao:
                cb_weixin.setChecked(false);
                cb_zhifubao.setChecked(false);
                cb_dianebao.setChecked(true);
                break;
            case R.id.but_ok://立即充值
                if (StringUtil.isSpace(money)){
                    money = et_money_num.getText().toString().trim();
                    if (StringUtil.isSpace(money)){
                        toast("请输入充值的金额");
                        break;
                    }
                }

                if (cb_weixin.isChecked()){
                    String type="2";
                    wxZhifu(type,money);
                }

                if (cb_zhifubao.isChecked()){
                    String type="1";
                    aliPay(type,money);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rightTxt:
                if (App.islogin){
                    startActivity(new Intent(RechargeActivity.this, CardRechargeActivity.class));
                }else {
                    startActivity(new Intent(RechargeActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    private void shaxin() {
        cb_100.setChecked(false);
        cb_200.setChecked(false);
        cb_300.setChecked(false);
        cb_500.setChecked(false);
        cb_800.setChecked(false);
        cb_1000.setChecked(false);
        et_money_num.setFocusable(false);
    }

    public void finishAc(){
        finish();
    }

    /*
    微信支付
     */
    private void wxZhifu(final String type,final String money) {
        if (NetUtil.isNetWorking(RechargeActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.rechargeBalance(type,money, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("success", result);
                            try {
                                JSONObject data=new JSONObject(result);

                                    IWXAPI api = WXAPIFactory.createWXAPI(RechargeActivity.this, null);
                                    // 将该app注册到微信
                                    api.registerApp(data.optString("app_id"));
                                    Log.i("1111111", "onSuccess: " + data.optString("app_id"));
                                    PayReq request = new PayReq();
                                    request.appId = data.optString("app_id");
                                    request.partnerId = data.optString("partner_id");
                                    request.prepayId= data.optString("prepay_id");
                                    request.packageValue = data.optString("package_value");
                                    request.nonceStr= data.optString("nonce_str");
                                    request.timeStamp= data.optString("time_stamp");
                                    request.sign= data.optString("sign");
                                    api.sendReq(request);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        }
    }


    /*
    支付宝支付
     */
    private void aliPay(final String type,final String money) {
        httpInterface.rechargeBalance(type,money, new MApiResultCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("success", result);
                try {
                    JSONObject object=new JSONObject(result);

                        final String orderInfo = object.optString("data");   // 订单信息

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(RechargeActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo,true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String response) {
                Log.e("获取.异常", response);
            }

            @Override
            public void onError(Call call, Exception exception) {
                Log.e("onError", call + "-----" + exception);
            }

            @Override
            public void onTokenError(String response) {
                Log.e("onTokenError", response);
            }
        });
    }



}

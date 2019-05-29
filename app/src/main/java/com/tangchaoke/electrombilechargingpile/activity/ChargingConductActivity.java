package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.DialogUtils;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.MyRoundProcess;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * 充电中
 */
public class ChargingConductActivity extends BaseActivity {
//    private List<Map<String, Object>> list = new ArrayList<>();
    //private MyRoundProcess mRoundProcess;
    //private LinearLayout ll_content;

    private LinearLayout ll_content;
    private LinearLayout toPile;
    private TextView id_type_style;
    private MyRoundProcess my_round_process;
    private LinearLayout circle_waiting_txt_layer;
    private TextView tv_waiting_time;
    private LinearLayout circle_charging_txt_layer;
    private TextView name;
    private TextView tv_time;
    private TextView left_time;
    private TextView cost_el;
    private TextView money;
    private TextView anpei;
    private TextView dianYa;
    private TextView percent;
    private Button stop_btn;
    private TextView tv_charging_code;
    private TextView tv_charging_location;
    private TextView tv_charging_time;

    private String oid;
//    private String type;
//    private String chargingmode;
    private boolean isSetTime=true;
    private long costTime=0L;
    private long leftTime=0L;
    private long currentTime = 0L;

    private int i = 0;

    private Bean.Ordering ordering;
    private DecimalFormat df=new DecimalFormat("0.00");
    private DecimalFormat df2=new DecimalFormat("00");
    private boolean isBegining=true;
    private DialogUtils dialogUtils;
    private DialogUtils dialogUtils2;
    private DialogUtils dialogUtils3;
    private DialogUtils dialogUtils4;

    private boolean isFinish = false;       //是否手动结束过

    private String socketOid;

    private boolean isFirstIn = true;         //是否第一次进入本界面

    @SuppressLint("InflateParams")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_charging_conduct);
        setTopTitle("正在充电");

        rightTxt.setText("故障报错");
        rightTxt.setVisibility(View.VISIBLE);

        my_round_process = findViewById(R.id.my_round_process);
        tv_time = findViewById(R.id.tv_time);
        ll_content = findViewById(R.id.ll_content);

        toPile = findViewById(R.id.toPile);
        name = findViewById(R.id.name);
        id_type_style = findViewById(R.id.id_type_style);
        circle_waiting_txt_layer = findViewById(R.id.circle_waiting_txt_layer);
        tv_waiting_time = findViewById(R.id.tv_waiting_time);
        circle_charging_txt_layer = findViewById(R.id.circle_charging_txt_layer);
        left_time = findViewById(R.id.left_time);
        cost_el = findViewById(R.id.cost_el);
        money = findViewById(R.id.money);
        anpei = findViewById(R.id.anpei);
        dianYa = findViewById(R.id.dianYa);
        percent = findViewById(R.id.percent);
        stop_btn = findViewById(R.id.stop_btn);
        tv_charging_code = findViewById(R.id.tv_charging_code);
        tv_charging_location = findViewById(R.id.tv_charging_location);
        tv_charging_time = findViewById(R.id.tv_charging_time);

        dialogUtils = new DialogUtils(this, true, "确认结束充电吗？", "", "取消", "确认", true, false);
        dialogUtils.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils.hide();
            }
        });
        dialogUtils.setOnTwoBtnClickListener(new DialogUtils.OnTwoBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils.hide();
                getelectricFinish(oid);
            }
        });

        dialogUtils2 = new DialogUtils(this, false, "人工终止，停止充电", "", "确定", "", true, false);
        dialogUtils2.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils2.hide();
                getUserBill(oid);
            }
        });

        dialogUtils3 = new DialogUtils(this, false, "电桩启动中，请耐心等待", "", "确定", "", true, false);
        dialogUtils3.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils3.hide();
            }
        });
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        ll_content.setOnClickListener(this);
        toPile.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        oid=getIntent().getStringExtra("oid");
        runnable.run();
//        getwait(oid);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 1000);
                /*if ("0".equals(ordering.launch)){
                    //launch="启动中";
                    i++;
                    tv_waiting_time.setText(i+"");
                    my_round_process.setProgress((i%101));
//                    if (i%3==0){
//                        getwait(oid);
//                    }
                }else if ("1".equals(ordering.launch)){
                    //launch="启动成功";
                    //0.人工终止 1.自动充满 2.后台终止 3.故障终止 4.充电中
                    if("4".equals(ordering.terminationCause)){

                        if (!isSetTime){*/
                            String costTimeStr;
                            long costHour=costTime/3600;
                            long costMin=costTime%3600/60;
                            long costSec=costTime%60;
                            costTimeStr=df2.format(costHour)+":"+df2.format(costMin)+":"+df2.format(costSec);
                            tv_time.setText(costTimeStr);
//                            if (leftTime>0) {
//                                String leftTimeStr;
//                                long leftHour = leftTime / 3600;
//                                long leftMin = leftTime % 3600 / 60;
//                                long leftSec = leftTime % 60;
//                                leftTimeStr = df2.format(leftHour) + "小时" + df2.format(leftMin) + "分钟";// + df2.format(leftSec)
//                                left_time.setText(leftTimeStr);
//                                leftTime--;
//                            }
                            costTime++;
//                        }
                        if (i%60==0){
                            getwait(oid);
                        }
                        i++;

                float percentage = (float)costSec/60f*100;
                my_round_process.setProgress(percentage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
//                if (isBegining){
//                    dialogUtils3.show();
//                }else {
                    finish();
//                }
                break;
            case R.id.ll_content:
                Intent intent3=new Intent(ChargingConductActivity.this,ChargingPileDetailsActivity.class);
                intent3.putExtra("oid",ordering.oid);
                intent3.putExtra("lat", App.lat);
                intent3.putExtra("lng",App.lng);
                startActivity(intent3);
                break;
            case R.id.stop_btn:
                if (isFinish){
                    getUserBill(oid);
                }else {
                    dialogUtils.show();
                }
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

    /*Activity 销毁时，取消动画*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (my_round_process != null) {
            my_round_process.cancelAnimate();
        }
        if(runnable!=null){
            runnable=null;
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
    }

    /*判断是否联网，在新线程下执行下面方法*/
    private void getwait(final String oid) {
        if (NetUtil.isNetWorking(ChargingConductActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getwait(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.OrderingAll orderingAll=new Gson().fromJson(result,Bean.OrderingAll.class);
                            if (1==orderingAll.status){
                                ordering=orderingAll.model;
                                name.setText(ordering.title);
                                if (isFirstIn) {    //如果是第一次进入当前界面则获取已进行时间，否则不更新
                                    costTime = ordering.alreadyTime;
                                    isFirstIn = false;
                                }
                                tv_charging_code.setText(StringUtil.deleteSpace(ordering.identity));
                                tv_charging_location.setText(StringUtil.deleteSpace(ordering.soketIdentity));
                                tv_charging_time.setText(ordering.chargingmode);
                                socketOid = ordering.soketOid;

//                                currentTime = ordering.alreadyTime%60;
//                                float percentage = (float)currentTime/60f*100;
//                                my_round_process.setProgress(percentage);

                                /*switch (ordering.chargingmode){
                                    case "2小时":
                                        percentage = ordering.alreadyTime/(2*60*60);
                                        break;
                                    case "4小时":
                                        percentage = ordering.alreadyTime/(4*60*60);
                                        break;
                                    case "6小时":
                                        percentage = ordering.alreadyTime/(6*60*60);
                                        break;
                                    case "8小时":
                                        percentage = ordering.alreadyTime/(8*60*60);
                                        break;
                                    case "10小时":
                                        percentage = ordering.alreadyTime/(10*60*60);
                                        break;
                                    case "自动充满":
                                        percentage = ordering.alreadyTime/(12*60*60);
                                        break;
                                }*/


//                                id_type_style.setText(StringUtil.deleteSpace(ordering.identity)+"号 | "+(0==ordering.type?"交流":"直流")+(StringUtil.isSpace(ordering.rechargePattern)?"":" | "+ordering.rechargePattern));
                                /*if ("0".equals(ordering.launch)){//启动中
                                    circle_charging_txt_layer.setVisibility(View.GONE);
                                    circle_waiting_txt_layer.setVisibility(View.VISIBLE);
                                    stop_btn.setEnabled(false);
                                }else if ("1".equals(ordering.launch)){//启动成功
                                    circle_charging_txt_layer.setVisibility(View.VISIBLE);
                                    circle_waiting_txt_layer.setVisibility(View.GONE);
                                    stop_btn.setEnabled(true);*/
                                    if("4".equals(ordering.terminationCause)){
                                        if (isSetTime){
                                            i=0;
                                            isSetTime=false;
                                            costTime=ordering.alreadyTime;
                                            isBegining=false;
                                        }
                                    }else if("0".equals(ordering.terminationCause)){//已充满
                                        stopIt("人工终止，停止充电");
                                    }else if("1".equals(ordering.terminationCause)){//已充满
                                        stopIt("自动充满，停止充电");
                                    }else if("2".equals(ordering.terminationCause)){//后台终止
                                        stopIt("后台终止，停止充电");
                                    }else if("3".equals(ordering.terminationCause)){//故障终止
                                        stopIt("故障终止，停止充电(原因:"+ordering.powerFaultReason+")");
                                    }
//                                }
//                                else {
//                                    handler.sendEmptyMessage(6);
//                                }
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

    private void stopIt(String titleStr) {
        if (runnable != null) {
            runnable = null;
        }
        if (handler != null) {
            handler = null;
        }
        dialogUtils2.setTitle(titleStr);
        dialogUtils2.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (126 == requestCode && 226 == resultCode){
            setResult(225);
            finish();
        }
    }

    /*
    * App37<<获取账单信息 >
    */
    private void getUserBill(final String oid) {
        if (NetUtil.isNetWorking(ChargingConductActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUserBill(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.getpayFinish getPay=new Gson().fromJson(result,Bean.getpayFinish.class);
                            if (1==getPay.status){
                                Intent intent=new Intent(ChargingConductActivity.this, PayendActivity.class);
                                intent.putExtra("startDate",getPay.list.startDate);
                                intent.putExtra("endDate",getPay.list.endDate);
                                intent.putExtra("electric",getPay.list.electric);
                                intent.putExtra("duration",getPay.list.duration);
                                intent.putExtra("cost",getPay.list.cost);
                                intent.putExtra("flag",1);
                                intent.putExtra("oid",oid);
                                startActivityForResult(intent, 126);
                            }else if (3 == getPay.status){
                                dialogUtils4 = new DialogUtils(ChargingConductActivity.this, true, "订单信息获取超时，是否继续获取？", "", "取消", "继续", true, false);
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

    /*充电结束*/
    private void getelectricFinish(final String oid) {
        if (NetUtil.isNetWorking(ChargingConductActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getelectricFinish(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                            public void onSuccess(String result) {
                            Bean.ChargingFinishAll getPay=new Gson().fromJson(result,Bean.ChargingFinishAll.class);
                            if (1==getPay.status){
                                /*Intent intent=new Intent(ChargingConductActivity.this,PayendActivity.class);
                                intent.putExtra("startDate",getPay.list.startDate);
                                intent.putExtra("endDate",getPay.list.endDate);
                                intent.putExtra("electric",getPay.list.electric);
                                intent.putExtra("cost",getPay.list.cost);
                                intent.putExtra("flag",1);
                                startActivity(intent);
                                finish();*/
                                if (my_round_process != null) {
                                    my_round_process.cancelAnimate();
                                }
                                if(runnable!=null){
                                    runnable=null;
                                }
                                if (handler!=null){
                                    handler=null;
                                }

                                getUserBill(oid);

                                isFinish = true;
                            }else {
                                toast(getPay.message);
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
/*
    @Override
    public void onBackPressed() {
        if (isBegining){
            dialogUtils3.show();
        }else {
            super.onBackPressed();
        }
    }*/
}

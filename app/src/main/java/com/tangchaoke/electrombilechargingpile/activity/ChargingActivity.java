package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class ChargingActivity extends BaseActivity {

    private LinearLayout ll_content;
    private LinearLayout toPile;
//    private TextView id_type_style;
    private MyRoundProcess my_round_process;
    private TextView tv_waiting_time;
    private LinearLayout circle_charging_txt_layer;
    private TextView tv_time;
    private TextView tv_left_time;              //充电时间/启动提示
    private TextView cost_el;
    private TextView money;
    private TextView anpei;
    private TextView dianYa;
    private TextView percent;
    private Button stop_btn;
    private TextView tv_dianzhuang_code;        //电桩编号
    private TextView tv_charging_name;          //电桩名称
    private TextView tv_charging_start_time;   //开始时间
    private TextView tv_charging_degrees;      //充电度数
    private TextView tv_charging_pay_cost;     //充电消费

    private String oid;
    //    private String type;
//    private String chargingmode;
    private boolean isSetTime=true;
    private long costTime=0L;
    private long leftTime=0L;

    private int i = 0;

    private Bean.Ordering ordering;
    private DecimalFormat df=new DecimalFormat("0.00");
    private DecimalFormat df2=new DecimalFormat("00");
    private boolean isBegining=true;
    private DialogUtils dialogUtils;
    private DialogUtils dialogUtils2;
    private DialogUtils dialogUtils3;
    private DialogUtils dialogUtils4;

    @SuppressLint("InflateParams")
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_charging);
        setTopTitle("充电中");
        rightTxt.setText("问题反馈");
        rightTxt.setVisibility(View.VISIBLE);

        my_round_process = findViewById(R.id.my_round_process);
        tv_time = findViewById(R.id.tv_time);
        ll_content = findViewById(R.id.ll_content);

        toPile = findViewById(R.id.toPile);
        tv_charging_name = findViewById(R.id.tv_charging_name);
        tv_waiting_time = findViewById(R.id.tv_waiting_time);
        circle_charging_txt_layer = findViewById(R.id.circle_charging_txt_layer);
        tv_left_time = findViewById(R.id.tv_left_time);
        cost_el = findViewById(R.id.cost_el);
        money = findViewById(R.id.money);
        anpei = findViewById(R.id.anpei);
        dianYa = findViewById(R.id.dianYa);
        percent = findViewById(R.id.percent);
        stop_btn = findViewById(R.id.stop_btn);
        tv_dianzhuang_code = findViewById(R.id.tv_dianzhuang_code);
        tv_charging_start_time = findViewById(R.id.tv_charging_start_time);
        tv_charging_degrees = findViewById(R.id.tv_charging_degrees);
        tv_charging_pay_cost = findViewById(R.id.tv_charging_pay_cost);

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

                if (my_round_process != null) {
                    my_round_process.cancelAnimate();
                }
                if(runnable!=null){
                    runnable=null;
                }
                if (handler!=null){
                    handler=null;
                }

                getElectricFinish(oid);
//                getUserBill(oid);
            }
        });

        dialogUtils2 = new DialogUtils(this, false, "充电结束", "", "确定", "", true, true);
        dialogUtils2.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils2.hide();
                getUserBill(oid);
//                ChargingActivity.this.finish();
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
//        type=getIntent().getStringExtra("type");
//        chargingmode=getIntent().getStringExtra("chargingmode");
//        oid="I7VA0344D1";
//        type="1";
//        chargingmode="自动充满";
//        getOrderingDetail(oid);
        runnable.run();
        getwait(oid);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what==6){
//                toast("启动失败");
//                finish();
//            }
        }
    };
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
//                    tv_waiting_time.setText(i+"");
//                    my_round_process.setProgress((i%101));
                    if (i%3==0){
                        getwait(oid);
                    }
                }else if ("1".equals(ordering.launch)){*/
                    //launch="启动成功";
                    //0.人工终止 1.自动充满 2.后台终止 3.故障终止 4.充电中
//                    if("4".equals(ordering.terminationCause)){

//                        if (!isSetTime){
                            String costTimeStr;
                            long costHour=costTime/3600;
                            long costMin=costTime%3600/60;
                            long costSec=costTime%60;
                            costTimeStr=df2.format(costHour)+":"+df2.format(costMin)+":"+df2.format(costSec);
                            tv_left_time.setText(costTimeStr);
                            /*if (leftTime>0) {
                                String leftTimeStr;
                                long leftHour = leftTime / 3600;
                                long leftMin = leftTime % 3600 / 60;
//                                long leftSec = leftTime % 60;
                                leftTimeStr = df2.format(leftHour) + "小时" + df2.format(leftMin) + "分钟";// + df2.format(leftSec)
                                tv_left_time.setText(leftTimeStr);
                                leftTime--;
                            }*/
                            costTime++;
//                        }
//                        if (i%60==0){
//                            getwait(oid);
//                        }
                        i++;
//                    }
//                }
//                else {
                    handler.sendEmptyMessage(6);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                /*if (isBegining){
                    dialogUtils3.show();
                }else {
                    finish();
                }*/
                finish();
                break;
            case R.id.toPile:
                Intent intent3=new Intent(ChargingActivity.this,ChargingPileDetailsActivity.class);
                intent3.putExtra("oid",ordering.oid);
                intent3.putExtra("lat", App.lat);
                intent3.putExtra("lng",App.lng);
                startActivity(intent3);
                break;
            case R.id.stop_btn:
                dialogUtils.show();
                break;
            case R.id.rightTxt:
                startActivity(new Intent(ChargingActivity.this, DevelopingActivity.class));
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
            handler=null;
        }
    }

    /*判断是否联网，在新线程下执行下面方法*/
    private void getwait(final String oid) {
        if (NetUtil.isNetWorking(ChargingActivity.this)){
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
                                tv_charging_name.setText(ordering.title);
//                                id_type_style.setText(ordering.identity+"号 | "+(0==ordering.type?"直流":"交流")+(StringUtil.isSpace(ordering.rechargePattern)?"":" | "+ordering.rechargePattern));
                                String code = ordering.soketIdentity;
                                String[] code1 = code.split(" ");
                                String codeStr = "";
                                for (int i = 0; i < code1.length; i++) {
                                    codeStr += code1[i];
                                }

                                tv_dianzhuang_code.setText("电桩编号：\t\t" + StringUtil.deleteSpace(ordering.identity) + "号");
                                /*if ("0".equals(ordering.launch)){//启动中
                                    circle_charging_txt_layer.setVisibility(View.GONE);
//                                    circle_waiting_txt_layer.setVisibility(View.GONE);

                                    tv_left_time.setText("电桩启动中，请稍候...");

                                    stop_btn.setEnabled(false);
                                }else if ("1".equals(ordering.launch)){//启动成功
//                                    circle_charging_txt_layer.setVisibility(View.VISIBLE);
//                                    circle_waiting_txt_layer.setVisibility(View.GONE);
                                    stop_btn.setEnabled(true);
                                    if("4".equals(ordering.terminationCause)){
                                        if (isSetTime){
                                            i=0;
                                            isSetTime=false;
                                            costTime=ordering.alreadyTime;
                                            leftTime=ordering.surplusTime;
                                            isBegining=false;
//                                            toast("启动成功");
                                        }
                                        cost_el.setText(df.format(ordering.electric));
                                        money.setText(df.format(ordering.electricCost));
                                        anpei.setText(df.format(ordering.realTimePower));
                                        dianYa.setText(df.format(ordering.realTimeVoltage));
                                        my_round_process.setProgress(ordering.percentage);
                                        String priceStr=ordering.percentage+"%";
                                        SpannableString styledText = new SpannableString(priceStr);
                                        styledText.setSpan(new TextAppearanceSpan(ChargingActivity.this,
                                                        R.style.bigText16)
                                                , priceStr.length()-1, priceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        percent.setText(styledText, TextView.BufferType.SPANNABLE);
                                    }else if("0".equals(ordering.terminationCause)){//已充满
                                        stopIt("人工终止，停止充电");
                                    }else if("1".equals(ordering.terminationCause)){//已充满
                                        stopIt("自动充满，停止充电");
                                    }else if("2".equals(ordering.terminationCause)){//后台终止
                                        stopIt("后台终止，停止充电");
                                    }else if("3".equals(ordering.terminationCause)){//故障终止
                                        stopIt("故障终止，停止充电(原因:"+ordering.powerFault+")");
                                    }
                                }
                                else {
                                    handler.sendEmptyMessage(6);
                                }*/
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
        dialogUtils2.setContent(titleStr);
        dialogUtils2.show();
    }


    /*
    充电中订单详情
    */
    private void getOrderingDetail(final String oid) {
        if (NetUtil.isNetWorking(ChargingActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getOrderingDetail(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.ChargingOrderDatailAll data = new Gson().fromJson(result,Bean.ChargingOrderDatailAll.class);
                            if (1 ==data.status){
                                tv_charging_start_time.setText(data.list.startDate);
                                tv_charging_degrees.setText(df.format(data.list.electric) + "kwh");
                                tv_charging_pay_cost.setText(StringUtil.doubleToString(data.list.cost)+"元");
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

    /*充电结束*/
    private void getElectricFinish(final String oid) {
        if (NetUtil.isNetWorking(ChargingActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getelectricFinish(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.ChargingFinishAll getPay=new Gson().fromJson(result,Bean.ChargingFinishAll.class);
                            if (1==getPay.status){
                                /*Intent intent=new Intent(ChargingActivity.this,PayendActivity.class);
                                intent.putExtra("startDate",getPay.list.startDate);
                                intent.putExtra("endDate",getPay.list.endDate);
                                intent.putExtra("electric",getPay.list.electric);
                                intent.putExtra("cost",getPay.list.cost);
                                intent.putExtra("flag",1);
                                startActivity(intent);
                                finish();*/
                                getUserBill(oid);
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
     * App37<<获取账单信息 >
     */
    private void getUserBill(final String oid) {
        if (NetUtil.isNetWorking(ChargingActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUserBill(oid, new MApiResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(String result) {
                            Bean.getpayFinish getPay=new Gson().fromJson(result,Bean.getpayFinish.class);
                            if (1==getPay.status){
                                Intent intent=new Intent(ChargingActivity.this,PayendActivity.class);
                                intent.putExtra("startDate",getPay.list.startDate);
                                intent.putExtra("endDate",getPay.list.endDate);
                                intent.putExtra("electric",getPay.list.electric);
                                intent.putExtra("cost",getPay.list.cost);
                                intent.putExtra("flag",1);
                                intent.putExtra("oid",oid);
                                startActivity(intent);
                                finish();
                            }else if (3 == getPay.status){
                                if (!ChargingActivity.this.isDestroyed()) {
                                    dialogUtils4 = new DialogUtils(ChargingActivity.this, true, "订单信息获取超时，是否继续获取？", "", "取消", "继续", true, false);
                                    dialogUtils4.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
                                        @Override
                                        public void onClick() {
                                            dialogUtils4.hide();
                                            ChargingActivity.this.finish();
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
                                }
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
}

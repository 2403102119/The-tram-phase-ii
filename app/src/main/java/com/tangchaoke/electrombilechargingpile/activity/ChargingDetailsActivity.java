package com.tangchaoke.electrombilechargingpile.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.ChargingTypeAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.LoadingDialog;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 电桩详情，扫码成功后的页面
 */
public class ChargingDetailsActivity extends BaseActivity {
    private Button but_ok;
    private LinearLayout ll_go_Recharge, ll_type,ll_type_1, ll_fault_error, ll_charging_cost;
    private RelativeLayout toDZXQ;
    private String type;
    private List<Map<String, Object>> list = new ArrayList<>();

    private TextView dz_name;
    private TextView cd_jk;
    private TextView gl_text;
    private TextView dy;
    private TextView fz_type;
    private TextView clock;
    private TextView PowerAmount;
    private TextView ServerAmount;
    private TextView chargingmode;
    private TextView balance;
    private TextView qiangwei;
    private TextView lay_fh;
    private TextView type_power;
    private TextView re_now;
    private int gun_num;
    private int back_re;
    private String back_ad = "";
    private TextView no_back2;
    private TextView ok_back2;
    private TextView no_back1;
    private TextView ok_back1;
    private String gun_re;
    private String chargingMode;
    private Bean.PowerInfo powerInfo;
    private LoadingDialog loadingDialog;
    private CheckBox cb_auto,cb_2hour,cb_4hour,cb_6hour,cb_8hour,cb_10hour;
    private NiceRecyclerView nrv_charge_type;
    private ChargingTypeAdapter adapter;
    private int checkPosition = 0;              //选择的充电时长位置
    private boolean isFirstIn = true;       //是否是第一次进入本界面

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_charging_details);
        setTopTitle("电桩详情");
        imgShare.setVisibility(View.VISIBLE);
        imgShare.setImageResource(R.mipmap.ic_shuaxin);

        but_ok = findViewById(R.id.but_ok);
        ll_go_Recharge = findViewById(R.id.ll_go_Recharge);
        ll_type = findViewById(R.id.ll_type);
        ll_type_1=findViewById(R.id.ll_type_1);
        ll_fault_error=findViewById(R.id.ll_fault_error);
        ll_charging_cost=findViewById(R.id.ll_charging_cost);
        toDZXQ=findViewById(R.id.toDZXQ);

        dz_name=findViewById(R.id.dz_name);
        cd_jk=findViewById(R.id.cd_jk);
        gl_text=findViewById(R.id.gl_text);
        dy=findViewById(R.id.dy);
        fz_type=findViewById(R.id.fz_type);
        clock=findViewById(R.id.clock);
        PowerAmount=findViewById(R.id.PowerAmount);
        ServerAmount=findViewById(R.id.ServerAmount);
        chargingmode=findViewById(R.id.chargingmode);
        balance=findViewById(R.id.balance);
        qiangwei=findViewById(R.id.qiangwei);
        lay_fh=findViewById(R.id.lay_fh);
        type_power=findViewById(R.id.type_power);
        re_now=findViewById(R.id.re_now);
        nrv_charge_type=findViewById(R.id.nrv_charge_type);

        cb_auto=findViewById(R.id.cb_auto);
        cb_2hour=findViewById(R.id.cb_2hour);
        cb_4hour=findViewById(R.id.cb_4hour);
        cb_6hour=findViewById(R.id.cb_6hour);
        cb_8hour=findViewById(R.id.cb_8hour);
        cb_10hour=findViewById(R.id.cb_10hour);

        dialog = new Dialog(ChargingDetailsActivity.this, R.style.Dialog);
        view = LayoutInflater.from(ChargingDetailsActivity.this).inflate(R.layout.item_dialog_pay, null);

        dialog1 = new Dialog(ChargingDetailsActivity.this, R.style.processDialog);
        view1 = LayoutInflater.from(ChargingDetailsActivity.this).inflate(R.layout.item_dialog_power, null);

        no_back1=view.findViewById(R.id.no_back1);
        ok_back1=view.findViewById(R.id.ok_back1);
        no_back2=view1.findViewById(R.id.no_back2);
        ok_back2=view1.findViewById(R.id.ok_back2);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        //底部弹出
        window.setGravity(Gravity.CENTER);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog1.setContentView(view1);
        dialog1.setCanceledOnTouchOutside(false);
        Window window1 = dialog1.getWindow();
        //底部弹出
        window1.setGravity(Gravity.CENTER);
        //弹出动画
        window1.setWindowAnimations(R.style.bottomDialog);
        window1.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void initListener() {
        but_ok.setOnClickListener(this);
        ll_go_Recharge.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        ll_fault_error.setOnClickListener(this);
        back.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        ll_type_1.setOnClickListener(this);
        ll_charging_cost.setOnClickListener(this);

        no_back1.setOnClickListener(this);
        ok_back1.setOnClickListener(this);
        no_back2.setOnClickListener(this);
        ok_back2.setOnClickListener(this);
        toDZXQ.setOnClickListener(this);

        cb_auto.setOnClickListener(this);
        cb_2hour.setOnClickListener(this);
        cb_4hour.setOnClickListener(this);
        cb_6hour.setOnClickListener(this);
        cb_8hour.setOnClickListener(this);
        cb_10hour.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()) {
            type = (getIntent().getStringExtra("toFragment"));
            back_re=(getIntent().getIntExtra("result_A",0));
            back_ad=getIntent().getStringExtra("way");
            powerInfo = (Bean.PowerInfo) getIntent().getSerializableExtra("PowerInfo");

            adapter = new ChargingTypeAdapter(list, this);
            nrv_charge_type.setAdapter(adapter);
            adapter.setOnItemClickListener(new ChargingTypeAdapter.OnItemClickListener() {
                @Override
                public void itemClick(int position) {
                    checkPosition = position;
                    for (int i = 0; i < list.size(); i++) {
                        Map map = list.get(i);
                        if (map.containsKey("isClick")) {
                            if (i == position) {
                                map.put("isClick", true);
                            }else {
                                map.put("isClick", false);
                            }
                            chargingMode = (String) list.get(position).get("value");

                            list.set(i, map);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

            setView(powerInfo);
        }
    }

    private Dialog dialog;
    private View view;
    private Dialog dialog1;
    private View view1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_ok://立即充电
                if(0 > powerInfo.balance){//余额不足0
                    dialog.show();
                }else {//余额大于0
                    //判断选枪和插枪
//                    if (1 == powerInfo.socketSite) {
                        dialog1.show();
//                    }else {
//                        toast("请您检查插枪后再试");
//                    }

                }
                break;
            case R.id.ll_go_Recharge://去充值
                if (App.islogin) {
                    startActivity(new Intent(ChargingDetailsActivity.this, RechargeActivity.class));
                }else {
                    startActivity(new Intent(ChargingDetailsActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_type://充电方式
                Intent intent1=new Intent(ChargingDetailsActivity.this,ChargingWayActivity.class);
                intent1.putExtra("tonum",(Serializable)list);
                intent1.putExtra("identity",powerInfo.identity);//充电编号
                startActivityForResult(intent1,1);
                break;
            case R.id.ll_fault_error:
                if (null != powerInfo){
                    Intent faultIntent = new Intent(this, MalfunctionRepairActivity.class);
                    faultIntent.putExtra("socketOid", powerInfo.socketOid);
                    startActivity(faultIntent);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.imgShare://刷新

                /*StringBuffer DZSb = new StringBuffer();
                for (int i = 0; i < powerInfo.identity.length(); i += 2) {
                    if ((powerInfo.identity.length() - i)%2 == 1 && powerInfo.identity.length() - i < 2) {
                        DZSb.append(powerInfo.identity.substring(i, i + 1));
                        DZSb.append(" ");
                    }else {
                        DZSb.append(powerInfo.identity.substring(i, i + 2));
                        DZSb.append(" ");
                    }
                }
                String DZStr = DZSb.toString().substring(0, DZSb.toString().length() - 1);*/

                loadingDialog = new LoadingDialog(ChargingDetailsActivity.this);
                loadingDialog.show();

                getPower_SX(App.token, powerInfo.identity, powerInfo.socket);
//                getbalance(App.token);
                break;
            case R.id.ll_type_1://选择枪位
                Intent intent=new Intent(ChargingDetailsActivity.this,SelectGunActivity.class);
//                intent.putExtra("agunSite",powerInfo.agunSite);
//                intent.putExtra("bgunSite",powerInfo.bgunSite);
//                intent.putExtra("agunStatus",powerInfo.agunStatus);
//                intent.putExtra("bgunStatus",powerInfo.bgunStatus);
//                intent.putExtra("gun_num",gun_num);//选择的枪
//                intent.putExtra("gunN",powerInfo.gunNum);//枪数量
//                intent.putExtra("identity",powerInfo.identity);//充电编号
                startActivityForResult(intent,0);
                break;
            case R.id.no_back1:
                dialog.dismiss();
                break;
            case R.id.ok_back1:
                dialog.dismiss();
                Intent intent2=new Intent(ChargingDetailsActivity.this,RechargeActivity.class);
                intent2.putExtra("monum",1);
                startActivity(intent2);
                break;
            case R.id.no_back2:
                dialog1.dismiss();
                break;
            case R.id.ok_back2:
                dialog1.dismiss();
                if (null == chargingMode) {
                    toast("请选择充电时长");
                }else {
                    getorder(App.token, powerInfo.oid, chargingMode, powerInfo.socket);
                }
                break;
            case R.id.toDZXQ:
                Intent intent3=new Intent(ChargingDetailsActivity.this,ChargingPileDetailsActivity.class);
                intent3.putExtra("oid",powerInfo.currPlaceOid);
                intent3.putExtra("lat",App.lat);
                intent3.putExtra("lng",App.lng);
                startActivity(intent3);
                break;
            case R.id.cb_auto:
                clearCheck();
                cb_auto.setChecked(true);
                break;
            case R.id.cb_2hour:
                clearCheck();
                cb_2hour.setChecked(true);
                break;
            case R.id.cb_4hour:
                clearCheck();
                cb_4hour.setChecked(true);
                break;
            case R.id.cb_6hour:
                clearCheck();
                cb_6hour.setChecked(true);
                break;
            case R.id.cb_8hour:
                clearCheck();
                cb_8hour.setChecked(true);
                break;
            case R.id.cb_10hour:
                clearCheck();
                cb_10hour.setChecked(true);
                break;
            case R.id.ll_charging_cost:
                Intent intent4 = new Intent(this, ChargingStandardActivity.class);
                intent4.putExtra("oid", powerInfo.currPlaceOid);
                startActivity(intent4);
                break;
        }
    }

    /*
    清除选择项
     */
    private void clearCheck(){
        cb_auto.setChecked(false);
        cb_2hour.setChecked(false);
        cb_4hour.setChecked(false);
        cb_6hour.setChecked(false);
        cb_8hour.setChecked(false);
        cb_10hour.setChecked(false);
    }

    /*
    设置展示的内容
     */
    private void setView(Bean.PowerInfo powerInfo){
        List<Bean.ChargingMode> chargingmodeList = powerInfo.chargingmode;
        for (int i = 0; i < chargingmodeList.size(); i++){
            Map<String, Object> map = new HashMap<>();
            map.put("value", chargingmodeList.get(i).value);
            map.put("chargingmode", chargingmodeList.get(i).chargingmode);
            if (0 == i){
                map.put("isClick", true);
            }else {
                map.put("isClick", false);
            }
            list.add(map);
        }

        if (0 < list.size()) {
            chargingMode = (String) list.get(0).get("value");
        }

        adapter.notifyDataSetChanged();

        balance.setText(StringUtil.doubleToString(powerInfo.balance) + "元");
        dz_name.setText(powerInfo.currPlace);
        cd_jk.setText(StringUtil.deleteSpace(powerInfo.identity) + "号");
        gl_text.setText(powerInfo.power+"");
        dy.setText(powerInfo.voltage);
//        fz_type.setText(powerInfo.sourceType);
//        clock.setText(powerInfo.StartTime+"-"+powerInfo.EndTime);
        PowerAmount.setText(StringUtil.doubleToString(powerInfo.powerRange)+ "元/时");
        ServerAmount.setText(StringUtil.deleteSpace(powerInfo.socket) + "号");
        chargingmode.setText("充电方式："+(chargingmodeList.get(0).chargingmode));

        if (StringUtil.isSpace(back_ad) && null != chargingmodeList && chargingmodeList.size() > 0){
            chargingmode.setText("充电方式："+(chargingmodeList.get(0).chargingmode));
        }else {
            chargingmode.setText("充电方式："+back_ad);
        }

        /*if (1 == powerInfo.socketSite){
            lay_fh.setVisibility(View.GONE);
        }*/


        if ("选择枪位：A枪".equals(qiangwei.getText().toString())){
            gun_num=1;
        }else  if ("选择枪位：B枪".equals(qiangwei.getText().toString())){
            gun_num=2;
        }else {
            gun_num=0;
        }



        //充电类型
        if ("2".equals(powerInfo.location)){
            type_power.setText("外");
            type_power.setBackgroundResource(R.drawable.shape_padding_green_10);
        }

//        if ("0".equals(powerInfo.guntype)){
//            re_now.setVisibility(View.VISIBLE);
//        }else {
//            re_now.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (125 == requestCode && 225 == resultCode){
            setResult(224);
            finish();
        }
    }

    //刷新页面重新调接口
    private void getPower_SX(final String token,final String identity1, final String identity2){
        if (NetUtil.isNetWorking(ChargingDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPowerInfo(token, identity1, identity2, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialog.dismiss();

                            Bean.PowerInfoall data =new Gson().fromJson(result,Bean.PowerInfoall.class);
                            if (1==data.status){
                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                powerInfo = data.model;

                                balance.setText(StringUtil.doubleToString(powerInfo.balance) + "元");
                                dz_name.setText(powerInfo.currPlace);
                                cd_jk.setText(StringUtil.deleteSpace(powerInfo.identity) + "号");
                                gl_text.setText(powerInfo.power+"");
                                dy.setText(powerInfo.voltage);

                                if ("2".equals(powerInfo.location)){
                                    type_power.setText("外");
                                    type_power.setBackgroundResource(R.drawable.shape_padding_green_10);
                                }else {
                                    type_power.setText("内");
                                    type_power.setBackgroundResource(R.drawable.shape_padding_red_10);
                                }

                                List<Bean.ChargingMode> chargingmodeList = data.model.chargingmode;
                                for (int i = 0; i < chargingmodeList.size(); i++){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("value", chargingmodeList.get(i).value);
                                    map.put("chargingmode", chargingmodeList.get(i).chargingmode);
                                    if (checkPosition == i){
                                        map.put("isClick", true);
                                    }else {
                                        map.put("isClick", false);
                                    }
                                    list.add(map);
                                }

                                adapter.notifyDataSetChanged();

                                if (list.size() > 0){
                                    if (checkPosition >= list.size()) {
                                        checkPosition = 0;
                                    }
                                    chargingMode = (String) list.get(checkPosition).get("value");
                                }


                                dz_name.setText(data.model.currPlace);
//                                cd_jk.setText(StringUtil.deleteSpace(powerInfo.identity)+"号");
                                gl_text.setText(data.model.power+"");
                                dy.setText(data.model.voltage);
//                                fz_type.setText(data.model.sourceType);
//                                clock.setText(data.model.StartTime+"-"+data.model.EndTime);
                                PowerAmount.setText(StringUtil.doubleToString(data.model.powerRange)+ "元/时");
                                ServerAmount.setText(data.model.socket + "号");




                                //枪位与插枪判断
                                /*if (1 == data.model.socketSite){
                                    lay_fh.setVisibility(View.GONE);
                                }*/




//                                //空闲
//                                if ("1".equals(data.model.agunStatus)&&"1".equals(data.model.bgunStatus)){
//                                    re_now.setVisibility(View.GONE);
//                                }

                                //插枪后隐藏
//                                if ("选择枪位：A枪".equals((String) qiangwei.getText())&&"1".equals(data.model.agunSite)){
//                                    lay_fh.setVisibility(View.GONE);
//                                }
//                                if ("选择枪位：B枪".equals(qiangwei.getText().toString())&&"1".equals(data.model.bgunSite)){
//                                    lay_fh.setVisibility(View.GONE);
//                                }

                            }else {
                                toast(data.message);
                            }

                        }




                        @Override
                        public void onFail(String response) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onTokenError(String response) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    //获取用户余额
    private void getbalance(){
        if (NetUtil.isNetWorking(ChargingDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getbalance( new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.balanceAll data=new Gson().fromJson(result,Bean.balanceAll.class);

                            if (1==data.status){

                                App.balance=data.model.balance;
                                SPUtil.saveData(ChargingDetailsActivity.this,"balance",data.model.balance);
                                balance.setText(new DecimalFormat("0.00").format(data.model.balance)+"元");

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

    //生成订单
    private void getorder(final String token,final String oid, final String chargingmode,final String identity){
        if (NetUtil.isNetWorking(ChargingDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getorder(token,oid, chargingmode,identity, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.getorder data1=new Gson().fromJson(result,Bean.getorder.class);

                            if (1==data1.status){
                                goElectricAfter(data1.oid);
                            }else {
                                toast(data1.message);
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


    //App45<<等待充电 >
    private void goElectricAfter(final String oid){
        if (NetUtil.isNetWorking(ChargingDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.goElectricAfter(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.ElectricAfter data1=new Gson().fromJson(result,Bean.ElectricAfter.class);

                            if (1==data1.status){
                                if (1 == data1.model.detectionStatus){//detectionStatus 检测状态|-1.插座正常 1.插座故障 0.插座已连接 2.插座未连接
                                    toast("该充电位故障，请使用其他充电位");
                                    deleteWrong(oid);
                                }else if (2 == data1.model.detectionStatus){
                                    toast("您还未连接，请连接后再试");
                                    deleteWrong(oid);
                                }else {
                                    Intent intent3 = new Intent(ChargingDetailsActivity.this, ChargingConductActivity.class);
                                    intent3.putExtra("oid", oid);
                                    startActivityForResult(intent3, 125);
                                }
                            }else {
                                toast(data1.message);
                                deleteWrong(oid);
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


    //App46<<删除错误订单 >
    private void deleteWrong(final String oid){
        if (NetUtil.isNetWorking(ChargingDetailsActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.deleteWrong(oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.getorder data1=new Gson().fromJson(result,Bean.getorder.class);

                            if (1==data1.status){

                            }else {
                                toast(data1.message);
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

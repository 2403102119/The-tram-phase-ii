package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import okhttp3.Call;

/**
 * 输号充电
 */
public class InputChargingActivity extends BaseActivity {
    private Button but_ok;
    private String DZ, DW;
    private EditText cdz_num, cdw_num;
//    private List<Map<String, Object>> list = new ArrayList<>();
//    private TextView ok_back;
//    private TextView takePhoto;
//    private TextView no_back;
    private DialogUtils dialogUtils;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_input_charging);
        setTopTitle("输号充电");

        but_ok = findViewById(R.id.but_ok);
        cdz_num=findViewById(R.id.cdz_num);
        cdw_num=findViewById(R.id.cdw_num);

//        dialog = new Dialog(InputChargingActivity.this, R.style.processDialog);
//        view = LayoutInflater.from(InputChargingActivity.this).inflate(R.layout.item_dialog_guzhang, null);
//
//        ok_back = view.findViewById(R.id.ok_back);
//        no_back=view.findViewById(R.id.no_back);
//
//        takePhoto=view.findViewById(R.id.takePhoto);
        dialogUtils=new DialogUtils(InputChargingActivity.this,false,"该电桩出现故障，请使用其他电桩","","确认","",true,false);
        dialogUtils.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils.hide();
            }
        });
    }

    @Override
    protected void initListener() {
        but_ok.setOnClickListener(this);
//        ok_back.setOnClickListener(this);
//        no_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

//    private Dialog dialog;
//    private View view;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_ok://确定
                DZ=cdz_num.getText().toString().trim();
                DW=cdw_num.getText().toString().trim();

                if (StringUtil.isSpace(DZ)){
                    toast("请输入电桩号");
                    break;
                }
                if (StringUtil.isSpace(DW)){
                    toast("请输入电位编号");
                    break;
                }

                /*StringBuffer DZSb = new StringBuffer();
                for (int i = 0; i < DZ.length(); i += 2) {
                    if ((DZ.length() - i)%2 == 1 && DZ.length() - i < 2) {
                        DZSb.append(DZ.substring(i, i + 1));
                        DZSb.append(" ");
                    }else {
                        DZSb.append(DZ.substring(i, i + 2));
                        DZSb.append(" ");
                    }
                }
                String DZStr = DZSb.toString().substring(0, DZSb.toString().length() - 1);*/
                String DZStr = StringUtil.addSpace(DZ);

                /*StringBuffer DWSb = new StringBuffer();
                for (int i = 0; i < DW.length(); i += 2) {
                    if ((DW.length() - i)%2 == 1 && DW.length() - i < 2) {
                        DWSb.append(DW.substring(i, i + 1));
                        DWSb.append(" ");
                    }else {
                        DWSb.append(DW.substring(i, i + 2));
                        DWSb.append(" ");
                    }
                }
                String DWStr = DWSb.toString().substring(0, DWSb.toString().length() - 1);*/
                String DWStr = StringUtil.addSpace(DW);

                getPower(App.token,DZStr, DWStr);
                break;

//            case R.id.ok_back:
//                dialog.dismiss();
//                break;
//            case R.id.no_back:
//                dialog.dismiss();
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (124 == requestCode && 224 == resultCode){
            setResult(223);
            finish();
        }
    }

    /*
        App20<<电桩详情 >
         */
    private void getPower(final String token,final String identity1, final String identity2){
        if (NetUtil.isNetWorking(InputChargingActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPowerInfo(token, identity1, identity2, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.PowerInfoall data =new Gson().fromJson(result,Bean.PowerInfoall.class);
                            if (1 == data.status){
                                if ("1".equals(data.model.stopStatus)) {
                                    dialogUtils.setTitle("该充电桩已停用，请使用其他电桩");
                                    dialogUtils.show();
                                }else{
                                    if ("1".equals(data.model.socketState)) {
                                        dialogUtils.setTitle("该充电位出现故障，请使用其他充电位");
                                        dialogUtils.show();
                                    } else {
                                        if ("1".equals(data.model.useState)) {
                                            dialogUtils.setTitle("该充电位正在使用中，请使用其他充电位");
                                            dialogUtils.show();
                                        } else {
                                            Intent intent = new Intent(InputChargingActivity.this, ChargingDetailsActivity.class);
                                            intent.putExtra("toFragment", data.model.oid);
                                            intent.putExtra("PowerInfo", data.model);
                                            startActivityForResult(intent,124);
                                        }
                                    }
                                }
                            }
                            else {
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

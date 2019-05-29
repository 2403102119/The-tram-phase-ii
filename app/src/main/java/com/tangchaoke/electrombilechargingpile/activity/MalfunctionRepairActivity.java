package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.NetUtil;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.DialogUtils;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;

import okhttp3.Call;

/*
* 故障报错界面
* */
public class MalfunctionRepairActivity extends BaseActivity {
    private ClearEditText cet_input_fault;
    private Button btn_submit;
    private String socketOid;
    private DialogUtils dialogUtils;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_malfunction_repair);

        title.setText("故障报错");

        cet_input_fault = findViewById(R.id.cet_input_fault);
        btn_submit = findViewById(R.id.btn_submit);

        dialogUtils = new DialogUtils(this, false, "",
                "感谢您对我们的支持，我们将尽快处理该问题，不断提升用户体验。",
                "确定","", false, true);
    }

    @Override
    protected void initListener() {
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent()){
            socketOid = getIntent().getStringExtra("socketOid");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                String faultStr = cet_input_fault.getText().toString().trim();
                if (StringUtil.isSpace(faultStr)){
                    toast("请输入您遇到问题，我们将尽快处理该问题，不断提升用户体验");
                    break;
                }

                if (StringUtil.isSpace(socketOid)){
                    toast("未获取到插座id");
                    break;
                }

                //拿到InputMethodManager
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //如果window上view获取焦点 && view不为空
                if(imm.isActive()&&getCurrentFocus()!=null){
                    //拿到view的token 不为空
                    if (getCurrentFocus().getWindowToken()!=null) {
                        //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }

                getRepair(faultStr, socketOid);

                break;
        }
    }

    public void getRepair(final String content, final String oid){
        if (NetUtil.isNetWorking(this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getRepair(content, oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean data = new Gson().fromJson(result, Bean.class);
                            if (1 == data.status){
//                                toast("感谢您对我们的支持，我们将尽快处理该问题，不断提升用户体验。");
//                                finish();
                                dialogUtils.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
                                    @Override
                                    public void onClick() {
//                                        dialogUtils.hide();
                                        MalfunctionRepairActivity.this.finish();
                                    }
                                });
                                dialogUtils.show();
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

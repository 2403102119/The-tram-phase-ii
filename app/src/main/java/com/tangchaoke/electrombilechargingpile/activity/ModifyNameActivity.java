package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.tangchaoke.electrombilechargingpile.App.token;

/**
 * 修改昵称
 */
public class ModifyNameActivity extends BaseActivity {
    private EditText et_name;
    private LinearLayout ll_activity;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_modify_name);
        title.setText("修改昵称");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("完成");

        et_name = findView(R.id.et_name);
        ll_activity = findView(R.id.ll_activity);

        et_name.setText(App.loginMsg.nickName);
    }

    @Override
    protected void initListener() {
        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = et_name.getText().toString().trim();
                if (StringUtil.isSpace(nickName)) {
                    toast("请输入昵称");
                } else {
                    Map<String,Object>map=new HashMap<>();
                    map.put("nickName",nickName);
                    String json=StringUtil.map2Json(map);
                    updateNickName(json);
                }
            }
        });
        ll_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputKeyboard(v);
            }
        });
    }

    @Override
    protected void initData() {
        String nickName = getIntent().getStringExtra("nickName");
        et_name.setText(nickName);
    }

    public void updateNickName(final String json){
        if (NetUtil.isNetWorking(ModifyNameActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateNickName(json,new MApiResultCallback(){
                        @Override
                        public void onSuccess(String result) {
                            Log.i("获取成功", result);
                            finish();
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



    //点击空白区域隐藏键盘.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ModifyNameActivity.this.getCurrentFocus() != null) {
                if (ModifyNameActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(ModifyNameActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //隐藏软键盘
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}

package com.tangchaoke.electrombilechargingpile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.MyMessageAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 消息
 */
public class MyMessageActivity extends BaseActivity {
    private NiceRecyclerView rv_myMessage;
    private List<Map<String, Object>> list = new ArrayList<>();
    private MyMessageAdapter adapter;

    private LinearLayout ll_empty;

    private int index1 = 0, index2 = 15, num = 15;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_my_message);
        title.setText("消息");
        rv_myMessage = findViewById(R.id.rv_myMessage);
        ll_empty = findViewById(R.id.ll_empty);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        adapter = new MyMessageAdapter(list, this);
        rv_myMessage.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index1 = 0;
                index2 = 15;
                Map<String,Object>map=new HashMap<>();
                map.put("page",index1);
                map.put("size",index2);
                String json= StringUtil.map2Json(map);
                getCheck(json);
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index1 += num;
                index2 += num;
                Map<String,Object>map=new HashMap<>();
                map.put("page",index1);
                map.put("size",index2);
                String json=StringUtil.map2Json(map);
                getCheck(json);
            }
        });

        Map<String,Object>map=new HashMap<>();
        map.put("page",index1);
        map.put("size",index2);
        String json=StringUtil.map2Json(map);
        getCheck(json);
    }







    //App12<<获取用户消息 >
    private void getCheck(final String json){
        if (NetUtil.isNetWorking(MyMessageActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.message(json,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("ok",result);

                            Bean.MessageAll data = new Gson().fromJson(result,Bean.MessageAll.class);

                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                List<Bean.content> messageList = data.content;
                                for (int i=0;i<messageList.size();i++){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("type", messageList.get(i).status);
                                    map.put("context", messageList.get(i).context);
                                    map.put("ctime", messageList.get(i).createTime);

                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();
                                if (0 == list.size()){
                                    ll_empty.setVisibility(View.VISIBLE);
                                }else {
                                    ll_empty.setVisibility(View.GONE);
                                }

                            finishRefresh();
                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                        }
                    });
                }
            });
        }
    }



}

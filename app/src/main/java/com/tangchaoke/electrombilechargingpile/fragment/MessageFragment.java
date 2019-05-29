package com.tangchaoke.electrombilechargingpile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.activity.LoginActivity;
import com.tangchaoke.electrombilechargingpile.adapter.FindAdapter;
import com.tangchaoke.electrombilechargingpile.adapter.MessageAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseFragment;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MessageFragment extends BaseFragment {
    private List<Map<String, Object>> list = new ArrayList<>();
    private NiceRecyclerView nrv_message;
    private MessageAdapter adapter;
    private SmartRefreshLayout mySmart;
    private LinearLayout ll_empty;

    private int index1 = 0, index2 = 15, num = 15;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index1 = 0;
                index2 = 15;
                if (App.islogin) {
                    Map<String,Object> map=new HashMap<>();
                    map.put("page",index1);
                    map.put("size",index2);
                    String json= StringUtil.map2Json(map);
                    getCheck(json);
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index1 += num;
                index2 += num;
                if (App.islogin) {
                    Map<String,Object> map=new HashMap<>();
                    map.put("page",index1);
                    map.put("size",index2);
                    String json=StringUtil.map2Json(map);
                    getCheck(json);
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    private void initData() {
        adapter = new MessageAdapter(getActivity(), list);
        nrv_message.setAdapter(adapter);
        adapter.setOnItemClickListener(new FindAdapter.OnItemClickListener() {
            @Override
            public void itemListener(int position) {

            }
        });

        if (App.islogin) {
            Map<String,Object>map=new HashMap<>();
            map.put("page",index1);
            map.put("size",index2);
            String json=StringUtil.map2Json(map);
            getCheck(json);
        }else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void initView(View view) {
        nrv_message = view.findViewById(R.id.nrv_message);
        mySmart = view.findViewById(R.id.mySmart);
        ll_empty = view.findViewById(R.id.ll_empty);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
        mySmart.setEnableAutoLoadmore(false);
    }

    //App12<<获取用户消息 >
    private void getCheck(final String json){
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.message(json, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("ok",result);

                            Bean.MessageAll data = new Gson().fromJson(result,Bean.MessageAll.class);



                            if (!mySmart.isLoading()){
                                list.clear();
                            }

                            List<Bean.content> messageList = data.content;
                            for (int i = 0; i<messageList.size(); i++){
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", messageList.get(i).title);
                                map.put("context", messageList.get(i).context);
                                map.put("ctime", messageList.get(i).createTime);

                                list.add(map);
                            }
                            adapter.notifyDataSetChanged();

                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                            if (0 == list.size()){
                                ll_empty.setVisibility(View.VISIBLE);
                            }else {
                                ll_empty.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }
    }


    private void finishRefresh(){
        mySmart.finishRefresh();
        mySmart.finishLoadmore();
    }
}

package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.ChargingEvaluationListAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.viewimg.SBI_ViewPagerSampleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 全部评价
 */
public class AllEvaluationActivity extends BaseActivity {
    private String oid="";
    private int index=0,num=15;

    private NiceRecyclerView rv_evaluation;
    private List<Map<String, Object>> evaluationList = new ArrayList<>();
    private ChargingEvaluationListAdapter adapterEvaluationList;
    private ArrayList<Bean.Evaluate> list;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_all_evaluation);
        setTopTitle("全部评价");

        rv_evaluation = findViewById(R.id.rv_evaluation);

        adapterEvaluationList = new ChargingEvaluationListAdapter(evaluationList, this);
        rv_evaluation.setAdapter(adapterEvaluationList);
        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index=0;
                getAllevaluateList(index+"",num+"",oid);
            }
        });
        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index+=num;
                getAllevaluateList(index+"",num+"",oid);
            }
        });
        adapterEvaluationList.setOnImgItemClickListener(new ChargingEvaluationListAdapter.OnImgItemClickListener() {
            @Override
            public void onImgItemClick(int position,int positions) {
                Intent intent=new Intent(AllEvaluationActivity.this,SBI_ViewPagerSampleActivity.class);
                intent.putStringArrayListExtra("listImg",list.get(position).imageUrl);
                intent.putExtra("position",positions);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void initData() {
        oid=getIntent().getStringExtra("oid");
        getAllevaluateList(index+"",num+"",oid);
    }


    /*
    判断是否联网，在新线程下执行下面方法
 */
    private void getAllevaluateList(final String index,final String num, final String oid){
        if (NetUtil.isNetWorking(AllEvaluationActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getAllevaluateList(index,num,oid, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.EvaluateModel evaluateModel =new Gson().fromJson(result, Bean.EvaluateModel.class);
                            list=evaluateModel.list;
                            if (evaluateModel.status==1){
                                if (!mySmart.isLoading()){
                                    evaluationList.clear();
                                }
                                //评价数据
                                for (int i = 0; i < evaluateModel.list.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("oid", evaluateModel.list.get(i).oid);
                                    map.put("headimage", evaluateModel.list.get(i).headimage);
                                    map.put("nickname", evaluateModel.list.get(i).nickname);
                                    map.put("ctime", evaluateModel.list.get(i).ctime);
                                    map.put("stars", evaluateModel.list.get(i).stars);
                                    map.put("content", evaluateModel.list.get(i).content);
                                    map.put("imageUrl", evaluateModel.list.get(i).imageUrl);
                                    evaluationList.add(map);
                                }
                                finishRefresh();
                                adapterEvaluationList.notifyDataSetChanged();
                            }else {
                                toast(evaluateModel.message);
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
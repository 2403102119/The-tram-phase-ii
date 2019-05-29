package com.tangchaoke.electrombilechargingpile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.activity.FindMapActivity;
import com.tangchaoke.electrombilechargingpile.adapter.FindAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseFragment;
import com.tangchaoke.electrombilechargingpile.thread.NetUtil;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luck.picture.lib.permissions.RxPermissions.TAG;

public class FindBFragment extends BaseFragment {
    private NiceRecyclerView nrv_find;
    private List<Map<String, Object>> list = new ArrayList<>();
    private FindAdapter adapter;
    private SmartRefreshLayout mySmart;
    private LinearLayout ll_empty;

    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_b, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    protected void initView(View view) {
        nrv_find = view.findViewById(R.id.nrv_find);
        mySmart = view.findViewById(R.id.mySmart);
        ll_empty = view.findViewById(R.id.ll_empty);

        mySmart.setEnableRefresh(true);
        mySmart.setEnableLoadmore(false);
        mySmart.setEnableAutoLoadmore(false);
    }

    protected void initListener() {
        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                index = 1;
                getData(index);
            }
        });

        mySmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                index++;
                getData(index);
            }
        });
    }

    protected void initData() {
        adapter = new FindAdapter(getActivity(), list);
        nrv_find.setAdapter(adapter);
        adapter.setOnItemClickListener(new FindAdapter.OnItemClickListener() {
            @Override
            public void itemListener(int position) {
                Intent intent = new Intent(getActivity(), FindMapActivity.class);
                intent.putExtra("Latitude", ((LatLonPoint) list.get(position).get("getLatLonPoint")).getLatitude());
                intent.putExtra("Longitude", ((LatLonPoint) list.get(position).get("getLatLonPoint")).getLongitude());
                intent.putExtra("PoiItem", ((PoiItem) list.get(position).get("PoiItem")));
                startActivity(intent);
            }
        });

        getData(index);
    }

    private void getData(final int index) {
        if (NetUtil.isNetWorking(getActivity())) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (!mySmart.isLoading()) {
                        list.clear();
                    }

                    PoiSearch.Query query = new PoiSearch.Query("超市", null, App.city);
                    PoiSearch poiSearch = new PoiSearch(getActivity(), query);
                    query.setPageSize(100);// 设置每页最多返回多少条poiitem
//                    query.setPageNum(index);//设置查询页码
                    query.setDistanceSort(true);
                    if (null != App.location) {
                        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(App.mineLocation.lat, App.mineLocation.lng), 3000));
                    }else {
                        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(App.lat, App.lng), 3000));
                    }
                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            if (1000 == i) {
                                for (int j = 0; j < poiResult.getPois().size(); j++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("getTitle", poiResult.getPois().get(j).getTitle());
                                    map.put("getCityName", poiResult.getPois().get(j).getCityName());
                                    map.put("getAdName", poiResult.getPois().get(j).getAdName());
                                    map.put("getSnippet", poiResult.getPois().get(j).getSnippet());
                                    map.put("getDistance", poiResult.getPois().get(j).getDistance());
                                    map.put("getLatLonPoint", poiResult.getPois().get(j).getLatLonPoint());
                                    map.put("PoiItem", poiResult.getPois().get(j));

                                    Log.i(TAG, "onPoiSearched: " + poiResult.getPois().get(j).getPhotos().size());
                                    if (poiResult.getPois().get(j).getPhotos().size() > 0) {
                                        map.put("getPhoto", poiResult.getPois().get(j).getPhotos().get(0).getUrl());

                                        Log.i(TAG, "onPoiSearched: " + poiResult.getPois().get(j).getPhotos().get(0).getUrl());
                                    }

                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();
                                if (0 == list.size()){
                                    ll_empty.setVisibility(View.VISIBLE);
                                }else {
                                    ll_empty.setVisibility(View.GONE);
                                }
                            } else {
                                ll_empty.setVisibility(View.VISIBLE);
                                toToast("查询失败");
                            }

                            finishRefresh();
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {

                        }
                    });
                    poiSearch.searchPOIAsyn();
                }
            });
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();

//        getData(index);
    }

    private void finishRefresh(){
        mySmart.finishRefresh();
        mySmart.finishLoadmore();
    }
}

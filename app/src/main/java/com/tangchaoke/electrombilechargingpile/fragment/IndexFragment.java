package com.tangchaoke.electrombilechargingpile.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.activity.ChargingPileDetailsActivity;
import com.tangchaoke.electrombilechargingpile.activity.CitySelectActivity;
import com.tangchaoke.electrombilechargingpile.activity.LoginActivity;
import com.tangchaoke.electrombilechargingpile.activity.ScanCodeActivity;
import com.tangchaoke.electrombilechargingpile.adapter.IndexListAdapter;
import com.tangchaoke.electrombilechargingpile.adapter.SearchResAdapter;
import com.tangchaoke.electrombilechargingpile.adapter.StringListAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseFragment;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;
import com.tangchaoke.electrombilechargingpile.util.InputTools;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.PakageUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;
import com.tangchaoke.electrombilechargingpile.view.MyMarkerRoundProcess;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;
import com.tangchaoke.electrombilechargingpile.view.RatingBar;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.Intents;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static cn.jpush.android.api.JPushInterface.a.i;

/**
 * Title:电桩Fragment
 * Author:李迪迦
 * Date：2019.5.17
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_index_qiehuan;
    private RelativeLayout ll_index_list;
    private ImageView iv_index_qiehuan;
    private NiceRecyclerView rv_index_list;
    private boolean isMaps = true;
    private ListView lv_location_popupList;
    private List<Map<String, Object>> locationList = new ArrayList<>();
    private LinearLayout ll_location_popup, ll_type_popup;
    private LinearLayout ll_te_city,ll_store_weizhi, ll_store_leixing, ll_store_juli, ll_store_jiage;
    private TextView tv_store_weizhi, tv_store_leixing, tv_store_juli, tv_store_jiage;
    private TextView te_city,tv_older_empty,tv_older_ok;
    private CheckBox cb_older_leixing_1,cb_older_leixing_2,cb_older_leixing_3,cb_older_zhuangtai_0,cb_older_zhuangtai_1;
    private boolean isFirst = true;             //
    private boolean weizhiBoo = false;
    private boolean leixingBoo = false;


    private Animation inAnim, upAnim;
    private RelativeLayout re_map;
    private ImageView iv_dingwei;
    private View view_location, view_type;
    private TextView gaode_map,baidu_map,guge_map;
    /*以下是高德地图*/
    AMap aMap;
    MapView mMapView = null;
    private String city, province, adCode;
    private double latitude, longitude;
    private double targetLatitude, targetLongitude;
    private GeocodeSearch geocoderSearch;

    View item_dialog_map;
    TextView canclePic;
    Dialog showMapDialog;
    private Bean.ElectricPileListModel dataList;
    private List<Marker> makerList=new ArrayList<>();
    private Map<String,Bean.ElectricPileList> pileMap  =new HashMap<>();
    private List<Map<String,Object>> pileListData = new ArrayList<>();
    private String dname="";
    public ArrayList<Bean.Fitt> fitt=new ArrayList<>();

    private ClearEditText words;
    private NiceRecyclerView searchRes;
    private SearchResAdapter searchResAdapter;
    private List<Map<String,Object>> searchData = new ArrayList<>();
//    private CityPicker cityPicker;
    private IndexListAdapter pileListAdapter;
    private String leiXingStr="";//更多-类型-筛选条件:不限 2直流快充 1交流慢充
    private String zhuangTaiStr="";//更多-状态-筛选条件:1空闲 2使用中
    private String weiZhiStr="";//位置-筛选条件:位置 1地上 2地下
    private String paiXuStr="";//排序-筛选条件:A.距离优先 B.价格优先
    private StringListAdapter adapterList;
    private ImageView iv_saoma;
//    private Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        initView(view);
        initData();

        mMapView = view.findViewById(R.id.map);
//        this.savedInstanceState=savedInstanceState;
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        geocoderSearch = new GeocodeSearch(getActivity());
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);
        initListener();
        initMapData();//初始化地图数据
        return view;
    }

    private void initView(View view) {
        ll_index_qiehuan = view.findViewById(R.id.ll_index_qiehuan);
        ll_index_list = view.findViewById(R.id.ll_index_list);
        iv_index_qiehuan = view.findViewById(R.id.iv_index_qiehuan);
        rv_index_list = view.findViewById(R.id.rv_index_list);
        lv_location_popupList = view.findViewById(R.id.lv_location_popupList);
        ll_location_popup = view.findViewById(R.id.ll_location_popup);
        ll_store_weizhi = view.findViewById(R.id.ll_store_weizhi);
        ll_store_leixing = view.findViewById(R.id.ll_store_leixing);
        tv_store_weizhi = view.findViewById(R.id.tv_store_weizhi);
        tv_store_leixing = view.findViewById(R.id.tv_store_leixing);
//        lv_type_popupList = view.findViewById(R.id.lv_type_popupList);
        ll_type_popup = view.findViewById(R.id.ll_type_popup);
        ll_te_city = view.findViewById(R.id.ll_te_city);
        te_city = view.findViewById(R.id.te_city);
        tv_older_empty = view.findViewById(R.id.tv_older_empty);
        tv_older_ok = view.findViewById(R.id.tv_older_ok);
        cb_older_leixing_1 = view.findViewById(R.id.cb_older_leixing_1);
        cb_older_leixing_2 = view.findViewById(R.id.cb_older_leixing_2);
        cb_older_leixing_3 = view.findViewById(R.id.cb_older_leixing_3);
        cb_older_zhuangtai_0 = view.findViewById(R.id.cb_older_zhuangtai_0);
        cb_older_zhuangtai_1 = view.findViewById(R.id.cb_older_zhuangtai_1);


        re_map = view.findViewById(R.id.re_map);
        iv_dingwei = view.findViewById(R.id.iv_dingwei);
        iv_saoma = view.findViewById(R.id.iv_saoma);
        ll_store_juli = view.findViewById(R.id.ll_store_juli);
        ll_store_jiage = view.findViewById(R.id.ll_store_jiage);
        tv_store_juli = view.findViewById(R.id.tv_store_juli);
        tv_store_jiage = view.findViewById(R.id.tv_store_jiage);
        view_location = view.findViewById(R.id.view_location);
        view_type = view.findViewById(R.id.view_type);

        //动画
        inAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
        upAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out);
        upAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_index_list.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //地图弹窗开始
        item_dialog_map = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog_map, null);
        canclePic = item_dialog_map.findViewById(R.id.canclePic);

        guge_map = item_dialog_map.findViewById(R.id.guge_map);
        gaode_map = item_dialog_map.findViewById(R.id.gaode_map);
        baidu_map = item_dialog_map.findViewById(R.id.baidu_map);
        checkMaps();

        showMapDialog = new Dialog(getActivity(), R.style.processDialog);
        showMapDialog.setContentView(item_dialog_map);
        showMapDialog.setCanceledOnTouchOutside(true);
        Window window = showMapDialog.getWindow();
        //底部弹出
        window.setGravity(Gravity.BOTTOM);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //地图弹窗结束

        words = view.findViewById(R.id.words);
        searchRes = view.findViewById(R.id.searchRes);
        searchResAdapter=new SearchResAdapter(R.layout.item_search_res,searchData);
        searchRes.setAdapter(searchResAdapter);
    }

    private void checkMaps() {
        //判断地图是否存在
        if (PakageUtil.isAvilible(getActivity(),"com.autonavi.minimap")){
            gaode_map.setVisibility(View.VISIBLE);
        }else {
            gaode_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(getActivity(),"com.baidu.BaiduMap")){
            baidu_map.setVisibility(View.VISIBLE);
        }else {
            baidu_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(getActivity(),"com.google.android.apps.maps")){
            guge_map.setVisibility(View.VISIBLE);
        }else {
            guge_map.setVisibility(View.GONE);
        }
    }

    private void initData() {
        pileListAdapter = new IndexListAdapter(pileListData, getActivity(),latitude,longitude);
        rv_index_list.setAdapter(pileListAdapter);
        pileListAdapter.setOnItemClickListener(new IndexListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getActivity(), ChargingPileDetailsActivity.class);
                intent.putExtra("oid",pileListData.get(position).get("oid")+"");
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                startActivity(intent);
            }
        });


        /*位置弹窗*/
        for (int i = 0; i < 1; i++) {
            Map<String, Object> mapm = new HashMap<>();
            mapm.put("oid", "");
            mapm.put("name", "不限");
            mapm.put("checkBox", false);
            locationList.add(mapm);

            Map<String, Object> mapm3 = new HashMap<>();
            mapm3.put("oid", "");
            mapm3.put("name", "室内");
            mapm3.put("checkBox", false);
            locationList.add(mapm3);

            Map<String, Object> mapm4 = new HashMap<>();
            mapm4.put("oid", "");
            mapm4.put("name", "室外");
            mapm4.put("checkBox", false);
            locationList.add(mapm4);
        }
        adapterList = new StringListAdapter(locationList, getActivity());
        lv_location_popupList.setAdapter(adapterList);
        lv_location_popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ll_location_popup.setVisibility(View.GONE);
                weizhiBoo = false;
                adapterList.setCurrCheck(i);
                adapterList.notifyDataSetChanged();

                if ("不限".equals((String) locationList.get(i).get("name"))) {
                    tv_store_weizhi.setText("位置");
                    weiZhiStr="";
                } else {
                    tv_store_weizhi.setText((String) locationList.get(i).get("name"));
                    weiZhiStr=i+"";
                }
                getData();
            }
        });
        for (int i = 0; i < locationList.size(); i++) {
            if (i == 0) {
                adapterList.setCurrCheck(i);
                adapterList.notifyDataSetChanged();
            }
        }
    }

    private void initListener() {
        ll_index_qiehuan.setOnClickListener(this);
        ll_store_weizhi.setOnClickListener(this);
        ll_store_leixing.setOnClickListener(this);
//        ll_te_city.setOnClickListener(this);
        ll_store_juli.setOnClickListener(this);
        ll_store_jiage.setOnClickListener(this);
        view_location.setOnClickListener(this);
        view_type.setOnClickListener(this);
        cb_older_leixing_1.setOnClickListener(this);
        cb_older_leixing_2.setOnClickListener(this);
        cb_older_leixing_3.setOnClickListener(this);
        cb_older_zhuangtai_0.setOnClickListener(this);
        cb_older_zhuangtai_1.setOnClickListener(this);
        tv_older_empty.setOnClickListener(this);
        tv_older_ok.setOnClickListener(this);

        iv_dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float zoom = aMap.getCameraPosition().zoom;

                LatLng latLng = new LatLng(latitude, longitude);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        });
        iv_saoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.islogin){
                    startActivity(new Intent(getActivity(), ScanCodeActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        //地图弹窗事件
        //谷歌
        guge_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapDialog.dismiss();
                Uri gmmIntentUri = Uri.parse("google.navigation:q="
                        + targetLatitude + "," + targetLongitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                        gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        //高德
        gaode_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                try {
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    Uri gmmIntentUri = Uri.parse("amapuri://route/plan/?slat="+ latitude +"&slon="+ longitude +"&sname=我的位置"+
                            "&dlat="+targetLatitude+"&dlon="+targetLongitude+"&dname="+dname+"&dev=0&t=0");
                    intent.setData(gmmIntentUri);
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    toToast("地图跳转失败");
                }
                showMapDialog.dismiss();
            }
        });
        //百度
        baidu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapDialog.dismiss();
                Intent i1 = new Intent();
                // 驾车导航
                i1.setData(Uri.parse("baidumap://map/navi?location="+targetLatitude+","+targetLongitude));
                startActivity(i1);
            }
        });
        canclePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapDialog.dismiss();
            }
        });

        words.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    ll_index_list.setVisibility(View.GONE);
                    searchRes.setVisibility(View.GONE);
                    re_map.setVisibility(View.VISIBLE);
                    iv_index_qiehuan.setImageResource(R.mipmap.ic_index_fenlei);
                    isMaps = true;
                }else {
                    searchRes.setVisibility(View.GONE);
                }
            }
        });
        words.setOnMyTextAfterEditListener(new ClearEditText.OnMyTextAfterEditListener() {
            @Override
            public void OnAfterEdit() {

            }

            @Override
            public void onMyTextChanged(CharSequence text) {
                String newText = text.toString().trim();
                if (!StringUtil.isSpace(newText)) {
                    searchRes.setVisibility(View.VISIBLE);
//                    getChargePlace(newText,latitude+"",longitude+"");
                    getChargePlace(newText);
                }
                else {
                    searchData.clear();
                    searchResAdapter.notifyDataSetChanged();
                }
            }
        });

        searchRes.setOnEmptyClickListener(new NiceRecyclerView.OnEmptyClickListener() {
            @Override
            public void onClick(View view) {
                clearSearch();
            }
        });
        searchResAdapter.setOnItemClickListener(new SearchResAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(getActivity(), ChargingPileDetailsActivity.class);
                intent.putExtra("oid",searchData.get(position).get("oid")+"");
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                startActivity(intent);
                InputTools.HideKeyboard(words);
                clearSearch();
            }
        });
    }


    private GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener=new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int i) {
            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    city=result.getRegeocodeAddress().getCity();
                    te_city.setText(StringUtil.cutStr(city,5));
                    App.city = city;
                    SPUtil.saveData(getActivity(), "city", city);
//                    LatLng latLng = new LatLng(latitude, longitude);
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  17.5f));
//                    getData();
                } else {
                    toToast("定位错误");
                }
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    private void getData() {
        int[] mapLoc = new  int[2] ;
        mMapView.getLocationInWindow(mapLoc);
        int x=mapLoc[0];
        int y=mapLoc[1];
        int width=mMapView.getWidth();
        int height=mMapView.getHeight();

        Log.e("latitude","latitude="+latitude);
        Log.e("longitude","longitude="+longitude);
        Log.e("x","x="+x);
        Log.e("y","y="+y);
        Log.e("width","width="+width);
        Log.e("height","height="+height);
        Point topPoint=new Point((x+width),y);
        Point bottomPoint=new Point(x,(y+height));
        LatLng topLatLng= aMap.getProjection().fromScreenLocation(topPoint);
        LatLng bottomLatLng= aMap.getProjection().fromScreenLocation(bottomPoint);


        List<String> list=new ArrayList<>();
        list.add(bottomLatLng.latitude+"");
        list.add(bottomLatLng.longitude+"");

        List<String> list1=new ArrayList<>();
        list1.add(topLatLng.latitude+"");
        list1.add(topLatLng.longitude+"");

        List<String> list2 = new ArrayList<>();
        list2.add(latitude+"");
        list2.add(longitude+"");

        Map<String,Object> map=new HashMap<>();
        map.put("amountType",paiXuStr);
        map.put("roomType",weiZhiStr);
        map.put("distance",paiXuStr);
        map.put("loweRrightCorner",list);
        map.put("topleftcorner",list1);
        map.put("userCoordinates",list2);
        String json = StringUtil.map2Json(map);

        String loweRrightCorner ="loweRrightCorner="+bottomLatLng.latitude;
        String loweRrightCorner1 ="&loweRrightCorner="+bottomLatLng.longitude;
        String topleftcorner ="&topleftcorner="+topLatLng.latitude;
        String topleftcorner1 ="&topleftcorner="+topLatLng.longitude;
        String userCoordinates ="&userCoordinates="+latitude;
        String userCoordinates1 ="&userCoordinates="+longitude;
        String roomType ="";
        String distance = "";
        String amountType ="";
        if (weiZhiStr.equals("")){
            roomType ="";
        }else {
            roomType ="&roomType="+weiZhiStr;
        }
        if (null!=paiXuStr&&paiXuStr.equals("2")){
            distance="&distance=2";
        }else if (null!=paiXuStr&&paiXuStr.equals("1")){
            amountType="&amountType=1";
        }else {
            amountType="";
            distance="";
        }



        getElectricPileList(loweRrightCorner,loweRrightCorner1,topleftcorner,topleftcorner1,userCoordinates,userCoordinates1,roomType,distance,amountType);
//        getElectricPileList(bottomLatLng.latitude+"",bottomLatLng.longitude+"",topLatLng.latitude+"",
//                topLatLng.longitude+"",latitude+"",longitude+"",weiZhiStr,zhuangTaiStr,paiXuStr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_te_city://选择城市
                clearSearch();
                Intent intent=new Intent(getActivity(),CitySelectActivity.class);
                intent.putExtra("flag",0);
                intent.putExtra("city",city);
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                startActivityForResult(intent,666);
                break;
            case R.id.ll_index_qiehuan:
                clearSearch();
                if (isMaps) {
                    ll_index_list.startAnimation(inAnim);
                    ll_index_list.setVisibility(View.VISIBLE);
                    re_map.setVisibility(View.GONE);
                    iv_index_qiehuan.setImageResource(R.mipmap.ic_index_maps);
                    isMaps = false;

                    ll_type_popup.setVisibility(View.GONE);
                    ll_location_popup.setVisibility(View.GONE);
                    weizhiBoo = false;
                    leixingBoo = false;

                } else {
                    ll_index_list.startAnimation(upAnim);
                    re_map.setVisibility(View.VISIBLE);
                    iv_index_qiehuan.setImageResource(R.mipmap.ic_index_fenlei);
                    isMaps = true;
                }
                break;
            case R.id.ll_store_weizhi://位置
                ll_type_popup.setVisibility(View.GONE);
                leixingBoo = false;
                if (weizhiBoo) {
                    ll_location_popup.setVisibility(View.GONE);
                    weizhiBoo = false;
                } else {
                    ll_location_popup.setVisibility(View.VISIBLE);
                    weizhiBoo = true;
                }
                break;
            case R.id.ll_store_leixing://类型
                ll_location_popup.setVisibility(View.GONE);
                weizhiBoo = false;
                if (leixingBoo) {
                    ll_type_popup.setVisibility(View.GONE);
                    leixingBoo = false;
                } else {
                    ll_type_popup.setVisibility(View.VISIBLE);
                    leixingBoo = true;
                }
                break;
            case R.id.ll_store_juli://距离优先
                ll_location_popup.setVisibility(View.GONE);
                ll_type_popup.setVisibility(View.GONE);
                leixingBoo = false;
                weizhiBoo = false;
                tv_store_juli.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_yellow));
                tv_store_jiage.setTextColor(ContextCompat.getColor(getActivity(), R.color.nomalText));
                paiXuStr="1";
                getData();
                break;
            case R.id.ll_store_jiage://价格优先
                ll_location_popup.setVisibility(View.GONE);
                ll_type_popup.setVisibility(View.GONE);
                leixingBoo = false;
                weizhiBoo = false;
                tv_store_jiage.setTextColor(ContextCompat.getColor(getActivity(), R.color.main_yellow));
                tv_store_juli.setTextColor(ContextCompat.getColor(getActivity(), R.color.nomalText));
                paiXuStr="2";
                getData();
                break;
            case R.id.view_location://
                ll_location_popup.setVisibility(View.GONE);
                weizhiBoo = false;
                break;
            case R.id.view_type://
                ll_type_popup.setVisibility(View.GONE);
                leixingBoo = false;
                break;
            case R.id.cb_older_leixing_1://更多-类型-不限
                cb_older_leixing_1.setChecked(true);
                cb_older_leixing_2.setChecked(false);
                cb_older_leixing_3.setChecked(false);
                leiXingStr="";
                break;
            case R.id.cb_older_leixing_2://更多-类型-直流快充
                cb_older_leixing_1.setChecked(false);
                cb_older_leixing_2.setChecked(true);
                cb_older_leixing_3.setChecked(false);
                leiXingStr="2";
                break;
            case R.id.cb_older_leixing_3://更多-类型-交流慢充
                cb_older_leixing_1.setChecked(false);
                cb_older_leixing_2.setChecked(false);
                cb_older_leixing_3.setChecked(true);
                leiXingStr="1";
                break;
            case R.id.cb_older_zhuangtai_0://更多-状态-不限
                cb_older_zhuangtai_0.setChecked(true);
                cb_older_zhuangtai_1.setChecked(false);
                zhuangTaiStr="";
                break;
            case R.id.cb_older_zhuangtai_1://更多-状态-空闲
                cb_older_zhuangtai_0.setChecked(false);
                cb_older_zhuangtai_1.setChecked(true);
                zhuangTaiStr="1";
                break;
            case R.id.tv_older_empty://更多-清空条件
                cb_older_leixing_1.setChecked(true);
                cb_older_leixing_2.setChecked(false);
                cb_older_leixing_3.setChecked(false);
                cb_older_zhuangtai_0.setChecked(true);
                cb_older_zhuangtai_1.setChecked(false);
                leiXingStr="";
                zhuangTaiStr="";
                break;
            case R.id.tv_older_ok://更多-确认
                getData();
                ll_type_popup.setVisibility(View.GONE);
                leixingBoo = false;
                break;
        }
    }

    private boolean dingweiBoo = true;
    private Marker curShowWindowMarker;
    private Marker markerGuDing;

    private void initMapData() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(10*1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);//控制定位到当前按钮的显示和隐藏
        mUiSettings.setZoomControlsEnabled(false);//控制缩放控件的显示和隐藏。
        mUiSettings.setCompassEnabled(false);//控制指南针的显示和隐藏。
        mUiSettings.setScaleControlsEnabled(false);//显示比例尺控件:例如1:10Km
        mUiSettings.setRotateGesturesEnabled(false);//旋转手势
        mUiSettings.setTiltGesturesEnabled(false);//倾斜手势


        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (isFirst) {
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location.getLongitude()), 200,
                            GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  17.5f));
                    isFirst=false;
//                    getData();
                }
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                if (pileListAdapter!=null) {
                    pileListAdapter.setLatitude(latitude);
                    pileListAdapter.setLongitude(longitude);
                }

                Bean.Location location1 = new Bean.Location(latitude, longitude);
                SPUtil.saveBean2Sp(getActivity(), location1, "mineLocation", "mineLocation");
                App.mineLocation = location1;

            }
        });


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (dingweiBoo){
                    int[] mapLoc = new  int[2] ;
                    mMapView.getLocationInWindow(mapLoc);
                    int x=mapLoc[0];
                    markerGuDing.setPositionByPixels((mMapView.getWidth() / 2)+x, (mMapView.getHeight() / 2));
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (dingweiBoo) {
                    paiXuStr="";
                    leiXingStr="";
                    zhuangTaiStr="";
                    weiZhiStr="";

                    tv_store_weizhi.setText("位置");
                    adapterList.setCurrCheck(0);

                    cb_older_leixing_1.setChecked(true);
                    cb_older_leixing_2.setChecked(false);
                    cb_older_leixing_3.setChecked(false);

                    cb_older_zhuangtai_0.setChecked(true);
                    cb_older_zhuangtai_1.setChecked(false);

                    tv_store_juli.setTextColor(ContextCompat.getColor(getActivity(), R.color.nomalText));
                    tv_store_jiage.setTextColor(ContextCompat.getColor(getActivity(), R.color.nomalText));

                    SPUtil.saveData(getActivity(),"lat",cameraPosition.target.latitude);
                    SPUtil.saveData(getActivity(),"lng",cameraPosition.target.longitude);

                    Bean.Location location = new Bean.Location(cameraPosition.target.latitude,cameraPosition.target.longitude);
                    SPUtil.saveBean2Sp(getActivity(), location, "location", "location");

                    App.lat = cameraPosition.target.latitude;
                    App.lng = cameraPosition.target.longitude;
                    App.location = location;

                    getData();
                }
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //marker不跟着走,也不自动获取数据了
                if (!pileMap.containsKey(marker.getId())) {
                    return false;
                }
                curShowWindowMarker=marker;
                dname=pileMap.get(marker.getId())==null?"目的地":pileMap.get(marker.getId()).title;
                dingweiBoo = false;
                markerGuDing.setPosition(markerGuDing.getPosition());
                marker.showInfoWindow();
//                LatLng latLng = new LatLng();
                aMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });

        //添加Marker
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击其它地方隐藏infoWindow
                if (curShowWindowMarker!=null&&curShowWindowMarker.isInfoWindowShown()) {
                    curShowWindowMarker.hideInfoWindow();
                }
                aMap.animateCamera(CameraUpdateFactory.zoomTo(17.5f));
                dingweiBoo = true;
            }
        });

        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (words.isFocused()) {
                    clearSearch();
                }
            }
        });

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (!pileMap.containsKey(marker.getId())) {
                    return null;
                }
                View infoWindow = getLayoutInflater().inflate(R.layout.item_marker_inflow, null);
                Bean.ElectricPileList ep= pileMap.get(marker.getId());
                render(marker, infoWindow,ep);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        GuDingMarker();
    }

    private void clearSearch() {
        searchRes.setVisibility(View.GONE);
        words.clearFocus();
        words.setText("");
        InputTools.HideKeyboard(words);
    }

    /**
     * 添加固定Marker
     */
    private void GuDingMarker() {
        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_dingdian));
        mMarkerOptions.draggable(true);
        //添加Marker到地图
        markerGuDing = aMap.addMarker(mMarkerOptions);
        int[] mapLoc = new  int[2];
        mMapView.getLocationInWindow(mapLoc);
        int x=mapLoc[0];
        markerGuDing.setPositionByPixels((mMapView.getWidth() / 2)+x, (mMapView.getHeight() / 2));
    }

    /**
     * 自定义infowinfow窗口
     */
    @SuppressLint("SetTextI18n")
    public void render(final Marker marker, View view, final Bean.ElectricPileList ep) {
        //导航弹出框
        view.findViewById(R.id.ll_luxian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMaps();
                showMapDialog.show();
                targetLatitude=marker.getPosition().latitude;
                targetLongitude=marker.getPosition().longitude;
            }
        });
        //打开充电站详情
        view.findViewById(R.id.ll_xiangqing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChargingPileDetailsActivity.class);
                intent.putExtra("oid",ep.oid);
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                startActivity(intent);
            }
        });
        final TextView juli=view.findViewById(R.id.juli);
        TextView pileName=view.findViewById(R.id.pileName);
        RatingBar pingfen=view.findViewById(R.id.pingfen);
        TextView price=view.findViewById(R.id.price);
        TextView tv_business_time=view.findViewById(R.id.tv_business_time);
        TextView quickCount=view.findViewById(R.id.quickCount);
        TextView slowCount=view.findViewById(R.id.slowCount);
        DistanceSearch distanceSearch = new DistanceSearch(getActivity());
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                int length =0;
                if (i==1000&&distanceResult.getDistanceResults().size()>0){
                    length=(int)(distanceResult.getDistanceResults().get(0).getDistance());
                }else {
                    toToast("没有计算出驾车距离,已使用直线距离");
                    length =(int)ep.distance;
                }
                juli.setText(AMapUtil.getFriendlyLength(length));
            }
        });
        LatLonPoint start = new LatLonPoint(latitude, longitude);
        LatLonPoint start1 = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        //设置起点和终点，其中起点支持多个
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        latLonPoints.add(start);
        DistanceSearch.DistanceQuery distanceQuery=new DistanceSearch.DistanceQuery();
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(start1);
        //设置测量方式，支持直线和驾车
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
        pileName.setText(ep.title);
//        pingfen.setStar(ep.allstar);
        price.setText("电价："+StringUtil.doubleToString(ep.powerRange)+"元/时");
        tv_business_time.setText("营业时间："+"24"+"小时");
        quickCount.setText("空闲"+ep.NsocketLeisurenum+"/共"+ep.NsocketNum);
        slowCount.setText("空闲"+ep.WsocketLeisurenum+"/共"+ep.WsocketNum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
        Log.i("111111111", "onSaveInstanceState: ");
    }*/


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        try {
            if (hidden) {
                mMapView.setVisibility(View.GONE);
            } else {
                mMapView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("onHiddenChanged", e.getMessage());
        }
    }

    /**获得充电桩列表*/
    private void getElectricPileList(final String loweRrightCorner,final String loweRrightCorner1,
                                     final String topleftcorner,final String topleftcorner1,
                                     final String userCoordinates,final String userCoordinates1,
                                     final String roomType,final String distance,
                                     final String amountType){
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getElectricPileList(loweRrightCorner,loweRrightCorner1,topleftcorner,topleftcorner1,userCoordinates,userCoordinates1,roomType,distance,amountType,new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
//                            dataList =new Gson().fromJson(result, Bean.ElectricPileListModel.class);
////                            mMapView.removeAllViews();
//                            if (dataList.status==1){
//                                if(isMaps) {
//                                    removeAllMakers();
//                                    pileMap.clear();
//                                }
//                                pileListData.clear();
//                                for (int i = 0; i < dataList.list.size(); i++) {
//                                    if(isMaps) {
//                                        LatLng latLng = new LatLng(Double.parseDouble(dataList.list.get(i).lat), Double.parseDouble(dataList.list.get(i).lng));
//                                        final MarkerOptions option = new MarkerOptions();
//                                        option.position(latLng);
////                                        ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
////                                        giflist.add(BitmapDescriptorFactory.fromResource(R.mipmap.logo_round));
////                                        giflist.add(BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_dingwei));
//                                        View v=LayoutInflater.from(getActivity()).inflate(R.layout.item_marker,null);
//                                        MyMarkerRoundProcess rp=v.findViewById(R.id.rp);
//                                        float totalQuantity=dataList.list.get(i).socketNum;
//                                        float leisureNum=dataList.list.get(i).socketLeisurenum;
//                                        float employNum=dataList.list.get(i).socketUsenum;
//                                        rp.setProgress((int)((leisureNum/totalQuantity)*100));
//                                        rp.setProgress2((int)((employNum/totalQuantity)*100));
//
//                                        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromView(v);
////                                        option.icons(giflist);
//                                        option.icon(bitmapDescriptor);
//                                        option.draggable(true);
//                                        Marker marker = aMap.addMarker(option);
//                                        makerList.add(marker);
//                                        pileMap.put(marker.getId(), dataList.list.get(i));
//                                    }
//                                    Map<String, Object> map=new HashMap<>();
//                                    map.put("oid",dataList.list.get(i).oid);
//                                    map.put("socketNum",dataList.list.get(i).socketNum);
//                                    map.put("socketAbnormalnum",dataList.list.get(i).socketAbnormalnum);
//                                    map.put("socketLeisurenum",dataList.list.get(i).socketLeisurenum);
//                                    map.put("socketUsenum",dataList.list.get(i).socketUsenum);
//                                    map.put("image",dataList.list.get(i).image);
//                                    map.put("address",dataList.list.get(i).address);
//                                    map.put("lng",dataList.list.get(i).lng);
//                                    map.put("distance",dataList.list.get(i).distance);
//                                    map.put("powerRange",dataList.list.get(i).powerRange);
//                                    map.put("businessHours",dataList.list.get(i).businessHours);
//                                    map.put("title",dataList.list.get(i).title);
//                                    map.put("NsocketLeisurenum",dataList.list.get(i).NsocketLeisurenum);
//                                    map.put("NsocketNum",dataList.list.get(i).NsocketNum);
//                                    map.put("WsocketNum",dataList.list.get(i).WsocketNum);
//                                    map.put("WsocketLeisurenum",dataList.list.get(i).WsocketLeisurenum);
//                                    map.put("lat",dataList.list.get(i).lat);
//                                    map.put("distance",dataList.list.get(i).distance);
//                                    pileListData.add(map);
//                                }
//                                pileListAdapter.notifyDataSetChanged();
//
//                            }else {
//                                toToast(dataList.message);
//                            }
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



    /**搜索充电桩列表*/
    private void getChargePlace(final String searchText){
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getChargePlace(searchText, new MApiResultCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    if (result.equals("[]")){
                                        toToast("未找到查询结果");
                                    }else {

                                        com.alibaba.fastjson.JSONArray jsonArray= com.alibaba.fastjson.JSONArray.parseArray(result);
                                        searchData.clear();
                                        for (int i = 0; i <jsonArray.size() ; i++) {
                                            Bean.Fitt list = new Gson().fromJson(jsonArray.getString(i), Bean.Fitt.class);
                                            fitt.add(list);
                                        }
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("title",fitt.get(i).title);
                                        map.put("oid",fitt.get(i).id);
                                        searchData.add(map);
//                                    Bean.SearchPileListModel searchPileList =new Gson().fromJson(result, Bean.SearchPileListModel.class);


                                        searchResAdapter.notifyDataSetChanged();
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

    private void removeAllMakers() {
        for (int i = 0; i < makerList.size(); i++) {
            makerList.get(i).remove();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==888 && requestCode==666){
            String cityStr = data.getStringExtra("cityStr");
            double lat = data.getDoubleExtra("lat",0d);
            double lng = data.getDoubleExtra("lng",0d);
            Log.e("cityStr888",cityStr);
            Log.e("lat",lat+"");
            Log.e("lng",lng+"");
            city=cityStr;
            App.lat=lat;
            App.lng=lng;
            App.city=city;
            SPUtil.saveData(getActivity(),"lat",latitude);
//            SPUtil.saveBean2Sp(getActivity(),latitude+"","lat","lat");
            SPUtil.saveData(getActivity(),"lng",longitude);
//            SPUtil.saveBean2Sp(getActivity(),longitude+"","lng","lng");

            Bean.Location location = new Bean.Location(latitude, longitude);

            App.location = location;
            SPUtil.saveBean2Sp(getActivity(), location, "location", "location");

            SPUtil.saveData(getActivity(),"city",city);
            te_city.setText(StringUtil.cutStr(city,5));
            LatLng latLng=new LatLng(lat,lng);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,  10.2f));
        }
    }

    public boolean onBack() {
        if (isMaps){
            return false;
        }else {
            ll_index_list.startAnimation(upAnim);
            re_map.setVisibility(View.VISIBLE);
            iv_index_qiehuan.setImageResource(R.mipmap.ic_index_fenlei);
            isMaps = true;
            return true;
        }
    }
}

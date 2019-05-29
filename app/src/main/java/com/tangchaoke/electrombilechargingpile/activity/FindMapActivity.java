package com.tangchaoke.electrombilechargingpile.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.FindMapAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.AMapUtil;
import com.tangchaoke.electrombilechargingpile.util.InputTools;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.PakageUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;
import com.tangchaoke.electrombilechargingpile.view.NiceRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindMapActivity extends BaseActivity {
    private List<Map<String, Object>> list = new ArrayList<>();
    private NiceRecyclerView nrv_find_map;
    private FindMapAdapter adapter;
    private ClearEditText cet_search_content;
    private LinearLayout ll_find_map;
    private TextView gaode_map,baidu_map,guge_map;
    View item_dialog_map;
    TextView canclePic;
    Dialog showMapDialog;

    /*以下是高德地图*/
    AMap aMap;
    MapView mMapView = null;
    private String city, province, adCode;
    private double latitude, longitude;
    private double targetLatitude, targetLongitude;
    private GeocodeSearch geocoderSearch;
    private ImageView img_find_map_back;
    private ImageView img_find_map_location;

    private boolean isFirst = true;             //是否是第一次进入本界面
    private boolean isMaps = true;              //是否展示的是地图

    private String dname="";

    private Bean.ElectricPileListModel dataList;
    private List<Marker> makerList=new ArrayList<>();
    private Map<String, PoiItem> pileMap=new HashMap<String, PoiItem>();
    private List<Map<String,Object>> pileListData = new ArrayList<>();

    private Animation inAnim, upAnim;
    private double lat;
    private double lng;
    private PoiItem poiItem;
    private int index = 1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_find_map);

        title.setText("附近超市");

        nrv_find_map = findViewById(R.id.nrv_find_map);
        cet_search_content = findViewById(R.id.cet_search_content);
        ll_find_map = findViewById(R.id.ll_find_map);
        img_find_map_back = findViewById(R.id.img_find_map_back);
        img_find_map_location = findViewById(R.id.img_find_map_location);


        if(null != getIntent()){
            lat = getIntent().getDoubleExtra("Latitude", App.lat);
            lng = getIntent().getDoubleExtra("Longitude", App.lng);
            poiItem = (PoiItem) getIntent().getSerializableExtra("PoiItem");
        }

        mMapView = findViewById(R.id.find_map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        geocoderSearch = new GeocodeSearch(FindMapActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);
        initListener();
        initMapData();//初始化地图数据

        //动画
        inAnim = AnimationUtils.loadAnimation(FindMapActivity.this, R.anim.top_in);
        upAnim = AnimationUtils.loadAnimation(FindMapActivity.this, R.anim.top_out);
        upAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_find_map.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        checkMaps();

        showMapDialog = new Dialog(FindMapActivity.this, R.style.processDialog);
        showMapDialog.setContentView(item_dialog_map);
        showMapDialog.setCanceledOnTouchOutside(true);
        Window window = showMapDialog.getWindow();
        //底部弹出
        window.setGravity(Gravity.BOTTOM);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //地图弹窗结束
    }

    @Override
    protected void initListener() {
        //地图弹窗开始
        item_dialog_map = LayoutInflater.from(FindMapActivity.this).inflate(R.layout.item_dialog_map, null);

        canclePic = item_dialog_map.findViewById(R.id.canclePic);

        guge_map = item_dialog_map.findViewById(R.id.guge_map);
        gaode_map = item_dialog_map.findViewById(R.id.gaode_map);
        baidu_map = item_dialog_map.findViewById(R.id.baidu_map);

        img_find_map_back.setOnClickListener(this);
        img_find_map_location.setOnClickListener(this);
        guge_map.setOnClickListener(this);
        gaode_map.setOnClickListener(this);
        baidu_map.setOnClickListener(this);
        canclePic.setOnClickListener(this);

        cet_search_content.setOnMyTextAfterEditListener(new ClearEditText.OnMyTextAfterEditListener() {
            @Override
            public void OnAfterEdit() {

            }

            @Override
            public void onMyTextChanged(CharSequence text) {
                final String newText = text.toString().trim();
                if (!StringUtil.isSpace(newText)) {
                    ll_find_map.setVisibility(View.VISIBLE);
                    getSuperMarketList(newText, index);
                }
                else {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {

        adapter = new FindMapAdapter(FindMapActivity.this, list);
        nrv_find_map.setAdapter(adapter);
        adapter.setOnItemClickListener(new FindMapAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {

                lat = ((LatLonPoint)list.get(position).get("getLatLonPoint")).getLatitude();
                lng = ((LatLonPoint)list.get(position).get("getLatLonPoint")).getLongitude();

                LatLng latLng = new LatLng(lat, lng);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  18.5f));
                clearSearch();
            }
        });

        /*if(null != getIntent()){
            MarkerOptions option = new MarkerOptions();
            option.position(new LatLng(lat, lng));

            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_dingwei);
            option.icon(bitmapDescriptor);
            option.draggable(true);
            Marker marker = aMap.addMarker(option);
            makerList.add(marker);
            pileMap.put(marker.getId(), poiItem);

            curShowWindowMarker=marker;
            dname=pileMap.get(marker.getId())==null?"目的地":pileMap.get(marker.getId()).getTitle();
            dingweiBoo = false;
            marker.showInfoWindow();
            aMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        }*/

        getSuperMarketList("", index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_find_map_back:
                finish();
                break;
            case R.id.img_find_map_location:
                float zoom = aMap.getCameraPosition().zoom;

                LatLng latLng = new LatLng(latitude, longitude);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                break;
            case R.id.guge_map:
                showMapDialog.dismiss();
                Uri gmmIntentUri = Uri.parse("google.navigation:q="
                        + targetLatitude + "," + targetLongitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                        gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.gaode_map:
                Intent intent=new Intent();
                try {
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    Uri gmIntentUri = Uri.parse("amapuri://route/plan/?slat="+ latitude +"&slon="+ longitude +"&sname=我的位置"+
                            "&dlat="+targetLatitude+"&dlon="+targetLongitude+"&dname="+dname+"&dev=0&t=0");
                    intent.setData(gmIntentUri);
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("地图跳转失败");
                }
                showMapDialog.dismiss();
                break;
            case R.id.baidu_map:
                showMapDialog.dismiss();
                Intent i1 = new Intent();
                // 驾车导航
                i1.setData(Uri.parse("baidumap://map/navi?location="+targetLatitude+","+targetLongitude));
                startActivity(i1);
                break;
            case R.id.canclePic:
                showMapDialog.dismiss();
                break;
        }
    }

    private boolean dingweiBoo = true;
    private Marker curShowWindowMarker;

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
                    LatLng latLng = new LatLng(lat, lng);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  18.5f));
                    isFirst=false;
//
////                    getData();
                }
                latitude=location.getLatitude();
                longitude=location.getLongitude();

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
                dname=pileMap.get(marker.getId())==null?"目的地":pileMap.get(marker.getId()).getTitle();
                dingweiBoo = false;
//                markerGuDing.setPosition(markerGuDing.getPosition());
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
                aMap.animateCamera(CameraUpdateFactory.zoomTo(18.5f));
                dingweiBoo = true;
            }
        });

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (!pileMap.containsKey(marker.getId())) {
                    return null;
                }
                View infoWindow = getLayoutInflater().inflate(R.layout.item_marker_find_map, null);
                PoiItem ep= pileMap.get(marker.getId());
                render(marker, infoWindow,ep);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
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
//                    te_city.setText(StringUtil.cutStr(city,5));
//                    LatLng latLng = new LatLng(latitude, longitude);
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  17.5f));
//                    getData();
                } else {
                    toast("定位错误");
                }
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    private void clearSearch() {
        ll_find_map.setVisibility(View.GONE);
        cet_search_content.clearFocus();
        cet_search_content.setText("");
        InputTools.HideKeyboard(cet_search_content);
    }

    private void removeAllMakers() {
        for (int i = 0; i < makerList.size(); i++) {
            makerList.get(i).remove();
        }
    }

    /**
     * 自定义infowinfow窗口
     */
    @SuppressLint("SetTextI18n")
    public void render(final Marker marker, View view, final PoiItem ep) {
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
                checkMaps();
                showMapDialog.show();
                targetLatitude=marker.getPosition().latitude;
                targetLongitude=marker.getPosition().longitude;
            }
        });
        final TextView juli=view.findViewById(R.id.juli);
        TextView pileName=view.findViewById(R.id.pileName);
        TextView price=view.findViewById(R.id.price);
        DistanceSearch distanceSearch = new DistanceSearch(FindMapActivity.this);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                int length =0;
               /* if (i==1000&&distanceResult.getDistanceResults().size()>0){
                    length=(int)(distanceResult.getDistanceResults().get(0).getDistance());
                }else {
                    toast("没有计算出驾车距离,已使用直线距离");*/
                    length =ep.getDistance();
//                }
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
        pileName.setText(ep.getTitle());
        price.setText(ep.getProvinceName()+ep.getCityName()+ep.getAdName() + ep.getSnippet());
    }

    private void checkMaps() {
        //判断地图是否存在
        if (PakageUtil.isAvilible(FindMapActivity.this, "com.autonavi.minimap")) {
            gaode_map.setVisibility(View.VISIBLE);
        } else {
            gaode_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(FindMapActivity.this, "com.baidu.BaiduMap")) {
            baidu_map.setVisibility(View.VISIBLE);
        } else {
            baidu_map.setVisibility(View.GONE);
        }
        if (PakageUtil.isAvilible(FindMapActivity.this, "com.google.android.apps.maps")) {
            guge_map.setVisibility(View.VISIBLE);
        } else {
            guge_map.setVisibility(View.GONE);
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 获取超市列表
     * @param newText
     * @param index
     */
    private void getSuperMarketList(final String newText, final int index){
        if (NetUtil.isNetWorking(FindMapActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    PoiSearch.Query query = new PoiSearch.Query("超市", "", App.city);
                    PoiSearch poiSearch = new PoiSearch(FindMapActivity.this, query);
                    query.setPageSize(100);// 设置每页最多返回多少条poiitem
//                    query.setPageNum(index);//设置查询页码
                    query.setDistanceSort(true);
                    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(App.mineLocation.lat, App.mineLocation.lng), 3000));

                    poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            if (1000 == i) {

                                if (!mySmart.isLoading()){
                                    list.clear();
                                }

                                if(isMaps) {
                                    removeAllMakers();
                                    pileMap.clear();
                                }
                                pileListData.clear();

                                Log.i("111111111", "onPoiSearched: " + poiResult.getPois().size());
                                for (int j = 0; j < poiResult.getPois().size(); j++) {
                                    if (isMaps) {
                                        LatLng latLng = new LatLng(poiResult.getPois().get(j).getLatLonPoint().getLatitude(), poiResult.getPois().get(j).getLatLonPoint().getLongitude());

                                        Log.i("111111111", "onPoiSearched: "+ poiResult.getPois().get(j).getLatLonPoint().getLatitude() + "-------"+poiResult.getPois().get(j).getLatLonPoint().getLongitude());


                                        if (lat == poiResult.getPois().get(j).getLatLonPoint().getLatitude()&& lng == poiResult.getPois().get(j).getLatLonPoint().getLongitude()){
                                            MarkerOptions option = new MarkerOptions();
                                            option.position(latLng);

                                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_add_dingwei);
                                            option.icon(bitmapDescriptor);
                                            option.draggable(true);
                                            Marker marker = aMap.addMarker(option);
                                            makerList.add(marker);
                                            pileMap.put(marker.getId(), poiResult.getPois().get(j));

                                            curShowWindowMarker=marker;
                                            dname=pileMap.get(marker.getId())==null?"目的地":pileMap.get(marker.getId()).getTitle();
                                            dingweiBoo = false;
                                            marker.showInfoWindow();
                                            aMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                                        }
                                    }

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("getTitle", poiResult.getPois().get(j).getTitle());
                                    map.put("getCityName", poiResult.getPois().get(j).getCityName());
                                    map.put("getAdName", poiResult.getPois().get(j).getAdName());
                                    map.put("getSnippet", poiResult.getPois().get(j).getSnippet());
                                    map.put("getDistance", poiResult.getPois().get(j).getDistance());
                                    map.put("getLatLonPoint", poiResult.getPois().get(j).getLatLonPoint());
                                    list.add(map);
                                }
                                adapter.notifyDataSetChanged();

                            }else {

                            }
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

    public boolean onBack() {
        if (isMaps){
            return false;
        }else {
            ll_find_map.startAnimation(upAnim);
            isMaps = true;
            return true;
        }
    }

}

package com.tangchaoke.electrombilechargingpile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.adapter.SC_CityRecyclerAdapter;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.City;
import com.tangchaoke.electrombilechargingpile.bean.CityModel;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.addapp.pickers.util.ConvertUtils;

public class CitySelectActivity extends BaseActivity {
    private RecyclerView sortListView;
    private SideBar sideBar;
    private TextView dialog,back;
    private SC_CityRecyclerAdapter adapter;
//    private ClearEditText mClearEditText;
    private String city;
    private double lat;
    private double lng;

    /**
     * 汉字转换成拼音的类
     */
//    private CharacterParser characterParser;
    private List<City> sourceDateList =new ArrayList<>();
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_city_select);
        sideBar = findViewById(R.id.sidebar);
        dialog = findViewById(R.id.contact_dialog);
        back = findViewById(R.id.back);
        sortListView = findViewById(R.id.citys);
        sideBar.setTextView(dialog);
//        mClearEditText = findViewById(R.id.words);
//        characterParser=new CharacterParser();
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
//        sourceDateList = filledData(getResources().getStringArray(R.array.cities));
        sourceDateList = filledData();
        adapter = new SC_CityRecyclerAdapter(CitySelectActivity.this, sourceDateList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CitySelectActivity.this);
        sortListView.setLayoutManager(linearLayoutManager);
        sortListView.setAdapter(adapter);

        adapter.setOnCityClickListener(new SC_CityRecyclerAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name,double lat,double lng) {
                try {
                    JSONObject jsonObject=new JSONObject((String) SPUtil.getData(CitySelectActivity.this,"changyongCity","{\"cities\":[{\"name\":\"郑州市\",\"lat\":34.754125,\"lng\":113.666051}]}"));
                    JSONArray array=jsonObject.optJSONArray("cities");
                    boolean flag=true;
                    for (int i = 0; i <array.length() ; i++) {
                        if (array.optJSONObject(i).optString("name").equals(name)){
                            flag=false;
                            break;
                        }
                    }
                    if (flag) {
                        JSONObject object=new JSONObject();
                        object.put("name",name);
                        object.put("lat",lat);
                        object.put("lng",lng);
                        array.put(object);
                    }
                    if (array.length()>3){
                        array.remove(0);
                    }
                    jsonObject.put("cities",array);
                    SPUtil.saveData(CitySelectActivity.this,"changyongCity",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent();
//                intent.putExtra("provinceStr",provinceStr);
                intent.putExtra("cityStr",name);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                App.city =name;
//                intent.putExtra("districtStr",data.get(position).get("area"));
                setResult(888,intent);
//                        App.finishAllActivity();
                finish();
            }

            @Override
            public void onLocateClick() {
                //重新定位
                mlocationClient.startLocation();
                adapter.updateLocateState(SC_CityRecyclerAdapter.LOCATING, "正在定位…",0,0);
            }
        });

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s);
                if (position != -1) {
//                    mRecyCity.scrollToPosition(position);
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

//        mlocationClient = new AMapLocationClient(CitySelectActivity.this);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置返回地址信息，默认为true
//        mLocationOption.setNeedAddress(true);
//        //设置定位监听
//        mlocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation amapLocation) {
//                if (amapLocation != null) {
//                    if (amapLocation.getErrorCode() == 0) {
//                        //定位成功回调信息，设置相关消息
////                        amapLocation.getCity();//城市信息
//                        adapter.updateLocateState(SC_CityRecyclerAdapter.SUCCESS, amapLocation.getCity());
//                        Log.e("onLocationChanged",amapLocation.getCountry()+amapLocation.getProvince()+amapLocation.getCity()+amapLocation.getDistrict());
//                        //                        amapLocation.getAOIName();//获取当前定位点的AOI信息
//                    } else {
//                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError","location Error, ErrCode:"
//                                + amapLocation.getErrorCode() + ", errInfo:"
//                                + amapLocation.getErrorInfo());
//                        adapter.updateLocateState(SC_CityRecyclerAdapter.FAILED, amapLocation.getCity());
//                    }
//                }
//            }
//        });
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
//        //设置定位参数
//        mlocationClient.setLocationOption(mLocationOption);
//        adapter.updateLocateState(SC_CityRecyclerAdapter.LOCATING, "正在定位…");
//        mlocationClient.startLocation();


        mlocationClient = new AMapLocationClient(CitySelectActivity.this);
        mLocationOption = new AMapLocationClientOption();//初始化定位参数
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setOnceLocation(true);//设置是否只定位一次,默认为false
        mLocationOption.setWifiActiveScan(true);//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setMockEnable(false);//设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setInterval(2000);//设置定位间隔,单位毫秒,默认为2000ms
        mlocationClient.setLocationOption(mLocationOption);//给定位客户端对象设置定位参数
        adapter.updateLocateState(SC_CityRecyclerAdapter.LOCATING, "正在定位…",0,0);
//        mlocationClient.startLocation();//启动定位
        //设置定位回调监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        adapter.updateLocateState(SC_CityRecyclerAdapter.SUCCESS, StringUtil.cutStr(aMapLocation.getCity(),5),aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        Log.e("onLocationChanged",aMapLocation.getCountry()+aMapLocation.getProvince()+aMapLocation.getCity()+aMapLocation.getDistrict());
                    }else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        city=getIntent().getStringExtra("city");
        lat=getIntent().getDoubleExtra("lat",0d);
        lng=getIntent().getDoubleExtra("lng",0d);
        adapter.updateLocateState(SC_CityRecyclerAdapter.SUCCESS, StringUtil.cutStr(city,5),lat,lng);
    }

//    @Override
//    protected void initEvent() {
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    public static String decodeUnicode(String unicode) {
        StringBuffer sb = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            sb.append((char) data);
        }
        return sb.toString();
    }

    /**
     * 为ListView填充数据
     */
    private List<City> filledData(){
        CityModel cityModel=new CityModel();
        try {
            String jsonCity=ConvertUtils.toString(getAssets().open("city2.json"));
            cityModel =new Gson().fromJson(jsonCity,CityModel.class);
//            JSONArray array=new JSONObject(jsonCity).optJSONArray("cities");
//            for (int i = 0; i < array.length(); i++) {
//                array.getJSONObject(i).put("name",decodeUnicode(array.getJSONObject(i).optString("name")));
//            }
//            jsonCity=array.toString();
//            Log.e("jsonCity",jsonCity);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<City> mSortList = new ArrayList<>();
        List<City> mSortList = cityModel.cities;
//        for (String name : date) {
//            //汉字转换成拼音
//            String pinyin = characterParser.getSelling(name);
//
//            City city = new City(name, pinyin);
//            mSortList.add(city);
//        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new CityComparator());
        return mSortList;
    }

    /**
     * a-z排序
     */
    public static class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.pinyin.substring(0, 1);
            String b = rhs.pinyin.substring(0, 1);
            return a.compareTo(b);
        }
    }
}

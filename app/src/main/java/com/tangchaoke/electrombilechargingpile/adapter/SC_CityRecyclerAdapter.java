package com.tangchaoke.electrombilechargingpile.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.bean.City;
import com.tangchaoke.electrombilechargingpile.bean.CityModel;
import com.tangchaoke.electrombilechargingpile.bean.Contact;
import com.tangchaoke.electrombilechargingpile.util.PinyinUtils;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.view.CharacterParser;
import com.tangchaoke.electrombilechargingpile.view.ClearEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * kylin on 2017年12月23日16:32:51
 */

public class SC_CityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    private static final int VIEW_TYPE_COUNT = 5;
    private Context mContext;
    private List<City> mCitys;
    private List<String> characterList; // 字母List
    private List<Contact> resultList; // 最终结果（包含分组的字母）

    private List<City> mHotCitys,changyongCitys;

    private HashMap<String, Integer> letterIndexes;

    private OnCityClickListener onCityClickListener;

    public static final int LOCATING    = 111;
    public static final int FAILED      = 666;
    public static final int SUCCESS     = 888;
    private int locateState = LOCATING;
    private String locatedCity;
    private double lat;
    private double lng;

    private final int ITEM_TYPE_CHARACTER = 5;
    private final int ITEM_TYPE_CITY = 6;
    private List<Contact> SourceDateList=new ArrayList<>();
    private CharacterParser characterParser;


    public SC_CityRecyclerAdapter(Context mContext, List<City> mCitys) {
        this.mContext = mContext;
        this.mCitys = (mCitys == null ? new ArrayList<City>() : mCitys);
        mLayoutInflater = LayoutInflater.from(mContext);

        this.mCitys.add(0, new City("搜索", "0"));
        this.mCitys.add(1, new City("定位", "1"));
        this.mCitys.add(2, new City("常用", "2"));
        this.mCitys.add(3, new City("最热", "3"));
        characterParser=new CharacterParser();

        handleCity();
    }

    private void handleCity() {
        resultList = new ArrayList<>();
        characterList = new ArrayList<>();
        letterIndexes = new HashMap<>();
        initData(mCitys);


        mHotCitys = new ArrayList<>();
        mHotCitys.add(new City("北京市","",39.904172,116.407417));
        mHotCitys.add(new City("上海市","",31.230378,121.473658));
        mHotCitys.add(new City("深圳市","",22.547,114.085947));
        mHotCitys.add(new City("广州市","",23.129112,113.264385));
        mHotCitys.add(new City("杭州市","",30.245853,120.209947));
        mHotCitys.add(new City("天津市","",39.085294,117.201538));
        mHotCitys.add(new City("武汉市","",30.592935,114.305215));
        mHotCitys.add(new City("成都市","",30.573095,104.066143));
        mHotCitys.add(new City("西安市","",34.343147,108.939621));

        changyongCitys = new ArrayList<>();

        try {
            String changyongStr=(String) SPUtil.getData(mContext,"changyongCity","");
            if (StringUtil.isSpace(changyongStr)) {
                JSONObject jsonObject=new JSONObject();
                JSONArray array = new JSONArray();
                JSONObject object=new JSONObject();
                object.put("name",locatedCity);
                object.put("lat",lat);
                object.put("lng",lng);
                array.put(object);
                jsonObject.put("cities",array);
                changyongStr=jsonObject.toString();
            }
            Log.e("changyongStr",changyongStr);

            CityModel cityModel =  new Gson().fromJson(changyongStr,CityModel.class);
            changyongCitys=cityModel.cities;
            Collections.reverse(changyongCitys); // 倒序排列
//            changyongCitys=beanCity.changyongCitys;
        } catch (Exception e) {
            e.printStackTrace();
        }

        SourceDateList.addAll(resultList);
    }

    private void initData(List<City> list) {


        letterIndexes.put("搜索", 0);
        letterIndexes.put("定位", 1);
        letterIndexes.put("常用", 2);
        letterIndexes.put("最热", 3);

        for (int i = 4; i < list.size(); i++) {
            //当前城市拼音首字母
            String name = PinyinUtils.getFirstLetter(list.get(i).pinyin);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            double lat = (list.get(i).lat);
            double lng = (list.get(i).lng);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new Contact(character, ITEM_TYPE_CHARACTER,lat,lng));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new Contact("#", ITEM_TYPE_CHARACTER,lat,lng));
                    }
                }
            }
            resultList.add(new Contact(list.get(i).name, ITEM_TYPE_CITY,lat,lng));
        }

        for (int i = 0; i < resultList.size(); i++) {
            Contact contact = resultList.get(i);
            if (contact.getmType()==ITEM_TYPE_CHARACTER){
                letterIndexes.put(contact.getmName(),i+4);
            }
        }
    }

    public void setData(List<City> mCitys){
        this.mCitys.clear();

        this.mCitys.add(0, new City("搜索", "0"));
        this.mCitys.add(1, new City("定位", "1"));
        this.mCitys.add(2, new City("常用", "2"));
        this.mCitys.add(3, new City("最热", "3"));
        this.mCitys.addAll(mCitys);

        resultList.clear();
        initData(this.mCitys);
        notifyDataSetChanged();
    }


    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city,double lat,double lng) {
        this.locateState = state;
        this.locatedCity = city;
        this.lat = lat;
        this.lng = lng;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CHARACTER) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_sc_character, parent, false));
        } else if (viewType == ITEM_TYPE_CITY) {
            View allCityView = mLayoutInflater.inflate(R.layout.item_sc_city, parent, false);

            return new CityHolder(allCityView);
        } else if (viewType == 0) {//搜索
            View searchView = mLayoutInflater.inflate(R.layout.item_sc_search_city, parent, false);
            return new SearchViewHolder(searchView);
        } else if (viewType == 1) {//定位
            View locateView = mLayoutInflater.inflate(R.layout.item_sc_locate_city, parent, false);
            locateView.findViewById(R.id.tv_located_city).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (locateState == FAILED) {
                        //重新定位
                        if (onCityClickListener != null) {
                            onCityClickListener.onLocateClick();
                        }
                    } else if (locateState == SUCCESS) {
                        //返回定位城市
                        if (onCityClickListener != null) {
                            onCityClickListener.onCityClick(locatedCity,lat,lng);
                        }
                    }
                }
            });
            return new LocateViewHolder(locateView);
        } else if (viewType == 2) {//常用
            return new ChangYongHolder(mLayoutInflater.inflate(R.layout.item_sc_hot_city, parent, false), mContext);
        } else {//最热
            return new HotCityHolder(mLayoutInflater.inflate(R.layout.item_sc_hot_city, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mCharater.setText(resultList.get(position - 4).getmName());
        } else if (holder instanceof CityHolder) {
            ((CityHolder) holder).mCityName.setText(resultList.get(position - 4).getmName());
            ((CityHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //返回定位城市
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(resultList.get(position - 4).getmName(),resultList.get(position - 4).lat,resultList.get(position - 4).lng);
                    }
                }
            });
        } else if (holder instanceof LocateViewHolder) {
            ((LocateViewHolder) holder).mTvLocatedCity.setText(locatedCity);
        } else if (holder instanceof HotCityHolder) {
            ((HotCityHolder) holder).setData(mHotCitys, onCityClickListener);
            ((HotCityHolder) holder).character.setText("热门城市");
        } else if (holder instanceof ChangYongHolder) {
            ((ChangYongHolder) holder).setData(changyongCitys, onCityClickListener);
            ((ChangYongHolder) holder).character.setText("常用城市");
        } else if (holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).words.setOnMyTextAfterEditListener(new ClearEditText.OnMyTextAfterEditListener() {
                @Override
                public void OnAfterEdit() {

                }

                @Override
                public void onMyTextChanged(CharSequence text) {
                    String newText = text.toString().trim();
                    filterData(newText);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return resultList.size() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : resultList.get(position - 4).getmType();
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name,double lat,double lng);

        void onLocateClick();
    }


    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getPositionForSection(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mCharater;
        CharacterHolder(View itemView) {
            super(itemView);
            mCharater = (TextView) itemView.findViewById(R.id.character);
        }
    }

    public class CityHolder extends RecyclerView.ViewHolder {
        TextView mCityName;
        CityHolder(View itemView) {
            super(itemView);
            mCityName = (TextView) itemView.findViewById(R.id.city_name);
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
//        TextView cancel;
        ClearEditText words;
        SearchViewHolder(View itemView) {
            super(itemView);
            words = (ClearEditText) itemView.findViewById(R.id.words);
//            cancel = (TextView) itemView.findViewById(R.id.cancel);
        }
    }

    public class LocateViewHolder extends RecyclerView.ViewHolder {
        TextView mTvLocatedCity;
        LocateViewHolder(View itemView) {
            super(itemView);
            mTvLocatedCity = itemView.findViewById(R.id.tv_located_city);
        }
    }

    public class ChangYongHolder extends RecyclerView.ViewHolder {
        private final RecyclerView changyongCity;
        private TextView character;
        private Context mContext;

        ChangYongHolder(View itemView, Context mContext) {
            super(itemView);
            changyongCity = itemView.findViewById(R.id.recy_hot_city);
            character = itemView.findViewById(R.id.character);
            this.mContext =mContext;
            changyongCity.setLayoutManager(new GridLayoutManager(mContext,3));
        }

        void setData(List<City> mchangyongCity, SC_CityRecyclerAdapter.OnCityClickListener listener){
            SC_HotCityAdapter hotCityAdapter = new SC_HotCityAdapter(mContext, mchangyongCity,listener);
            changyongCity.setAdapter(hotCityAdapter);
        }
    }

    public class HotCityHolder extends RecyclerView.ViewHolder {
        private final RecyclerView mRecyHotCity;
        private TextView character;
        private Context mContext;

        HotCityHolder(View itemView, Context mContext) {
            super(itemView);
            mRecyHotCity = (RecyclerView) itemView.findViewById(R.id.recy_hot_city);
            character = (TextView) itemView.findViewById(R.id.character);
            this.mContext =mContext;
            mRecyHotCity.setLayoutManager(new GridLayoutManager(mContext,3));
        }

        void setData(List<City> mHotCity, SC_CityRecyclerAdapter.OnCityClickListener listener){
            SC_HotCityAdapter hotCityAdapter = new SC_HotCityAdapter(mContext, mHotCity,listener);
            mRecyHotCity.setAdapter(hotCityAdapter);
        }
    }

    class BeanCity{
        List<String> changyongCitys;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<City> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            resultList.clear();
            resultList.addAll(SourceDateList);
            notifyDataSetChanged();
        } else {
            for (City city : mCitys) {
                String name = city.name;
                if ((name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr))
                        &&!name.contains("最热")
                        &&!name.contains("常用")
                        &&!name.contains("定位")
                        &&!name.contains("搜搜")) {
                    filterDateList.add(city);
                }
            }
            setData(filterDateList);
        }
    }
}

package com.tangchaoke.electrombilechargingpile.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.thread.HttpInterface;

/**
 * kylin on 2017/12/12.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected HttpInterface httpInterface;

    protected RelativeLayout top;   //标题栏

    protected LinearLayout back; //返回按钮
    protected TextView backText; //返回布局右边文字

    protected TextView title;   //中间标题

    protected ImageView imgSetting; //设置按钮
    protected ImageView imgShouCang;   //收藏按钮
    protected ImageView imgShare; //分享按钮
    protected TextView rightTxt; //发布
    protected ImageView imgShare_more;  //更多分享
    protected ImageView img_back;       //左边图片

    protected LinearLayout rightBtnLayer;   //四个按钮父布局

    public SmartRefreshLayout mySmart;   //子布局SmartRefreshLayout
    protected FrameLayout container;   //子布局

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setTheme(R.style.LowTheme);
        }
        setContentView(R.layout.activity_base);
        httpInterface = new HttpInterface(this);

        top = findView(R.id.top);   //标题栏

        imgSetting = findView(R.id.imgSetting); //设置按钮
        imgShouCang = findView(R.id.imgShouCang);   //收藏按钮
        imgShare = findView(R.id.imgShare); //分享按钮
        rightTxt = findView(R.id.rightTxt); //发布
        imgShare_more = findView(R.id.imgShare_more); //更多分享按钮

        back = findView(R.id.back); //返回按钮

        title = findView(R.id.title);   //中间标题
        img_back = findView(R.id.img_back);   //中间标题


        backText = findView(R.id.backText); //返回布局右边文字
        rightBtnLayer = findView(R.id.rightBtnLayer);   //四个按钮父布局
        mySmart = findView(R.id.mySmart);   //子布局SmartRefreshLayout
        mySmart.setEnableAutoLoadmore(false);
        container = findView(R.id.container);   //子布局
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView(savedInstanceState);
        initListener();
        initData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 时间监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onClick(View v) {
    }

    protected void setContainer(int layoutId){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutId,container,true);
    }

    protected void setTopTitle(String title){
        this.title.setText(title);
    }

    /**
     * 弹吐司
     */
    public void toast(Object message) {
        Toast toast = Toast.makeText(this, message + "", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void finishRefresh(){
        mySmart.finishRefresh();
        mySmart.finishLoadmore();
    }

    /**
     * findViewById
     */
    public <T extends View> T findView(int resId) {
        return (T) super.findViewById(resId);
    }
}

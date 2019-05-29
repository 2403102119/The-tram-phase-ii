package com.tangchaoke.electrombilechargingpile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tangchaoke.electrombilechargingpile.activity.PaymentActivity;
import com.tangchaoke.electrombilechargingpile.activity.RechargeActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * kylin on 2018/5/4.
 */

public class App extends Application {
    public static App app;
    public static boolean isDebug = true;
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());
    public static String city ="";
    public static double lat=0;
    public static double lng=0;
    public static String account="";

    public static String token;

    public static boolean islogin;

    public static Bean.Init_data loginMsg;

    public static double balance;

    public static boolean isBindAlias = false;          //是否已绑定别名

    public static Bean.Location location;
    public static Bean.Location mineLocation;           //定位坐标

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        token = SPUtil.getData(this, "Authorization", "")+"";
        islogin = (boolean) SPUtil.getData(this, "islogin", false);
        city = SPUtil.getData(this, "city", "郑州")+"";
        lat = Double.parseDouble(SPUtil.getData(this, "lat", 0)+"");
        lng = Double.parseDouble(SPUtil.getData(this, "lng", 0)+"");

        location = SPUtil.getBeanFromSp(this, "location", "location");
        mineLocation = SPUtil.getBeanFromSp(this, "mineLocation", "mineLocation");

        isBindAlias = (boolean) SPUtil.getData(this,"isBindAlias", false);
        account = SPUtil.getData(this, "account", "")+"";

        loginMsg = SPUtil.getBeanFromSp(this, "loginMsg", "loginMsg");
        registerActivityListener();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (!(activity.getClass().equals(MainActivity.class)))
                activity.finish();
        }
        mActivitys.clear();
    }

    @SuppressLint("ObsoleteSdkInt")
    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /*
                     *  监听到 Activity创建事件 将该 Activity 加入list,MainActivity主页面不加入
                     */
//                    if (!(activity.getClass().equals(MainActivity.class)))
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /*
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    public static void dealWithPayResult(){
        for (Activity a:
             mActivitys) {
            if (a instanceof RechargeActivity){
                a.finish();
            }
        }
    }
}

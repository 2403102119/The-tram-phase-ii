package com.tangchaoke.electrombilechargingpile.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.activity.ChargingActivity;
import com.tangchaoke.electrombilechargingpile.activity.MyMessageActivity;
import com.tangchaoke.electrombilechargingpile.activity.OrderDetailActivity;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static final String TYPE_THIS = "test1";
    private static final String TYPE_ANOTHER = "test2";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.i(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的通知");

            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "用户点击打开了通知");

            openNotification(context,bundle);

        } else {
            Log.i(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("key");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        if (App.islogin) {
            if ("0".equals(myValue)) {      //订单消息
                String orderID = (String) bundle.get("orderID");
                String orderStatus = (String) bundle.get("orderStatus");
                Intent mIntent;
                if (!StringUtil.isSpace(orderID) && !StringUtil.isSpace(orderStatus)) {
                    if ("0".equals(orderStatus)) {
                        mIntent = new Intent(context, ChargingActivity.class);
                    } else {
                        mIntent = new Intent(context, OrderDetailActivity.class);
                    }
                    mIntent.putExtra("orderID", orderID);
                } else {
                    mIntent = new Intent(context, MyMessageActivity.class);
                }

                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
            } else if ("1".equals(myValue)) {        //系统消息
                Intent mIntent = new Intent(context, MyMessageActivity.class);
                mIntent.putExtras(bundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
            }
        }
    }
}
package com.tangchaoke.electrombilechargingpile.thread;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tangchaoke.electrombilechargingpile.R;


/**
 * 网络连接工具类
 */
public class NetUtil {
    /**
     * 是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean isNetWorking(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.system_busy), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

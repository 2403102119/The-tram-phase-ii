package com.tangchaoke.electrombilechargingpile.thread;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.activity.LoginActivity;
import com.tangchaoke.electrombilechargingpile.util.SharedPreferencesUtil;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;


/**
 * Created by Administrator on 2018/1/4.
 */

public class TokenErrorUtil {
    private LayoutInflater inflater;

    public TokenErrorUtil(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    /*目前使用的是这个*/
    public void tokenError(final Context context) {
        boolean isLogin = (boolean) SharedPreferencesUtil.getData(context, UriUtil.isLogin, false);
        if (!isLogin) {
            return;
        }
        SharedPreferencesUtil.saveData(context, UriUtil.isLogin, false);
        SharedPreferencesUtil.saveData(context, UriUtil.token, "");
//        setAlias(context, "*");
        View tokenErrorView = inflater.inflate(R.layout.token_error, null);
        TextView goToLogin = (TextView) tokenErrorView.findViewById(R.id.goToLogin);
        TextView cancel = (TextView) tokenErrorView.findViewById(R.id.cancel);
        AlertDialog.Builder loginTipBuilder = new AlertDialog.Builder(context);
        loginTipBuilder.setView(tokenErrorView);
        final AlertDialog tokenErrorDialog = loginTipBuilder.create();
        tokenErrorDialog.setCancelable(false);
        tokenErrorDialog.show();
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenErrorDialog.dismiss();
                toLogin(context, 0);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenErrorDialog.dismiss();
                toLogin(context, 1);
            }
        });
    }

    private void toLogin(Context context, int flag) {
        //清除登录信息
        SharedPreferencesUtil.saveData(context, UriUtil.isLogin, false);

        //清空Activity栈
        App.finishAllActivity();
        if (flag == 0) {
            //跳转到登录界面
            Intent intent2 = new Intent(context, LoginActivity.class);
            context.startActivity(intent2);
        }
    }
}

package com.tangchaoke.electrombilechargingpile.thread;

import okhttp3.Call;

public abstract class MApiResultCallback {
    public void inProgress(float progress){};
    public abstract void onSuccess(String result);//获取成功
    public abstract void onFail(String response);//联网成功，返回的不是成功数据。eg 注册时已存在
    public abstract void onError(Call call, Exception exception);//请求出错  OKHTTP  的回调
    public abstract void onTokenError(String response);
}

package com.tangchaoke.electrombilechargingpile.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;


import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * kylin on 2017/6/26.
 */

public class UserClient {

    public static final int SHOW_LOADING = 0x10;
    public static final int DIMISS_LOADING = 0x11;
//    public static final int POST = 0x00;
//    public static final int PUT = 0x01;

    private Class<?> mClass;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private List<Pair<String, File>> filePairList;

    private String urlLink;

    private int responseCode;
    private String message;

    private String response = "";

    public String httpurl;

    final Handler mDeliver = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LOADING:
                    if (loadingDialog != null) {
                        loadingDialog.show();
                    }
                    break;
                case DIMISS_LOADING:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private File file;
    private Response fileResponse;
    private LoadingDialog loadingDialog;
    private String json="";
    private RequestMethod REQUEST_TYPE = RequestMethod.POST;

    public enum RequestMethod {
        POST, PUT, GET, DELETE, PATCH
    }

    public UserClient(String url, Class<?> mClass) {
        this.urlLink = url;
        params = new HashMap<String, String>();
        headers = new HashMap<String, String>();
        filePairList = new ArrayList<Pair<String, File>>();
        this.mClass = mClass;
    }

    public UserClient(String url) {
        this.urlLink = url;
        params = new HashMap<String, String>();
        headers = new HashMap<String, String>();
        filePairList = new ArrayList<Pair<String, File>>();
        this.mClass = mClass;
    }

    public void AddParam(String name, String value) {
        params.put(name, value);
    }

    public void AddHeader(String name, String value) {
        headers.put(name, value);
    }

    public void addFiles(Pair<String, File> file) {
        filePairList.add(file);
    }

    public void addJson(String json) {
        this.json = json;
    }

    ;

    public void addRequestType(RequestMethod REQUEST_TYPE) {
        this.REQUEST_TYPE = REQUEST_TYPE;
    }

    private boolean show = true;

    public void executePost(final MApiResultCallback callback, final LoadingDialog loadingDialog, final Context context) throws IOException {
        LoggerUtil.e("userclient token", " " + urlLink);
        show = true;
        if (loadingDialog != null) {
            mDeliver.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (show) {
                        try {
                            loadingDialog.show();
                        } catch (Exception e) {

                        }
                    }
                    //mDeliver.sendEmptyMessageDelayed(SHOW_LOADING,500);
                }
            }, 500);
        }

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = null;
        Request.Builder builder = new Request.Builder().url(urlLink);

        if (App.token != "")
            builder = builder.header("Authorization", "Bearer " + App.token);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        switch (REQUEST_TYPE) {
            case POST:
                request = builder
                        .post(body)
                        .build();
                break;
            case PUT:
                request = builder
                        .put(body)
                        .build();
                break;
            case DELETE:
                request = builder
                        .delete(body)
                        .build();
                break;
            case GET:
                request=builder
                        .get()
                        .build();
                break;
        }
        Log.e("请求：json", "executePost: " + json);

        dealRequest(context, client, request, loadingDialog, callback);

    }

    private void dealRequest(final Context context, OkHttpClient client, final Request request, final LoadingDialog loadingDialog, final MApiResultCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException exception) {
                exception.printStackTrace();
                if (null == request.body()){

                    MUIToast.toast(context, R.string.connect_failed_toast);
                }else {
                    Log.i("onError++++++", "onError: " + request.body().toString());
                }

                show = false;
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    mDeliver.post(new Runnable() {
                        @Override
                        public void run() {
                            //mDeliver.removeMessages(SHOW_LOADING);
                            try {
                                loadingDialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    });
                }
                mDeliver.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!exception.getMessage().contains("cancle")) {
                                MUIToast.toast(context, R.string.connect_failed_toast);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                callback.onError(call, exception);
                callback.onTokenError(response);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();

                show = false;
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    mDeliver.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                loadingDialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    });
                }else {
                }
                switch (REQUEST_TYPE){
                    case POST:
                        if (201 == response.code()|200==response.code()) {
                            //201在post中是成功，其他请求都是200
                            Log.e("成功：数据", "onResponse: " + responseStr);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(responseStr);
                                            callback.onTokenError(responseStr);
                                        }
                                    });
                                }
                            }).start();
                        } else {
                            Log.e("失败：code", "onResponse: " + response.code());
                            Log.e("失败：数据", "onResponse: " + responseStr);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onFail(responseStr);
                                            callback.onTokenError(responseStr);
                                        }
                                    });
                                }
                            }).start();
                        }
                        break;
                    default:
                        if (200 == response.code()|201==response.code()) {
                            //201在post中是成功，其他请求都是200
                            Log.e("成功：数据", "onResponse: " + responseStr);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(responseStr);
                                            callback.onTokenError(responseStr);
                                        }
                                    });
                                }
                            }).start();

                        } else {
                            Log.e("失败：code", "onResponse: " + response.code());
                            Log.e("失败：数据", "onResponse: " + responseStr);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onFail(responseStr);
                                            callback.onTokenError(responseStr);
                                        }
                                    });
                                }
                            }).start();
                        }
                        break;
                }
            }
        });
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
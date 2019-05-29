package com.tangchaoke.electrombilechargingpile.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;
import com.tangchaoke.electrombilechargingpile.view.CircleImageView;
import com.tangchaoke.electrombilechargingpile.view.PickPicDialog;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.Intents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/**
 * Title：个人信息页面
 * Author：李迪迦
 * Date：2019.5.16
 * Description：修改个人信息
 */
public class PersonalInformationActivity extends BaseActivity {
    private CircleImageView iv_personal_img;
    private LinearLayout ll_personal_img, ll_name, ll_gender;
    private TextView tv_personal_name, tv_personal_sex, tv_personal_phone, tv_personal_top_name;
    private PickPicDialog pickPicDialog;
    TextView exit;
//    private String PV = App.loginMsg.province;
    private String CT = App.loginMsg.city;
    private Dialog exitDialog;

    private static final String TAG = "获取.成功";

    /*
    给用户设置别名
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    Log.i("123456789","11111"+msg.obj);
                    Log.i("11111111111", "gotResult: " + new JPushMessage().toString());
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    SPUtil.saveData(PersonalInformationActivity.this,"isBindAlias",true);

                    App.isBindAlias = true;
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            toast(logs);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_personal_information);
        title.setText("个人信息");

        iv_personal_img = findViewById(R.id.iv_personal_img);
        ll_personal_img = findViewById(R.id.ll_personal_img);
        ll_name = findViewById(R.id.ll_name);
        ll_gender = findViewById(R.id.ll_gender);
        tv_personal_name = findViewById(R.id.tv_personal_name);
        tv_personal_sex = findViewById(R.id.tv_personal_sex);
        tv_personal_phone = findViewById(R.id.tv_personal_phone);
        tv_personal_top_name = findViewById(R.id.tv_personal_top_name);
        exit = findView(R.id.exit);


        if (App.islogin) {
            ImageLoadUtil.showImage(PersonalInformationActivity.this, UriUtil.ip + App.loginMsg.headPath, iv_personal_img);
            tv_personal_phone.setText(StringUtil.replacePhone(App.loginMsg.account));
//            tv_personal_top_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.account):App.loginMsg.nickName);
            tv_personal_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.loginMsg.account):App.loginMsg.nickName);
        }

        mySmart.setEnableRefresh(true);             //是否下拉刷新
        mySmart.setEnableLoadmore(false);           //是否上拉加载

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
//        iv_personal_img.setOnClickListener(this);
        ll_personal_img.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        ll_gender.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        ImageLoadUtil.showImage(PersonalInformationActivity.this, UriUtil.ip + App.loginMsg.headPath, iv_personal_img);
        //昵称
        if ("".equals(App.loginMsg.nickName)) {
            tv_personal_name.setText("未设置");
        } else {
            tv_personal_name.setText(App.loginMsg.nickName);
        }
        //性别
        if ("".equals(App.loginMsg.sex)) {
            tv_personal_sex.setText("未设置");
        } else {
            tv_personal_sex.setText(App.loginMsg.sex);
        }

        pickPicDialog = new PickPicDialog(PersonalInformationActivity.this);
        pickPicDialog.setOnActionClickListener(new PickPicDialog.OnPicListener() {
            @Override
            public void onTake() {
                PictureSelector.create(PersonalInformationActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .previewImage(true)
                        .compress(true)
                        .circleDimmedLayer(true)// 是否圆形裁剪
                        .enableCrop(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }

            @Override
            public void onAlbum() {
                PictureSelector.create(PersonalInformationActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .isCamera(false)
                        .previewImage(true)
                        .compress(true)
                        .enableCrop(true)
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(true)// 是否圆形裁剪
                        .showCropGrid(false)
                        .showCropFrame(false)
                        .scaleEnabled(true)
                        .rotateEnabled(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.ic_figure_head)
                .placeholder(R.mipmap.ic_figure_head))
                .load(UriUtil.ip + App.loginMsg.headPath)
                .into(iv_personal_img);

        //刷新事件监听
        mySmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getUser();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_img://修改头像
                pickPicDialog.show();
                break;
            case R.id.ll_name://昵称
                Intent intent = new Intent(PersonalInformationActivity.this, ModifyNameActivity.class);
                intent.putExtra("nickName", tv_personal_name.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.ll_gender://性别
                List<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                Picker(list, "选择性别", 1);
                break;
            case R.id.exit:
                showbackDialog();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void Picker(List<String> list, String title, final int type) {
        SinglePicker picker = new SinglePicker(this, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(ContextCompat.getColor(this, R.color.white));
        picker.setTopHeight(45);
        picker.setTopLineColor(ContextCompat.getColor(this, R.color.white));
        picker.setTopLineHeight(1);
        picker.setTitleText(title);
        picker.setTitleTextColor(ContextCompat.getColor(this, R.color.nomalText));
        picker.setTitleTextSize(14);
        picker.setCancelTextColor(ContextCompat.getColor(this, R.color.nomalText));
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(ContextCompat.getColor(this, R.color.nomalText));
        picker.setSubmitTextSize(14);
        picker.setSelectedTextColor(ContextCompat.getColor(this, R.color.nomalText));
        picker.setUnSelectedTextColor(ContextCompat.getColor(this, R.color.hintColor));
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(ContextCompat.getColor(this, R.color.hintColor));//线颜色
        config.setAlpha(120);//线透明度
        picker.setLineConfig(config);
        picker.setItemWidth(300);
        picker.setBackgroundColor(ContextCompat.getColor(this, R.color.backGrey));
        picker.setSelectedIndex(0);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                if (1 == type) {//选择性别
                    Map<String,Object> map=new HashMap<>();
                    map.put("sex",item);
                    String json=StringUtil.map2Json(map);
                    updateSex(json);
                } else {                     //选择年龄
                    updataAge(App.token, item);
                }
            }
        });
        picker.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (App.islogin) {
            getUser();
        } else {
            //TODO 修改

            startActivity(new Intent(PersonalInformationActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                if (mediaList.size() > 0) {
                    String path = mediaList.get(0).getCompressPath();


                    Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                            .error(R.mipmap.ic_figure_head)
                            .placeholder(R.mipmap.ic_figure_head))
                            .load(path)
                            .into(iv_personal_img);
                    try {
                        String base64 = StringUtil.encodeBase64File(path);

                        updateHead(App.token, base64, "jpeg");
                    } catch (Exception e) {
                        toast("数据解析失败");
                    }
                }
                break;
        }
    }

    /*
    获取用户信息
     */
    private void getUser() {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUser( new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);

                            Bean.Init_data data = new Gson().fromJson(result, Bean.Init_data.class);

                                SPUtil.saveBean2Sp(PersonalInformationActivity.this, data, "loginMsg", "loginMsg");

                                App.loginMsg = data;

                                ImageLoadUtil.showImage(PersonalInformationActivity.this, UriUtil.ip + data.headPath, iv_personal_img);
                                tv_personal_name.setText(data.nickName);
//                                tv_personal_top_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.account):App.loginMsg.nickName);
                                tv_personal_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.loginMsg.account):App.loginMsg.nickName);
                                if (!StringUtil.isSpace(data.sex)) {
                                    tv_personal_sex.setText(data.sex);
                                }
                                tv_personal_phone.setText(StringUtil.replacePhone(data.account));



                            finishRefresh();
                        }

                        @Override
                        public void onFail(String response) {
                            finishRefresh();
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            finishRefresh();
                        }

                        @Override
                        public void onTokenError(String response) {
                            finishRefresh();
                        }
                    });
                }
            });
        }
    }


    /*
    App7<<上传头像 修改头像
     */
    private void updateHead(final String token, final String headImage, final String PicType) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateHead(token, headImage, PicType, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);

                            Bean.UploadPic data = new Gson().fromJson(result, Bean.UploadPic.class);
                            if (1 == data.status) {
                                getUser();
                            } else {
                                toast(data.message);
                            }

                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }


    /*
    //App9<<修改性别
     */
    private void updateSex(final String json) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateSex(json, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);


                                getUser();


                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
//App9<<修改年龄
 */
    private void updataAge(final String token, final String age) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateAge(token, age, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.UploadAge data = new Gson().fromJson(result, Bean.UploadAge.class);
                            if (1 == data.status) {
                                getUser();
                            } else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
     城市
     */
    private void updataCity(final String token, final String city, final String province) {
        if (NetUtil.isNetWorking(PersonalInformationActivity.this)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.updateCity(token, city, province, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.Fogcity data = new Gson().fromJson(result, Bean.Fogcity.class);
                            if (1 == data.status) {
                                getUser();
                            } else {
                                toast(data.message);
                            }
                        }

                        @Override
                        public void onFail(String response) {

                        }

                        @Override
                        public void onError(Call call, Exception exception) {

                        }

                        @Override
                        public void onTokenError(String response) {

                        }
                    });
                }
            });
        }
    }

    /*
    点击退出登录，弹窗
     */
    private void showbackDialog() {
        exitDialog = new Dialog(PersonalInformationActivity.this, R.style.processDialog);
        View view = LayoutInflater.from(PersonalInformationActivity.this).inflate(R.layout.item_dialog_backuser, null);

        TextView me_back = view.findViewById(R.id.me_back);
        TextView canclePic = view.findViewById(R.id.canclePic);

        me_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                Bean.Init_data loginMsg = new Bean.Init_data();

                App.islogin = false;
                App.loginMsg = loginMsg;
                App.isBindAlias = false;

                SPUtil.saveData(PersonalInformationActivity.this, "islogin", false);
//                SPUtil.saveData(PersonalInformationActivity.this, "account", "");
                SPUtil.saveBean2Sp(PersonalInformationActivity.this, loginMsg,"loginMsg", "loginMsg");

                SPUtil.saveData(PersonalInformationActivity.this, "isBindAlias", false);

                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));

//                Intent intent1 = new Intent(PersonalInformationActivity.this, MainActivity.class);
                //Activity之间的参数传递
//                intent1.putExtra("toFragment", 111111111);
//                startActivity(intent1);
                App.finishAllActivity();
            }
        });
        canclePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });


        exitDialog.setContentView(view);
        exitDialog.setCanceledOnTouchOutside(false);

        Window window = exitDialog.getWindow();
        //底部弹出
        window.setGravity(Gravity.BOTTOM);
        //弹出动画
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        exitDialog.show();


    }

}

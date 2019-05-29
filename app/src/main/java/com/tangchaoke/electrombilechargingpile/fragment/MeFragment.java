package com.tangchaoke.electrombilechargingpile.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.activity.ForgotPasswordActivity;
import com.tangchaoke.electrombilechargingpile.activity.LoginActivity;
import com.tangchaoke.electrombilechargingpile.activity.MyOrderActivity;
import com.tangchaoke.electrombilechargingpile.activity.MyWalletActivity;
import com.tangchaoke.electrombilechargingpile.activity.PersonalInformationActivity;
import com.tangchaoke.electrombilechargingpile.activity.WithRegardActivity;
import com.tangchaoke.electrombilechargingpile.base.BaseFragment;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.SPUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;
import com.tangchaoke.electrombilechargingpile.view.CircleImageView;
import com.tangchaoke.electrombilechargingpile.view.MLImageView;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * 我的Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_GeRenZhongXin, ll_me_wallet, ll_WoDeDingDan, ll_XiuGaiMiMa,
            LL_LianXiWoMen, ll_GuanYuWoMen;
    private CircleImageView iv_my_head;
    private MLImageView iv_my_head1;
    private TextView tv_me_name;
    private LinearLayout ed_user;
    private LinearLayout no_user;
    private Button login_ed;
    private TextView me_balance;
    private String phonenum;
    public ArrayList<Bean.Conpany> compay=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        ll_GeRenZhongXin = view.findViewById(R.id.ll_GeRenZhongXin);
        ll_me_wallet = view.findViewById(R.id.ll_me_wallet);
        ll_WoDeDingDan = view.findViewById(R.id.ll_WoDeDingDan);
        ll_XiuGaiMiMa = view.findViewById(R.id.ll_XiuGaiMiMa);
        LL_LianXiWoMen = view.findViewById(R.id.LL_LianXiWoMen);
        ll_GuanYuWoMen = view.findViewById(R.id.ll_GuanYuWoMen);
        iv_my_head = view.findViewById(R.id.iv_my_head);
        iv_my_head1 = view.findViewById(R.id.iv_my_head1);
        tv_me_name = view.findViewById(R.id.tv_me_name);

        ed_user=view.findViewById(R.id.ed_user);
        no_user=view.findViewById(R.id.no_user);
        login_ed=view.findViewById(R.id.Login_ed);
        me_balance=view.findViewById(R.id.me_balance);
    }

    private void initData() {
        getUser();
    }

    private void initListener() {
        ll_GeRenZhongXin.setOnClickListener(this);
        ll_me_wallet.setOnClickListener(this);
        ll_WoDeDingDan.setOnClickListener(this);
        ll_XiuGaiMiMa.setOnClickListener(this);
        LL_LianXiWoMen.setOnClickListener(this);
        ll_GuanYuWoMen.setOnClickListener(this);

        login_ed.setOnClickListener(this);
        iv_my_head.setOnClickListener(this);
        iv_my_head1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_GeRenZhongXin://个人信息
                if (App.islogin) {
                    startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_me_wallet://钱包
                if (App.islogin) {
//                    startActivity(new Intent(getActivity(), AccountDetailsActivity.class));
                    startActivity(new Intent(getActivity(), MyWalletActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_WoDeDingDan://我的订单
                if (App.islogin) {
                    startActivity(new Intent(getActivity(), MyOrderActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_XiuGaiMiMa://修改密码
                if (App.islogin) {
//                    startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
                    Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                    intent.putExtra("activity", "me");
                    startActivity(intent);
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_LianXiWoMen://联系我们
                getXieYi();
                break;
            case R.id.ll_GuanYuWoMen://关于我们
                startActivity(new Intent(getActivity(), WithRegardActivity.class));
                break;
            case R.id.Login_ed://未登录状态下登录按钮
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.iv_my_head://个人信息
                startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                break;
            case R.id.iv_my_head1:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

    /*
    点击联系我们，弹窗
     */
    private void showCallDialog(String compayTel) {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog_call_us, null);

        TextView tv_dialog_top = view.findViewById(R.id.tv_dialog_top);
        TextView tv_dialog_left = view.findViewById(R.id.tv_dialog_left);
        TextView tv_dialog_right = view.findViewById(R.id.tv_dialog_right);

        tv_dialog_top.setText(compayTel);
        tv_dialog_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dialog_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isPhone(phonenum)||StringUtil.isFixedPhone(phonenum)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phonenum));
                    startActivity(intent);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getActivity(),"号码格式不正确",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (App.islogin){

            ed_user.setVisibility(View.VISIBLE);
            no_user.setVisibility(View.GONE);

            if (null!=App.loginMsg){
                Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.mipmap.ic_figure_head)
                        .placeholder(R.mipmap.ic_figure_head))
                        .load(UriUtil.ip+App.loginMsg.headPath)
                        .into(iv_my_head);
                tv_me_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.account):App.loginMsg.nickName);
            }else {
                iv_my_head.setImageResource(R.mipmap.ic_figure_head);
                tv_me_name.setText(StringUtil.replacePhone(App.account));
            }


//            tv_me_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.account):App.loginMsg.nickName);


        }else {
            ed_user.setVisibility(View.GONE);
            no_user.setVisibility(View.VISIBLE);
        }
    }

    /*
    获取用户信息
     */
    private void getUser() {
        if (NetUtil.isNetWorking(getActivity())) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getUser( new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功", result);

                            Bean.Init_data data = new Gson().fromJson(result, Bean.Init_data.class);


                            App.account=data.account;
                            App.loginMsg = data;
                            SPUtil.saveBean2Sp(getActivity(), data, "loginMsg", "loginMsg");

                            ImageLoadUtil.showImage(getActivity(), UriUtil.ip + data.headPath, iv_my_head);
                            tv_me_name.setText(StringUtil.isSpace(App.loginMsg.nickName)?StringUtil.replacePhone(App.loginMsg.account):App.loginMsg.nickName);

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
    App11<<联系我们 >
     */
    private void getXieYi(){
        if (NetUtil.isNetWorking(getActivity())){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getCompany(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取成功",result);

                            JSONArray jsonArray = JSONArray.parseArray(result);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Bean.Conpany list = new Gson().fromJson(jsonArray.getString(i), Bean.Conpany.class);
                                compay.add(list);
                            }
                                phonenum=compay.get(0).customerPhone;
                                showCallDialog(compay.get(0).customerPhone);


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

}

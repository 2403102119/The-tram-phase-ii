package com.tangchaoke.electrombilechargingpile.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;

/**
 * kylin on 2017/11/21.
 */

public class DialogUtils {

    private TextView oneBtn,twoBtn,title,content;
//    private Activity activity;
    private AlertDialog diaglog;
//    private HttpInterface httpInterface;
//    private boolean isTwoBtn;

    private OnTwoBtnClickListener onTwoBtnClickListener;

    private OnOneBtnClickListener onOneBtnClickListener;

    @SuppressLint("InflateParams")
    public DialogUtils(Activity activity, boolean isTwoBtn, String titleStr, String contentStr, String oneBtnText, String twoBtnText, boolean isShowTitle, boolean isShowContent){
//        this.activity=activity;
//        this.httpInterface=httpInterface;
//        this.isTwoBtn=isTwoBtn;
        View diaglog_view;
        if (isTwoBtn){
            diaglog_view= LayoutInflater.from(activity).inflate(R.layout.diaglog_notice_two_btn, null);
        }else{
            diaglog_view= LayoutInflater.from(activity).inflate(R.layout.diaglog_notice_one_btn, null);
        }

        oneBtn = diaglog_view.findViewById(R.id.oneBtn);
        oneBtn.setText(oneBtnText);

        twoBtn = diaglog_view.findViewById(R.id.twoBtn);
        title = diaglog_view.findViewById(R.id.title);
        content = diaglog_view.findViewById(R.id.content);
        title.setText(titleStr);
        content.setText(contentStr);
        if (isShowTitle){
            title.setVisibility(View.VISIBLE);
        }else {
            title.setVisibility(View.GONE);
        }
        if (isShowContent){
            content.setVisibility(View.VISIBLE);
        }else {
            content.setVisibility(View.GONE);
        }

        oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOneBtnClickListener.onClick();
            }
        });
        if (isTwoBtn) {
            twoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTwoBtnClickListener.onClick();
                }
            });
            twoBtn.setText(twoBtnText);
        }


        diaglog = new AlertDialog.Builder(activity).create();
        diaglog.setView(diaglog_view);
        diaglog.setCanceledOnTouchOutside(false);
        diaglog.setCancelable(false);
    }

    public interface OnOneBtnClickListener{
        void onClick();
    }

    public interface OnTwoBtnClickListener{
        void onClick();
    }

    public void setOnOneBtnClickListener(OnOneBtnClickListener onOneBtnClickListener){
        this.onOneBtnClickListener=onOneBtnClickListener;
    }

    public void setOnTwoBtnClickListener(OnTwoBtnClickListener onTwoBtnClickListener){
        this.onTwoBtnClickListener=onTwoBtnClickListener;
    }

    public void setTitle(String titleStr){
        title.setText(titleStr);
    }

    public void setContent(String contentStr){
        content.setText(contentStr);
    }

    public void setContentVisibility(boolean visibility){
        content.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    public void show(){
        diaglog.show();
    }

    public void hide(){
        diaglog.dismiss();
    }
}

package com.tangchaoke.electrombilechargingpile.thread;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;


/**
 * Created by Administrator on 2017/12/12.
 */

public class LoadingDialog extends Dialog {
    Context context;
    TextView text;
    String hint = "";
    boolean isBackkeyWork = true;

    public void setBackkeyWork(boolean isBackkeyWork){
        this.isBackkeyWork = isBackkeyWork;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, boolean isBackkeyWork) {
        super(context);
        this.context = context;
        this.isBackkeyWork = isBackkeyWork;
    }

    public LoadingDialog(Context context, String hint) {
        super(context);
        this.context = context;
        this.hint = hint;
    }

    public LoadingDialog(Context context, String hint, boolean isBackkeyWork) {
        super(context);
        this.context = context;
        this.hint = hint;
        this.isBackkeyWork = isBackkeyWork;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setCancelable(true);
        setContentView(R.layout.dialog_loading);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.dimAmount = 0.5f;
        getWindow().setAttributes(p);

        imageView = (ImageView) findViewById(R.id.img);
        text = (TextView) findViewById(R.id.loading_text);
        animation = AnimationUtils.loadAnimation(context, R.anim.circle_center);

        animation.setInterpolator(new LinearInterpolator());
        if (!"".equals(hint)) {
            text = (TextView) findViewById(R.id.loading_text);
            text.setVisibility(View.VISIBLE);
            text.setText(hint);
        } else {
            text.setVisibility(View.GONE);
        }
    }

    ImageView imageView;
    Animation animation;

    @Override
    public void show() {
        try {
            super.show();
            imageView.startAnimation(animation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (isBackkeyWork) {
            dismiss();
            ((Activity)context).onBackPressed();
        }
    }
}

package com.tangchaoke.electrombilechargingpile.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tangchaoke.electrombilechargingpile.R;


/**
 * kylin on 2017/12/16.
 */

public class PickPicDialog extends Dialog {

    private Context context;
    private OnPicListener onPicListener;

    public interface OnPicListener{
        void onTake();
        void onAlbum();
    }
    public void setOnActionClickListener(OnPicListener OnPicListenerr){
        this.onPicListener = OnPicListenerr;
    }


    public PickPicDialog(Context context) {
        super(context, R.style.processDialog);
        this.context = context;

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.pick_photo, null);

        view.findViewById(R.id.takePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onPicListener!=null){
                    onPicListener.onTake();
                }
            }
        });

        view.findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onPicListener!=null){
                    onPicListener.onAlbum();
                }
            }
        });

        view.findViewById(R.id.canclePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setContentView(view);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }



}

package com.tangchaoke.electrombilechargingpile.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.base.BaseActivity;
import com.tangchaoke.electrombilechargingpile.bean.Bean;
import com.tangchaoke.electrombilechargingpile.thread.MApiResultCallback;
import com.tangchaoke.electrombilechargingpile.thread.MUIToast;
import com.tangchaoke.electrombilechargingpile.thread.ThreadPoolManager;
import com.tangchaoke.electrombilechargingpile.util.CameraUtils;
import com.tangchaoke.electrombilechargingpile.util.DialogUtils;
import com.tangchaoke.electrombilechargingpile.util.NetUtil;
import com.tangchaoke.electrombilechargingpile.util.StringUtil;
import com.tangchaoke.electrombilechargingpile.zxing.camera.CameraManager;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.CaptureActivityHandler;
import com.tangchaoke.electrombilechargingpile.zxing.decoding.InactivityTimer;
import com.tangchaoke.electrombilechargingpile.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import okhttp3.Call;

/**
 * 扫描
 */
public class ScanCodeActivity extends BaseActivity implements SurfaceHolder.Callback {
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private DialogUtils dialogUtils;
    private boolean isLight = false;            //是否已开启闪光灯
    private ImageView img_light;
    private TextView tv_light_tip;
    private FrameLayout fl_scan_code;
    private LinearLayout ll_input_code;

    private CameraUtils cameraUtils;

    private SensorManager sm;
    private Sensor ligthSensor;
    private SensorEventListener sensorEventListener;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContainer(R.layout.activity_scan_code);
        CameraManager.init(ScanCodeActivity.this);
        viewfinderView = findViewById(R.id.viewfinder_content);
        img_light = findViewById(R.id.img_light);
        tv_light_tip = findViewById(R.id.tv_light_tip);
        fl_scan_code = findViewById(R.id.fl_scan_code);
        ll_input_code = findViewById(R.id.ll_input_code);
        hasSurface = false;
//        inactivityTimer = new InactivityTimer(getActivity());

        title.setText("扫码充电");

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        findViewById(R.id.tv_shoudong_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanCodeActivity.this, InputChargingActivity.class);
                startActivityForResult(intent, 123);
                /*fl_scan_code.setVisibility(View.GONE);
                ll_input_code.setVisibility(View.GONE);
                final Dialog dialog = new Dialog(ScanCodeActivity.this, R.style.Dialog);
                View view1 = LayoutInflater.from(ScanCodeActivity.this).inflate(R.layout.dialog_input_charging_code, null);
                Button btn_begin_charging = view1.findViewById(R.id.btn_begin_charging);
                final ClearEditText cet_input_code = view1.findViewById(R.id.cet_input_code);
                btn_begin_charging.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String DZ=cet_input_code.getText().toString().trim();

                        if (StringUtil.isSpace(DZ)){
                            toast("请输入电桩号");
                            return;
                        }

                        *//*StringBuffer DZSb = new StringBuffer();
                        for (int i = 0; i < DZ.length(); i += 2) {
                            if ((DZ.length() - i)%2 == 1 && DZ.length() - i < 2) {
                                DZSb.append(DZ.substring(i, i + 1));
                                DZSb.append(" ");
                            }else {
                                DZSb.append(DZ.substring(i, i + 2));
                                DZSb.append(" ");
                            }
                        }
                        String DZStr = DZSb.toString().substring(0, DZSb.toString().length() - 1);*//*
                        dialog.dismiss();

                        getPower(App.token,DZ);
                    }
                });
                dialog.setContentView(view1);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        fl_scan_code.setVisibility(View.VISIBLE);
                        ll_input_code.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

        dialogUtils=new DialogUtils(ScanCodeActivity.this,false,"该电桩出现故障，请使用其他电桩","","确认","",true,false);
        dialogUtils.setOnOneBtnClickListener(new DialogUtils.OnOneBtnClickListener() {
            @Override
            public void onClick() {
                dialogUtils.hide();
            }
        });
    }

    @Override
    protected void initData() {

    }

    protected void initListener() {
        img_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLight){
                    // 关闪光灯
                    CameraManager.get().offLight();
                    isLight = false;
                    img_light.setImageResource(R.mipmap.light_close);
                    tv_light_tip.setText("轻触点亮");
                    tv_light_tip.setTextColor(getResources().getColor(R.color.white));
                }else {
                    CameraManager.get().openLight();
                    isLight = true;
                    img_light.setImageResource(R.mipmap.light_open);
                    tv_light_tip.setText("轻触关闭");
                    tv_light_tip.setTextColor(getResources().getColor(R.color.green));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) ScanCodeActivity.this.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        /*cameraUtils = new CameraUtils(ScanCodeActivity.this, false, surfaceView);
        cameraUtils.initCameraUtils();
        cameraUtils.setIsDarkListener(new CameraUtils.IsDarkListener() {
            @Override
            public void isDark(boolean isDrak) {
                if (isDrak){
                    // 关闪光灯
                    isLight = true;
                    img_light.setImageResource(R.mipmap.light_close);
                    tv_light_tip.setText("轻触点亮");
                    tv_light_tip.setTextColor(getResources().getColor(R.color.white));
                }else {
                    if (isLight) {
                        img_light.setImageResource(R.mipmap.light_open);
                        tv_light_tip.setText("轻触关闭");
                        tv_light_tip.setTextColor(getResources().getColor(R.color.green));

                    }
                }
            }
        });*/
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //获取精度
                float acc = event.accuracy;
                //获取光线强度
                float lux = event.values[0];
                String sb = "精度:"+acc+",光线强度:"+lux+"摄像头模式:";

//光线强度展示在ligthSensorView上

//                ligthSensorView.setText(sb);
                int retval = Float.compare(lux, (float) 10.0);
                if(retval>0){
//光线强度>10.0隐藏Button
                    if (!isLight) {
                        img_light.setVisibility(View.GONE);
                        tv_light_tip.setVisibility(View.GONE);
                    }
                }
                else {
//显示Button
                    img_light.setVisibility(View.VISIBLE);
                    tv_light_tip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sm.registerListener(sensorEventListener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
//        CameraManager.get().stopPreview();
//        cameraUtils.releaseCamera();
        sm.unregisterListener(sensorEventListener);
    }

    @Override
    public void onDestroy() {
//        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     */
    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
        //http://192.168.1.253/ChargingPile/action/DownLoadAction/getUrl?id=
        String resultString = result.getText();
        Log.e("resultString",resultString+"");

        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(ScanCodeActivity.this, "非法验证码!", Toast.LENGTH_SHORT).show();
        } else {
            if (resultString.contains("action/DownLoadAction/getUrl")) {

                String[] id=resultString.split("[?]id=");
//                Toast.makeText(getActivity(), resultString, Toast.LENGTH_SHORT).show();

                if (id.length>1) {
                    playBeepSoundAndVibrate();
                    String DWStr = StringUtil.addSpace(id[1]);
                    getPower(App.token, DWStr, id[2]);
                }else {
                    Toast.makeText(ScanCodeActivity.this, "数据有误!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ScanCodeActivity.this, "非法验证码!", Toast.LENGTH_SHORT).show();
            }
        }
        handler.sendEmptyMessageDelayed(R.id.restart_preview,3000);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(ScanCodeActivity.this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            ScanCodeActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) ScanCodeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (123 == requestCode && 223 == resultCode){
            finish();
        }
    }

    /*
        App20<<电桩详情 >
         */
    private void getPower(final String token,final String identity1, final String identity2){
        if (NetUtil.isNetWorking(ScanCodeActivity.this)){
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.getPowerInfo(token, identity1, identity2, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.PowerInfoall data = new Gson().fromJson(result, Bean.PowerInfoall.class);
                            if (1 == data.status) {
                                if ("1".equals(data.model.stopStatus)) {
                                    dialogUtils.setTitle("该充电桩已停用，请使用其他电桩");
                                    dialogUtils.show();
                                }else if ("0".equals(data.model.stopStatus)) {

                                    if ("1".equals(data.model.socketState)) {
                                        dialogUtils.setTitle("该充电位出现故障，请使用其他充电位");
                                        dialogUtils.show();
                                    } else {
                                        if ("1".equals(data.model.useState)) {
                                            dialogUtils.setTitle("该充电位正在使用中，请使用其他充电位");
                                            dialogUtils.show();
                                        } else {
                                            Intent intent = new Intent(ScanCodeActivity.this, ChargingDetailsActivity.class);
                                            intent.putExtra("toFragment", data.model.oid);
                                            intent.putExtra("PowerInfo", data.model);
                                            startActivity(intent);
                                        }
                                    }
                                } else {
                                    MUIToast.show(ScanCodeActivity.this, data.message);
                                }
                            }else {
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
}
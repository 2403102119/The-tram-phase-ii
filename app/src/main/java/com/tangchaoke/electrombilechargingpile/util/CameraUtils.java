package com.tangchaoke.electrombilechargingpile.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraUtils {
    private Context context;

    private final String TAG = this.getClass().getSimpleName();

    private Camera camera;
    //是否已获取权限
    private boolean isRequestPermission = false;
    /**
     * 当前打开摄像头类型，当前写的是后置摄像头
     */
    private int currentCameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
    /**
     * 预览控件
     */
    private SurfaceView displaySfv;
    private SurfaceHolder.Callback mCallBack;

    //需要申请的权限
    private String[] permissions = {Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;

    public void initCameraUtils(){
        if (!isRequestPermission) {
            checkAndInitCamera();
        }
    }

    public CameraUtils(Context context, boolean isRequestPermission, SurfaceView displaySfv) {
        this.context = context;
        this.isRequestPermission = isRequestPermission;
        this.displaySfv = displaySfv;
    }

    /**
     * 检查权限
     */
    private void checkAndInitCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                isRequestPermission = true;
                ActivityCompat.requestPermissions((Activity) context, permissions, CAMERA_PERMISSION_CODE);
            } else {
                initCamera();
            }
        } else {
            initCamera();
        }
    }


    /**
     * 初始化照片
     */
    private void initCamera() {
        if (camera != null) {
            camera.startPreview();
            setPreviewLight();
        }
        Log.e(TAG, "initCamera");
        //1. Obtain an instance of Camera from open(int).
        //这里可以根据前后摄像头设置
        camera = openCamera(currentCameraType);
        if (camera == null) {
            return;
        }
        //2. Get existing (default) settings with getParameters().
        //获得存在的默认配置属性
//        Camera.Parameters parameters = camera.getParameters();

        //3. If necessary, modify the returned Camera.Parameters object and call setParameters(Camera.Parameters).
        //可以根据需要修改属性，这些属性包括是否自动持续对焦、拍摄的gps信息、图片视频格式及大小、预览的fps、
        // 白平衡和自动曝光补偿、自动对焦区域、闪光灯状态等。
        //具体可以参阅https://developer.android.com/reference/android/hardware/Camera.Parameters.html
      /*  if (parameters.getSupportedFocusModes().contains(Camera.Parameters
                .FOCUS_MODE_CONTINUOUS_PICTURE)) {
            //自动持续对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }*/
        //在设置图片和预览的大小时要注意当前摄像头支持的大小，不同手机支持的大小不同，如果你的SurfaceView不是全屏，有可能被拉伸。
        // parameters.getSupportedPreviewSizes(),parameters.getSupportedPictureSizes()
     /*   List<Camera.Size> picSizes = parameters.getSupportedPictureSizes();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Camera.Size picSize = getPictureSize(picSizes, width, height);
        parameters.setPictureSize(picSize.width, picSize.height);
        camera.setParameters(parameters);*/
        //4. Call setDisplayOrientation(int) to ensure correct orientation of preview.
        //你可能会遇到画面方向和手机的方向不一致的问题，竖向手机的时候，但是画面是横的，这是由于摄像头默认捕获的画面横向的
        // 通过调用setDisplayOrientation来设置PreviewDisplay的方向，可以解决这个问题。
//        setCameraDisplayOrientation(this, currentCameraType, camera);

        //5. Important: Pass a fully initialized SurfaceHolder to setPreviewDisplay(SurfaceHolder).
        // Without a surface, the camera will be unable to start the preview.
        //camera必须绑定一个surfaceview才可以正常显示。
        try {
            camera.setPreviewDisplay(displaySfv.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //6. Important: Call startPreview() to start updating the preview surface.
        // Preview must be started before you can take a picture.
        //在调用拍照之前必须调用startPreview()方法,但是在此时有可能surface还未创建成功。
        // 所以加上SurfaceHolder.Callback()，在回调再次初始化下。
        camera.startPreview();
        setPreviewLight();
        //7. When you want, call
        // takePicture(Camera.ShutterCallback, Camera.PictureCallback, Camera.PictureCallback, Camera.PictureCallback)
        // to capture a photo. Wait for the callbacks to provide the actual image data.
        //当如果想要拍照的时候，调用takePicture方法，这个下面我们会讲到。

        //8. After taking a picture, preview display will have stopped. To take more photos, call startPreview() again first.
        //在拍照结束后相机预览将会关闭，如果要再次拍照需要再次调用startPreview（)

        //9. Call stopPreview() to stop updating the preview surface.
        //通过调用stopPreview方法可以结束预览
        //10. Important: Call release() to release the camera for use by other applications.
        // Applications should release the camera immediately in onPause()(and re-open() it in onResume()).
        //建议在onResume调用open的方法，在onPause的时候执行release方法

        SurfaceHolder holder = displaySfv.getHolder();
        if (holder != null) {
            if(mCallBack != null){
                holder.removeCallback(mCallBack);
            }
            mCallBack = new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    Log.e(TAG, "surfaceCreated" + holder + this);
                    checkAndInitCamera();

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    Log.e(TAG, "surfaceChanged" + holder + this);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    Log.e(TAG, "surfaceDestroyed" + holder + this);
                }
            };
            holder.addCallback(mCallBack);
        }
    }


    private Camera openCamera(int type) {
        int cameraTypeIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == type) {
                cameraTypeIndex = cameraIndex;
                break;
            }
        }
        if (cameraTypeIndex != -1) {
            return Camera.open(cameraTypeIndex);
        }
        return null;
    }


    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    //上次记录的时间戳
    long lastRecordTime = System.currentTimeMillis();

    //上次记录的索引
    int darkIndex = 0;
    //一个历史记录的数组，255是代表亮度最大值
    long[] darkList = new long[]{255, 255, 255, 255};
    //扫描间隔
    int waitScanTime = 300;

    //亮度低的阀值
    int darkValue = 60;
    private void setPreviewLight() {
        //不需要的时候直接清空
//        if(noNeed){
//            camera.setPreviewCallback(null);
//            return;
//        }
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastRecordTime < waitScanTime) {
                    return;
                }
                lastRecordTime = currentTime;

                int width = camera.getParameters().getPreviewSize().width;
                int height = camera.getParameters().getPreviewSize().height;
                //像素点的总亮度
                long pixelLightCount = 0L;
                //像素点的总数
                long pixeCount = width * height;
                //采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
                int step = 10;
                //data.length - allCount * 1.5f的目的是判断图像格式是不是YUV420格式，只有是这种格式才相等
                //因为int整形与float浮点直接比较会出问题，所以这么比
                if (Math.abs(data.length - pixeCount * 1.5f) < 0.00001f) {
                    for (int i = 0; i < pixeCount; i += step) {
                        //如果直接加是不行的，因为data[i]记录的是色值并不是数值，byte的范围是+127到—128，
                        // 而亮度FFFFFF是11111111是-127，所以这里需要先转为无符号unsigned long参考Byte.toUnsignedLong()
                        pixelLightCount += ((long) data[i]) & 0xffL;
                    }
                    //平均亮度
                    long cameraLight = pixelLightCount / (pixeCount / step);
                    //更新历史记录
                    int lightSize = darkList.length;
                    darkList[darkIndex = darkIndex % lightSize] = cameraLight;
                    darkIndex++;
                    boolean isDarkEnv = true;
                    //判断在时间范围waitScanTime * lightSize内是不是亮度过暗
                    for (int i = 0; i < lightSize; i++) {
                        if (darkList[i] > darkValue) {
                            isDarkEnv = false;
                        }
                    }
                    Log.e(TAG, "摄像头环境亮度为 ： " + cameraLight);
                    if (!((Activity)context).isFinishing()) {
                        //亮度过暗就提醒
                            isDarkListener.isDark(isDarkEnv);
                    }
                }
            }
        });
    }

    //是否过暗监听
    public void setIsDarkListener(IsDarkListener isDarkListener) {
        this.isDarkListener = isDarkListener;
    }

    public IsDarkListener isDarkListener;

    public interface IsDarkListener{
        void isDark(boolean isDrak);
    }

}

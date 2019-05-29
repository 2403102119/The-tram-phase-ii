package com.tangchaoke.electrombilechargingpile.zxing.activity;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
//public class CaptureActivity extends AppCompatActivity implements Callback {

//    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private boolean hasSurface;
//    private Vector<BarcodeFormat> decodeFormats;
//    private String characterSet;
//    private InactivityTimer inactivityTimer;
//    private MediaPlayer mediaPlayer;
//    private boolean playBeep;
//    private static final float BEEP_VOLUME = 0.10f;
//    private boolean vibrate;
//    public static final int RESULT_CODE_QR_SCAN = 0xA1;
//    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scanner);
//        CameraManager.init(getApplication());
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
//
//        findViewById(R.id.tv_shoudong_code).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(CaptureActivity.this, InputChargingActivity.class));
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        decodeFormats = null;
//        characterSet = null;
//
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    /**
//     * Handler scan result
//     *
//     * @param result
//     * @param barcode
//     */
//    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        String resultString = result.getText();
//        //FIXME
//        if (TextUtils.isEmpty(resultString)) {
//            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
//        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
//            Log.e("ssssssssssssssss", "scan 0 = " + resultString);
//            resultIntent.putExtras(bundle);// 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
//        }
//        CaptureActivity.this.finish();
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//
//    }
//
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it
//            // too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(
//                    R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private static final long VIBRATE_DURATION = 200L;
//
//    private void playBeepSoundAndVibrate() {
//        if (playBeep && mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
//    }
//
//    /**
//     * When the beep has finished playing, rewind to queue up another one.
//     */
//    private final OnCompletionListener beepListener = new OnCompletionListener() {
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            mediaPlayer.seekTo(0);
//        }
//    };

//}
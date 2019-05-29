package com.tangchaoke.electrombilechargingpile.viewimg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;

public class SBI_OneImageSampleActivity extends Activity {

    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %%";

    private SBI_ZoomImageView zoomImageView;

    private Toast toast;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Use full screen window and translucent action bar
         */
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setBackgroundDrawable(new ColorDrawable(0xFF000000));
//        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
//            getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//            getActionBar().setBackgroundDrawable(new ColorDrawable(0x88000000));
//            // Note: if you use ActionBarSherlock use here getSupportActionBar()
//        }

        setContentView(R.layout.activity_sbi_one_image_sample);

        zoomImageView = (SBI_ZoomImageView) findViewById(R.id.zoomImageView);

        String imgPath = getIntent().getStringExtra("imgPath");

        ImageLoadUtil.showImage(SBI_OneImageSampleActivity.this,imgPath, zoomImageView);

        // Lets attach some listeners (optional)
        zoomImageView.setOnPhotoTapListener((SBI_ZoomImageView.OnPhotoTapListener) new PhotoTapListener());
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem zoomToggle = menu.findItem(R.id.menu_zoom_toggle);
        zoomToggle.setTitle(zoomImageView.isZoomEnabled() ? R.string.menu_zoom_disable
                : R.string.menu_zoom_enable);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_zoom_toggle:
                zoomImageView.setIsZoomEnabled(!zoomImageView.isZoomEnabled());
                return true;

            case R.id.menu_scale_fit_center:
                zoomImageView.setScaleType(ScaleType.FIT_CENTER);
                return true;

            case R.id.menu_scale_fit_start:
                zoomImageView.setScaleType(ScaleType.FIT_START);
                return true;

            case R.id.menu_scale_fit_end:
                zoomImageView.setScaleType(ScaleType.FIT_END);
                return true;

            case R.id.menu_scale_fit_xy:
                zoomImageView.setScaleType(ScaleType.FIT_XY);
                return true;

            case R.id.menu_scale_scale_center:
                zoomImageView.setScaleType(ScaleType.CENTER);
                return true;

            case R.id.menu_scale_scale_center_crop:
                zoomImageView.setScaleType(ScaleType.CENTER_CROP);
                return true;

            case R.id.menu_scale_scale_center_inside:
                zoomImageView.setScaleType(ScaleType.CENTER_INSIDE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private class PhotoTapListener implements SBI_ZoomImageView.OnPhotoTapListener {
        @Override
        public void onPhotoTap(View view, float x, float y) {

            finish();
//            float xPercentage = x * 100f;
//            float yPercentage = y * 100f;
//
//            if (toast != null) {
//                toast.cancel();
//            }
//
//            toast = Toast.makeText(SBI_OneImageSampleActivity.this,
//                    String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage),
//                    Toast.LENGTH_SHORT);
//            toast.show();
        }
    }
}


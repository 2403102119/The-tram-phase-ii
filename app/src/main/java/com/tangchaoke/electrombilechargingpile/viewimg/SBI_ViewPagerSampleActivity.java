/*******************************************************************************
 * Copyright 2013 Tomasz Zawada
 *
 * Based on the excellent PhotoView by Chris Banes:
 * https://github.com/chrisbanes/PhotoView
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tangchaoke.electrombilechargingpile.viewimg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangchaoke.electrombilechargingpile.R;
import com.tangchaoke.electrombilechargingpile.util.ImageLoadUtil;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;

import java.util.ArrayList;
import java.util.List;


public class SBI_ViewPagerSampleActivity extends Activity {
    ViewPager viewPager;

    public class SamplePagerAdapter extends PagerAdapter {
        private List<String> list;

        public SamplePagerAdapter(List<String> listImg) {
            this.list = listImg;
        }

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            } else {
                return list.size();
            }
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            final SBI_ZoomImageView zoomImageView = new SBI_ZoomImageView(container.getContext());

            zoomImageView.post(new Runnable() {
                @Override
                public void run() {
                    final String stringss = list.get(position);
//                    Log.e("跳转后list长度", list.size() + "..." + position + "...." + stringss);

                    ImageLoadUtil.showImage(SBI_ViewPagerSampleActivity.this, UriUtil.ip+stringss, zoomImageView);
                }
            });

            container.addView(zoomImageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return zoomImageView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbi_view_pager);

        final ArrayList<String> listImg = getIntent().getStringArrayListExtra("listImg");
        final int mPosition = getIntent().getIntExtra("position", 0);

        ImageView back = (ImageView) findViewById(R.id.back);
        final TextView title = (TextView) findViewById(R.id.title);
        title.setText((mPosition+1)+"/"+listImg.size());
        viewPager = (ViewPager) findViewById(R.id.zoomViewPager);
        viewPager.setAdapter(new SamplePagerAdapter(listImg));
        viewPager.setCurrentItem(mPosition);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                title.setText((position+1)+"/"+listImg.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Add margin between pages (optional)
        viewPager.setPageMargin((int) getResources().getDisplayMetrics().density * 10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

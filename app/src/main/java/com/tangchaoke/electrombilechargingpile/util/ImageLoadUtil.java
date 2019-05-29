package com.tangchaoke.electrombilechargingpile.util;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tangchaoke.electrombilechargingpile.R;


/**
 * kylin on 2017/11/29.
 */

public class ImageLoadUtil {
    public static void showImage(Activity context, String str, ImageView photo) {
        if (context!=null&&!context.isDestroyed()) {
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                    .error(R.mipmap.ic_figure_head)
                    .placeholder(R.mipmap.ic_figure_head))
                    .load(str)
                    .into(photo);
        }
    }

    /*//图片上边两个圆角、第二个为角度=====new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.TOP)
    public static void showImageTop(Context context, String str, ImageView photo) {
       //上面两个圆角，下面两个直角

        //如果是四周已经是圆角则RoundedCornersTransformation.CornerType.ALL
        Glide.with(context)
                .load(str).error(R.mipmap.ic_figure_head)
                .fallback(R.mipmap.ic_figure_head)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(photo);

    }

    //图片下边两个圆角
    public static void showImageBottom(Context context, String str, ImageView photo) {
        //下面两个圆角，上面两个直角
        Glide.with(context)
                .load(str)
                .error(R.mipmap.ic_figure_head)
                .fallback(R.mipmap.ic_figure_head)
                //                .bitmapTransform(new GrayscaleTransformation(this))//带灰色蒙层
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0,
                        RoundedCornersTransformation.CornerType.BOTTOM))
                .into(photo);
    }



    //高斯模糊
    public static void showImageMohu(Context context, String str, ImageView photo, int radius) {
        Glide.with(context)
                .load(str)
                .error(R.mipmap.ic_figure_head)
                .fallback(R.mipmap.ic_figure_head)
                //radius取值1-25,值越大图片越模糊
                .bitmapTransform(new BlurTransformation(context, radius))
                .into(photo);
    }*/

}

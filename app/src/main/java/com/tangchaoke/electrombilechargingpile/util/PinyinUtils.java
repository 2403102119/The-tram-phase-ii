package com.tangchaoke.electrombilechargingpile.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * kylin on 16/9/24.
 */
public class PinyinUtils {
    /**
     * 获取拼音的首字母（大写）
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin){
        if (TextUtils.isEmpty(pinyin)) return "搜索";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()){
            return c.toUpperCase();
        } else if ("0".equals(c)){
            return "搜索";
        } else if ("1".equals(c)){
            return "定位";
        } else if ("0".equals(c)){
            return "常用";
        } else if ("1".equals(c)){
            return "热门";
        }
        return "搜索";
    }
}
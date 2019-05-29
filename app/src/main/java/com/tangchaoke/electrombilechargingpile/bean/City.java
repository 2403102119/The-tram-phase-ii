package com.tangchaoke.electrombilechargingpile.bean;

/**
 * kylin on 16/9/24.
 */

public class City {
    public String name;
    public String pinyin;
    public double lat;
    public double lng;

    public City(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }

    public City(String name, String pinyin,double lat,double lng) {
        this.name = name;
        this.pinyin = pinyin;
        this.lat = lat;
        this.lng = lng;
    }
}
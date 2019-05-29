package com.tangchaoke.electrombilechargingpile.bean;

import java.io.Serializable;

/**
 * kylin on 16/9/24.
 */
public class Contact implements Serializable {
    private String mName;
    private int mType;
    public double lat;
    public double lng;

    public Contact(String name, int type, double lat, double lng) {
        mName = name;
        mType = type;
        this.lat = lat;
        this.lng = lng;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

}

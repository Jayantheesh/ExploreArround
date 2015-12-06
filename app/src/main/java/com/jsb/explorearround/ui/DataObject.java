package com.jsb.explorearround.ui;

/**
 * Created by JSB on 10/9/15.
 */
public class DataObject {

    private String mName;
    private String mAddress;
    private String mStatus;
    private String mDistance;

    public DataObject(String name, String address, String status, String distance) {
        mName = name;
        mAddress = address;
        mStatus = status;
        mDistance = distance;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}

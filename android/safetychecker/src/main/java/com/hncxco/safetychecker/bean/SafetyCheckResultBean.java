package com.hncxco.safetychecker.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SafetyCheckResultBean implements Parcelable {
    private int checkStatus;
    private CheckResultBean xposedCheckResult;
    private CheckResultBean lishuNetCheckResult;
    private CheckResultBean bamenCheckResult;
    private CheckResultBean gameGuardianCheckResult;

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public CheckResultBean getXposedCheckResult() {
        if (xposedCheckResult == null) {
            xposedCheckResult = new CheckResultBean();
        }
        return xposedCheckResult;
    }

    public void setXposedCheckResult(CheckResultBean xposedCheckResult) {
        this.xposedCheckResult = xposedCheckResult;
    }

    public CheckResultBean getLishuNetCheckResult() {
        if (lishuNetCheckResult == null) {
            lishuNetCheckResult = new CheckResultBean();
        }
        return lishuNetCheckResult;
    }

    public CheckResultBean getBamenCheckResult() {
        if (bamenCheckResult == null) {
            bamenCheckResult = new CheckResultBean();
        }
        return bamenCheckResult;
    }

    public CheckResultBean getGameGuardianCheckResult() {
        if (gameGuardianCheckResult == null) {
            gameGuardianCheckResult = new CheckResultBean();
        }
        return gameGuardianCheckResult;
    }

    public void setGameGuardianCheckResult(CheckResultBean gameGuardianCheckResult) {
        this.gameGuardianCheckResult = gameGuardianCheckResult;
    }

    public void setBamenCheckResult(CheckResultBean bamenCheckResult) {
        this.bamenCheckResult = bamenCheckResult;
    }

    public void setLishuNetCheckResult(CheckResultBean lishuNetCheckResult) {
        this.lishuNetCheckResult = lishuNetCheckResult;
    }

    public SafetyCheckResultBean() {
        xposedCheckResult = new CheckResultBean();
        lishuNetCheckResult = new CheckResultBean();
        bamenCheckResult = new CheckResultBean();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.checkStatus);
        dest.writeParcelable(this.xposedCheckResult, flags);
        dest.writeParcelable(this.lishuNetCheckResult, flags);
        dest.writeParcelable(this.bamenCheckResult, flags);
        dest.writeParcelable(this.gameGuardianCheckResult, flags);
    }

    protected SafetyCheckResultBean(Parcel in) {
        this.checkStatus = in.readInt();
        this.xposedCheckResult = in.readParcelable(CheckResultBean.class.getClassLoader());
        this.lishuNetCheckResult = in.readParcelable(CheckResultBean.class.getClassLoader());
        this.bamenCheckResult = in.readParcelable(CheckResultBean.class.getClassLoader());
        this.gameGuardianCheckResult = in.readParcelable(CheckResultBean.class.getClassLoader());
    }

    public static final Creator<SafetyCheckResultBean> CREATOR = new Creator<SafetyCheckResultBean>() {
        @Override
        public SafetyCheckResultBean createFromParcel(Parcel source) {
            return new SafetyCheckResultBean(source);
        }

        @Override
        public SafetyCheckResultBean[] newArray(int size) {
            return new SafetyCheckResultBean[size];
        }
    };
}

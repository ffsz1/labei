package com.hncxco.safetychecker.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckResultBean implements Parcelable {
    //检查结果状态，0为正常，1为异常
    private int checkStatus;

    //java层检查安装列表的结果状态，0为正常，1为异常
    private int javaInstalledCheckStatus;

    //java层检查Exception输出的结果状态，0为正常，1为异常
    private int javaExceptionCheckStatus;

    //jni层检查安装列表的结果状态，0为正常，1为异常
    private int jniInstalledCheckStatus;

    private String installAppName;

    public String getInstallAppName() {
        return installAppName;
    }

    public void setInstallAppName(String installAppName) {
        this.installAppName = installAppName;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getJavaInstalledCheckStatus() {
        return javaInstalledCheckStatus;
    }

    public void setJavaInstalledCheckStatus(int javaInstalledCheckStatus) {
        if (javaInstalledCheckStatus == 1) {
            setCheckStatus(1);
        }
        this.javaInstalledCheckStatus = javaInstalledCheckStatus;
    }

    public int getJavaExceptionCheckStatus() {
        return javaExceptionCheckStatus;
    }

    public void setJavaExceptionCheckStatus(int javaExceptionCheckStatus) {
        if (javaExceptionCheckStatus == 1) {
            setCheckStatus(1);
        }
        this.javaExceptionCheckStatus = javaExceptionCheckStatus;
    }

    public int getJniInstalledCheckStatus() {
        return jniInstalledCheckStatus;
    }

    public void setJniInstalledCheckStatus(int jniInstalledCheckStatus) {
        if (jniInstalledCheckStatus == 1) {
            setCheckStatus(1);
        }
        this.jniInstalledCheckStatus = jniInstalledCheckStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.checkStatus);
        dest.writeInt(this.javaInstalledCheckStatus);
        dest.writeInt(this.javaExceptionCheckStatus);
        dest.writeInt(this.jniInstalledCheckStatus);
    }

    public CheckResultBean() {
    }

    protected CheckResultBean(Parcel in) {
        this.checkStatus = in.readInt();
        this.javaInstalledCheckStatus = in.readInt();
        this.javaExceptionCheckStatus = in.readInt();
        this.jniInstalledCheckStatus = in.readInt();
    }

    public static final Parcelable.Creator<CheckResultBean> CREATOR = new Parcelable.Creator<CheckResultBean>() {
        @Override
        public CheckResultBean createFromParcel(Parcel source) {
            return new CheckResultBean(source);
        }

        @Override
        public CheckResultBean[] newArray(int size) {
            return new CheckResultBean[size];
        }
    };
}

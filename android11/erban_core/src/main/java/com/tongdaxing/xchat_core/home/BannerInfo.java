package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class BannerInfo implements Parcelable {
    /*
    bannerId：1 //id
	bannerName: xx//横幅广告名称
	bannerPic:xx //横幅图片
	skipType:xx // 1跳app页面，2跳聊天室，3跳h5页面
	skipUri:xx //跳转uri
     */
    private int bannerId;
    private String bannerName;
    private String bannerPic;
    private int skipType;
    private String skipUri;

    public BannerInfo() {
    }

    @Override
    public String toString() {
        return "BannerInfo{" +
                "bannerId=" + bannerId +
                ", bannerName='" + bannerName + '\'' +
                ", bannerPic='" + bannerPic + '\'' +
                ", skipType=" + skipType +
                ", skipUri='" + skipUri + '\'' +
                '}';
    }

    protected BannerInfo(Parcel in) {
        bannerId = in.readInt();
        bannerName = in.readString();
        bannerPic = in.readString();
        skipType = in.readInt();
        skipUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bannerId);
        dest.writeString(bannerName);
        dest.writeString(bannerPic);
        dest.writeInt(skipType);
        dest.writeString(skipUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BannerInfo> CREATOR = new Creator<BannerInfo>() {
        @Override
        public BannerInfo createFromParcel(Parcel in) {
            return new BannerInfo(in);
        }

        @Override
        public BannerInfo[] newArray(int size) {
            return new BannerInfo[size];
        }
    };

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri;
    }


    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }


}

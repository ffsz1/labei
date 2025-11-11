package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhouxiangfeng
 * @date 2017/5/17
 */

public class HomeIcon implements Parcelable {

    private String pic;
    private String activity;
    private String params;
    private String title;
    private String url;
    private String subtitle;
    /**
     * 3 webview 2 房间
     */
    private int skipType;
    private String skipUri;

    protected HomeIcon(Parcel in) {
        pic = in.readString();
        activity = in.readString();
        params = in.readString();
        title = in.readString();
        url = in.readString();
        subtitle = in.readString();
        skipType = in.readInt();
        skipUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pic);
        dest.writeString(activity);
        dest.writeString(params);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(subtitle);
        dest.writeInt(skipType);
        dest.writeString(skipUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeIcon> CREATOR = new Creator<HomeIcon>() {
        @Override
        public HomeIcon createFromParcel(Parcel in) {
            return new HomeIcon(in);
        }

        @Override
        public HomeIcon[] newArray(int size) {
            return new HomeIcon[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri;
    }

    @Override
    public String toString() {
        return "HomeIcon{" +
                "pic='" + pic + '\'' +
                ", activity='" + activity + '\'' +
                ", params='" + params + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", skipType=" + skipType +
                ", skipUri='" + skipUri + '\'' +
                '}';
    }
}

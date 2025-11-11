package com.tongdaxing.xchat_core.find;

/**
 * Created by huangmeng1 on 2018/1/31.
 */

public class FindInfo {
    /**
     * advName : 测试啊
     * advIcon : http://img.hulelive.com/FvbRJRJgufTpntjatyfL8xx1yBQ5?imageslim
     * skipType : 3
     * skipUri : www.baidu.com
     */

    private String advName;
    private String advIcon;
    private int skipType;
    private String skipUri;

    @Override
    public String toString() {
        return "FindInfo{" +
                "advName='" + advName + '\'' +
                ", advIcon='" + advIcon + '\'' +
                ", skipType=" + skipType +
                ", skipUri='" + skipUri + '\'' +
                '}';
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

    public String getAdvIcon() {
        return advIcon;
    }

    public void setAdvIcon(String advIcon) {
        this.advIcon = advIcon;
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
}

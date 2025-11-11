package com.erban.main.vo;

/**
 * Created by PaperCut on 2018/1/30.
 */
public class AdvertiseVo {
    private String advName;
    private String advIcon;
    private Byte skipType;
    private String skipUri;

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

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri;
    }
}

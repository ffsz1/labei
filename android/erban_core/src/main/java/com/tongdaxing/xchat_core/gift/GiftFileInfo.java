package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;

/**
 * Created by chenran on 2017/8/7.
 */

public class GiftFileInfo implements Serializable{
    private String giftUrl;
    private String giftFile;

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getGiftFile() {
        return giftFile;
    }

    public void setGiftFile(String giftFile) {
        this.giftFile = giftFile;
    }
}

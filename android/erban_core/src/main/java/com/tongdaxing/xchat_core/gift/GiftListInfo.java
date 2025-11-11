package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenran on 2017/8/5.
 */

public class GiftListInfo implements Serializable{
    private List<GiftInfo> gift;
    private String giftVersion;

    public List<GiftInfo> getGift() {
        return gift;
    }

    public void setGift(List<GiftInfo> gift) {
        this.gift = gift;
    }

    public String getGiftVersion() {
        return giftVersion;
    }

    public void setGiftVersion(String giftVersion) {
        this.giftVersion = giftVersion;
    }
}

package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;

import lombok.Data;

/**
 * 捡海螺的礼物
 * Created by zwk on 2018/9/13.
 */
@Data
public class EggGiftInfo implements Serializable {
    private int giftId;
    private int giftType;//2 普通礼物  3神秘礼物
    private String giftName;
    private int giftNum;
    private int goldPrice;  //
    private String picUrl;//礼物图片
    private String vggUrl;
    private int goldCost;
    private long createTime;
}

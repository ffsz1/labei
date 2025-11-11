package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;

/**
 * 捡海螺的礼物
 * Created by zwk on 2018/9/13.
 */
@Data
public class ConchGiftInfo implements Serializable {
    private List<EggGiftInfo> giftList;
    private int ticketId;
    private int conchNum;
}

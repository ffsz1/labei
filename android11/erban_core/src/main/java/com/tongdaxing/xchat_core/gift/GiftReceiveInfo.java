package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by chenran on 2017/7/28.
 */
@Data
public class GiftReceiveInfo implements Serializable {
    private long uid;
    private long targetUid;
    private int giftId;
    private int giftNum;
    private String targetNick;
    private String targetAvatar;
    private String nick;
    private String avatar;
    private int personCount;
    private List<Integer> roomIdList;
    private String roomUid;
    private String userNo;
    private int userGiftPurseNum;
    private int useGiftPurseGold;
    private long giftSendTime;
    private int experLevel;
}

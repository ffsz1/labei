package com.juxiao.xchat.service.api.room.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomAdminParamBO {
    private Long uid;//管理员UID
    private Long roomUid;//房主UID
    private Long roomId;
    private String title;
    private Byte type;
    private Byte operatorStatus;
    private String avatar;
    private String roomDesc;
    private String roomNotice;
    private String backPic;
    private String roomPwd;
    private String roomTag;
    private Integer tagId;
    private Integer giftEffectSwitch;
    private Integer publicChatSwitch;
    /** 玩法介绍 */
    private String playInfo;
    private Integer giftCardSwitch;
}

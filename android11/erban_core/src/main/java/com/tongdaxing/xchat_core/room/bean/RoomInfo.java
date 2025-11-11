package com.tongdaxing.xchat_core.room.bean;


import java.io.Serializable;
import java.util.List;

import lombok.Data;


/**
 * @author zhouxiangfeng
 * @date 2017/5/24
 */

@Data
public class RoomInfo implements Serializable {

    public static final int ROOMTYPE_AUCTION = 1;
    public static final int ROOMTYPE_LIGHT_CHAT = 2;
    public static final int ROOMTYPE_HOME_PARTY = 3;
    /**
     * 房间座驾特效开关，默认关闭过滤（0关1开）
     */
    private int giftCardSwitch;
    private long uid;

    /**
     * 官方账号与非官方账号
     */
    private int officeUser;

    private long roomId;

    public String title;
    public int charmOpen; // (0.隐藏 1.显示)
    private int type;
    private List<Integer> hideFace;
    private String roomNotice;
    private String roomDesc;
    private String backPic;
    private String backPicUrl;
    //房间玩法
    private String playInfo;
    //小礼物特效
    private int giftEffectSwitch;
    //公屏开关
    private int publicChatSwitch;
    //音效质量等级 0为AUDIO_PROFILE_DEFAULT、1为AUDIO_PROFILE_MUSIC_STANDARD_STEREO、2为AUDIO_PROFILE_MUSIC_HIGH_QUALITY、3
    // 为AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO
    private int audioLevel;

    private String userDescription;

    /**
     * 房间是否开启，是否正在直播
     */
    private boolean valid;

    /**
     * 1:房主在房间，2 :房主不在房间
     */
    private int operatorStatus;
    private String meetingName;

    private int isPermitRoom;

    /**
     * 是否捡海螺厅 1，是；2，否
     */
    private int giftDrawEnable;

    /**
     * 是使用即构，还是声网  1.声网 2.即构
     */
    private int audioChannel;

    private long erbanNo;

    public String roomPwd;

    private String roomTag;
    public int tagId;
    public String tagPict;
    /**
     * 房间在线人数
     */
    private int onlineNum;

    /**
     * 跳转私聊
     *
     * @return
     */
    private long p2pUid;
}

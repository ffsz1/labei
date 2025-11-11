package com.tongdaxing.xchat_core.user.bean;

import com.tongdaxing.xchat_core.room.bean.RoomInfo;

import java.util.List;

import lombok.Data;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

@Data
public class AttentionInfo {
    //耳伴号
    private long erbanNo;
    public String nick;
    public long fansNum;
    public long uid;
    public String avatar;
    public boolean valid;
    private int operatorStatus;
    private int type;
    private int gender;
    private String title;
    private RoomInfo userInRoom;
    public List<Long> uidList;
    private boolean isFan;
    private String userDesc;
    private String userVoice;
    private String voiceDura;
}

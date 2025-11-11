package com.juxiao.xchat.dao.room.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomLinkVo implements Serializable{
    private Long uid;
    private Long erbanNo;
    private Date birth;
    private Byte star;
    private String nick;
    private String email;
    private String signture;
    private String userVoice;
    private Integer voiceDura;
    private Integer followNum;
    private Integer fansNum;
    private Byte defUser;
    private Long fortune;
    private Byte gender;
    private String avatar;
    private String userDesc;
    private Date createTime;
    private Date updateTime;
    private Long roomId;
    private Integer onlineNum;
    private String roomAvatar;
    private Boolean isLike;
    private Integer linkNum;
}

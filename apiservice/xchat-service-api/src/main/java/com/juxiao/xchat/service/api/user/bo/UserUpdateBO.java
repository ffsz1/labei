package com.juxiao.xchat.service.api.user.bo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserUpdateBO {
    private Long uid;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private Byte star;
    private String nick;
    private String email;
    private String signture;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Byte gender;
    private String avatar;
    private String region;
    private String userDesc;
    private String userVoice;
    private String shareChannel;
    private String shareUid;
    private String roomUid;
    private Integer voiceDura;
    private String accompanyType;
    private String height;
    private String weight;
    private Byte defUser;
    //新人邀请的邀请码
    private String shareCode;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

package com.juxiao.xchat.manager.common.user.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 简单的用户VO对象
 */
@Getter
@Setter
public class UserSimpleVO {
    private Long uid;
    private String nick;
    private Byte gender;
    private String signature;
    private String avatar;
    private String userVoice;
    private Integer voiceDuration;
    private Integer glamourLevel;
    private Integer fortuneLevel;
}

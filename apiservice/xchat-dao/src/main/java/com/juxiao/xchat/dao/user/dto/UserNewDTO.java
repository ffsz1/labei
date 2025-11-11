package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserNewDTO {
    private Long uid;
    private Long erbanNo;
    private String phone;
    private Date birth;
    private Byte star;
    private String nick;
    private String email;
    private String signture;
    private String userVoice;
    private Integer followNum;
    private Integer fansNum;
    private Long fortune;
    private Byte gender;
    private String avatar; // 头像
    private Integer age;
    private Integer experLevel; //等级值
    private Integer charmLevel; //魅力值
}

package com.juxiao.xchat.manager.external.netease.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class MicUserVo implements Serializable{

    private static final long serialVersionUID = -8596692873850525778L;
    private Long uid;
    private String nick;
    private String avatar;
    private Byte gender;
}

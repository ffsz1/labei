package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Data;

/**
 * @author chris
 * @Title: 构建封装群信息与成员列表数据
 * @date 2018/9/14
 * @time 上午9:17
 */
@Data
public class TeamMembersBO {

    private String tname;

    private String announcement;

    private String owner;

    private Integer maxusers;

    private Integer joinmode;

    private Integer tid;

    private String intro;

    private Integer size;

    private String custom;

    private String clientCustom;

    private Boolean mute;

    private Long createtime;

    private Long updatetime;

    private String admins;

    private String members;


}

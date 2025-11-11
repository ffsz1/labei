package com.juxiao.xchat.manager.external.netease.ret;

import lombok.*;

/**
 * @author chris
 * @Title: 封装群聊返回响应数据参数
 * @date 2018/11/26
 * @time 10:25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NetEaseTeamRet {

    private int code;

    private String tid;

    private String faccid;

    private String mutes;

    private int count;

    private String infos;

    private String data;

    private String tinfo;

    private String tinfos;
}


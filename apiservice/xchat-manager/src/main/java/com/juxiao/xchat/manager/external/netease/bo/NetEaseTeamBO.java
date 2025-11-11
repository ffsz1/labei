package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 下午2:26
 */
@Data
public class NetEaseTeamBO {

    /**
     * 网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符 [必须参数]
     */
    private String tid;
    /**
     * 群主用户帐号，最大长度32字符  [必须参数]
     */
    private String owner;
    /**
     * ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员 [必须参数]
     */
    private List<String> members;
    /**
     * 管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414 [必须参数]
     */
    private Integer magree;
    /**
     * 邀请发送的文字，最大长度150字符 [必须参数]
     */
    private String msg;
    /**
     * 自定义扩展字段，最大长度512
     */
    private String attach;

    /**
     * 被移除人的accid，用户账号，最大长度32字符;注：member或members任意提供一个，优先使用member参数【移除】
     */
    private String member;




}

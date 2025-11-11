package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 下午2:24
 */
@Data
public class NetEaseCreateTeamBO {

    /**
     * 网易云通信服务器产生，群唯一标识，创建群时会返回
     */
    private String tid;

    /**
     * 群名称，最大长度64字符 [必传]
     */
    private String tame;
    /**
     * 群主用户帐号，最大长度32字符 [必传]
     */
    private String owner;
    /**
     * ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员 [必传]
     */
    private String members;
    /**
     * 群公告，最大长度1024字符
     */
    private String announcement;
    /**
     * 群描述，最大长度512字符
     */
    private String intro;
    /**
     * 邀请发送的文字，最大长度150字符 [必传]
     */
    private String msg;
    /**
     * 管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414 [必传]
     */
    private Integer magree;
    /**
     * 群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414 [必传]
     */
    private Integer joinmode;
    /**
     * 自定义高级群扩展属性，第三方可以跟据此属性自定义扩展自己的群属性。（建议为json）,最大长度1024字符
     */
    private String custom;
    /**
     * 群头像，最大长度1024字符
     */
    private String icon;
    /**
     * 被邀请人同意方式，0-需要同意(默认),1-不需要同意。其它返回414
     */
    private Integer beinvitemode;
    /**
     * 谁可以邀请他人入群，0-管理员(默认),1-所有人。其它返回414
     */
    private Integer invitemode;
    /**
     * 谁可以修改群资料，0-管理员(默认),1-所有人。其它返回414
     */
    private Integer uptinfomode;
    /**
     * 谁可以更新群自定义属性，0-管理员(默认),1-所有人。其它返回414
     */
    private Integer upcustommode;

}

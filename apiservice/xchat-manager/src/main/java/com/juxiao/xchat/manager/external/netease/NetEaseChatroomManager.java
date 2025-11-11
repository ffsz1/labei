package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.ret.NeteaseReqAddrRet;

/**
 * 对接云信聊天室API
 */
public interface NetEaseChatroomManager {

    /**
     * 设置聊天室内用户角色
     *
     * @param roomId
     * @param accid
     * @param save
     * @param ext
     * @return
     * @throws Exception
     */
    int updateRole(Long roomId, String accid, Boolean save, String ext) throws Exception;

    /**
     * 请求聊天室地址与令牌
     *
     * @param roomId     聊天室id
     * @param accid      进入聊天室的账号
     * @param clientType 1:weblink（客户端为web端时使用）; 2:commonlink（客户端为非web端时使用）;3:wechatlink(微信小程序使用), 默认1
     * @param ip         客户端ip，传此参数时，会根据用户ip所在地区，返回合适的地址
     */
    NeteaseReqAddrRet requestAddr(Long roomId, Long accid, int clientType, String ip) throws Exception;
}

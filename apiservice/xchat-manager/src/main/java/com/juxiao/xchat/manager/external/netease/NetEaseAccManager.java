package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;

/**
 * @class: NetEaseAccManager.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
public interface NetEaseAccManager {

    /**
     * 只更新网易云性别，用于在用户第一次补全资料，性别一经补全，不可更改
     *
     * @param accid
     * @param gender
     * @return
     * @throws Exception
     */
    NetEaseRet updateUserGender(String accid, int gender) throws Exception;

    /**
     * 更新用户信息
     *
     * @param accid
     * @param name
     * @param icon
     * @return
     * @throws Exception
     */
    NetEaseRet updateUserInfo(String accid, String name, String icon) throws Exception;

    /**
     * 封禁云信用户
     *
     * @param accid
     * @param needkick
     * @throws Exception
     */
    void block(String accid, String needkick) throws Exception;

    /**
     * 解除限制
     *
     * @param accid
     * @throws Exception
     */
    void unblock(String accid) throws Exception;

    void createNetEaseAcc(String accid, String token, String props) throws Exception;

}

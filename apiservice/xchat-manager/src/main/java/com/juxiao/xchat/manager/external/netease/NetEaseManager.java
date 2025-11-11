package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.ret.FriendRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;

import java.util.Date;

/**
 * 网易对接接口操作
 *
 * @class: NetEaseManager.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface NetEaseManager {

    /**
     * 发送加好友信息
     *
     * @param accid
     * @param faccid
     * @param type   1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
     * @param msg
     * @return
     * @throws Exception
     */
    NetEaseRet addFriends(Long accid, Long faccid, Byte type, String msg) throws Exception;

    /**
     * 删除好友
     *
     * @param accid
     * @param faccid
     * @return
     * @throws Exception
     */
    NetEaseRet deleteFriends(Long accid, Long faccid) throws Exception;

    /**
     * 查询好友
     * @param accid
     * @return
     * @throws Exception
     */
    FriendRet listFriends(Long accid) throws Exception;

    /**
     * 获取指定时间段开始的好友列表
     * @param accid
     * @param beginDate
     * @return
     * @throws Exception
     */
    FriendRet listFriends(Long accid, Date beginDate) throws Exception;

}

package com.tongdaxing.xchat_core.im.friend;

import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;
import java.util.Map;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IIMFriendCore extends IBaseCore {

    /**
     * 获取我的好友列表
     */
    List<NimUserInfo> getMyFriends();

    /**
     * 获取我的黑名单列表
     *
     * @return
     */
    List<String> getBlackList();

    /**
     * 获取禁言列表
     *
     * @return
     */
    List<String> getMuteList();

    /**
     * 通过对方的好友请求
     *
     * @param account
     * @param isPass
     */
    void passRequestFriend(String account, boolean isPass);

    /**
     * 是否是好友
     *
     * @param uid
     * @return
     */
    boolean isMyFriend(String uid);

    /**
     * 添加好友
     *
     * @param uid
     * @param tip
     */
    void addFriend(String uid, String tip);

    /**
     * 删除好友
     *
     * @param uid
     */
    void deleteFriend(String uid);

    /**
     * 请求加为好友
     *
     * @param uid
     */
    void requestFriend(String uid);

    /**
     * 添加黑名单
     *
     * @param uid
     */
    void addToBlackList(String uid);

    /**
     * 移出黑名单
     *
     * @param uid
     */
    void removeFromBlackList(String uid);

    /**
     * 用户是否在黑名单中
     *
     * @param uid
     */
    boolean isUserInBlackList(String uid);

    /**
     * 更新我的好友
     * 更新好友关系
     * 目前支持更新好友的备注名和好友关系扩展字段，见 @FriendFieldEnum
     * <p>
     * 注意：备注名最长128个字符；扩展字段需要传入 Map， SDK 会负责转成Json String，最大长度256字符。
     */
    void updateFriendFields(String tempnick, Map<FriendFieldEnum, Object> map);

    /**
     * 设置指定好友的消息提醒与否
     * 网易云通信支持对用户设置或关闭消息提醒（静音），关闭后，收到该用户发来的消息时，不再进行通知栏消息提醒。个人用户的消息提醒设置支持漫游。
     * 设置/关闭消息提醒
     */
    void setMessageNotify(String account, boolean checkState);

    boolean isNeedMessageNotify(String account);
}


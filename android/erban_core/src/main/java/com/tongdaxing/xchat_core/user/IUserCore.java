package com.tongdaxing.xchat_core.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by chenran on 2017/3/15.
 */

public interface IUserCore extends IBaseCore {
    void saveCache(long userId, UserInfo uInfo);

    /**
     * 请求详细用户信息
     * 回调onRequestDetailUserInfo
     *
     * @param userId
     */
    public void requestUserInfo(long userId);

    /**
     * 通过uid查询缓存，同步接口，返回可能NULL
     *
     * @param userId
     * @return
     */
    @Nullable
    public UserInfo getCacheUserInfoByUid(long userId);

    /**
     * 通过uid查询缓存，同步接口，返回可能NULL
     *
     * @param userId
     * @param refresh
     * @return
     */
    public UserInfo getCacheUserInfoByUid(long userId, boolean refresh);

    /**
     * 异步接口，直接从服务器拿uidList的信息填充到rstMap中，没有获取到信息的uid对应value为null
     *
     * @param uidList
     * @return
     */
    void requestUserInfoMapByUidList(@NonNull List<Long> uidList, LinkedHashMap<Long, UserInfo> rstMap, int type);

    void requestUserInfoMapByUidList(@NonNull List<Long> uidList, LinkedHashMap<Long, UserInfo> rstMap);

    /**
     * 优先同步获取缓存返回，数量不够就异步从服务器更新获取
     *
     * @param uidList
     * @return map，缓存中没有的uid，对应的value为null
     */
    @NonNull
    LinkedHashMap<Long, UserInfo> getCacheThenServerUserInfoMapByUidList(List<Long> uidList);

    @NonNull
    LinkedHashMap<Long, UserInfo> getCacheThenServerUserInfoMapByUidList(List<Long> uidList, int type);

    /**
     * 通过uid查询缓存（内存）的当前登录用户的详细信息，同步接口，返回可能NULL
     *
     * @return
     */
    @Nullable
    public UserInfo getCacheLoginUserInfo();


    /**
     * 信息不全登录后调
     *
     * @param userInfo
     */
    public void requestCompleteUserInfo(UserInfo userInfo, String shareChannel, String shareUid, String roomUid);

    /**
     * 修改个人信息
     * 如果不需修改字段，传空或者""
     */
    public void requestUpdateUserInfo(UserInfo userInfo);

    /**
     * 保存用户的邀请码
     */
    public void saveInviteCode(UserInfo userInfo, String inviteCode);

    /**
     * 上传照片
     *
     * @param url
     */
    public void requestAddPhoto(String url);

    /**
     * 删除照片
     *
     * @param pid
     */
    public void requestDeletePhoto(long pid);

    /**
     * 排序类型,1收到的礼物数量多少排序,2礼物价格高低排序
     *
     * @param uid
     */
    public void requestUserGiftWall(long uid, int orderType);

    /**
     * 获取对应用户的收到的神秘礼物列表
     *
     * @param orderType 排序类型,1收到的礼物数量多少排序,2礼物价格高低排序
     * @param uid       登陆的用户
     * @param queryUid  查询的用户
     */
    public void requestUserMysteryGiftWall(long uid, long queryUid, int orderType);

    /**
     * 关键词搜索用户
     */
    void searchUserInfo(String skey, int pageSize, int pageNo);


    void getTaskList();

    void requestUserInfo(long uid, OkHttpManager.MyCallBack callBack);
}

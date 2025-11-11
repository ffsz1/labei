package com.tongdaxing.xchat_core.im.message;

import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IIMMessageCore extends IBaseCore {

    //---------------------------消息配置--------------------------


    void clearAllUnreadMsg();

    int queryUnreadMsg();

    void deleteRecentContact(String account);

    /**
     *更新消息提醒配置 StatusBarNotificationConfig
     */
    void updateMessageNotiConfig(StatusBarNotificationConfig config);

    /**
     * 个人消息免打扰
     */
    void setMessageNoti(String account ,boolean checkState);

    /**
     * 群组消息免打扰
     */
    void setTeamMessageNoti(String teamId,boolean mute);

//---------------------------云端历史记录--------------------------
    /**
     * 从服务器拉取消息历史记录。
     *
     * @param anchor    IMMessage 起始时间点的消息，不能为 null。
     * @param toTime    long 结束时间点单位毫秒
     * @param limit     int 本次查询的消息条数上限(最多 100 条)
     * @param direction QueryDirectionEnum 查询方向，
     *                  QUERY_OLD 按结束时间点逆序查询，逆序排列；
     *                  QUERY_NEW 按起始时间点正序起查，正序排列
     * @param persist   boolean 通过该接口获取的漫游消息记录，要不要保存到本地消息数据库。
     * @return InvocationFuture
     */
    InvocationFuture<List<IMMessage>> pullMessageHistoryEx(IMMessage anchor, long toTime, int limit, QueryDirectionEnum direction, boolean persist);

    /**
     * 从服务器拉取消息历史记录。该接口查询方向为从后往前。以锚点 anchor 作为起始点（不包含锚点），
     * 往前查询最多 limit 条消息。
     *
     * @param anchor  IMMessage 查询锚点。
     * @param limit   int 本次查询的消息条数上限(最多 100 条)
     * @param persist boolean 通过该接口获取的漫游消息记录，要不要保存到本地消息数据库。
     * @return InvocationFuture
     */
    InvocationFuture<List<IMMessage>> pullMessageHistory(IMMessage anchor, int limit, boolean persist);



    //---------------------------本地历史记录--------------------------
    /**
     * 查询消息历史
     * 当进行首次查询时，锚点可以用使用 MessageBuilder#createEmptyMessage 接口生成。查询结果不包含锚点。
     *
     * @param anchor        IMMessage 查询锚点
     * @param directionEnum QueryDirectionEnum 查询方向
     * @param limit         int 查询结果的条数限制
     * @param asc           boolean 查询结果的排序规则，如果为 true，结果按照时间升级排列，如果为 false，按照时间降序排列
     * @return 调用跟踪，可设置回调函数，接收查询结果
     */

    InvocationFuture<List<IMMessage>> queryMessageListEx(IMMessage anchor, QueryDirectionEnum directionEnum, int limit, boolean asc);

    /**
     * 根据起始、截止时间点以及查询方向从本地消息数据库中查询消息历史。<br>
     * 根据提供的方向 (direction)，以 anchor 为起始点，往前或往后查询由 anchor 到 toTime 之间靠近 anchor 的 limit 条消息。<br>
     * 查询范围由 toTime 和 limit 共同决定，以先到为准。如果到 toTime 之间消息大于 limit 条，返回 limit 条记录，如果小于 limit 条，返回实际条数。<br>
     * 查询结果排序规则：direction 为 QUERY_OLD 时 按时间降序排列，direction 为 QUERY_NEW 时按照时间升序排列。<br>
     * 注意：查询结果不包含锚点。
     *
     * @param anchor    查询锚点
     * @param toTime    查询截止时间，若方向为 QUERY_OLD，toTime 应小于 anchor.getTime()。方向为 QUERY_NEW，toTime 应大于 anchor.getTime() <br>
     * @param direction 查询方向
     * @param limit     查询结果的条数限制
     * @return 调用跟踪，可设置回调函数，接收查询结果
     */
    InvocationFuture<List<IMMessage>> queryMessageListExTime(IMMessage anchor, long toTime, QueryDirectionEnum direction, int limit);

    /**
     * 通过uuid批量获取IMMessage(同步版本)
     */
    List<IMMessage> queryMessageListByUuidBlock(List<String> uuids);

    /**
     * 通过uuid批量获取IMMessage(异步版本)
     */
    InvocationFuture<List<IMMessage>> queryMessageListByUuid(List<String> uuids);

    /**
     * 通过消息类型从本地消息数据库中查询消息历史。查询范围由 msgTypeEnum 参数和 anchor 的 sessionId 决定。该接口查询方向为从后往前。以锚点 anchor 作为起始点（不包含锚点），往前查询最多 limit 条消息。
     * @param msgTypeEnum MsgTypeEnum 消息类型
     * @param anchor IMMessage        搜索的消息锚点
     * @param limit int               搜索结果的条数限制
     * @return
     */
    InvocationFuture<List<IMMessage>>  queryMessageListByType(MsgTypeEnum msgTypeEnum, IMMessage anchor, int limit);

    /**
     * @param keyword String 文本消息的搜索关键字
     * @param fromAccounts List<String> 消息说话者帐号列表，如果消息说话在该列表中，
     * 那么无需匹配 keyword，对应的消息记录会直接加入搜索结果中。
     * @param anchor IMMessage 搜索的消息锚点
     * @param limit int 搜索结果的条数限制
     * @return 调用跟踪，可设置回调函数，接收查询结果
     */
    void searchMessageHistory(String keyword, List<String> fromAccounts,IMMessage anchor,int limit);

    /**
     * @param keyword      文本消息的搜索关键字
     * @param fromAccounts 消息说话者帐号列表，如果消息说话在该列表中，那么无需匹配keyword，对应的消息记录会直接加入搜索结果集中。
     * @param time         查询范围时间点，比time小（从后往前查）
     * @param limit        搜索结果的条数限制
     * @return InvocationFuture
     */
    void searchAllMessageHistory(String keyword, List<String> fromAccounts, long time, int limit);

    /**
     *删除单条消息
     */
    void deleteChattingHistory(IMMessage message);

    /**
     * 删除与某个聊天对象的全部消息记录
     */
    void clearChattingHistory(String account, SessionTypeEnum sessionTypeEnum);

    /**
     * 不需要消息提醒的场景：
     *1. 如果用户正在与某一个人聊天，当这个人的消息到达时，是不应该有通知栏提醒的。
     *2. 如果用户停留在最近联系人列表界面，收到消息也不应该有通知栏提醒（但会有未读数变更通知）。
     *网易云通信 SDK 提供内置的消息提醒功能，如需使用，开发者需要在进出聊天界面以及最近联系人列表界面时，通知 SDK。相关接口如下：
     * 设定正在聊天的对象，当收到新的消息通知时，与改对象聊天的消息不提醒
     */
    void setChattingAccount(String account, SessionTypeEnum sessionType);

    IMMessage sendGiftMsg(int giftId, long uid, int num);
}

package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.ret.AddrNetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.MicUserResult;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.RoomResult;

/**
 * 网易对接接口操作
 *
 * @class: NetEaseRoomManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface NetEaseRoomManager {

    /**
     * 用于开通房间
     *
     * @param accid 房主账号
     * @param name  名称
     * @param ext   扩展字段
     * @return
     */
    RoomResult openRoom(String accid, String name, String ext);

    /**
     * 查询聊天室信息
     *
     * @param roomId
     * @return
     * @throws Exception
     */
    RoomResult getRoomMessage(Long roomId);

    /**
     * 切换房间状态--房主才能操作
     *
     * @param roomId   房间号
     * @param operator 操作者账号--必须是创建房间的人
     * @param valid    false 表示关闭聊天室
     * @return
     */
    RoomResult toggleRoomStatus(Long roomId, String operator, boolean valid);

    /**
     * 列出队列中的所有元素
     *
     * @param roomId 房间ID
     * @return
     */
    MicUserResult queueList(Long roomId);

    /**
     * 取出队列中的元素
     *
     * @param roomId   房间ID
     * @param position 队列中的位置
     * @return
     */
    NetEaseRet queuePoll(Long roomId, String position);

    /**
     * 往聊天室有序队列中新加或更新元素
     *
     * @param roomId   房间ID
     * @param position 队列中的位置
     * @param value    添加的人
     * @param operator 提交者
     * @param transie  提交者不在线时，提交的元素是否删除
     * @return
     */
    NetEaseRet queueOffer(Long roomId, String position, String value, String operator, boolean transie);

    /**
     * 往聊天室内发消息
     *
     * @param roomId    房间ID
     * @param msgId     消息标识, 可用UUID随机串
     * @param fromAccid 发送人
     * @param msgType   消息类型
     * @param attach    消息内容
     * @return
     */
    NetEaseRet sendChatRoomMsg(Long roomId, String msgId, String fromAccid, int msgType, String attach);

    /**
     * 更新房间信息
     *
     * @param roomId    聊天室ID
     * @param name      房间名
     * @param ext       拓展字段
     * @param notifyExt 通知拓展字段
     * @return
     */
    NetEaseRet updateRoomInfo(Long roomId, String name, String ext, String notifyExt);

    /**
     * 删除清理整个队列
     *
     * @param roomId 聊天室ID
     * @return
     */
    NetEaseRet queueDrop(Long roomId);

    void deleteRobot(Long roomId, String id);

    void addRobot(long roomId, String accids);

    AddrNetEaseRet inRoom(long roomId, String accId, int clientType)throws Exception;

    /**
     * 大厅官方小秘书, 发送消息
     * @param message 消息内容
     * @return
     */
    NetEaseRet sendOfficialMsg(String message);

    /**
     * 查询房间管理员
     * @param roomId
     * @param accids
     * @return
     */
    NetEaseRet queryMembers(long roomId, String accids);

}

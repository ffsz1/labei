package com.tongdaxing.xchat_core.room;

import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * Created by chenran on 2017/2/16.
 */

public interface IRoomCore extends IBaseCore {

    /**
     * 是否是房间主人
     *
     * @return
     */
    boolean isRoomOwner();

    void setDialogInfo(List<ActionDialogInfo> dialogInfo);

    List<ActionDialogInfo> getDialogInfo();

    /**
     * 获取当前roomInfo
     *
     * @return
     */
    RoomInfo getCurRoomInfo();

    /**
     * 更新房间设置信息
     *
     * @param title
     * @param desc
     * @param pwd
     * @param label 标签名字
     * @param tagId 标签id
     */
    void updateByAdmin(long roomUid, String title, String desc, String pwd, String label, int tagId);

    /**
     * 更新房间设置信息
     *
     * @param title
     * @param desc
     * @param pwd
     * @param label 标签名字
     * @param tagId 标签id
     */
    void updateRoomInfo(String title, String desc, String pwd, String label, int tagId);

    /**
     * 获取当前房间消息
     *
     * @return
     */
    List<ChatRoomMessage> getMessages();

    /**
     * 发送聊天室文本消息
     *
     * @param str
     */
    void sendRoomTextMsg(String str);

    void openRoom(long uid, int type);

    /**
     * 开房间
     * uid：必填
     * type：房间类型，1竞拍房，2悬赏房，必填
     * title：房间标题
     * roomDesc：房间描述
     * backPic：房间背景图
     * rewardId：当type为2时，必填如rewardId
     */
    void openRoom(long uid, int type, String title, String roomDesc, String backPic, String rewardId);

    /**
     * 获取房间信息
     *
     * @param uid
     * @param pageType 区分来自那一个页面
     */
    void requestRoomInfo(long uid, int pageType);

    /**
     * 关闭房间
     * uid=900093
     */
    void closeRoomInfo(String uid);

    void getRoomConsumeList(long roomUid);

    void roomSearch(String key);

    void getUserRoom(long uid);

    void userRoomIn(long roomUid);

    void userRoomOut();

    /** 获取房间标签 */
    void getRoomTagList();
}

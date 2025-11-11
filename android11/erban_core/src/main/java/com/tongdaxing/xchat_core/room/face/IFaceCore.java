package com.tongdaxing.xchat_core.room.face;

import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * @author chenran
 * @date 2017/9/9
 */

public interface IFaceCore extends IBaseCore {
    /**
     * 全麦按钮出发
     *
     * @return --
     */
    FaceInfo getPlayTogetherFace();

    /**
     * 发表情展示给其他人看
     *
     * @param faceInfo 要显示的表情信息
     */
    void sendFace(FaceInfo faceInfo);

    /**
     * 发速配表情展示给其他人看
     */
    void sendRoomMatchFace(boolean isShow,int[] numArr,int second);

    /**
     * 发速配表情展示给其他人看
     *
     * @param msg 要显示的表情信息
     */
    void onReceiveRoomMatchFace(ChatRoomMessage msg);

    /**
     * 房主发送运气表情给所有用户
     *
     * @param faceInfo--发送全麦运气的表情
     */
    void sendAllFace(FaceInfo faceInfo);

    /**
     * 根据对应的表情的id找到对应的FaceInfo
     *
     * @param faceId-- faceInfo的id
     * @return 对应id的faceInfo
     */
    FaceInfo findFaceInfoById(int faceId);

    /**
     * @return 打开表情的dialog, 获得所有的表情信息
     */
    List<FaceInfo> getFaceInfos();

    /**
     * 当前有没有在显示表情
     *
     * @return boolean
     */
    boolean isShowingFace();

    //////////////2017/11/29 Android 2.3.0的新需求///////////////

    /**
     * 获取服务端表情的json
     */
    void getOnlineFaceJsonOrZip();

    /**
     * 初始化成功后获取对应的表情列表
     *
     * @param encrypt --
     */
    void onReceiveOnlineFaceJson(String encrypt);

    /**
     * 返回对应版本的表情的zip包
     */
    void getOnlineFaceZipFile();

    /**
     * 放弃解签
     */
    void sendRoomMatchAbandon(int second);

    /**
     * 收到来自其他客户端的表情
     *
     * @param chatRoomMessageList -
     */
    void onReceiveChatRoomMessages(List<ChatRoomMessage> chatRoomMessageList);
}

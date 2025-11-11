package com.tongdaxing.xchat_core.room.face;

import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.RoomMatchAttachment;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * @author chenran
 * @date 2017/9/9
 */

public interface IFaceCoreClient extends ICoreClient {
    String METHOD_ON_RECEIVE_FACE = "onReceiveFace";
    String METHOD_ON_NOTI_FACE = "onNotiFace";
    String METHOD_ON_UNZIP_SUCCESS = "onUnzipSuccess";

    String METHOD_ON_RECEIVE_MATCH_FACE = "onReceiveMatchFace";

    void onNotiFace(ChatRoomMessage chatRoomMessage);

    void onReceiveFace(List<FaceReceiveInfo> faceReceiveInfos);

    void onUnzipSuccess();

    void onReceiveMatchFace(RoomMatchAttachment faceAttachment);
}

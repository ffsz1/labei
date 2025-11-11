package com.tongdaxing.xchat_core.im.avroom;

import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/5/29.
 */

public interface IAVRoomCore extends IBaseCore {
    void joinChannel(String channelId, int uid);

    void joinHighQualityChannel(String channelId, int uid, boolean record);

    void leaveChannel();

    void setRole(int role);

    void setMute(boolean mute);

    void setRemoteMute(boolean mute);

    void setRecordMute(boolean mute);

    int startAudioMixing(String filePath, boolean loopback, int cycle);

    int resumeAudioMixing();

    int pauseAudioMixing();

    int stopAudioMixing();

    void adjustAudioMixingVolume(int volume);

    void adjustRecordingSignalVolume(int volume);

    boolean isAudienceRole();

    boolean isMute();

    boolean isRemoteMute();

    boolean isRecordMute();

    void requestRoomOwnerInfo(String uid);

    UserInfo getRoomOwner();

    void removeRoomOwnerInfo();

    //关闭公屏消息
    void changeRoomMsgFilter(boolean roomOwner, int publicChatSwitch, String ticket, long uid);

}

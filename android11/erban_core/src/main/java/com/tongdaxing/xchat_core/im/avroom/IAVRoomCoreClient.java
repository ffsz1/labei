package com.tongdaxing.xchat_core.im.avroom;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/6/6.
 */

public interface IAVRoomCoreClient extends ICoreClient {

    public static final String METHOD_ON_CREAT_AV_ROOM = "onCreatAVRoom";
    public static final String METHOD_ON_CREAT_AV_ROOM_FAITH = "onCreatAVRoomFail";
    public static final String METHOD_ON_JOIN_AV_ROOM = "onJoinAVRoom";
    public static final String METHOD_ON_JOIN_AV_ROOM_FAITH = "onJoinAVRoomFail";
    public static final String METHOD_ON_LEAVE_AV_ROOM = "onLeaveAVRoom";
    public static final String METHOD_ON_SPEEK = "onSpeek";
    public static final String METHOD_ON_AUDIO_MIXING_FINISHED = "onAudioMixingFinished";
    public static final String METHOD_ON_AUDIO_MIXING_STARTED = "onAudioMixingStarted";
    public static final String METHOD_ON_AUDIO_MIXING_ERROR = "onAudioMixingError";
    public static final String METHOD_ON_USER_MUTE_AUDIO = "onUserMuteAudio";
    public static final String METHOD_ON_MY_AUDIO_MUTE = "onMyAudioMute";
    public static final String METHOD_ON_NETWORK_BAD = "onNetworkBad";
    public static final String METHOD_ON_CONNECT_LOST = "onConnectionLost";
    String micInlistMoveToTop = "micInlistMoveToTop";
    String onMicInListChange = "onMicInListChange";
    String onUserCome = "onUserCome";
    String dealUserComeMsg = "dealUserComeMsg";
    String sendMsg = "sendMsg";
    String onMicInListToUpMic = "onMicInListToUpMic";
    String micInListDismiss = "micInListDismiss";


    void micInListDismiss();

    void onMicInListToUpMic(int key, String uid);

    void micInlistMoveToTop(int micPosition, String roomId, String value);

    void onMicInListChange();

    void sendMsg(String msg);


    void onAudioMixingStarted();


    void onAudioMixingFinished();

    void onAudioMixingError();

    void onNetworkBad();

    void onConnectionLost();

    void onCreatAVRoom();

    void onCreatAVRoomFail();

    void onJoinAVRoom();

    void onJoinAVRoomFail(int code);

    void onLeaveAVRoom();

    void onSpeek(Map<String, Integer> map);

    void onUserMuteAudio(int uid);

    void onMyAudioMute();

    void onUserCarIn(String carUrl);

}

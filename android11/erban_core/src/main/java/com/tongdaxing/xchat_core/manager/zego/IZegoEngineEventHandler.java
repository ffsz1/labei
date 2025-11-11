package com.tongdaxing.xchat_core.manager.zego;

import com.zego.zegoaudioroom.ZegoAudioAVEngineDelegate;
import com.zego.zegoaudioroom.ZegoAudioDeviceEventDelegate;
import com.zego.zegoaudioroom.ZegoAudioLiveEvent;
import com.zego.zegoaudioroom.ZegoAudioLiveEventDelegate;
import com.zego.zegoaudioroom.ZegoAudioLivePlayerDelegate;
import com.zego.zegoaudioroom.ZegoAudioLivePublisherDelegate;
import com.zego.zegoaudioroom.ZegoAudioLiveRecordDelegate;
import com.zego.zegoaudioroom.ZegoAudioRoomDelegate;
import com.zego.zegoaudioroom.ZegoAudioStream;
import com.zego.zegoaudioroom.ZegoAudioStreamType;
import com.zego.zegoaudioroom.ZegoAuxData;
import com.zego.zegoaudioroom.ZegoLoginAudioRoomCallback;
import com.zego.zegoavkit2.IZegoMediaPlayerCallback;
import com.zego.zegoavkit2.soundlevel.IZegoSoundLevelCallback;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoliveroom.entity.ZegoBigRoomMessage;
import com.zego.zegoliveroom.entity.ZegoConversationMessage;
import com.zego.zegoliveroom.entity.ZegoRoomMessage;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;
import com.zego.zegoliveroom.entity.ZegoUserState;

import java.util.HashMap;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/3
 * 描述        即构房间各种回调时间的抽象类  1，2，3，4，5，6，9
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public abstract class IZegoEngineEventHandler implements ZegoLoginAudioRoomCallback, IZegoSoundLevelCallback,
        ZegoAudioAVEngineDelegate, ZegoAudioLivePlayerDelegate, ZegoAudioLiveEventDelegate, ZegoAudioLivePublisherDelegate,
        ZegoAudioLiveRecordDelegate, ZegoAudioRoomDelegate, IZegoMediaPlayerCallback, ZegoAudioDeviceEventDelegate {

    /**
     * 房间登录成功回调
     *
     * @param errorCode 0：登录成功 其他：登录失败
     */
    @Override
    public void onLoginCompletion(int errorCode) {

    }

    /**
     * 用户声浪的监听回调
     */
    @Override
    public void onSoundLevelUpdate(ZegoSoundLevelInfo[] zegoSoundLevelInfos) {

    }

    @Override
    public void onCaptureSoundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {

    }

    /**
     * 当 AV 引擎销毁时会回调此接口，如 ZegoAudioRoom.logoutRoom() 成功后
     */
    @Override
    public void onAVEngineStop() {

    }

    /**
     * 流播放状态回调。
     */
    @Override
    public void onPlayStateUpdate(int stateCode, ZegoAudioStream zegoAudioStream) {

    }

    @Override
    public void onPlayQualityUpdate(String streamID, ZegoStreamQuality zegoStreamQuality) {

    }

    /**
     * 推/拉流状态回调。
     */
    @Override
    public void onAudioLiveEvent(ZegoAudioLiveEvent zegoAudioLiveEvent, HashMap<String, String> hashMap) {

    }

    /**
     * 推流接口，推流状态更新，混流等通过此接口实现。
     */
    @Override
    public void onPublishStateUpdate(int stateCode, String streamId, HashMap<String, Object> hashMap) {

    }

    @Override
    public ZegoAuxData onAuxCallback(int expectDataLength) {
        return null;
    }

    @Override
    public void onPublishQualityUpdate(String streamID, ZegoStreamQuality zegoStreamQuality) {

    }

    /**
     * 录音接口，如开启录音后，采集到的音频数据会通过此接口返回。
     *
     * @param audioData        录制的音频源数据
     * @param sampleRate       采样率
     * @param numberOfChannels 声道数
     * @param bitDepth         位深，通常为 16 bit
     * @param type             音频源类型
     */
    @Override
    public void onAudioRecord(byte[] audioData, int sampleRate, int numberOfChannels, int bitDepth, int type) {

    }

    /**
     * 房间状态回调。
     */
    @Override
    public void onKickOut(int errorCode, String roomId) {

    }

    @Override
    public void onDisconnect(int i, String s) {

    }

    // 该方法只会监听其他用户的开始推流，停止推流的回调，对于当前登录用户  无任何作用
    @Override
    public void onStreamUpdate(ZegoAudioStreamType zegoAudioStreamType, ZegoAudioStream zegoAudioStream) {

    }

    @Override
    public void onUserUpdate(ZegoUserState[] zegoUserStates, int updateType) {

    }

    @Override
    public void onUpdateOnlineCount(String s, int i) {

    }

    @Override
    public void onRecvRoomMessage(String s, ZegoRoomMessage[] zegoRoomMessages) {

    }

    @Override
    public void onRecvConversationMessage(String s, String s1, ZegoConversationMessage zegoConversationMessage) {

    }

    @Override
    public void onRecvBigRoomMessage(String s, ZegoBigRoomMessage[] zegoBigRoomMessages) {

    }

    @Override
    public void onRecvCustomCommand(String s, String s1, String s2, String s3) {

    }

    // 在处理过程中，没有发现该函数有任何回调
    @Override
    public void onStreamExtraInfoUpdated(ZegoAudioStream[] zegoAudioStreams, String s) {

    }

    /**
     * 音频播放回调事件
     */
    @Override
    public void onPlayStart() {

    }

    @Override
    public void onPlayPause() {

    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayResume() {

    }

    @Override
    public void onPlayError(int i) {

    }

    @Override
    public void onVideoBegin() {

    }

    @Override
    public void onAudioBegin() {

    }

    @Override
    public void onPlayEnd() {

    }

    @Override
    public void onBufferBegin() {

    }

    @Override
    public void onBufferEnd() {

    }

    @Override
    public void onSeekComplete(int i, long l) {

    }

    /**
     * 音视频设备错误通知。
     * @param deviceName  DeviceNameCamera 摄像头设备名字  DeviceNameMicrophone 麦克风设备名字
     * @param errorCode 未知
     */
    @Override
    public void onAudioDevice(String deviceName, int errorCode) {

    }
}

package com.tongdaxing.xchat_core.manager;

import android.support.annotation.IntRange;

import com.tongdaxing.xchat_core.manager.zego.BaseAudioEngine;
import com.tongdaxing.xchat_core.manager.zego.IBaseAudioEvent;
import com.tongdaxing.xchat_core.manager.zego.ZegoAudioRoomManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

/**
 * <p> 即构与声网音频相关控制类入口 </p>
 *
 * @author Edwin
 * @date 2018/11/08
 */
public final class RtcEngineManager implements IBaseAudioEvent {

    private static volatile RtcEngineManager sEngineManager;
    private static final Object SYNC_OBJECT = new Object();
    /**
     * 声波回调间隔
     */
    public final static int AUDIO_UPDATE_INTERVAL = 600;
    /**
     * 控制选用的语音类型 默认是声网
     */
    private int audioOrganization = AGORA;
    /**
     * 新增类型，需要在setAudioOrganization方法中设置注解范围
     */
    public static final int AGORA = 1;
    public static final int ZEGO = 2;

    public static RtcEngineManager get() {
        if (sEngineManager == null) {
            synchronized (SYNC_OBJECT) {
                if (sEngineManager == null) {
                    sEngineManager = new RtcEngineManager();
                }
            }
        }
        return sEngineManager;
    }

    /**
     * 是否启动即构
     *
     * @param audioOrganization 根据定义的静态变量设置
     */
    public void setAudioOrganization(@IntRange(from = AGORA, to = ZEGO) int audioOrganization) {
        this.audioOrganization = audioOrganization;
    }

    @Override
    public void setOnLoginCompletionListener(OnLoginCompletionListener listener) {
        factory().setOnLoginCompletionListener(listener);
    }

    @Override
    public void startRtcEngine(long uid, String appId, RoomInfo curRoomInfo) {
        factory().startRtcEngine(uid, appId, curRoomInfo);
    }

    @Override
    public void stopAudioMixing() {
        factory().stopAudioMixing();
    }

    @Override
    public void leaveChannel() {
        factory().leaveChannel();
    }

    @Override
    public void setRemoteMute(boolean mute) {
        factory().setRemoteMute(mute);
    }

    /**
     * 设置角色，上麦，下麦（调用）
     *
     * @param role CLIENT_ROLE_AUDIENCE: 听众 ，CLIENT_ROLE_BROADCASTER: 主播
     */
    @Override
    public void setRole(int role) {
        factory().setRole(role);
    }

    /**
     * 设置是否能说话，静音,人自己的行为
     *
     * @param mute true：静音，false：不静音
     */
    @Override
    public void setMute(boolean mute) {
        factory().setMute(mute);
    }

    //音乐播放相关---------------begin--------------------------

    @Override
    public void adjustAudioMixingVolume(int volume) {
        factory().adjustAudioMixingVolume(volume);
    }

    @Override
    public void adjustRecordingSignalVolume(int volume) {
        factory().adjustRecordingSignalVolume(volume);
    }

    @Override
    public void resumeAudioMixing() {
        factory().resumeAudioMixing();
    }

    @Override
    public void pauseAudioMixing() {
        factory().pauseAudioMixing();
    }

    @Override
    public long getAudioMixingCurrentPosition() {
        return factory().getAudioMixingCurrentPosition();
    }

    @Override
    public long getAudioMixingDuration() {
        return factory().getAudioMixingDuration();
    }

    @Override
    public boolean isAudienceRole() {
        return factory().isAudienceRole();
    }

    @Override
    public boolean isRemoteMute() {
        return factory().isRemoteMute();
    }

    @Override
    public boolean isMute() {
        return factory().isMute();
    }

    /**
     * 即构关闭指定用户的流，声网不使用
     *
     * @param uid 流id
     */
    @Override
    public void stopPlayingStream(String uid) {
        factory().stopPlayingStream(uid);
    }

    @Override
    public int startAudioMixing(String filePath, boolean loopback, int cycle) {
        return factory().startAudioMixing(filePath, loopback, cycle);
    }

    private BaseAudioEngine factory() {
        switch (audioOrganization) {
            case 1://声网SDK
                return RtcAudioRoomManager.get();
            case 2://即构SDK
                return ZegoAudioRoomManager.get();
            default:
                throw new IllegalArgumentException("参数错误");
        }
    }

}

package com.tongdaxing.xchat_core.manager.zego;

import com.tongdaxing.xchat_core.manager.OnLoginCompletionListener;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/4
 * 描述        音频相关的接口方法类
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public interface IBaseAudioEvent {

    /**
     * 设置登录成功的回调
     */
    void setOnLoginCompletionListener(OnLoginCompletionListener listener);

    /**
     * 初始化音频组件
     *
     * @param uid         用户的uid  （zego streamId）
     * @param appId       token
     * @param curRoomInfo 房间信息
     */
    void startRtcEngine(long uid, String appId, RoomInfo curRoomInfo);

    /**
     * 退出房间
     */
    void leaveChannel();

    /**
     * 禁闭喇叭
     */
    void setRemoteMute(boolean mute);

    /**
     * 设置角色，上麦，下麦（调用）
     *
     * @param role CLIENT_ROLE_AUDIENCE: 听众 ，CLIENT_ROLE_BROADCASTER: 主播
     */
    void setRole(int role);

    /**
     * 设置是否能说话，静音,人自己的行为
     *
     * @param mute true：静音，false：不静音
     */
    void setMute(boolean mute);

    /**
     * 设置音乐播放的声音
     *
     * @param volume 音量大小
     */
    void adjustAudioMixingVolume(int volume);

    /**
     * 设置人声播放的声音
     *
     * @param volume 音量大小
     */
    void adjustRecordingSignalVolume(int volume);

    /**
     * 停止音乐播放
     */
    void stopAudioMixing();

    /**
     * 播放音乐
     *
     * @param filePath 文件路径
     */
    int startAudioMixing(String filePath, boolean loopback, int cycle);

    /**
     * 恢复播放
     */
    void resumeAudioMixing();

    /**
     * 暂停播放
     */
    void pauseAudioMixing();

    /**
     * 获取当前播放进度
     */
    long getAudioMixingCurrentPosition();

    /**
     * 获取整个文件的播放时间
     */
    long getAudioMixingDuration();

    /**
     * 获取用户身份状态
     */
    boolean isAudienceRole();

    /**
     * 获取是否关闭喇叭的声音的状态
     */
    boolean isRemoteMute();

    /**
     * 获取是否禁麦的状态
     */
    boolean isMute();

    /**
     * 关闭某个指定流数据
     *
     * @param uid 要禁止的用户的uid
     */
    void stopPlayingStream(String uid);

}

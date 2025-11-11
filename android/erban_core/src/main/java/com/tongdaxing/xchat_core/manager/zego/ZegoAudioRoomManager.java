package com.tongdaxing.xchat_core.manager.zego;

import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_APP_ID;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_AUDIO_CHANNEL_COUNT;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_AUTO_MANUAL_PUBLISH;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_BUSINESS_TYPE_LIVE_BROADCAST;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_DEBUG_SIGN_KEY;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_DEGUB_APP_ID;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_NORMAL_AUDIO_BITRATE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_CAPTURE_SOUND_LEVEL_UPDATE_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_LOGIN_COMPLETION_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_OPERATION_SUCCESS_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_PLAY_RETRY_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_PUBLISH_RETRY_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_RESTART_CONNECTION_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_ON_SOUND_LEVEL_UPDATE_CODE;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_PUBLISH_RETRY_COUNT;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_SIGN_KEY;
import static com.tongdaxing.xchat_core.manager.zego.ZegoAudioConstants.ZEGO_SOUND_LEVEL_CYCLE;

import static io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE;
import static io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER;

import android.os.Handler;
import android.os.Message;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.OnLoginCompletionListener;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.zego.zegoaudioroom.ZegoAudioLiveEvent;
import com.zego.zegoaudioroom.ZegoAudioRoom;
import com.zego.zegoaudioroom.ZegoAudioStream;
import com.zego.zegoaudioroom.ZegoAudioStreamType;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.entity.ZegoAudioRecordConfig;
import com.zego.zegoliveroom.entity.ZegoUserState;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/1
 * 描述        即构音频相关控制类
 * <p>
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 */
public class ZegoAudioRoomManager extends BaseAudioEngine {

    private static final String TAG = "room_log ---> Zego";

    private static volatile ZegoAudioRoomManager sEngineManager;

    private ZegoAudioRoom mZegoAudioRoom;
    private ZegoEngineEventHandler engineEventHandler;

    /**
     * 是否已经登录房间
     */
    private boolean isLoginRoom;
    /**
     * 是否已经推流
     */
    private boolean isStartPublish;
    /**
     * 是否已经拉流
     */
    private boolean isStartPlay;
    /**
     * 当前推流失败重试的次数
     */
    private int retryPublishCount = 0;
    /**
     * 当前拉流失败重试的次数
     */
    private int retryPlayCount = 0;

    private OnLoginCompletionListener listener;

    private boolean isOnDisConnect;

    private ZegoAudioRoomManager() {
        speakZegoQueueMembersInfo = new ArrayList<>();
    }

    /**
     * 单例类
     */
    public static ZegoAudioRoomManager get() {
        if (sEngineManager == null) {
            sEngineManager = new ZegoAudioRoomManager();
        }
        return sEngineManager;
    }

    @Override
    public void setOnLoginCompletionListener(OnLoginCompletionListener listener) {
        this.listener = listener;
    }

    /**
     * 启动即构的sdk
     *
     * @param streamId    流
     * @param appId       鉴权
     * @param curRoomInfo 房间信息
     */
    @Override
    public void startRtcEngine(long streamId, String appId, RoomInfo curRoomInfo) {
        this.mCurrentRoomInfo = curRoomInfo;
        joinChannel(mCurrentRoomInfo.getRoomId(), streamId, appId);
    }

    /**
     * 初始化即构sdk
     */
    private void joinChannel(long channelId, long streamId, String appId) {
        LogUtil.d(TAG, "joinChannel ---> RoomId = " + channelId + " ---> uid = " + streamId + " ---> appId = " + appId);
        //初始化数据
        this.uid = streamId;
        this.isMute = false;
        this.isRemoteMute = false;
        this.isOnDisConnect = false;

        //设置是否是测试环境  统一配置
        // 记得修改正式环境
        ZegoAudioRoom.setUseTestEnv(false);
        //设置是否打印调试日志
        // 记得修改正式环境
        ZegoAudioRoom.setVerbose(BasicConfig.INSTANCE.isDebuggable());
        //设置自动模式 对应的值可参考Android系统的音频模式
        ZegoAudioRoom.setAudioDeviceMode(ZegoConstants.AudioDeviceMode.Communication);
        //设置业务类型
        ZegoAudioRoom.setBusinessType(ZEGO_BUSINESS_TYPE_LIVE_BROADCAST);
        //设置用户信息
        ZegoAudioRoom.setUser(streamId + "", appId + "");
        //loginRoom()
        initZegoEngine(channelId);
    }

    private void initZegoEngine(long channelId) {
        //设置用户是自动发布语音直播还是手动发布语音直播 必须在 loginRoom(String, ZegoLoginAudioRoomCallback) 之前调用
        //true 需要手动推流   false 自动推流
        if (mZegoAudioRoom == null) {
            mZegoAudioRoom = new ZegoAudioRoom();
        }
        mZegoAudioRoom.setManualPublish(ZEGO_AUTO_MANUAL_PUBLISH);
        //设置手机内置扬声器常开
        mZegoAudioRoom.setBuiltinSpeakerOn(true);
        //设置播放音量 音量大小 [0,100]
        mZegoAudioRoom.setPlayVolume(100);
        //远程喇叭 true 开启；false 静音
        mZegoAudioRoom.enableSpeaker(!isRemoteMute);
        // 默认开启不禁麦
        mZegoAudioRoom.enableMic(true);
        //初始化
        mZegoAudioRoom.initWithAppId(ZEGO_APP_ID, ZEGO_SIGN_KEY, BasicConfig.INSTANCE.getAppContext());
        //设置音频延迟模式（编码模式）。
        mZegoAudioRoom.setLatencyMode(ZegoConstants.LatencyMode.Normal);
        //设置码率
        mZegoAudioRoom.setAudioBitrate(ZEGO_NORMAL_AUDIO_BITRATE);
        //设置双声道模式
        mZegoAudioRoom.setAudioChannelCount(ZEGO_AUDIO_CHANNEL_COUNT);
        //启用用户声浪
        ZegoSoundLevelMonitor.getInstance().setCycle(ZEGO_SOUND_LEVEL_CYCLE);
        if (engineEventHandler == null) {
            engineEventHandler = new ZegoEngineEventHandler(this);
        }
        //是否启用音频录制
        isNeedRecord();
        ZegoSoundLevelMonitor.getInstance().setCallback(engineEventHandler);
        //设置音乐播放器的回调
        ZegoAudioPlayerManager.get().setCallback(engineEventHandler);
        //设置拉流的事件回调
        mZegoAudioRoom.setAudioPlayerDelegate(engineEventHandler);
        //设置推流的事件回调
        mZegoAudioRoom.setAudioPublisherDelegate(engineEventHandler);
        //设置推拉流状态更新回调
        mZegoAudioRoom.setAudioLiveEventDelegate(engineEventHandler);
        //设置退出房间的回调
        mZegoAudioRoom.setAudioAVEngineDelegate(engineEventHandler);
        //设置房间事件的回调
        mZegoAudioRoom.setAudioRoomDelegate(engineEventHandler);
        //设置用户状态监听
        mZegoAudioRoom.setUserStateUpdate(true);
        //设置音频设备错误通知回调
        mZegoAudioRoom.setAudioDeviceEventDelegate(engineEventHandler);
        //登录房间
        loginRoom(String.valueOf(channelId));
    }

    private long getAppId() {
        if (BasicConfig.INSTANCE.isDebuggable()) {
            return ZEGO_DEGUB_APP_ID;
        }
        return ZEGO_APP_ID;
    }

    private byte[] getAppSignature() {
        if (BasicConfig.INSTANCE.isDebuggable()) {
            return ZEGO_DEBUG_SIGN_KEY;
        }
        return ZEGO_SIGN_KEY;
    }

    /**
     * 登录房间
     *
     * @param channelId 房间roomId
     */
    private void loginRoom(String channelId) {
        if (mZegoAudioRoom != null) {
            mZegoAudioRoom.enableAux(false);
            boolean loginRoom = mZegoAudioRoom.loginRoom(String.valueOf(channelId), engineEventHandler);
            LogUtil.d(TAG, "loginRoom ---> loginRoom = " + loginRoom);
        }
    }

    /**
     * 停止播放某个流
     *
     * @param streamId 流id
     */
    @Override
    public void stopPlayingStream(String streamId) {
        if (mZegoAudioRoom != null) {
            mZegoAudioRoom.stopPlay(streamId);
        }
    }

    /**
     * 退出房间
     */
    @Override
    public void leaveChannel() {
        if (mZegoAudioRoom != null) {
            boolean logoutRoom = mZegoAudioRoom.logoutRoom();
            LogUtil.d(TAG, "leaveChannel ---> logoutRoom = " + logoutRoom);
            mZegoAudioRoom.unInit();
            mZegoAudioRoom = null;
            this.isStartPlay = false;
            this.isLoginRoom = false;
            this.isOnDisConnect = false;
            this.isStartPublish = false;
        }
        //清空回调事件
        engineEventHandler = null;
        //停止声浪的监听
        ZegoSoundLevelMonitor.getInstance().setCallback(null);
        ZegoSoundLevelMonitor.getInstance().stop();
        //清空任务队列
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        //重置状态
        this.isAudienceRole = true;
        this.isRemoteMute = false;
        this.needRecord = false;
        this.isMute = false;
    }

    /**
     * 关闭/打开喇叭
     *
     * @param mute true：打开  false：关闭
     */
    @Override
    public void setRemoteMute(boolean mute) {
        if (mZegoAudioRoom != null) {
            boolean enable = mZegoAudioRoom.enableSpeaker(!mute);
            LogUtil.d(TAG, "setRemoteMute ---> mute（true：打开  false：关闭） = " + mute + " ---> enable = " + enable);
            if (enable) {
                isRemoteMute = mute;
//                if (isRemoteMute) {
//                    ZegoSoundLevelMonitor.getInstance().stop();
//                } else {
//                    ZegoSoundLevelMonitor.getInstance().start();
//                }
            }
        }
    }

    /**
     * 设置角色，上麦，下麦（调用）
     *
     * @param role CLIENT_ROLE_AUDIENCE: 听众 ，CLIENT_ROLE_BROADCASTER: 主播
     */
    @Override
    public void setRole(int role) {
        if (mZegoAudioRoom != null) {
            boolean result = setClientRole(role);
            isAudienceRole = role != CLIENT_ROLE_BROADCASTER;
            LogUtil.d(TAG, "setRole ---> role（1 主播，2 听众） = " + role + " ---> result = " + result + " ---> " + "isAudienceRole = " + isAudienceRole);
        }
    }

    /**
     * 设置是否能说话，静音,人自己的行为
     *
     * @param mute true：静音，false：不静音
     */
    @Override
    public void setMute(boolean mute) {
        if (mZegoAudioRoom != null) {
            boolean enableMic = mZegoAudioRoom.enableMic(!mute);
            LogUtil.d(TAG, "setMute ---> mute = " + mute + " ---> " + "enableMicResult（true:成功， false:失败） = " + enableMic);
            if (enableMic) {
                isMute = mute;
                //停止音乐
                ZegoAudioPlayerManager.get().stop();
//                    if (isMute) {
//                        ZegoSoundLevelMonitor.getInstance().stop();
//                    } else {
//                        ZegoSoundLevelMonitor.getInstance().start();
//                    }
            }
        }
    }

    /**
     * 设置用户角色
     *
     * @param clientRole CLIENT_ROLE_AUDIENCE: 听众 ，CLIENT_ROLE_BROADCASTER: 主播
     */
    private boolean setClientRole(int clientRole) {
        if (mZegoAudioRoom != null) {
            LogUtil.d(TAG, "setClientRole --->  clientRole = " + clientRole + " ---> isLoginRoom = "
                    + isLoginRoom + " ---> " + "isStartPublish = " + isStartPublish);
            if (clientRole == CLIENT_ROLE_AUDIENCE) {
                if (isLoginRoom && isStartPublish) {
                    this.isStartPublish = false;
                    return mZegoAudioRoom.stopPublish();
                }
            } else {
                LogUtil.d(TAG, "setClientRole --->  clientRole = " + clientRole + " ---> isLoginRoom = "
                        + isLoginRoom + " ---> " + "isStartPublish = " + isStartPublish + " ---> " + "isImRoomConnection = "
                        + IMNetEaseManager.get().isImRoomConnection() + " ---> isOwnerOnMic = " + AvRoomDataManager.get().isOwnerOnMic());
                if (isLoginRoom && IMNetEaseManager.get().isImRoomConnection() && AvRoomDataManager.get().isOwnerOnMic()) {
                    boolean startPublish = mZegoAudioRoom.startPublish();
                    LogUtil.d(TAG, "setClientRole --->  startPublish = " + startPublish);
                    return startPublish;
                }
            }
        }
        return false;
    }

    /**
     * 是否需要开启录音 默认不需要
     */
    private void isNeedRecord() {
        if (needRecord && mZegoAudioRoom != null) {
            ZegoAudioRecordConfig config = new ZegoAudioRecordConfig();
            //声道数。 支持的声道数：1(单声道)； 2(双声道)。
            config.channels = 2;
            config.mask = ZegoConstants.AudioRecordMask.Mix;
            config.sampleRate = 44100;

            mZegoAudioRoom.enableSelectedAudioRecord(config);
            mZegoAudioRoom.setAudioRecordDelegate(engineEventHandler);
        }
    }

    /*------------------------------------音乐播放相关 start ----------------------------------*/

    /**
     * 设置播放音乐的音量
     *
     * @param volume - 音量，从0到100，默认是50
     */
    @Override
    public void adjustAudioMixingVolume(int volume) {
        ZegoAudioPlayerManager.get().setVolume(volume);
    }

    /**
     * 设置人声大小
     *
     * @param volume volume - 音量，从0到100，默认是50
     */
    @Override
    public void adjustRecordingSignalVolume(int volume) {
        if (mZegoAudioRoom != null) {
            mZegoAudioRoom.getLiveRoomInstance().setCaptureVolume(volume);
        }
    }

    /**
     * 恢复播放
     */
    @Override
    public void resumeAudioMixing() {
        ZegoAudioPlayerManager.get().resume();
    }

    /**
     * 暂停播放
     */
    @Override
    public void pauseAudioMixing() {
        ZegoAudioPlayerManager.get().pause();
    }

    /**
     * 获取当前播放进度
     */
    @Override
    public long getAudioMixingCurrentPosition() {
        return ZegoAudioPlayerManager.get().getAudioMixingCurrentPosition();
    }

    /**
     * 获取整个文件的播放时间
     */
    @Override
    public long getAudioMixingDuration() {
        return ZegoAudioPlayerManager.get().getAudioMixingDuration();
    }

    /**
     * 播放音乐
     */
    @Override
    public int startAudioMixing(String filePath, boolean loopback, int cycle) {
        if (StringUtils.isEmpty(filePath)) {
            return -1;
        }
        try {
            ZegoAudioPlayerManager.get().stop();
            ZegoAudioPlayerManager.get().start(filePath);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    /**
     * 停止播放音乐
     */
    @Override
    public void stopAudioMixing() {
        ZegoAudioPlayerManager.get().stop();
    }

    /*------------------------------------音乐播放相关 end ----------------------------------*/

    /**
     * Zego回调事件处理类
     */
    private Handler handler = new ZegoEngineHandler(this);

    private static class ZegoEngineHandler extends Handler {

        private WeakReference<ZegoAudioRoomManager> mReference;

        ZegoEngineHandler(ZegoAudioRoomManager manager) {
            mReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ZegoAudioRoomManager zegoEngineManager = mReference.get();
            if (zegoEngineManager == null) {
                return;
            }
            if (msg.what == ZEGO_ON_LOGIN_COMPLETION_CODE) {
                //判断是否是重连
                if (zegoEngineManager.isOnDisConnect) {
                    zegoEngineManager.isOnDisConnect = false;
                    zegoEngineManager.setRole(zegoEngineManager.isAudienceRole ? CLIENT_ROLE_AUDIENCE : CLIENT_ROLE_BROADCASTER);
                } else {
                    // 进入房间后，会重复初始化用户角色 故此处无需再次初始化
                    //zegoEngineManager.initRole();
                    IMNetEaseManager.get().joinAvRoom();
                }
            } else if (msg.what == ZEGO_ON_SOUND_LEVEL_UPDATE_CODE) {
                ZegoSoundLevelInfo[] speakers = (ZegoSoundLevelInfo[]) msg.obj;
                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo == null) {
                    return;
                }
                if (zegoEngineManager.speakZegoQueueMembersInfo.size() > 0) {
                    zegoEngineManager.speakZegoQueueMembersInfo.clear();
                }
                for (ZegoSoundLevelInfo speaker : speakers) {
                    int micPosition = AvRoomDataManager.get().getMicPositionByStreamID(speaker.streamID);
                    if (micPosition == Integer.MIN_VALUE) {
                        continue;
                    }
                    zegoEngineManager.speakZegoQueueMembersInfo.add(speaker);
                }
                IMNetEaseManager.get().getChatRoomEventObservable().onNext(new RoomEvent().setEvent(
                        RoomEvent.SPEAK_ZEGO_STATE_CHANGE).setSpeakQueueMembersPosition(zegoEngineManager.speakZegoQueueMembersInfo));
            } else if (msg.what == ZEGO_ON_CAPTURE_SOUND_LEVEL_UPDATE_CODE) {
                ZegoSoundLevelInfo levelInfo = (ZegoSoundLevelInfo) msg.obj;
                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo == null) {
                    return;
                }
                int micPosition = AvRoomDataManager.get().getMicPositionByStreamID(levelInfo.streamID);
                IMNetEaseManager.get().getChatRoomEventObservable().onNext(new RoomEvent()
                        .setEvent(RoomEvent.CURRENT_SPEAK_STATE_CHANGE).setCurrentMicPosition(micPosition).setCurrentMicStreamLevel(levelInfo.soundLevel));
            } else if (msg.what == ZEGO_ON_PUBLISH_RETRY_CODE && !zegoEngineManager.isStartPublish) {
                if (zegoEngineManager.retryPublishCount <= ZEGO_PUBLISH_RETRY_COUNT) {
                    zegoEngineManager.retryPublishCount++;
                    //重试推流处理
                    if (zegoEngineManager.mZegoAudioRoom != null) {
                        zegoEngineManager.mZegoAudioRoom.restartPublishStream();
                    }
                } else {
                    //重试次数超过十次 退出房间
                    zegoEngineManager.retryPublishCount = 0;
                    zegoEngineManager.onPublishOrPlayError();
                }
            } else if (msg.what == ZEGO_ON_PLAY_RETRY_CODE && !zegoEngineManager.isStartPlay) {
                if (zegoEngineManager.retryPlayCount <= ZEGO_PUBLISH_RETRY_COUNT) {
                    zegoEngineManager.retryPlayCount++;
                    //重试拉流处理
                    String streamID = (String) msg.obj;
                    if (zegoEngineManager.mZegoAudioRoom != null) {
                        zegoEngineManager.mZegoAudioRoom.restartPlayStream(streamID);
                    }
                } else {
                    //重试次数超过十次 退出房间
                    zegoEngineManager.retryPlayCount = 0;
                    zegoEngineManager.onPublishOrPlayError();
                }
            } else if (msg.what == ZEGO_ON_RESTART_CONNECTION_CODE && zegoEngineManager.isOnDisConnect) {
                //重连失败的逻辑  多次登录  如果还有问题  退出房间
                RoomInfo roomInfo = zegoEngineManager.mCurrentRoomInfo;
                if (roomInfo == null) {
                    return;
                }
                zegoEngineManager.loginRoom(String.valueOf(roomInfo.getRoomId()));
                //提示信息
                IMNetEaseManager.get().getChatRoomEventObservable()
                        .onNext(new RoomEvent().setEvent(RoomEvent.PLAY_OR_PUBLISH_NETWORK_ERROR));
            }
        }
    }

    /**
     * 推拉流失败次数超过十次的逻辑处理
     */
    private void onPublishOrPlayError() {
        IMNetEaseManager.get().getChatRoomEventObservable()
                .onNext(new RoomEvent().setEvent(RoomEvent.ZEGO_RESTART_CONNECTION_EVENT));
    }

    /**
     * Zego回调事件管理类
     */
    private static class ZegoEngineEventHandler extends IZegoEngineEventHandler {

        private WeakReference<ZegoAudioRoomManager> mReference;

        ZegoEngineEventHandler(ZegoAudioRoomManager manager) {
            mReference = new WeakReference<>(manager);
        }

        /**
         * 房间登录成功回调（ 与声网 onJoinChannelSuccess 功能一致）
         * 常见登录错误码请查看 {@link ZegoAudioConstants} 的房间相关错误码
         *
         * @param errorCode 0：登录成功 其他：登录失败
         */
        @Override
        public void onLoginCompletion(int errorCode) {
            LogUtil.d(TAG, "onLoginCompletion ---> errorCode = " + errorCode);
            if (errorCode == ZEGO_ON_LOGIN_COMPLETION_CODE) {
                //登录房间成功
                mReference.get().isLoginRoom = true;
                //开启用户声浪监听
                ZegoSoundLevelMonitor.getInstance().start();
                //登录云信房间
                sendHandlerMsg(ZEGO_ON_LOGIN_COMPLETION_CODE, ZEGO_ON_LOGIN_COMPLETION_CODE, false);
            } else if (errorCode == 4103) {
                sendHandlerMsg(ZEGO_ON_RESTART_CONNECTION_CODE, ZEGO_ON_RESTART_CONNECTION_CODE, true);
            } else {
                if (mReference.get().listener != null) {
                    LogUtil.d(TAG, "onLoginCompletion ---> onLoginCompletionError ---> errorCode = " + errorCode);
                    mReference.get().listener.onLoginCompletionFail("登录房间失败 errorCode ：" + errorCode);
                }
            }
        }

        /**
         * 网络拉流状态回调
         *
         * @param stateCode       状态码 状态码查看 {@link ZegoAudioConstants} onPlayStateUpdate的错误码说明
         * @param zegoAudioStream 流信息
         */
        @Override
        public void onPlayStateUpdate(int stateCode, ZegoAudioStream zegoAudioStream) {
            LogUtil.d(TAG, "onPlayStateUpdate ---> stateCode = " + stateCode + ", streamId = " + zegoAudioStream.getStreamId());
            if (stateCode == ZEGO_ON_OPERATION_SUCCESS_CODE) {
                //用户拉流成功
                mReference.get().isStartPlay = true;
            } else {
                sendHandlerMsg(ZEGO_ON_PLAY_RETRY_CODE, zegoAudioStream.getStreamId(), true);
            }
        }

        /**
         * 网络推流状态回调
         *
         * @param stateCode 状态码 状态码请查看 {@link ZegoAudioConstants} onPublishStateUpdate的错误码说明
         * @param streamId  流ID
         * @param hashMap   推流数据
         */
        @Override
        public void onPublishStateUpdate(int stateCode, String streamId, HashMap<String, Object> hashMap) {
            LogUtil.d(TAG, "onPublishStateUpdate ---> stateCode = " + stateCode + ", streamId = " + streamId);
            if (stateCode == ZEGO_ON_OPERATION_SUCCESS_CODE) {
                //用户推流成功
                mReference.get().isStartPublish = true;
                if (mReference.get().isMute()) {
                    //如果当前用户禁麦
                    mReference.get().mZegoAudioRoom.enableMic(false);
                }
            } else {
                //用户推流失败，延迟6秒，进行重新推流操作
                sendHandlerMsg(ZEGO_ON_PUBLISH_RETRY_CODE, ZEGO_ON_PUBLISH_RETRY_CODE, true);
            }
        }

        /**
         * 因为登录抢占原因等被挤出房间。（与声网 onConnectionInterrupted 功能一致 ）
         *
         * @param errorCode 原因 状态码请查看 {@link ZegoAudioConstants} OnKickOut的错误码说明
         * @param roomId    房间id
         */
        @Override
        public void onKickOut(int errorCode, String roomId) {
            LogUtil.d(TAG, "onKickOut ---> errorCode = " + errorCode + " ---> roomId = " + roomId);
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.RTC_ENGINE_NETWORK_CLOSE)
            );
        }

        /**
         * 与 Server 断开连接。（与声网 onConnectionLost 功能一致）
         *
         * @param errorCode 错误码 状态码请查看 {@link ZegoAudioConstants} onDisconnect的错误码说明
         * @param roomId    房间id
         */
        @Override
        public void onDisconnect(int errorCode, String roomId) {
            if (errorCode == 16777219) {
                // 用户90S以后断开的回调状态码
                if (mReference == null || mReference.get() == null) {
                    return;
                }
                //改变状态
                mReference.get().isStartPublish = false;
                mReference.get().isOnDisConnect = true;
                mReference.get().isLoginRoom = false;
                //重新登录
                sendHandlerMsg(ZEGO_ON_RESTART_CONNECTION_CODE, ZEGO_ON_RESTART_CONNECTION_CODE, true);
                LogUtil.d(TAG, "onDisconnect restartLoginRoom ---> errorCode = " + errorCode + " ---> roomId = " + roomId);
            }
            LogUtil.d(TAG, "onDisconnect ---> errorCode = " + errorCode + " ---> roomId = " + roomId);
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.RTC_ENGINE_NETWORK_CLOSE)
            );
        }

        @Override
        public void onAudioLiveEvent(ZegoAudioLiveEvent zegoAudioLiveEvent, HashMap<String, String> hashMap) {
            switch (zegoAudioLiveEvent) {
                //拉流时间回调
                case Audio_Play_BeginRetry:
                    break;
                case Audio_Play_RetrySuccess:
                    break;
                case Audio_Play_TempDisconnected:
                    break;

                //推流事件回调
                case Audio_Publish_BeginRetry:
                    break;
                case Audio_Publish_RetrySuccess:
                    break;
                case Audio_Publish_TempDisconnected:
                    break;
                default:
                    break;
            }
            LogUtil.d(TAG, "onAudioLiveEvent ---> zegoAudioLiveEvent = " + zegoAudioLiveEvent.name());
        }

        /*--------------------------声浪监听 start----------------------*/

        /**
         * soundLevel 更新回调 (与声网 onAudioVolumeIndication 功能一致)
         */
        @Override
        public void onSoundLevelUpdate(ZegoSoundLevelInfo[] zegoSoundLevelInfos) {
            super.onSoundLevelUpdate(zegoSoundLevelInfos);
            sendHandlerMsg(ZEGO_ON_SOUND_LEVEL_UPDATE_CODE, zegoSoundLevelInfos, false);
        }

        /**
         * 当前登录用户的声浪回调监听
         *
         * @param zegoSoundLevelInfo 当前登录用户的声浪信息
         */
        @Override
        public void onCaptureSoundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {
            super.onCaptureSoundLevelUpdate(zegoSoundLevelInfo);
            sendHandlerMsg(ZEGO_ON_CAPTURE_SOUND_LEVEL_UPDATE_CODE, zegoSoundLevelInfo, false);
        }

        /**
         * 音频数据播放结束。（与声网 onAudioMixingFinished 功能一致）
         */
        @Override
        public void onPlayEnd() {
            super.onPlayEnd();
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.METHOD_ON_AUDIO_MIXING_FINISHED));
        }

        @Override
        public void onStreamUpdate(ZegoAudioStreamType zegoAudioStreamType, ZegoAudioStream zegoAudioStream) {
            LogUtil.d(TAG, "onStreamUpdate ---> zegoAudioStreamType = " + zegoAudioStreamType + " ---> "
                    + "zegoAudioStream =" + zegoAudioStream.getStreamId());
        }

        @Override
        public void onAudioDevice(String deviceName, int errorCode) {
            if ("microphone".equals(deviceName) && errorCode == -1) {
                IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                        new RoomEvent().setEvent(RoomEvent.ZEGO_AUDIO_DEVICE_ERROR));
            }
            LogUtil.d(TAG, "onAudioDevice ---> deviceName = " + deviceName + " ---> errorCode = " + errorCode);
        }

        @Override
        public void onUserUpdate(ZegoUserState[] zegoUserStates, int updateType) {
            if (zegoUserStates != null && zegoUserStates.length > 0) {
                for (ZegoUserState zegoUserState : zegoUserStates) {
                    LogUtil.d(TAG, "onUserUpdate ---> userID " + zegoUserState.userID + " ---> userName = "
                            + zegoUserState.userName + " ---> updateFlag = " + zegoUserState.updateFlag + " ---> roomRole = " + zegoUserState.roomRole);
                }
            }
            LogUtil.d(TAG, "onUserUpdate ---> updateType = " + updateType);
        }

        /**
         * 发送消息
         *
         * @param what 类型
         * @param obj  数据
         */
        private void sendHandlerMsg(int what, Object obj, boolean isDelayed) {
            if (mReference == null || mReference.get() == null || mReference.get().handler == null) {
                return;
            }
            Message message = mReference.get().handler.obtainMessage(what, obj);
            if (isDelayed) {
                mReference.get().handler.sendMessageDelayed(message, 8000);
            } else {
                mReference.get().handler.sendMessage(message);
            }
        }
    }
}

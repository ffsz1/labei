package com.tongdaxing.xchat_core.manager;

import static io.agora.rtc.Constants.AUDIO_PROFILE_DEFAULT;
import static io.agora.rtc.Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY;
import static io.agora.rtc.Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO;
import static io.agora.rtc.Constants.AUDIO_PROFILE_MUSIC_STANDARD_STEREO;
import static io.agora.rtc.Constants.AUDIO_RECORDING_QUALITY_LOW;
import static io.agora.rtc.Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_core.manager.zego.BaseAudioEngine;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.live.LiveTranscoding;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/8
 * 描述        声网语音Room管理类
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public class RtcAudioRoomManager extends BaseAudioEngine {

    private static final String TAG = "room_log ---> Agora";

    private static volatile RtcAudioRoomManager sEngineManager;

    private RtcEngine mRtcEngine;
    private EngineEventHandler engineEventHandler;

    private OnLoginCompletionListener listener;

    private RtcAudioRoomManager() {
        speakQueueMembersPosition = new ArrayList<>();
    }

    public static RtcAudioRoomManager get() {
        if (sEngineManager == null) {
            sEngineManager = new RtcAudioRoomManager();
        }
        return sEngineManager;
    }

    @Override
    public void setOnLoginCompletionListener(OnLoginCompletionListener listener) {
        this.listener = listener;
    }

    @Override
    public void startRtcEngine(long uid, String appId, RoomInfo curRoomInfo) {
        joinChannel(curRoomInfo.getRoomId(), curRoomInfo.getAudioLevel(), uid, appId);
        if (curRoomInfo.getUid() == uid && curRoomInfo.getType() != RoomInfo.ROOMTYPE_HOME_PARTY) {
            //设置用户角色为主播,轰趴房不能默认设置房主为主播
            setRole(Constants.CLIENT_ROLE_BROADCASTER);
        } else {
            setRole(Constants.CLIENT_ROLE_AUDIENCE);
        }
    }

    private void joinChannel(long channelId, int audioLevel, long uid, String appId) {
        LogUtil.d(TAG, "RoomId = " + channelId + " ---> uid = " + uid + " ---> appId = " + appId);
        int quality = AUDIO_PROFILE_DEFAULT;
        if (audioLevel == 1) {
            quality = AUDIO_PROFILE_MUSIC_STANDARD_STEREO;
        } else if (audioLevel == 2) {
            quality = AUDIO_PROFILE_MUSIC_HIGH_QUALITY;
        } else if (audioLevel >= 3) {
            quality = AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO;
        }
        initRtcEngine(channelId, uid, quality, AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT, appId);
    }

    private void initRtcEngine(long channelId, long uid, int quality, int audioShowRoom, String appId) {
        this.uid = uid;
        this.isMute = false;
        this.isRemoteMute = false;
        if (mRtcEngine == null) {
            try {
                if (engineEventHandler == null) {
                    engineEventHandler = new EngineEventHandler(this);
                }
                mRtcEngine = RtcEngine.create(BasicConfig.INSTANCE.getAppContext(), "9cd492e46006470781b2ccf38368fd9e", engineEventHandler);
            } catch (Exception e) {
                LogUtil.d("setRole", "need to check rtc sdk init fatal error = " + e != null ? e.getMessage() : "");
            }
            //设置频道模式为直播
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableAudioVolumeIndication(900, 3);
            mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory() + File.separator + BasicConfig.INSTANCE.getAppContext().getPackageName() + "/log/agora-rtc.log");
        }
        if (mRtcEngine != null) {
            mRtcEngine.setAudioProfile(quality, audioShowRoom);
            //创建并加入频道
            int joinChannel = mRtcEngine.joinChannel(appId, String.valueOf(channelId), null, (int) uid);
            LogUtil.d(TAG, "loginRoom ---> loginRoom = " + joinChannel);
        }
    }

    @Override
    public void stopAudioMixing() {
        if (mRtcEngine != null) {
            mRtcEngine.stopAudioMixing();
        }
    }

    /**
     * 该方法目前仅用于即构的处理，当前只是实现  不处理，外部勿调用
     *
     * @param uid 流id
     */
    @Deprecated
    @Override
    public void stopPlayingStream(String uid) {

    }

    @Override
    public void leaveChannel() {
        //addOrRemovePublishStreamUrl(false);
        if (mRtcEngine != null) {
            stopAudioMixing();
            int leaveChannel = mRtcEngine.leaveChannel();
            LogUtil.d(TAG, "leaveChannel ---> logoutRoom = " + leaveChannel);
            mRtcEngine = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        isAudienceRole = true;
        isMute = false;
        isRemoteMute = false;
        needRecord = false;
    }

    @Override
    public void setRemoteMute(boolean mute) {
        if (mRtcEngine != null) {
            int result = mRtcEngine.muteAllRemoteAudioStreams(mute);
            LogUtil.d(TAG, "setRemoteMute ---> mute = " + mute + " ---> result = " + result);
            if (result == 0) {
                isRemoteMute = mute;
            } else {
                LogUtil.d("setRemoteMute", "mute = " + mute + "  result = " + result);
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
        if (mRtcEngine != null) {
            //先已是否静音为准
            int result;
            if (role == Constants.CLIENT_ROLE_BROADCASTER && IMNetEaseManager.get().isImRoomConnection() && AvRoomDataManager.get().isOwnerOnMic()) {
                isAudienceRole = false;
                result = mRtcEngine.setClientRole(isMute ? Constants.CLIENT_ROLE_AUDIENCE : role);
            } else {
                isAudienceRole = true;
                result = mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            }
            LogUtil.d(TAG, "setRole ---> role = " + role + " ---> result = " + result + " ---> isAudienceRole = " + isAudienceRole);
        }
    }

    /**
     * 设置是否能说话，静音,人自己的行为
     *
     * @param mute true：静音，false：不静音
     */
    @Override
    public void setMute(boolean mute) {
        if (mRtcEngine != null) {
            int result = mRtcEngine.setClientRole(mute ? Constants.CLIENT_ROLE_AUDIENCE : Constants.CLIENT_ROLE_BROADCASTER);
            LogUtil.d(TAG, "setMute ---> mute（true：静音，false：不静音） = " + mute + " ---> result = " + result);
            if (result == 0) {
                isMute = mute;
            } else {
                LogUtil.d("setMute", "mute（true：静音，false：不静音） = " + mute + "  result = " + result);
            }
        }
    }

    private Handler handler = new RtcEngineHandler(this);

    private static class RtcEngineHandler extends Handler {

        private WeakReference<RtcAudioRoomManager> mReference;

        RtcEngineHandler(RtcAudioRoomManager manager) {
            mReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RtcAudioRoomManager rtcEngineManager = mReference.get();
            if (rtcEngineManager == null) {
                return;
            }
            if (msg.what == 0) {
                IMNetEaseManager.get().joinAvRoom();
                if (rtcEngineManager.needRecord) {
                    rtcEngineManager.mRtcEngine.startAudioRecording(Environment.getExternalStorageDirectory()
                            + File.separator + BasicConfig.INSTANCE.getAppContext().getPackageName()
                            + "/audio/" + System.currentTimeMillis() + ".aac", AUDIO_RECORDING_QUALITY_LOW);
                }
            } else if (msg.what == 1) {
                IRtcEngineEventHandler.AudioVolumeInfo[] speakers = (IRtcEngineEventHandler.AudioVolumeInfo[]) msg.obj;
                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo == null) {
                    return;
                }
                if (rtcEngineManager.speakQueueMembersPosition.size() > 0) {
                    rtcEngineManager.speakQueueMembersPosition.clear();
                }
                for (IRtcEngineEventHandler.AudioVolumeInfo speaker : speakers) {
                    // 0 代表的是房主,其他代表的是uid
                    long uid = speaker.uid == 0 ? rtcEngineManager.uid : speaker.uid;
                    int micPosition = AvRoomDataManager.get().getMicPosition(uid);
                    if (micPosition == Integer.MIN_VALUE) {
                        continue;
                    }
                    rtcEngineManager.speakQueueMembersPosition.add(micPosition);
                }
                IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                        new RoomEvent().setEvent(RoomEvent.SPEAK_STATE_CHANGE)
                                .setMicPositionList(rtcEngineManager.speakQueueMembersPosition));
            } else if (msg.what == 2) {
                Integer uid = (Integer) msg.obj;
            }
        }
    }

    private static class EngineEventHandler extends IRtcEngineEventHandler {
        private WeakReference<RtcAudioRoomManager> mReference;

        EngineEventHandler(RtcAudioRoomManager manager) {
            mReference = new WeakReference<>(manager);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            LogUtil.d(TAG, "onJoinChannelSuccess ---> channel = " + channel + " ---> uid = " + uid + " ---> elapsed = " + elapsed);
            if (mReference.get() != null) {
                mReference.get().handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onLastmileQuality(int quality) {
            super.onLastmileQuality(quality);
            if (quality >= 3) {
                IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                        new RoomEvent().setEvent(RoomEvent.RTC_ENGINE_NETWORK_BAD)
                );
            }
        }

        @Override
        public void onConnectionInterrupted() {
            super.onConnectionInterrupted();
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.RTC_ENGINE_NETWORK_CLOSE)
            );
        }

        @Override
        public void onConnectionLost() {
            super.onConnectionLost();
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.RTC_ENGINE_NETWORK_CLOSE)
            );
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            super.onAudioVolumeIndication(speakers, totalVolume);
            RtcAudioRoomManager manager = mReference.get();
            if (manager != null) {
                Message message = manager.handler.obtainMessage();
                message.what = 1;
                message.obj = speakers;
                manager.handler.sendMessage(message);
            }
        }

        @Override
        public void onUserMuteAudio(int uid, boolean muted) {
            super.onUserMuteAudio(uid, muted);
            RtcAudioRoomManager manager = mReference.get();
            if (manager != null) {
                if (muted) {
                    Message message = manager.handler.obtainMessage();
                    message.what = 2;
                    message.obj = uid;
                    manager.handler.sendMessage(message);
                }
            }
        }

        @Override
        public void onAudioMixingFinished() {
            super.onAudioMixingFinished();
            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                    new RoomEvent().setEvent(RoomEvent.METHOD_ON_AUDIO_MIXING_FINISHED)
            );
        }
    }

    //音乐播放相关---------------begin--------------------------

    @Override
    public void adjustAudioMixingVolume(int volume) {
        if (mRtcEngine != null) {
            mRtcEngine.adjustAudioMixingVolume(volume);
        }
    }

    @Override
    public void adjustRecordingSignalVolume(int volume) {
        if (mRtcEngine != null) {
            mRtcEngine.adjustRecordingSignalVolume(volume);
        }
    }

    @Override
    public void resumeAudioMixing() {
        if (mRtcEngine != null) {
            mRtcEngine.resumeAudioMixing();
        }
    }

    @Override
    public void pauseAudioMixing() {
        if (mRtcEngine != null) {
            mRtcEngine.pauseAudioMixing();
        }
    }

    /**
     * 获取当前播放进度
     */
    @Override
    public long getAudioMixingCurrentPosition() {
        if (mRtcEngine != null) {
            return mRtcEngine.getAudioMixingCurrentPosition();
        }
        return 0;
    }

    /**
     * 获取整个文件的播放时间
     */
    @Override
    public long getAudioMixingDuration() {
        if (mRtcEngine != null) {
            return mRtcEngine.getAudioMixingDuration();
        }
        return 0;
    }

    @Override
    public int startAudioMixing(String filePath, boolean loopback, int cycle) {
        if (mRtcEngine != null) {
            mRtcEngine.stopAudioMixing();
            int result;
            try {
                result = mRtcEngine.startAudioMixing(filePath, false, false, 1);
            } catch (Exception e) {
                return -1;
            }
            return result;
        }
        return -1;
    }
    //音乐播放相关---------------end--------------------------
}

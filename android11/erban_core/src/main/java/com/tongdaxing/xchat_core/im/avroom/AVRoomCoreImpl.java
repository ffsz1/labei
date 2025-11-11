package com.tongdaxing.xchat_core.im.avroom;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment
        .CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN;

import static io.agora.rtc.Constants.AUDIO_PROFILE_DEFAULT;
import static io.agora.rtc.Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY;
import static io.agora.rtc.Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO;
import static io.agora.rtc.Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT;
import static io.agora.rtc.Constants.AUDIO_SCENARIO_GAME_STREAMING;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;


/**
 * Created by zhouxiangfeng on 2017/5/29.
 */

public class AVRoomCoreImpl extends AbstractBaseCore implements IAVRoomCore {

    private RtcEngine mRtcEngine;
    private static Map<String, Integer> speakers;
    private static String uid;
    private boolean isAudienceRole;
    private boolean isMute;
    private boolean isRemoteMute;
    private boolean isRecordMute;

    private long time;

    private AvRoomHandler handler = new AvRoomHandler();

    static class AvRoomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_JOIN_AV_ROOM);
            } else if (msg.what == 1) {
                IRtcEngineEventHandler.AudioVolumeInfo[] handlerSpeakers = (IRtcEngineEventHandler.AudioVolumeInfo[]) msg.obj;
                if (speakers == null) {
                    speakers = new HashMap<>();
                }
                speakers.clear();
                if (handlerSpeakers != null) {
                    for (int i = 0; i < handlerSpeakers.length; i++) {
                        String handlerUid = handlerSpeakers[i].uid + "";
                        if (handlerUid.equals("0")) {
                            handlerUid = uid;
                        }
                        speakers.put(handlerUid, handlerSpeakers[i].volume);
                    }
                }
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_SPEEK, speakers);
            } else if (msg.what == 2) {
                Integer uid = (Integer) msg.obj;
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_USER_MUTE_AUDIO, uid.intValue());
            }
        }
    };
    private UserInfo roomOwnerInfo = null;

    private class EngineEventHandler extends IRtcEngineEventHandler {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            LogUtil.i("enterRoom", "av enterRoom Success--->" + (System.currentTimeMillis() - time));
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
        }

        @Override
        public void onActiveSpeaker(int uid) {
            super.onActiveSpeaker(uid);
        }

        @Override
        public void onLastmileQuality(int quality) {
            super.onLastmileQuality(quality);
            if (quality >= 3) {
                notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_NETWORK_BAD);
            }
        }

        @Override
        public void onConnectionInterrupted() {
            super.onConnectionInterrupted();
            notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_CONNECT_LOST);
        }

        @Override
        public void onConnectionLost() {
            super.onConnectionLost();
            notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_CONNECT_LOST);
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            super.onAudioVolumeIndication(speakers, totalVolume);
            Message message = handler.obtainMessage();
            message.what = 1;
            message.obj = speakers;
            handler.sendMessage(message);
        }

        @Override
        public void onUserMuteAudio(int uid, boolean muted) {
            super.onUserMuteAudio(uid, muted);
            if (muted) {
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = uid;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onAudioMixingFinished() {
            super.onAudioMixingFinished();
            notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.METHOD_ON_AUDIO_MIXING_FINISHED);
        }
    }

    @Override
    public void joinChannel(String channelId, int uid) {
        this.uid = uid + "";
        this.isMute = false;
        this.isRemoteMute = false;
        this.isRecordMute = false;
//        ensureRtcEngineReadyLock();
        if (mRtcEngine == null) {
            try {
                mRtcEngine = RtcEngine.create(BasicConfig.INSTANCE.getAppContext(), "a8f174814a1e43478c6f3d99c1941239", new EngineEventHandler());
            } catch (Exception e) {
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setAudioProfile(AUDIO_PROFILE_MUSIC_HIGH_QUALITY, AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT);
            mRtcEngine.enableAudioVolumeIndication(600, 3); // 200 ms
            mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                    + File.separator + BasicConfig.INSTANCE.getAppContext().getPackageName() + "/log/agora-rtc.log");
        }
        time = System.currentTimeMillis();
        LogUtil.i("enterRoom", "av enterRoom--->");
        mRtcEngine.joinChannel(null, channelId, null, uid);
    }

    @Override
    public void joinHighQualityChannel(String channelId, int uid, boolean record) {
        this.uid = uid + "";
        this.isMute = false;
        this.isRemoteMute = false;
        this.isRecordMute = false;
        if (mRtcEngine == null) {
            try {
                mRtcEngine = RtcEngine.create(BasicConfig.INSTANCE.getAppContext(), "c5f1fa4878d141f99f3e86ec59f619d9", new EngineEventHandler());
            } catch (Exception e) {
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setAudioProfile(AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO, AUDIO_SCENARIO_GAME_STREAMING);
            mRtcEngine.enableAudioVolumeIndication(600, 3); // 200 ms
            mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
//            mRtcEngine.setParameters("{\"rtc.log_filter\":65535}");
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                    + File.separator + BasicConfig.INSTANCE.getAppContext().getPackageName() + "/log/agora-rtc.log");
        }
        time = System.currentTimeMillis();
        LogUtil.i("enterRoom", "av enterRoom--->");
        mRtcEngine.joinChannel(null, channelId, null, uid);
    }

    @Override
    public void leaveChannel() {
        if (mRtcEngine != null) {
            stopAudioMixing();
            mRtcEngine.leaveChannel();
            mRtcEngine = null;
        }
        isAudienceRole = true;
        isMute = false;
        isRemoteMute = false;
    }

    private RtcEngine ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            try {
                mRtcEngine = RtcEngine.create(BasicConfig.INSTANCE.getAppContext(), "c5f1fa4878d141f99f3e86ec59f619d9", new EngineEventHandler());
            } catch (Exception e) {
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.setAudioProfile(AUDIO_PROFILE_DEFAULT, AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT);
            mRtcEngine.enableAudioVolumeIndication(600, 3); // 200 ms
            mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                    + File.separator + BasicConfig.INSTANCE.getAppContext().getPackageName() + "/log/agora-rtc.log");
        }
        return mRtcEngine;
    }

    @Override
    public void setRole(int role) {
        if (mRtcEngine != null) {
            if (isMute) {
                mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            } else {
                mRtcEngine.setClientRole(role);
            }

            if (role == Constants.CLIENT_ROLE_BROADCASTER) {
                isAudienceRole = false;
            } else {
                isAudienceRole = true;
            }
        }
    }

    @Override
    public void setMute(boolean mute) {
        if (mRtcEngine != null) {
            int result = 0;
            if (mute) {
                result = mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            } else {
                result = mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            }
            if (result == 0) {
                isMute = mute;
            }
        }
    }

    @Override
    public void setRemoteMute(boolean mute) {
        if (mRtcEngine != null) {
            int result = mRtcEngine.muteAllRemoteAudioStreams(mute);
            if (result == 0) {
                isRemoteMute = mute;
            }
        }
    }

    @Override
    public void setRecordMute(boolean recordMute) {
        if (mRtcEngine != null) {
            int result = -1;
            if (recordMute) {
                result = mRtcEngine.adjustRecordingSignalVolume(0);
            } else {
                result = mRtcEngine.adjustRecordingSignalVolume(100);
            }
            if (result == 0) {
                isRecordMute = recordMute;
            }
        }
    }

    @Override
    public int startAudioMixing(String filePath, boolean loopback, int cycle) {
        if (mRtcEngine != null) {
            mRtcEngine.stopAudioMixing();
            int result = 0;
            try {
                result = mRtcEngine.startAudioMixing(filePath, loopback, false, cycle);
            } catch (Exception e) {
                return -1;
            }
            return result;
        }
        return -1;
    }

    @Override
    public int resumeAudioMixing() {
        if (mRtcEngine != null) {
            int result = mRtcEngine.resumeAudioMixing();
            return result;
        }
        return -1;
    }

    @Override
    public int pauseAudioMixing() {
        if (mRtcEngine != null) {
            int result = mRtcEngine.pauseAudioMixing();
            return result;
        }
        return -1;
    }

    @Override
    public int stopAudioMixing() {
        if (mRtcEngine != null) {
            int result = mRtcEngine.stopAudioMixing();
            return result;
        }

        return -1;
    }

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
    public boolean isAudienceRole() {
        return isAudienceRole;
    }

    @Override
    public boolean isMute() {
        return isMute;
    }

    @Override
    public boolean isRemoteMute() {
        return isRemoteMute;
    }

    @Override
    public boolean isRecordMute() {
        return isRecordMute;
    }

    @Override
    public void requestRoomOwnerInfo(String uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", uid);
        params.put("uid",CoreManager.getCore(IAuthCore.class).getCurrentUid()+"");
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (response.isSuccess()) {
                    roomOwnerInfo = response.getData();

                }
            }
        });
    }

    @Override
    public UserInfo getRoomOwner() {
        return roomOwnerInfo;
    }

    @Override
    public void removeRoomOwnerInfo() {
        roomOwnerInfo = null;
    }


    @Override
    public void changeRoomMsgFilter(boolean roomOwner, final int publicChatSwitch, String ticket, final long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uid + "");
        params.put("ticket", ticket);
        params.put("publicChatSwitch", String.valueOf(publicChatSwitch));
        String shortUrl;
        if (roomOwner) {
            shortUrl = UriProvider.updateRoomInfo();
        } else {
            params.put("roomUid", AvRoomDataManager.get().mCurrentRoomInfo.getUid() + "");
            shortUrl = UriProvider.updateByAdimin();
        }
        OkHttpManager.getInstance().doPostRequest(shortUrl, params, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json json) {
                int code = json.num("code");
                if (code == 200) {
                    if (publicChatSwitch == 0) {
                        IMNetEaseManager.get().systemNotificationBySdk(uid, CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN, -1);
                    } else {
                        IMNetEaseManager.get().systemNotificationBySdk(uid, CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE, -1);
                    }
                }
            }
        });
    }
}

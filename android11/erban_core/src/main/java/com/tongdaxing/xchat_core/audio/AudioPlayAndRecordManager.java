package com.tongdaxing.xchat_core.audio;

import android.content.Context;
import android.media.AudioManager;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;

/**
 * @author zhouxiangfeng
 * @date 2017/5/20
 */

public class AudioPlayAndRecordManager {

    private static AudioPlayAndRecordManager audioPlayManager;
    private AudioPlayer player;
    private AudioRecorder recorder;

    private AudioPlayAndRecordManager() {

    }

    public static AudioPlayAndRecordManager getInstance() {
        if (audioPlayManager == null) {
            audioPlayManager = new AudioPlayAndRecordManager();
        }
        return audioPlayManager;
    }

    public AudioPlayer getAudioPlayer(Context context, String filePath, OnPlayListener listener) {
        // 构造播放器对象
        player = new AudioPlayer(context, filePath, listener);
        return player;
    }

    public void play() {
        // 开始播放。需要传入一个 Stream Type 参数，表示是用听筒播放还是扬声器。取值可参见
        // android.media.AudioManager#STREAM_***
        // AudioManager.STREAM_VOICE_CALL 表示使用听筒模式
        // AudioManager.STREAM_MUSIC 表示使用扬声器模式
        player.start(AudioManager.STREAM_MUSIC);
    }


    public void seekTo(int pausedPostion) {
        // 如果中途切换播放设备，重新调用 start，传入指定的 streamType 即可。player 会自动停止播放，然后再以新的 streamType 重新开始播放。
        // 如果需要从中断的地方继续播放，需要外面自己记住已经播放过的位置，然后在 onPrepared 回调中调用 seekTo
        player.seekTo(pausedPostion);
    }


    public void stopPlay() {
        // 主动停止播放
        player.stop();
    }


    public AudioRecorder getAudioRecorder(Context context, IAudioRecordCallback callback) {
        // 初始化recorder
        recorder = new AudioRecorder(
                context,
                RecordType.AAC, // 录制音频类型（aac/amr)
                0, // 最长录音时长，到该长度后，会自动停止录音, 默认120s
                callback);
        return recorder;
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void startRecord() {

        if (null != recorder) {
            if (recorder.isRecording()) {
                recorder.completeRecord(true);
                recorder.destroyAudioRecorder();
            }
            recorder.startRecord();
        }
    }

    public void stopRecord(boolean cancel) {
        if (null != recorder && recorder.isRecording()) {
            recorder.completeRecord(cancel);

        }
    }

    /**
     * 在录音过程中可以获取当前录音时最大振幅（40ms更新一次数据），接口为
     */
    public int getCurrentRecordMaxAmplitude(Context var1, RecordType var2, int var3, IAudioRecordCallback var4) {
        return recorder.getCurrentRecordMaxAmplitude();
    }

    /** 释放资源 */
    public void release() {
        if (isPlaying()) {
            stopPlay();
        }
        if (player != null) {
            player.setOnPlayListener(null);
        }
    }
}

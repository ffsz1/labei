package com.tongdaxing.xchat_core.audio;

import android.content.Context;
import android.media.AudioManager;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;

/**
 * Created by zhouxiangfeng on 2017/5/20.
 */

public class AudioPlayManager {

    private AudioPlayer player;

    public AudioPlayManager(Context context, String filePath, OnPlayListener listener) {
        player = new AudioPlayer(context, filePath, listener);
    }

    public void setDataSource(String dataSource) {
        player.setDataSource(dataSource);
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

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    /** 释放资源 */
    public void release() {
        if (isPlaying()) {
            stopPlay();
        }
        player.setOnPlayListener(null);
    }
}

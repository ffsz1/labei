package com.tongdaxing.xchat_core.manager.zego;

import com.zego.zegoavkit2.IZegoMediaPlayerCallback;
import com.zego.zegoavkit2.ZegoMediaPlayer;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/1
 * 描述        即构声网音乐播放相关逻辑
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public class ZegoAudioPlayerManager {

    private static volatile ZegoAudioPlayerManager sZegoAudioPlayer;
    private static final Object SYNC_OBJECT = new Object();

    private ZegoMediaPlayer mZegoMediaPlayer;

    private ZegoAudioPlayerManager() {
        //初始化
        mZegoMediaPlayer = new ZegoMediaPlayer();
        //设置播放模式 PlayerTypeAux：播放，并且将其混入推流中
        mZegoMediaPlayer.init(ZegoMediaPlayer.PlayerTypeAux);
    }

    /**
     * 单例类
     */
    public static ZegoAudioPlayerManager get() {
        if (sZegoAudioPlayer == null) {
            synchronized (SYNC_OBJECT) {
                if (sZegoAudioPlayer == null) {
                    sZegoAudioPlayer = new ZegoAudioPlayerManager();
                }
            }
        }
        return sZegoAudioPlayer;
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.pause();
        }
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.resume();
        }
    }

    /**
     * 获取当前播放进度
     */
    public long getAudioMixingCurrentPosition() {
        if (mZegoMediaPlayer != null) {
            return mZegoMediaPlayer.getCurrentDuration();
        }
        return 0;
    }

    /**
     * 获取整个文件的播放时间
     */
    public long getAudioMixingDuration() {
        if (mZegoMediaPlayer != null) {
            return mZegoMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 设置播放器事件回调
     */
    public void setCallback(IZegoMediaPlayerCallback callback) {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.setCallback(callback);
        }
    }

    /**
     * 设置音量
     *
     * @param volume volume - 音量，从0到100，默认是50
     */
    public void setVolume(int volume) {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.setVolume(volume);
        }
    }

    /**
     * 开始播放
     *
     * @param path 媒体文件的路径
     */
    public void start(String path) {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.start(path, false);
        }
    }

    /**
     * 结束播放
     */
    public void stop() {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.stop();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mZegoMediaPlayer != null) {
            mZegoMediaPlayer.stop();
            mZegoMediaPlayer.uninit();
            mZegoMediaPlayer = null;
            sZegoAudioPlayer = null;
        }
    }

}

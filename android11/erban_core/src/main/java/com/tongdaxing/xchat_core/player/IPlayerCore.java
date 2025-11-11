package com.tongdaxing.xchat_core.player;

import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

/**
 * Created by chenran on 2017/10/28.
 */

public interface IPlayerCore extends IBaseCore {
    public static final int STATE_STOP = 0;
    public static final int STATE_PLAY = 1;
    public static final int STATE_PAUSE = 2;

    boolean closeMicToStopMusic = false;

    /**
     * 刷新本地音乐库
     */
    void refreshLocalMusic(List<LocalMusicInfo> lastScannedSongs);

    void addMusicToPlayerList(LocalMusicInfo localMusicInfo);

    void deleteMusicFromPlayerList(LocalMusicInfo localMusicInfo);

    /**
     * 查询本地音乐列表
     */
    List<LocalMusicInfo> requestLocalMusicInfos();

    /**
     * 查询本地播放器列表
     *
     * @return
     */
    List<LocalMusicInfo> requestPlayerListLocalMusicInfos();

    boolean isRefresh();

    int getState();

    LocalMusicInfo getCurrent();

    List<LocalMusicInfo> getPlayerListMusicInfos();

    int play(LocalMusicInfo localMusicInfo);

    void pause();

    void stop();

    void seekVolume(int volume);

    void seekRecordingVolume(int volume);

    int getCurrentVolume();

    int getCurrentRecordingVolume();

    int playNext();

    int playForward();
}

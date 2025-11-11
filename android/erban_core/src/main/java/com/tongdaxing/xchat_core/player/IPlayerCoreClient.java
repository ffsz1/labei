package com.tongdaxing.xchat_core.player;

import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by chenran on 2017/10/28.
 */

public interface IPlayerCoreClient extends ICoreClient{
    public static final String METHOD_ON_REFRESH_LOCAL_MUSIC= "onRefreshLocalMusic";
    public static final String METHOD_ON_REFRESH_LOCAL_MUSIC_PROGRESS = "onRefreshLocalMusicProgress";
    public static final String METHOD_ON_REFRESH_PLAYER_LIST = "onRefreshPlayerList";
    public static final String METHOD_ON_MUSIC_PLAYING = "onMusicPlaying";
    public static final String METHOD_ON_MUSIC_PAUSE = "onMusicPause";
    public static final String METHOD_ON_MUSIC_STOP = "onMusicStop";
    public static final String METHOD_ON_CURRENT_MUSIC_UPDATE = "onCurrentMusicUpdate";
    public static final String METHOD_ON_MUSIC_PROGRESS_UPDATE = "onMusicProgressUpdate";
    void onRefreshLocalMusicProgress(int progress);
    void onRefreshLocalMusic(List<LocalMusicInfo> localMusicInfoList);
    void onRefreshPlayerList(List<LocalMusicInfo> playerListMusicInfoList);
    void onMusicPlaying(LocalMusicInfo localMusicInfo);
    void onMusicPause(LocalMusicInfo localMusicInfo);
    void onMusicStop();
    void onCurrentMusicUpdate(LocalMusicInfo localMusicInfo);
    void onMusicProgressUpdate(int total, int current);
}

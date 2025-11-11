package com.tongdaxing.xchat_core.player;

import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by chenran on 2017/10/31.
 */

public interface IPlayerDbCore extends IBaseCore{

    /**
     * 存储本地音乐
     * @param localId
     */
    public void addToPlayerList(long localId);

    /**
     * 查询歌曲
     * @param localId
     * @return
     */
    public LocalMusicInfo requestLocalMusicInfoByLocalId(long localId);

    /**
     * 删除本地音乐
     * @param localId
     */
    public void deleteFromPlayerList(long localId);

    /**
     * 扫描本地音乐
     */
    public void replaceAllLocalMusics(List<LocalMusicInfo> localMusicInfoList);

    /**
     * 查询本地所有音乐
     */
    public RealmResults<LocalMusicInfo> queryAllLocalMusicInfos();

    /**
     * 查询本地音乐播放列表音乐
     */
    public RealmResults<LocalMusicInfo> queryPlayerListLocalMusicInfos();
}

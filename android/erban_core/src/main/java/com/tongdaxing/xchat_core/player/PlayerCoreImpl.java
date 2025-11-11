package com.tongdaxing.xchat_core.player;

import android.os.Handler;
import android.os.Message;

import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_core.utils.AsyncTaskScanMusicFile;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.pref.CommonPref;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.RealmResults;

/**
 * Created by chenran on 2017/10/28.
 */

public class PlayerCoreImpl extends AbstractBaseCore implements IPlayerCore {
    private AsyncTaskScanMusicFile scanMediaTask;
    private boolean isRefresh;
    private List<LocalMusicInfo> playerListMusicInfos;
    private LocalMusicInfo current;
    private long currentLocalId;
    private int state;
    private int volume;
    private int recordingVolume;
    private Disposable mDisposable;

    public PlayerCoreImpl() {
        CoreManager.addClient(this);
        playerListMusicInfos = requestPlayerListLocalMusicInfos();
        if (playerListMusicInfos == null) {
            playerListMusicInfos = new ArrayList<>();
        }
        state = STATE_STOP;
        volume = CommonPref.instance(BasicConfig.INSTANCE.getAppContext()).getInt("volume", 50);
        recordingVolume = CommonPref.instance(BasicConfig.INSTANCE.getAppContext()).getInt("recordingVolume", 50);

        mDisposable = IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(new Consumer<RoomEvent>() {
                    @Override
                    public void accept(RoomEvent roomEvent) throws Exception {
                        if (roomEvent == null) return;
                        int event = roomEvent.getEvent();
                        if (event == RoomEvent.ROOM_EXIT
                                || event == RoomEvent.KICK_OUT_ROOM
                                || event == RoomEvent.ADD_BLACK_LIST) {
                            state = STATE_STOP;
                            current = null;
                            currentLocalId = 0;
                        } else if (event == RoomEvent.DOWN_MIC
                                || event == RoomEvent.KICK_DOWN_MIC) {
                            if (AvRoomDataManager.get().isOwner(roomEvent.getAccount()))
                                stop();
                        } else if (event == RoomEvent.METHOD_ON_AUDIO_MIXING_FINISHED) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public LocalMusicInfo getCurrent() {
        return current;
    }

    private PlayHandler handler = new PlayHandler(this);

    static class PlayHandler extends Handler {
        private WeakReference<PlayerCoreImpl> mWeakReference;

        PlayHandler(PlayerCoreImpl playerCore) {
            mWeakReference = new WeakReference<>(playerCore);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    PlayerCoreImpl playerCore = mWeakReference.get();
                    if (playerCore != null)
                        playerCore.playNext();
                    break;
                case 1:
                    break;
                default:
            }
        }
    }

    @Override
    public List<LocalMusicInfo> getPlayerListMusicInfos() {
        return playerListMusicInfos;
    }

    @Override
    public void refreshLocalMusic(List<LocalMusicInfo> lastScannedSongs) {
        if (isRefresh) {
            return;
        }

        isRefresh = true;
        scanMediaTask = new AsyncTaskScanMusicFile(BasicConfig.INSTANCE.getAppContext(), 0, new AsyncTaskScanMusicFile.ScanMediaCallback() {
            @Override
            public void onProgress(int progress, String message, int total) {
                notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_LOCAL_MUSIC_PROGRESS, progress);
            }

            @Override
            public void onComplete(boolean result) {
                isRefresh = false;
                if (currentLocalId > 0) {
                    current = CoreManager.getCore(IPlayerDbCore.class).requestLocalMusicInfoByLocalId(currentLocalId);
                    notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_CURRENT_MUSIC_UPDATE, current);
                }
                List<LocalMusicInfo> localMusicInfoList = requestLocalMusicInfos();
                notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_LOCAL_MUSIC, localMusicInfoList);
                List<LocalMusicInfo> playerList = requestPlayerListLocalMusicInfos();
                playerListMusicInfos = playerList;
                notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_PLAYER_LIST, playerList);
            }
        }, lastScannedSongs);
        scanMediaTask.execute(BasicConfig.INSTANCE.getAppContext());
    }

    @Override
    public void addMusicToPlayerList(LocalMusicInfo localMusicInfo) {
        CoreManager.getCore(IPlayerDbCore.class).addToPlayerList(localMusicInfo.getLocalId());
        List<LocalMusicInfo> localMusicInfoList = requestLocalMusicInfos();
        notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_LOCAL_MUSIC, localMusicInfoList);
        List<LocalMusicInfo> playerList = requestPlayerListLocalMusicInfos();
        playerListMusicInfos = playerList;
        notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_PLAYER_LIST, playerList);
    }

    @Override
    public void deleteMusicFromPlayerList(LocalMusicInfo localMusicInfo) {
        if (current != null && localMusicInfo.getLocalId() == current.getLocalId()) {
            stop();
        }
        CoreManager.getCore(IPlayerDbCore.class).deleteFromPlayerList(localMusicInfo.getLocalId());
        List<LocalMusicInfo> localMusicInfoList = requestLocalMusicInfos();
        notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_LOCAL_MUSIC, localMusicInfoList);
        List<LocalMusicInfo> playerList = requestPlayerListLocalMusicInfos();
        playerListMusicInfos = playerList;
        notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_REFRESH_PLAYER_LIST, playerList);
    }

    @Override
    public List<LocalMusicInfo> requestLocalMusicInfos() {
        RealmResults<LocalMusicInfo> localMusicInfos = CoreManager.getCore(IPlayerDbCore.class).queryAllLocalMusicInfos();
        List<LocalMusicInfo> localMusicInfoList = new ArrayList<>();
        if (localMusicInfos != null && localMusicInfos.size() > 0) {
            for (int i = 0; i < localMusicInfos.size(); i++) {
                LocalMusicInfo localMusicInfo = localMusicInfos.get(i);
                localMusicInfoList.add(localMusicInfo);
            }
        }
        return localMusicInfoList;
    }

    @Override
    public List<LocalMusicInfo> requestPlayerListLocalMusicInfos() {
        RealmResults<LocalMusicInfo> localMusicInfos = CoreManager.getCore(IPlayerDbCore.class).queryPlayerListLocalMusicInfos();
        List<LocalMusicInfo> localMusicInfoList = new ArrayList<>();
        if (localMusicInfos != null && localMusicInfos.size() > 0) {
            for (int i = 0; i < localMusicInfos.size(); i++) {
                LocalMusicInfo localMusicInfo = localMusicInfos.get(i);
                localMusicInfoList.add(localMusicInfo);
            }
        }
        return localMusicInfoList;
    }

    @Override
    public int play(LocalMusicInfo localMusicInfo) {
        if (localMusicInfo == null) {
            localMusicInfo = current;
        }

        if (current != null && state == STATE_PAUSE && localMusicInfo != null && current.getLocalId() == localMusicInfo.getLocalId()) {
            state = STATE_PLAY;
            RtcEngineManager.get().resumeAudioMixing();
            notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_MUSIC_PLAYING, current);
            return 0;
        }

        if (localMusicInfo == null) {
            return -2;
        }

        File file = new File(localMusicInfo.getLocalUri());
        if (!file.exists()) {
            return -1;
        }

        RtcEngineManager.get().adjustAudioMixingVolume(volume);
        RtcEngineManager.get().adjustRecordingSignalVolume(recordingVolume);
        int result = RtcEngineManager.get().startAudioMixing(localMusicInfo.getLocalUri(), false, 1);
        if (result == -1) {
            return -1;
        }
        current = localMusicInfo;
        currentLocalId = current.getLocalId();
        state = STATE_PLAY;
        notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_MUSIC_PLAYING, current);
        return 0;
    }

    @Override
    public void pause() {
        if (state == STATE_PLAY) {
            RtcEngineManager.get().pauseAudioMixing();
            state = STATE_PAUSE;
            notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_MUSIC_PAUSE, current);
        }
    }

    @Override
    public void stop() {
        if (state != STATE_STOP) {
            RtcEngineManager.get().stopAudioMixing();
            state = STATE_STOP;
            current = null;
            currentLocalId = 0;
            notifyClients(IPlayerCoreClient.class, IPlayerCoreClient.METHOD_ON_MUSIC_STOP);
        }
    }

    @Override
    public int playNext() {
        if (current == null) {
            if (playerListMusicInfos.size() > 0) {
                return play(playerListMusicInfos.get(0));
            } else {
                return -3;
            }
        } else {
            int index = playerListMusicInfos.indexOf(current);
            if (index == playerListMusicInfos.size() - 1) {
                index = 0;
            } else {
                index += 1;
            }
            return play(playerListMusicInfos.get(index));
        }
    }

    @Override
    public int playForward() {
        if (current == null) {
            if (playerListMusicInfos.size() > 0) {
                return play(playerListMusicInfos.get(0));
            } else {
                return -3;
            }
        } else {
            int index = playerListMusicInfos.indexOf(current);
            if (index == playerListMusicInfos.size() - 1) {
                index = 0;
            } else if (index == 0) {
                index = playerListMusicInfos.size() - 1;
            } else {
                index -= 1;
            }
            return play(playerListMusicInfos.get(index));
        }
    }

    @Override
    public void seekVolume(int volume) {
        this.volume = volume;
        CommonPref.instance(BasicConfig.INSTANCE.getAppContext()).putInt("volume", volume);
        RtcEngineManager.get().adjustAudioMixingVolume(volume);
    }

    @Override
    public void seekRecordingVolume(int volume) {
        this.recordingVolume = volume;
        CommonPref.instance(BasicConfig.INSTANCE.getAppContext()).putInt("recordingVolume", volume);
        RtcEngineManager.get().adjustRecordingSignalVolume(volume);
    }

    @Override
    public int getCurrentVolume() {
        return volume;
    }

    @Override
    public int getCurrentRecordingVolume() {
        return recordingVolume;
    }
}

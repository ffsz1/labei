package com.tongdaxing.xchat_core.player;

import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by chenran on 2017/10/31.
 */

public class PlayerDbCoreImpl extends AbstractBaseCore implements IPlayerDbCore {
    private Realm mRealm;

    public PlayerDbCoreImpl() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void addToPlayerList(long localId) {
        mRealm.beginTransaction();
        LocalMusicInfo localMusicInfo = mRealm.where(LocalMusicInfo.class).equalTo("localId", localId).findFirst();
        localMusicInfo.setInPlayerList(true);
        mRealm.copyToRealmOrUpdate(localMusicInfo);
        mRealm.commitTransaction();
    }

    @Override
    public LocalMusicInfo requestLocalMusicInfoByLocalId(long localId) {
        LocalMusicInfo localMusicInfo = mRealm.where(LocalMusicInfo.class).equalTo("localId", localId).findFirst();
        return localMusicInfo;
    }

    @Override
    public void deleteFromPlayerList(long localId) {
        mRealm.beginTransaction();
        LocalMusicInfo localMusicInfo = mRealm.where(LocalMusicInfo.class).equalTo("localId", localId).findFirst();
        localMusicInfo.setInPlayerList(false);
        mRealm.copyToRealmOrUpdate(localMusicInfo);
        mRealm.commitTransaction();
    }

    @Override
    public void replaceAllLocalMusics(List<LocalMusicInfo> localMusicInfoList) {
        if (localMusicInfoList != null) {
            for (int i=0; i<localMusicInfoList.size(); i++) {
                LocalMusicInfo localMusicInfo = localMusicInfoList.get(i);
                LocalMusicInfo local = mRealm.where(LocalMusicInfo.class).equalTo("localId", localMusicInfo.getLocalId()).findFirst();
                if (local != null) {
                    localMusicInfo.setInPlayerList(local.isInPlayerList());
                }
            }
            try {//java.lang.IllegalArgumentException Illegal Argument: Failure when converting to UTF-8; error_code = 5; 0xbb50 0xdc74
                mRealm.beginTransaction();
                mRealm.delete(LocalMusicInfo.class);
                mRealm.copyToRealmOrUpdate(localMusicInfoList);
                mRealm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RealmResults<LocalMusicInfo> queryAllLocalMusicInfos() {
        RealmResults<LocalMusicInfo> localMusicInfos = mRealm.where(LocalMusicInfo.class).findAll();
        return localMusicInfos;
    }

    @Override
    public RealmResults<LocalMusicInfo> queryPlayerListLocalMusicInfos() {
        RealmResults<LocalMusicInfo> localMusicInfos = mRealm.where(LocalMusicInfo.class).equalTo("isInPlayerList", true).findAll();
        return localMusicInfos;
    }
}

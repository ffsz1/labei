package com.tongdaxing.xchat_core.user;


import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import io.realm.Realm;

/**
 * Created by chenran on 2017/3/15.
 */

public class UserDbCoreImpl extends AbstractBaseCore implements IUserDbCore {
    private Realm mRealm;

    public UserDbCoreImpl() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void saveDetailUserInfo(UserInfo userInfo) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(userInfo);
        mRealm.commitTransaction();
    }

    @Override
    public UserInfo queryDetailUserInfo(long uid) {
        UserInfo userInfo = mRealm.where(UserInfo.class).equalTo("uid", uid).findFirst();
        return userInfo;
    }

    @Override
    public void updateHasWx(UserInfo userInfo, boolean hasWx) {
        if (userInfo != null) {
            mRealm.beginTransaction();
            userInfo.setHasWx(hasWx);
            mRealm.copyToRealmOrUpdate(userInfo);
            mRealm.commitTransaction();
        }
    }

    @Override
    public void updateHasQq(UserInfo userInfo, boolean hasQq) {
        if (userInfo != null) {
            mRealm.beginTransaction();
            userInfo.setHasQq(hasQq);
            mRealm.copyToRealmOrUpdate(userInfo);
            mRealm.commitTransaction();
        }
    }

    @Override
    public void updatePhone(UserInfo userInfo, String phone) {
        if (userInfo != null) {
            mRealm.beginTransaction();
            userInfo.setPhone(phone);
            mRealm.copyToRealmOrUpdate(userInfo);
            mRealm.commitTransaction();
        }
    }
}

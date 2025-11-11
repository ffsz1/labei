package com.tongdaxing.xchat_core.realm;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by chenran on 2017/7/25.
 */

public class RealmCoreImpl extends AbstractBaseCore implements IRealmCore{
    private Realm mRealm;

    public RealmCoreImpl() {
        Realm.init(BasicConfig.INSTANCE.getAppContext());
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("erban.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

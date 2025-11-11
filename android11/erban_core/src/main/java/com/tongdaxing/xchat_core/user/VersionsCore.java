package com.tongdaxing.xchat_core.user;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;
import com.tongdaxing.xchat_framework.util.util.Json;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public interface VersionsCore extends IBaseCore {
    boolean canUseAliPay();

    void getVersions(int version);

    void checkVersion();

    int checkKick();

    void getConfig();

    Json getConfigData();

    void requestSensitiveWord();

    String getSensitiveWordData();
}

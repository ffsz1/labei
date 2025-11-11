package com.tongdaxing.xchat_core.user;

import com.tongdaxing.xchat_core.user.bean.CheckUpdataBean;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public interface VersionsCoreClient extends ICoreClient {

    String METHOD_GET_VERSION = "onGetVersion";
    String METHOD_GET_VERSION_ERROR = "onGetVersionError";
    String METHOD_VERSION_UPDATA_DIALOG = "onVersionUpdataDialog";

    void onGetVersion(Boolean isBoolean);
    void onGetVersion(String error);
    void onVersionUpdataDialog(CheckUpdataBean checkUpdataBean);
}

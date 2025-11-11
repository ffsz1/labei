package com.tongdaxing.xchat_core.room;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * Created by Administrator on 2018/3/24.
 */

public interface IBgClient extends ICoreClient {
    String bgModify = "bgModify";

    void bgModify(String bgType,String bgUrl);
}

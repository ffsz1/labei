package com.jph.takephoto.uitl;

import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;

/**
 * Function:
 * Author: Edward on 2019/5/30
 */
public class UserBehaviorLimitUtils {
    /**
     * @return
     */
    public static int getLimitSendLevel() {
        Json json = CoreManager.getCore(VersionsCore.class).getConfigData();
        String levelStr = json.str(Constants.LIMIT_USER_SEND_PIC);
        try {
            return Integer.valueOf(levelStr);
        } catch (Exception e) {

            return 0;
        }
    }
}

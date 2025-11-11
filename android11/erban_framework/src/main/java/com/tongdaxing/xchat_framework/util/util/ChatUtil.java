package com.tongdaxing.xchat_framework.util.util;

import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.coremanager.IAppInfoCore;

public class ChatUtil {

    /**
     * 禁言
     * @return
     */
    public static boolean checkBanned() {
        Json json = CoreManager.getCore(IAppInfoCore.class).getBannedMap();
        boolean all = json.boo(IAppInfoCore.BANNED_ALL + "");
        boolean room = json.boo(IAppInfoCore.BANNED_PUBLIC_ROOM + "");
        if (all || room) {
            SingleToastUtil.showToast("亲，由于您的发言违反了平台绿色公约，若有异议请联系客服");
            return true;
        }
        return false;
    }
}

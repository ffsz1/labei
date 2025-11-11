package com.tongdaxing.xchat_core.share;

import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import cn.sharesdk.framework.Platform;

/**
 * Created by chenran on 2017/8/14.
 */

public interface IShareCore extends IBaseCore{
    void shareH5(WebViewInfo webViewInfo, Platform platform);
    void sharePage(WebViewInfo webViewInfo, Platform platform);
    void shareRoom(Platform platform, long roomUid, String title);
    void reportShare(int sharePageId, Platform platform);
}

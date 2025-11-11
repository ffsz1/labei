package com.tongdaxing.xchat_core.user;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.coremanager.IAppInfoCore;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

public class AppInfoImpl extends AbstractBaseCore implements IAppInfoCore{
    private Json integerBooleanMap = new Json();
    private long requestBannedTime = 0;


    @Override
    public String getSensitiveWord() {
        String sensitiveWordData = CoreManager.getCore(VersionsCore.class).getSensitiveWordData();
        return sensitiveWordData;
    }

    @Override
    public Json getBannedMap() {
        return integerBooleanMap;
    }

    @Override
    public void checkBanned() {
        long l = System.currentTimeMillis();
        //请求间距5分钟
        if (l - requestBannedTime < 300 * 1000) {
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getBannedType(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") == 200) {
                    long l = System.currentTimeMillis();
                    requestBannedTime = l;
                    Json data = response.json_ok("data");
//                    sensitiveWordData = data;
                    integerBooleanMap.set(IAppInfoCore.BANNED_ALL, data.boo("all"));
                    integerBooleanMap.set(IAppInfoCore.BANNED_ROOM, data.boo("room"));
                    integerBooleanMap.set(IAppInfoCore.BANNED_PUBLIC_ROOM, data.boo("broadcast"));
                    integerBooleanMap.set(IAppInfoCore.BANNED_P2P, data.boo("chat"));
                }
            }
        });
    }

    @Override
    public void checkBanned(boolean resetTime) {
        if (resetTime){
            requestBannedTime = 0;
        }
        checkBanned();
    }

}

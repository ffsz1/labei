package com.tongdaxing.xchat_core.count;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * Created by seven on 2017/8/8.
 */

public class CountCoreImpl extends AbstractBaseCore implements ICountCore {
    public  static final String TAG ="CountCoreImpl";
    @Override
    public void AcgChattime() {
        Map<String,String> param = CommonParamUtil.getDefaultParam();
        OkHttpManager.getInstance().getRequest(UriProvider.getAvgChattime(), param, new OkHttpManager.MyCallBack<ServiceResult<HomeInfo>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(ICountCoreClient.class, ICountCoreClient.METHOD_GET_ON_AVGCHATTIME_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<HomeInfo> response) {
                if (null != response ){
                    if (response.isSuccess()){
                        notifyClients(ICountCoreClient.class  , ICountCoreClient.METHOD_GET_ON_AVGCHATTIME,response.getData());
                    }else{
                        notifyClients(ICountCoreClient.class, ICountCoreClient.METHOD_GET_ON_AVGCHATTIME_FAIL,response.getMessage());
                    }
                }
            }
        });
    }
}

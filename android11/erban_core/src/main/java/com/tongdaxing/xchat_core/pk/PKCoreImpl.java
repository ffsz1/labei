package com.tongdaxing.xchat_core.pk;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;

import java.util.List;
import java.util.Map;

public class PKCoreImpl extends AbstractBaseCore implements IPkCore {
    public static final String TAG = "PKCoreImpl";

    public PKCoreImpl() {
    }

    @Override
    public void savePK(long roomId,final PkVoteInfo info) {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("roomId",roomId + "");
        params.put("pkType",info.pkType + "");
        params.put("opUid",info.opUid + "");
        params.put("uid",info.uid + "");
        params.put("pkUid",info.pkUid + "");
        params.put("expireSeconds",info.getExpireSeconds() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.savePk(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_SAVE_PK_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") != 200) {
                    notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_SAVE_PK_FAIL, response.str("message", "网络异常"));
                }else {
                    long pkId = response.num_l("data",0);
                    if (pkId > 0){
                        info.setVoteId(pkId);
                        notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_SAVE_PK,info);
                    }else {
                        notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_SAVE_PK_FAIL,"PK id 异常！");
                    }
                }
            }
        });
    }


    @Override
    public void cancelPK(long roomId, long uid) {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("roomId",roomId + "");
        params.put("uid",uid + "");
//        UriProvider.cancelPk()
        OkHttpManager.getInstance().doPostRequest(UriProvider.cancelPk(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
//                notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_SAVE_PK_FAIL, e.getMessage());
                LogUtil.d("PkCustomAttachment","取消异常");
            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") != 200) {
                    LogUtil.d("PkCustomAttachment","取消失败");
                }else {
                    LogUtil.d("PkCustomAttachment","取消成功");
                }
            }
        });
    }



    @Override
    public void getPkHistoryList(long roomId, int page) {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("roomId",roomId + "");
        params.put("pageNum",page + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getPkHistoryList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<PkVoteInfo>>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_HISTORY_LIST_FAIL,e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<PkVoteInfo>> response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_HISTORY_LIST,response.getData());
                }else {
                    notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_HISTORY_LIST_FAIL,response.getMessage());
                }
            }
        });
    }

}

package com.tongdaxing.xchat_core.user;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_core.user.bean.FansListInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class AttentionCoreImpl extends AbstractBaseCore implements AttentionCore {
    private static final String TAG = "AttentionCoreImpl";

    @Override
    public void getAttentionList(long uid, final int page, int pageSize) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("pageNo", String.valueOf(page));

        OkHttpManager.getInstance().getRequest(UriProvider.getAllFans(), params, new OkHttpManager.MyCallBack<ServiceResult<List<AttentionInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_ATTENTION_LIST_FAIL, e.getMessage(), page);
            }

            @Override
            public void onResponse(ServiceResult<List<AttentionInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_ATTENTION_LIST, response.getData(), page);
                    } else {
                        notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_ATTENTION_LIST_FAIL, response.getMessage(), page);
                    }
                }
            }
        });
    }

    @Override
    public void getFansList(long uid, final int pageCount, int pageSize, final int pageType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("pageNo", String.valueOf(pageCount));
        params.put("pageSize", String.valueOf(pageSize));

        OkHttpManager.getInstance().getRequest(UriProvider.getFansList(), params, new OkHttpManager.MyCallBack<ServiceResult<FansListInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_FANSLIST_FAIL, e.getMessage(), pageType, pageCount);
            }

            @Override
            public void onResponse(ServiceResult<FansListInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_FANSLIST, response.getData(), pageType, pageCount);
                    } else {
                        notifyClients(AttentionCoreClient.class, AttentionCoreClient.METHOD_GET_FANSLIST_FAIL, response.getMessage(), pageType, pageCount);
                    }
                }
            }
        });
    }
}

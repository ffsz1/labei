package com.tongdaxing.xchat_core.user;

import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_core.user.bean.FansListInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public interface AttentionCoreClient extends ICoreClient {
    public static final String METHOD_GET_ATTENTION_LIST = "onGetAttentionList";
    public static final String METHOD_GET_ATTENTION_LIST_FAIL = "onGetAttentionListFail";
    public static final String METHOD_GET_FANSLIST = "onGetMyFansList";
    public static final String METHOD_GET_FANSLIST_FAIL = "onGetMyFansListFail";

    public void onGetAttentionList(List<AttentionInfo> attentionInfo);

    public void onGetAttentionListFail(String error);

    void onGetMyFansList(FansListInfo fansListInfo, int pageType, int page);

    void onGetMyFansListFail(String error, int pageType, int page);
}

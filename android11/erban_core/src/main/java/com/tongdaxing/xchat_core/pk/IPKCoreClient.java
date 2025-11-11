package com.tongdaxing.xchat_core.pk;

import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * PK活动接口方法名称，用于CoreManager观察回调
 */
public interface IPKCoreClient extends ICoreClient {

    /**
     * 发起保存一个PK回调方法名
     */
    public static final String METHOD_ON_SAVE_PK = "onSavePk";
    public static final String METHOD_ON_SAVE_PK_FAIL = "onSavePkFail";
    public void onSavePk(PkVoteInfo pkVoteInfo);
    public void onSavePkFail(String error);

    /**
     * 获取Pk历史列表
     */
    public static final String METHOD_ON_PK_HISTORY_LIST = "onPkHistoryList";
    public static final String METHOD_ON_PK_HISTORY_LIST_FAIL = "onPkHistoryListFail";
    public void onPkHistoryList(List<PkVoteInfo> pkVoteInfos);
    public void onPkHistoryListFail(String error);

    /**
     * pk礼物赠送回调
     */
    public static final String METHOD_ON_PK_GIFT = "onPkGift";
    public static final String METHOD_ON_PK_MULTI_GIFT = "onPkMultiGift";
    public static final String METHOD_ON_PK_GIFT_FAIL = "onPkGiftFail";
    public void onPkGift(long target);
    public void onPkMultiGift(List<Long> targetUids);
    public void onPkGiftFail(String error);

}

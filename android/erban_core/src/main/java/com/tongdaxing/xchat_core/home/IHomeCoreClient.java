package com.tongdaxing.xchat_core.home;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * @author zhouxiangfeng
 * @date 2017/5/17
 */

public interface IHomeCoreClient extends ICoreClient {
    String METHOD_ON_AUTO_JUMP = "onAutoJump";

    String METHOD_ON_RECYCLER_VIEW_LISTENER = "onRecyclerViewListener";

    String METHOD_ON_GET_HOME_DATA = "onGetHomeData";
    String METHOD_GET_HOME_LIST_ERROR = "onGetHomeListFail";

    String METHOD_ON_GET_DATA_BY_TAB = "onGetHomeDataByTab";
    String METHOD_ON_GET_DATA_BY_TAB_ERROR = "onGetHomeDataByTabFail";

    String METHOD_ON_SORT_GET_DATA_BY_TAB = "onGetSortDataByTab";
    String METHOD_ON_SORT_GET_DATA_BY_TAB_ERROR = "onGetSortDataByTabFail";

    public static final String METHOD_ON_GET_HOT_DATA = "onGetHotData";
    public static final String METHOD_ON_GET_HOT_DATA_FAIL = "onGetHotDataFail";

    public static final String METHOD_ON_GET_LIGHT_CHAT_DATA = "onGetLightChatData";
    public static final String METHOD_ON_GET_LIGHT_CHAT_DATA_FAIL = "onGetLightChatDataFail";

    public static final String METHOD_ON_GET_HOME_PARTY_DATA = "onGetHomePartyData";
    public static final String METHOD_ON_GET_HOME_PARTY_DATA_FAIL = "onGetHomePartyDataFail";

    public static final String METHOD_ON_COMMIT_BACK = "onCommitFeedback";
    public static final String METHOD_ON_COMMIT_BACK_FAIL = "onCommitFeedbackFail";

    public static final String METHOD_ON_GET_BANNER_LIST = "onGetBannerList";
    public static final String METHOD_ON_GET_BANNER_LIST_FAIL = "onGetBannerListFail";

    String METHOD_ON_GET_HOME_RANKING_LIST = "onGetHomeRankingList";
    String METHOD_ON_GET_HOME_RANKING_LIST_ERROR = "onGetHomeRankingListFail";

    String METHOD_ON_GET_HOME_TAB_LIST = "onGetHomeTabList";
    String METHOD_ON_GET_HOME_TAB_LIST_ERROR = "onGetHomeTabListFail";

    String onGetHomeDataByMenuFail = "onGetHomeDataByMenuFail";
    String onGetHomeDataByMenuSuccess = "onGetHomeDataByMenuSuccess";

    public void onAutoJump(BannerInfo bannerInfo);

    public void onRecyclerViewListener(int value);

    public void onGetHomeDataByMenuFail(String message);

    public void onGetHomeDataByMenuSuccess(List<TabInfo> tablist);

    public void onGetHomeData(List<HomeRoom> homeDataList);

    public void onGetHomeListFail(String error);

    public void onGetHotData(HomeRoomList homeRoomList);

    public void onGetHotDataFail(String error);

    public void onGetLightChatData(HomeRoomList homeRoomList);

    public void onGetLightChatDataFail(String error);

    public void onGetHomePartyData(HomeRoomList homeRoomList);

    public void onGetHomePartyDataFail(String error);

    void onGetBannerList(List<BannerInfo> bannerInfoList);

    void onGetBannerListFail();

    void onCommitFeedback();

    void onCommitFeedbackFail(String error);
}

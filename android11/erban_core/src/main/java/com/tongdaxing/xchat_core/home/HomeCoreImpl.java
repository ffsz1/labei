package com.tongdaxing.xchat_core.home;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.utils.Logger;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zhouxiangfeng
 * @date 2017/5/17
 */

public class HomeCoreImpl extends AbstractBaseCore implements IHomeCore {
    private static final String TAG = "HomeCoreImpl";

    private List<TabInfo> mTabInfoList;

    @Override
    public List<TabInfo> getMainTabInfos() {
        return mTabInfoList;
    }

    @Override
    public void setMainTabInfos(List<TabInfo> tabInfoList) {
        this.mTabInfoList = tabInfoList;
    }

    @Override
    public void getHomeData(final int page, int pageSize) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("pageNum", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));

        OkHttpManager.getInstance().getRequest(UriProvider.getMainHotData(), params, new OkHttpManager.MyCallBack<ServiceResult<HomeInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_GET_HOME_LIST_ERROR, e.getMessage(), page);
            }

            @Override
            public void onResponse(ServiceResult<HomeInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_DATA, response.getData(), page);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_GET_HOME_LIST_ERROR, response.getMessage(), page);
                    }
                }
            }
        });
    }

    @Override
    public void getHomePartyData(final int tabType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("type", String.valueOf(3));

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomListV2(), params, new OkHttpManager.MyCallBack<ServiceResult<HomeRoomList>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_PARTY_DATA_FAIL, e.getMessage(), tabType);
            }

            @Override
            public void onResponse(ServiceResult<HomeRoomList> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_PARTY_DATA, response.getData(), tabType);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_PARTY_DATA_FAIL, response.getMessage(), tabType);
                    }
                }
            }
        });
    }

    @Override
    public void getLightChatData(final int tabType) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("type", String.valueOf(2));

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomListV2(), param, new OkHttpManager.MyCallBack<ServiceResult<HomeRoomList>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_LIGHT_CHAT_DATA_FAIL, e.getMessage(), tabType);
            }

            @Override
            public void onResponse(ServiceResult<HomeRoomList> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_LIGHT_CHAT_DATA, response.getData(), tabType);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_LIGHT_CHAT_DATA_FAIL, response.getMessage(), tabType);
                    }
                }
            }
        });
    }

    @Override
    public void getHotData(final int tabType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("type", String.valueOf(1));

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomListV2(), params, new OkHttpManager.MyCallBack<ServiceResult<HomeRoomList>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOT_DATA_FAIL, e.getMessage(), tabType);
            }

            @Override
            public void onResponse(ServiceResult<HomeRoomList> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOT_DATA, response.getData(), tabType);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOT_DATA_FAIL, response.getMessage(), tabType);
                    }
                }
            }
        });
    }

    @Override
    public void getHomeBanner() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getBannerList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<BannerInfo>>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_BANNER_LIST_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<BannerInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_BANNER_LIST, response.getData());
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_BANNER_LIST_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getRankingData() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getHomeRanking(), params, new OkHttpManager.MyCallBack<ServiceResult<RankingInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_RANKING_LIST_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RankingInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_RANKING_LIST, response.getData());
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_RANKING_LIST_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getMainTabData() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getMainTabList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<TabInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_TAB_LIST_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<TabInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_TAB_LIST, response.getData());
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_HOME_TAB_LIST_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getMainDataByTab(final int tagId, final int pageNum) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("tagId", String.valueOf(tagId));
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("pageNum", String.valueOf(pageNum));
        params.put("pageSize", String.valueOf(Constants.PAGE_SIZE));

        OkHttpManager.getInstance().getRequest(UriProvider.getMainDataByTab(), params, new OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_DATA_BY_TAB_ERROR, e.getMessage(), tagId, pageNum);
            }

            @Override
            public void onResponse(ServiceResult<List<HomeRoom>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_DATA_BY_TAB, response.getData(), tagId, pageNum);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_GET_DATA_BY_TAB_ERROR, response.getMessage(), tagId, pageNum);
                    }
                }
            }
        });
    }

    @Override
    public void getSortDataByTab(final int tagId, final int pageNum) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("tagId", String.valueOf(tagId));
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("pageNum", String.valueOf(pageNum));
        params.put("pageSize", String.valueOf(Constants.PAGE_SIZE));

        OkHttpManager.getInstance().getRequest(UriProvider.getMainDataByTab(), params, new OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_SORT_GET_DATA_BY_TAB_ERROR, e.getMessage(), tagId, pageNum);
            }

            @Override
            public void onResponse(ServiceResult<List<HomeRoom>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_SORT_GET_DATA_BY_TAB, response.getData(), tagId, pageNum);
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.METHOD_ON_SORT_GET_DATA_BY_TAB_ERROR, response.getMessage(), tagId, pageNum);
                    }
                }
            }
        });
    }


    @Override
    public void getMainDataByMenu() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getMainDataByMenu(), params, new OkHttpManager.MyCallBack<ServiceResult<List<TabInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IHomeCoreClient.class, IHomeCoreClient.onGetHomeDataByMenuFail);
            }

            @Override
            public void onResponse(ServiceResult<List<TabInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.onGetHomeDataByMenuSuccess, response.getData());
                    } else {
                        notifyClients(IHomeCoreClient.class, IHomeCoreClient.onGetHomeDataByMenuFail, response.getErrorMessage());
                    }
                }
            }
        });
    }
}





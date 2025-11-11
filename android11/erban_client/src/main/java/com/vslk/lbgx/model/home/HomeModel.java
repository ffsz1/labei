package com.vslk.lbgx.model.home;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

public class HomeModel extends BaseMvpModel {
    /**
     * 获取首页分类列表数据
     */
    public void getRecommendBannerData(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("type", String.valueOf(3));
        OkHttpManager.getInstance().getRequest(UriProvider.getRedBagDialogType(), params, myCallBack);
    }

    /**
     * 获取首页分类列表数据
     */
    public void getHomeTabMenuList(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getMainDataByMenu(), params, myCallBack);
    }


    /**
     * 获取首页精选房间列表
     */
    public void getHomeHotFeatrueList(int mPage, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("pageNum", mPage + "");
        params.put("pageSize", Constants.BILL_PAGE_SIZE + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getHomePopularRoom(), params, myCallBack);
    }


    /**
     * 获取首页热门房间列表
     */
    public void getHomeHotRoomList(int mPage, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("pageNum", mPage + "");
        params.put("pageSize", Constants.PAGE_HOME_HOT_SIZE + "");
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.getMainHotData(), params, myCallBack);
    }

    /**
     * 陪陪
     */
    public void getBestCompanies(int mPage, int gender, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("pageNum", mPage + "");
        params.put("gender", gender + "");
        params.put("pageSize", Constants.PAGE_HOME_HOT_SIZE + "");
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.bestCompanies(), params, myCallBack);
    }

    /**
     * 萌新
     */
    public void getNewUsers(int mPage, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("pageNum", mPage + "");
        params.put("pageSize", Constants.PAGE_HOME_HOT_SIZE + "");
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.newUsers(), params, myCallBack);
    }


    /**
     * 根据分类id获取不同的房间列表
     */
    public void getHomeRoomListByTabId(int tagId, int pageNum, int pageSize, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("tagId", String.valueOf(tagId));
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("pageNum", String.valueOf(pageNum));
        params.put("pageSize", String.valueOf(pageSize));
        OkHttpManager.getInstance().getRequest(UriProvider.getMainDataByTab(), params, myCallBack);
    }


    /**
     * 根据首页广告
     */
    public void getHomeRoomBanner(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getHomeHeadBanner(), params, myCallBack);
    }

    /**
     * 获取首页icons图标
     */
    public void getHomeIconsList(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.getIndexHomeIcons(), param, myCallBack);

    }
}

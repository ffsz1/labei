package com.vslk.lbgx.model.mengcoin;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * 文件描述：萌币中心相关数据模型
 *
 * @auther：zwk
 * @data：2019/1/15
 */
public class MengCoinModel {


    /**
     * 获取当前用户的萌币任务列表接口
     *
     * @param myCallBack
     */
    public void getCurrentUserMengCoinTaskList(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getMengCoinTaskList(), params, myCallBack);
    }

    /**
     * 通过missionId领取对应萌币任务奖励接口
     *
     * @param missionId
     * @param myCallBack
     */
    public void receiveMengCoinByMissionId(int missionId, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("missionId", missionId + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.receiveMengCoinById(), params, myCallBack);
    }
}

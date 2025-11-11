package com.vslk.lbgx.model.rank;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.bean.RankingResultInfo;
import com.tongdaxing.xchat_core.bean.UserLevelInfo;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * 排行榜 model
 *
 * @author dell
 */
public class RankingListModel extends BaseMvpModel {

    /**
     * 排行榜
     *
     * @param type     排行榜类型 0巨星榜，1贵族榜，2房间榜
     * @param dateType 榜单周期类型 0日榜，1周榜，2总榜
     */
    public void getRankingList(int type, int dateType, final OkHttpManager.MyCallBack<List<RankingInfo>> callBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        //服务器的type从1开始
        params.put("type", type + 1 + "");
        //服务器的dateType从1开始
        params.put("datetype", dateType + 1 + "");
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(UriProvider.getRankingList(), params, new OkHttpManager.MyCallBack<ServiceResult<RankingResultInfo>>() {
            @Override
            public void onError(Exception e) {
                if (callBack != null) {
                    callBack.onError(new Exception("加载失败"));
                }
            }

            @Override
            public void onResponse(ServiceResult<RankingResultInfo> response) {
                if (response != null && response.isSuccess()) {
                    if (callBack != null) {
                        callBack.onResponse(response.getData() == null ? null : response.getData().rankVoList);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onError(new Exception(response != null ? response.getMessage() : "加载失败"));
                    }
                }
            }
        });
    }

    /**
     * 获取用户等级或魅力
     */
    public void getUserLevel(String url, OkHttpManager.MyCallBack<ServiceResult<UserLevelInfo>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(url, param, callBack);
    }

    /**
     * 获取房间日排行榜榜首头像
     * @param myCallBack
     */
    public void getRoomDayRankFirst(String roomUid,OkHttpManager.MyCallBack myCallBack){
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("roomUid",roomUid);
        OkHttpManager.getInstance().getRequest(UriProvider.getRoomRankFirstAvatar(),params,myCallBack);
    }

}

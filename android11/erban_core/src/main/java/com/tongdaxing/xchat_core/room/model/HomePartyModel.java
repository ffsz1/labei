package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.bean.RankingResultInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * <p> 轰趴房model层：数据获取 </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class HomePartyModel extends RoomBaseModel {

    public HomePartyModel() {
    }

    public void lockMicroPhone(int micPosition, String roomUid, String ticker,OkHttpManager.MyCallBack myCallBack) {
        operateMicroPhone(micPosition,"1",roomUid,ticker,myCallBack);
    }

    /**
     * 释放该麦坑的锁
     *
     * @param micPosition
     */
    public void unLockMicroPhone(int micPosition, String roomUid, String ticker,OkHttpManager.MyCallBack myCallBack) {
        operateMicroPhone(micPosition,"0",roomUid,ticker,myCallBack);
    }

    /**
     * 操作麦坑的锁
     * @param micPosition
     * @param state 1：锁，0：不锁
     * @param roomUid
     * @param ticker
     */
    private void operateMicroPhone(int micPosition, String state, String roomUid, String ticker, OkHttpManager.MyCallBack myCallBack){
        Map<String,String> param = CommonParamUtil.getDefaultParam();
        param.put("position", micPosition + "");
        param.put("state", state);
        param.put("roomUid", roomUid);
        param.put("ticket",ticker);
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.getlockMicroPhone(), param, myCallBack);
    }


    /**
     * 开麦
     *
     * @param micPosition
     * @param roomUid
     */
    public void openMicroPhone(int micPosition, long roomUid,OkHttpManager.MyCallBack myCallBack) {
        openOrCloseMicroPhone(micPosition, 0, roomUid, CoreManager.getCore(IAuthCore.class).getTicket(), myCallBack);
    }

    /**
     * 闭麦
     *
     * @param micPosition
     * @param roomUid
     */
    public void closeMicroPhone(int micPosition, long roomUid,OkHttpManager.MyCallBack myCallBack) {
        openOrCloseMicroPhone(micPosition, 1, roomUid, CoreManager.getCore(IAuthCore.class).getTicket(), myCallBack);
    }

    /**
     * 开闭麦接口
     *
     * @param micPosition
     * @param state       1:闭麦，0：开麦
     * @param roomUid
     * @param ticket
     */
    private void openOrCloseMicroPhone(int micPosition, int state, long roomUid, String ticket, OkHttpManager.MyCallBack myCallBack) {
        Map<String,String> param = CommonParamUtil.getDefaultParam();
        param.put("position", micPosition + "");
        param.put("state", state + "");
        param.put("roomUid", roomUid + "");
        param.put("ticket",ticket);
        param.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().doPostRequest(UriProvider.operateMicroPhone(),param,myCallBack);
    }

    /**
     * 排行榜由于model依赖顺序，神豪榜接口在此增加*******
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

}

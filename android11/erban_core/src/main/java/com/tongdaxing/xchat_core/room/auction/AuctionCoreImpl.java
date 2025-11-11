package com.tongdaxing.xchat_core.room.auction;

import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public class AuctionCoreImpl extends AbstractBaseCore implements IAuctionCore {

    private static final String TAG = "AuctionCoreImpl";
    public AuctionInfo auctionInfo;

    public AuctionCoreImpl() {
        CoreManager.addClient(this);
        exitRoomEvent();
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onQuitRoom(RoomInfo roomInfo) {
        auctionInfo = null;
    }

    private void exitRoomEvent() {
//        RxBusHelper.doOnMainThread(ExitRoomEvent.class, new RxBusHelper.OnEventListener<ExitRoomEvent>() {
//            @Override
//            public void onEvent(ExitRoomEvent exitRoomEvent) {
//                auctionInfo = null;
//            }
//
//            @Override
//            public void onError(ErrorBean errorBean) {
//
//            }
//        });
    }

    private void parseAttachMent(IMCustomAttachment attachMent) {
//        if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_AUCTION) {
//            AuctionAttachment auctionAttachment = (AuctionAttachment) attachMent;
//            if (attachMent.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_START) {
//                auctionInfo = auctionAttachment.getAuctionInfo();
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_START, auctionInfo);
//            } else if (attachMent.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_FINISH) {
//                auctionInfo = auctionAttachment.getAuctionInfo();
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_FINISH, auctionInfo);
//                auctionInfo = null;
//            } else {
//                auctionInfo = auctionAttachment.getAuctionInfo();
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_UPDATE, auctionInfo);
//            }
//        }
    }

//    private void requestAuctionInfo(long uid) {
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("uid", String.valueOf(uid));
//        ResponseListener listener = new ResponseListener<AuctionInfoResult>() {
//            @Override
//            public void onResponse(AuctionInfoResult response) {
//                if (response != null) {
//                    if (response.isSuccess()) {
//                        AuctionInfo info = response.getData();
//                        if (info == null || info.getAuctId() == null) {
//                            auctionInfo = null;
//                        } else {
//                            auctionInfo = info;
//                        }
//                        notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_CURRENT_AUCTION_INFO_UPDATE, auctionInfo);
//                    }
//                }
//            }
//        };
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.requestAuctionInfo(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionInfoResult.class, Request.Method.GET);
//
//    }

    @Override
    public AuctionInfo getCurrentAuctionInfo() {
        return auctionInfo;
    }

    @Override
    public void auctionStart(long uid,long auctUid,int auctMoney,int servDura,int minRaiseMoney,String auctDesc) {
//        CoreManager.getCore(IStatisticsCore.class).onEventStart(getContext(), IStatisticsCore.EVENT_STARTAUCTION, "开启竞拍");
//
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("uid", String.valueOf(uid));
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
//        params.put("auctUid", String.valueOf(auctUid));
//        params.put("auctMoney", String.valueOf(auctMoney));
//        params.put("servDura", String.valueOf(servDura));
//        params.put("minRaiseMoney", String.valueOf(minRaiseMoney));
//        params.put("auctDesc", auctDesc);
//        ResponseListener listener = new ResponseListener<AuctionInfoResult>() {
//            @Override
//            public void onResponse(AuctionInfoResult response) {
//                if (response.getCode() == 2103) {
//                    notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_RECHARGE);
//                }
//            }
//        };
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.auctionStart(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionInfoResult.class, Request.Method.POST);
    }

    @Override
    public void auctionUp( long roomUid, long auctUid, String auctId, int type, int money) {
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("uid", String.valueOf(auctUid));
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
//        params.put("roomUid", String.valueOf(roomUid));
//        params.put("auctId", auctId);
//        params.put("type", String.valueOf(type));
//        params.put("money", String.valueOf(money));
//        ResponseListener listener = new ResponseListener<AuctionInfoResult>() {
//            @Override
//            public void onResponse(AuctionInfoResult response) {
//                if (response.isSuccess()) {
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_UP);
//                } else {
//                    if (response.getCode() == 2103) {
//                        notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_RECHARGE);
//                    }
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_UP_FAIL, response.getCode());
//                }
//            }
//        };
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_AUCTION_UP_FAIL, 0);
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.auctionUp(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionInfoResult.class, Request.Method.POST);
    }

    @Override
    public void finishAuction(long uid, String auctId) {
//        Map<String, String> attributes = new HashMap<>();
//        attributes.put("roomUid", uid+"");
//        attributes.put("auctId", auctId);
//        CoreManager.getCore(IStatisticsCore.class).onEvent(getContext(), IStatisticsCore.EVENT_STARTAUCTION, "结束竞拍", attributes);
//
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("uid", String.valueOf(uid));
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
//        params.put("auctId", auctId);
//
//        ResponseListener listener = new ResponseListener<AuctionInfoResult>() {
//            @Override
//            public void onResponse(AuctionInfoResult response) {
//            }
//        };
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.finishAuction(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionInfoResult.class, Request.Method.POST);
    }

    @Override
    public void requestWeekAuctionList(long roomUid) {
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("roomUid", roomUid+"");
//
//        ResponseListener listener = new ResponseListener<AuctionListUserInfoResult>() {
//            @Override
//            public void onResponse(AuctionListUserInfoResult response) {
//                if (response.isSuccess()) {
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_WEEK_AUCTION_LIST, response.getData());
//                } else {
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_WEEK_AUCTION_LIST_FAIL, "服务器异常，请重试");
//                }
//            }
//        };
//
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_WEEK_AUCTION_LIST_FAIL, error.getErrorStr());
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.weekAucionList(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionListUserInfoResult.class, Request.Method.GET);
    }

    @Override
    public void requestTotalAuctionList(long roomUid) {
//        RequestParam params = CommonParamUtil.fillCommonParam();
//        params.put("roomUid", roomUid+"");
//
//        ResponseListener listener = new ResponseListener<AuctionListUserInfoResult>() {
//            @Override
//            public void onResponse(AuctionListUserInfoResult response) {
//                if (response.isSuccess()) {
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_TOTAL_AUCTION_LIST, response.getData());
//                } else {
//                    notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_TOTAL_AUCTION_LIST_FAIL, "服务器异常，请重试");
//                }
//            }
//        };
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                Logger.error(TAG, error.getErrorStr());
//                notifyClients(IAuctionCoreClient.class, IAuctionCoreClient.METHOD_ON_REQUEST_TOTAL_AUCTION_LIST_FAIL, error.getErrorStr());
//            }
//        };
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.totalAuctionList(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        params, listener, errorListener,
//                        AuctionListUserInfoResult.class, Request.Method.GET);
    }
}

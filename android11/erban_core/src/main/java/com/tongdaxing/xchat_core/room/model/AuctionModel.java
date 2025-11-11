package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * <p> 竞拍房model</p>
 *
 * @author xiaoyu
 * @date 2017/12/26
 */
public class AuctionModel {

    private static final String TAG = "AuctionModel";
    private AuctionInfo auctionInfo = null;
    private Api api;

    private AuctionModel() {
        api = RxNet.create(Api.class);
    }

    private static AuctionModel INSTANCE;

    public static AuctionModel get() {
        if (INSTANCE == null) {
            synchronized (AuctionModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuctionModel();
                }
            }
        }
        return INSTANCE;
    }

    public AuctionInfo getAuctionInfo() {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null &&
                auctionInfo != null &&
                currentRoomInfo.getUid() != auctionInfo.getUid())
            auctionInfo = null;
        return auctionInfo;
    }

    public void setAuctionInfo(AuctionInfo auctionInfo) {
        this.auctionInfo = auctionInfo;
    }

    public boolean isInAuctionNow() {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null &&
                auctionInfo != null &&
                currentRoomInfo.getUid() != auctionInfo.getUid())
            auctionInfo = null;
        return auctionInfo != null && auctionInfo.getAuctId() != null;
    }

    public Single<AuctionInfo> requestAuctionInfo(long uid) {
        return api.requestAuctionInfo(String.valueOf(uid)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ServiceResult<AuctionInfo>, SingleSource<? extends AuctionInfo>>() {
                    @Override
                    public SingleSource<? extends AuctionInfo> apply(ServiceResult<AuctionInfo> auctionInfoResult) throws Exception {
                        if (auctionInfoResult != null && auctionInfoResult.getData() != null)
                            auctionInfo = auctionInfoResult.getData();
                        if (auctionInfoResult != null && auctionInfoResult.getData() != null) {
                            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                    new RoomEvent().setEvent(RoomEvent.AUCTION_UPDATE).setAuctionInfo(auctionInfo));
                        }
                        return Single.create(new SingleOnSubscribe<AuctionInfo>() {
                            @Override
                            public void subscribe(SingleEmitter<AuctionInfo> e) throws Exception {
                                if (auctionInfo == null) {
                                    e.onError(new Exception("获取不到auctionInfo"));
                                } else {
                                    e.onSuccess(auctionInfo);
                                }
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public Single<ServiceResult<AuctionInfo>> startAuction(long uid, long auctUid, int auctMoney, int servDura, int minRaiseMoney, String auctDesc) {
        return api.startAuction(String.valueOf(uid), CoreManager.getCore(IAuthCore.class).getTicket(),
                String.valueOf(auctUid), String.valueOf(auctMoney), String.valueOf(servDura),
                String.valueOf(minRaiseMoney), auctDesc).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ServiceResult<AuctionInfo>, SingleSource<? extends ServiceResult<AuctionInfo>>>() {
                    @Override
                    public SingleSource<? extends ServiceResult<AuctionInfo>> apply(final ServiceResult<AuctionInfo> auctionInfoResult) throws Exception {
                        if (auctionInfoResult != null && auctionInfoResult.getCode() == 2103) {
                            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                    new RoomEvent().setEvent(RoomEvent.RECHARGE));
                        }
                        return Single.create(new SingleOnSubscribe<ServiceResult<AuctionInfo>>() {
                            @Override
                            public void subscribe(SingleEmitter<ServiceResult<AuctionInfo>> e) throws Exception {
                                if (auctionInfoResult == null)
                                    e.onError(new Exception("返回竞拍房数据为空"));
                                else {
                                    e.onSuccess(auctionInfoResult);
                                    SingleToastUtil.showToast("发起竞拍成功");
                                }
                            }
                        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public Single<ServiceResult<AuctionInfo>> onAuctionUp(long roomUid, long auctUid, String auctId,
                                                 int type, int money) {
        return api.onAuctionUp(String.valueOf(auctUid), CoreManager.getCore(IAuthCore.class).getTicket(),
                String.valueOf(roomUid), String.valueOf(auctId), String.valueOf(type),
                String.valueOf(money)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ServiceResult<AuctionInfo>, SingleSource<? extends ServiceResult<AuctionInfo>>>() {
                    @Override
                    public SingleSource<? extends ServiceResult<AuctionInfo>>
                    apply(final ServiceResult<AuctionInfo> auctionInfoResult) throws Exception {
                        if (auctionInfoResult != null && auctionInfoResult.getCode() == 2103) {
                            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                    new RoomEvent().setEvent(RoomEvent.RECHARGE));
                        } else if (auctionInfoResult == null || auctionInfoResult.getCode() != 200) {
                            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                    new RoomEvent().setEvent(RoomEvent.AUCTION_UPDATE_FAIL)
                                            .setCode(auctionInfoResult == null ? 0 : auctionInfoResult.getCode()));
                        }
                        return Single.create(new SingleOnSubscribe<ServiceResult<AuctionInfo>>() {
                            @Override
                            public void subscribe(SingleEmitter<ServiceResult<AuctionInfo>> e) throws Exception {
                                if (auctionInfoResult == null)
                                    e.onError(new Exception("加价失败!"));
                                else e.onSuccess(auctionInfoResult);
                            }
                        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public Single<ServiceResult<AuctionInfo>> finishAuction(long uid, String auctId) {
        return api.finishAuction(String.valueOf(uid), CoreManager.getCore(IAuthCore.class).getTicket(), auctId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ServiceResult<AuctionInfo>, SingleSource<? extends ServiceResult<AuctionInfo>>>() {
                    @Override
                    public SingleSource<? extends ServiceResult<AuctionInfo>> apply(final ServiceResult<AuctionInfo> auctionInfoResult) throws Exception {
                        if (auctionInfoResult != null && auctionInfoResult.getCode() == 200)
                            auctionInfo = null;
                        if (auctionInfoResult != null && auctionInfoResult.getData() != null) {
                            IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                    new RoomEvent().setEvent(RoomEvent.AUCTION_FINISH)
                                            .setAuctionInfo(auctionInfoResult.getData()));
                        }
                        return Single.create(new SingleOnSubscribe<ServiceResult<AuctionInfo>>() {
                            @Override
                            public void subscribe(SingleEmitter<ServiceResult<AuctionInfo>> e) throws Exception {
                                if (auctionInfoResult == null)
                                    e.onError(new Exception("拍卖结束失败!"));
                                else e.onSuccess(auctionInfoResult);
                            }
                        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }


    public Single<ServiceResult<List<AuctionListUserInfo>>> requestWeekAuctionList(long roomUid) {
        return api.requestWeekAuctionList(String.valueOf(roomUid))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ServiceResult<List<AuctionListUserInfo>>> requestTotalAuctionList(long roomUid) {
        return api.requestTotalAuctionList(String.valueOf(roomUid))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    interface Api {

        /**
         * 获取总竞拍榜
         *
         * @param roomUid 房主uid
         * @return -
         */
        @GET("/sumlist/query")
        Single<ServiceResult<List<AuctionListUserInfo>>> requestTotalAuctionList(@Query("roomUid") String roomUid);

        /**
         * 获取周竞拍榜
         *
         * @param roomUid 房主uid
         * @return -
         */
        @GET("/weeklist/query")
        Single<ServiceResult<List<AuctionListUserInfo>>> requestWeekAuctionList(@Query("roomUid") String roomUid);

        /**
         * 房主结束竞拍
         *
         * @param uid    必填房主uid
         * @param ticket 必填
         * @param auctId 当前拍卖单ID
         * @return -
         */
        @FormUrlEncoded
        @POST("/auction/finish")
        Single<ServiceResult<AuctionInfo>> finishAuction(@Field("uid") String uid, @Field("ticket") String ticket,
                                                @Field("auctId") String auctId);

        /**
         * 用户参与竞拍报价
         * uid: 参与人id
         * ticket：必填
         * roomUid:房主uid
         * auctId:拍卖单ID
         * type:用户竞拍动作，1加价，2出价（自行填写价格）
         * money：金额
         *
         * @param uid auction房间的id
         * @return -竞拍房信息
         */
        @FormUrlEncoded
        @POST("/auctrival/up")
        Single<ServiceResult<AuctionInfo>> onAuctionUp(@Field("uid") String uid, @Field("ticket") String ticket,
                                              @Field("roomUid") String roomUid, @Field("auctId") String auctId,
                                              @Field("type") String type, @Field("money") String money);

        /**
         * -
         *
         * @param uid auction房间的id
         * @return -竞拍房信息
         */
        @GET("/auction/get")
        Single<ServiceResult<AuctionInfo>> requestAuctionInfo(@Query("uid") String uid);

        /**
         * 房主/管理员 开始竞拍
         *
         * @param uid           必填房主uid
         * @param ticket        必填
         * @param auctUid       被拍卖声优UID，必填
         * @param auctMoney     拍卖起拍价，必填
         * @param servDura      服务时长，本期需求直接写死30（分钟）
         * @param minRaiseMoney 最低竞拍报价
         * @param auctDesc      竞拍描述，选填
         * @return 竞拍房信息 2103表示要充值
         */
        @FormUrlEncoded
        @POST("/auction/start")
        Single<ServiceResult<AuctionInfo>> startAuction(@Field("uid") String uid, @Field("ticket") String ticket,
                                               @Field("auctUid") String auctUid, @Field("auctMoney") String auctMoney,
                                               @Field("servDura") String servDura, @Field("minRaiseMoney") String minRaiseMoney,
                                               @Field("auctDesc") String auctDesc);
    }
}

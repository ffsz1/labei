package com.vslk.lbgx.ui.me.wallet.model;

import com.google.gson.JsonObject;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.pay.bean.ExchangeAwardInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by MadisonRong on 05/01/2018.
 */

public class PayModel extends BaseMvpModel {

    private PayService payService;

    public PayModel() {
        payService = RxNet.create(PayService.class);
    }

    public PayService getPayService() {
        return payService;
    }

    /**
     * 获取用户信息
     */
    public UserInfo getUserInfo() {
        long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        return CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(currentUid);
    }

    /**
     * 发起充值
     * @param uid 用户id
     * @param chargeProdId 充值产品ID
     * @param payChannel 充值渠道，目前只支持
     *        {@link com.tongdaxing.xchat_core.Constants#CHARGE_ALIPAY} 和
     *        {@link com.tongdaxing.xchat_core.Constants#CHARGE_WX}
     * @param clientIp 客户端ip地址
     * @param ticket 凭证
     * @param callBack 回调方法
     */
    public void requestCharge(String uid,
                              String chargeProdId,
                              String payChannel,
                              String clientIp,
                              String ticket,
                              CallBack<JsonObject> callBack) {
//        execute(payService.requestCharge(uid, chargeProdId, payChannel, clientIp, ticket)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()),
//                callBack);
    }

    /**
     * 发起兑换开心
     * @param uid
     * @param diamondNum
     * @param ticket
     * @param callBack
     */
//    public void requestExchangeGold(String uid, String diamondNum,
//                                    String ticket,
//                                    CallBack<WalletInfo> callBack) {
//         execute(payService.requestExchangeGold(uid, diamondNum, ticket)
//                 .subscribeOn(Schedulers.io())
//                 .observeOn(AndroidSchedulers.mainThread()),
//                 callBack);
//    }

    /**
     * 支付功能的服务端接口
     */
    public interface PayService {

        /**
         * 获取我的钱包信息
         * @param uid 当前用户 id
         * @param ticket 凭证
         */
        @GET("/purse/query")
        Observable<ServiceResult<WalletInfo>> getMyWallet(@Query("uid") String uid,
                                                          @Query("ticket") String ticket,
                                                          @Header("Cache-Control") String cacheStrategy);

        /**
         * 获取充值产品列表
         * @param channelType 1支付宝微信公众号充值3苹果充值
         */
        @GET("/chargeprod/list")
        Observable<ServiceResult<List<ChargeBean>>> getChargeList(@Query("channelType") int channelType);

        /**
         * 发起充值
         * @param uid 用户id
         * @param chargeProdId 充值产品ID
         * @param payChannel 充值渠道，目前只支持
         *        {@link com.tongdaxing.xchat_core.Constants#CHARGE_ALIPAY} 和
         *        {@link com.tongdaxing.xchat_core.Constants#CHARGE_WX}
         * @param clientIp 客户端ip地址
         * @param ticket 凭证
         */
        @FormUrlEncoded
        @POST("/charge/apply")
        Observable<ServiceResult<JsonObject>> requestCharge(@Field("uid") String uid,
                                                            @Field("chargeProdId") String chargeProdId,
                                                            @Field("payChannel") String payChannel,
                                                            @Field("clientIp") String clientIp,
                                                            @Field("ticket") String ticket);

        /**
         * 发起兑换开心
         * @param uid 用户id
         * @param diamondNum 当前用户钱包里的钻石余额
         * @param ticket 凭证
         */
        @FormUrlEncoded
        @POST("/change/gold")
        Observable<ServiceResult<ExchangeAwardInfo>> requestExchangeGold(@Field("uid") String uid,
                                                                         @Field("diamondNum") String diamondNum,
                                                                         @Field("ticket") String ticket);

        /**
         * 发起兑换开心
         * @param uid 用户id
         * @param diamondNum 当前用户钱包里的钻石余额
         * @param ticket 凭证
         */
        @FormUrlEncoded
        @POST("/change/gold")
        Observable<ServiceResult<ExchangeAwardInfo>> requestExchangeGoldSms(@Field("uid") String uid,
                                                                         @Field("diamondNum") String diamondNum,
                                                                            @Field("smsCode") String smsCode,
                                                                         @Field("ticket") String ticket);
    }
}

package com.tongdaxing.xchat_core.follow;

import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

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
 * 关注的model
 *
 * @author xiaoyu
 * @date 2017/12/26
 */

public class FollowModel {

    private Api api;

    private FollowModel() {
        api = RxNet.create(Api.class);
    }

    private static FollowModel INSTANCE;

    public static FollowModel get() {
        if (INSTANCE == null) {
            synchronized (FollowModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FollowModel();
                }
            }
        }
        return INSTANCE;
    }

    public Single<ServiceResult> follow(final long likedUid, final boolean follow) {
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        String uid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        String type = String.valueOf(follow ? 1 : 2);
        String likeUid = String.valueOf(likedUid);
        return api.follow(ticket, uid, type, likeUid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).flatMap(new Function<ServiceResult,
                        SingleSource<? extends ServiceResult>>() {
                    @Override
                    public SingleSource<? extends ServiceResult> apply(final ServiceResult serviceResult) throws Exception {
                        // 发送聊天室事件给所有观察者
                        IMNetEaseManager.get().getChatRoomEventObservable().onNext(
                                new RoomEvent().setEvent(follow ? RoomEvent.FOLLOW : RoomEvent.UNFOLLOW)
                                        .setSuccess(serviceResult != null));
                        // 允许发起信息的人也收到回调
                        return Single.create(new SingleOnSubscribe<ServiceResult>() {
                            @Override
                            public void subscribe(SingleEmitter<ServiceResult> e) throws Exception {
                                if (serviceResult == null)
                                    e.onError(new Exception(follow ? "follow fail" : "unfollow fail"));
                                else e.onSuccess(serviceResult);
                            }
                        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public Single<ServiceResult<Boolean>> isFollowed(long uid, final long isLikeUid) {
        return api.isFollowed(String.valueOf(uid), String.valueOf(isLikeUid)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    interface Api {
        /**
         * 关注
         *
         * @param ticket  -
         * @param uid     -
         * @param type    -  1 关注, 2 取消关注
         * @param likeUid -
         * @return -
         */
        @FormUrlEncoded
        @POST("/fans/like")
        Single<ServiceResult> follow(@Field("ticket") String ticket, @Field("uid") String uid,
                                     @Field("type") String type, @Field("likedUid") String likeUid);

        /**
         * 是否已经关注
         *
         * @param uid       -
         * @param isLikeUid -
         * @return -
         */
        @GET("/fans/islike")
        Single<ServiceResult<Boolean>> isFollowed(@Query("uid") String uid, @Query("isLikeUid") String isLikeUid);
    }

}

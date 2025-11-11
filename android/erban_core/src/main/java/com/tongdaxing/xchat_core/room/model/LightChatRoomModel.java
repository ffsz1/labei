package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/25
 */
public class LightChatRoomModel extends RoomBaseModel {
    private final LightChatRoomService mLightChatRoomService;

    public LightChatRoomModel() {
        mLightChatRoomService = RxNet.create(LightChatRoomService.class);
    }

    /**
     * 点赞
     *
     * @param type     type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
     * @param likedUid 被点赞人uid，必填
     * @return
     */
    public void praise(int type, long likedUid, CallBack<String> callBack) {
        execute(mLightChatRoomService.praise(CoreManager.getCore(IAuthCore.class).getTicket(),
                CoreManager.getCore(IAuthCore.class).getCurrentUid(), type, likedUid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()), callBack);
    }

    public interface LightChatRoomService {

        /**
         * 点赞
         *
         * @param ticket
         * @param uid      点赞人uid，必填
         * @param type     type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
         * @param likedUid 被点赞人uid，必填
         * @return
         */
        @FormUrlEncoded
        @POST("fans/like")
        Observable<ServiceResult<String>> praise(@Field("ticket") String ticket, @Field("uid") long uid,
                                                 @Field("type") int type, @Field("likedUid") long likedUid);
    }
}

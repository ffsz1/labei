package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/24
 */
public class LightChatConsumeModel extends BaseMvpModel {
    private final LightChatConsumeService mLightChatConsumeService;

    public LightChatConsumeModel() {
        mLightChatConsumeService = RxNet.create(LightChatConsumeService.class);
    }

    /**
     * 获取轻聊房贡献榜人数信息列表
     *
     * @param roomUid
     * @param callBack
     * @return
     */
    public void getRoomConsumeList(long roomUid, CallBack<List<RoomConsumeInfo>> callBack) {
        execute(mLightChatConsumeService.getRoomConsumeList(roomUid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()), callBack);

    }


    public interface LightChatConsumeService {

        /**
         * 获取轻聊房贡献榜人数信息列表
         *
         * @param roomUid
         * @return
         */
        @GET("roomctrb/query")
        Observable<ServiceResult<List<RoomConsumeInfo>>> getRoomConsumeList(@Query("uid") long roomUid);
    }
}

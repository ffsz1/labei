package com.tongdaxing.xchat_core.room.presenter;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.model.RoomInviteModel;
import com.tongdaxing.xchat_core.room.view.IRoomInviteView;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/21
 */
public class RoomInvitePresenter extends AbstractMvpPresenter<IRoomInviteView> {

    private RoomInviteModel mRoomInviteModel;
    private Gson gson;

    public RoomInvitePresenter() {
        mRoomInviteModel = new RoomInviteModel();
        gson = new Gson();
    }


    /**
     * 查询成员列表
     *
     * @param index
     * @param page
     */
    public void requestChatMemberByPage(int index, final int page) {
        mRoomInviteModel.getMembers(index, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().onRequestChatMemberByPageFail("网络异常", page);
            }

            @Override
            public void onResponse(Json response) {
                if (response.num(IMKey.errno) != 0) {
                    if (getMvpView() != null)
                        getMvpView().onRequestChatMemberByPageFail(response.str(IMKey.errmsg, "网络异常"), page);
                    return;
                }
                String data = response.str("data");
                List<IMChatRoomMember> imChatRoomMembers = null;
                try {
                    imChatRoomMembers = gson.fromJson(data, new TypeToken<List<IMChatRoomMember>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<IMChatRoomMember> onlineChatMembers = getFilterOnMicList(imChatRoomMembers);

                if (getMvpView() != null) {
                    getMvpView().onRequestMemberByPageSuccess(onlineChatMembers, page);
                }
            }
        });
    }

    @Nullable
    private List<IMChatRoomMember> getFilterOnMicList(List<IMChatRoomMember> imChatRoomMembers) {
        List<IMChatRoomMember> onlineChatMembers = new ArrayList<>();
        if (imChatRoomMembers != null) {
            Set<String> micUIds = new HashSet<>();
            //排除麦上的用户
            SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
            for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
                RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo.mChatRoomMember != null) {
                    String account = roomQueueInfo.mChatRoomMember.getAccount();
                    micUIds.add(account);
                }
            }
            for (int i = 0; i < imChatRoomMembers.size(); i++) {
                IMChatRoomMember imChatRoomMember = imChatRoomMembers.get(i);
                if (imChatRoomMember == null || micUIds.contains(imChatRoomMember.getAccount()))
                    continue;
                onlineChatMembers.add(imChatRoomMember);
            }
        }
        return onlineChatMembers;
    }
}

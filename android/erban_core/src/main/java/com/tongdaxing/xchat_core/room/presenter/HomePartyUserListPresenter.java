package com.tongdaxing.xchat_core.room.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_core.room.model.HomePartyUserListModel;
import com.tongdaxing.xchat_core.room.view.IHomePartyUserListView;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class HomePartyUserListPresenter extends AbstractMvpPresenter<IHomePartyUserListView> {

    private final HomePartyUserListModel mHomePartyUserListMode;
    private Gson gson;

    public HomePartyUserListPresenter() {
        mHomePartyUserListMode = new HomePartyUserListModel();

        gson = new Gson();
    }

    /**
     * 分页获取房间成员：第一页包含队列成员，固定成员，游客50人，之后每一页获取游客50人
     *
     * @param page  页数
     * @param index 从哪个索引开始取
     */
    public void requestChatMemberByIndex(final int page, int index, final List<OnlineChatMember> oldList) {

        mHomePartyUserListMode.getMembers(index, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onRequestChatMemberByPageFail("网络异常", page);
                }
            }

            @Override
            public void onResponse(Json response) {
                if (getMvpView() != null) {
                    if (response.num(IMKey.errno) != 0) {
                        getMvpView().onRequestChatMemberByPageFail(response.str(IMKey.errmsg, "网络异常"), page);
                        return;
                    }

                    String data = response.str("data");
                    List<IMChatRoomMember> imChatRoomMembers = null;
                    try {
                        imChatRoomMembers = gson.fromJson(data, new TypeToken<List<IMChatRoomMember>>() {}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ListUtils.isListEmpty(imChatRoomMembers)) {
                        getMvpView().onRequestChatMemberByPageFail("没有更多数据", page);
                    } else {
                        List<OnlineChatMember> onlineChatMembers = mHomePartyUserListMode.memberToOnlineMember(imChatRoomMembers, false, oldList);
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                    }
                }
            }
        });
    }

    /**
     * 成员进来刷新在线列表
     */
    public void onMemberInRefreshData(String account, List<OnlineChatMember> onlineChatMembers, final int page,
            ChatRoomMessage chatRoomMessage) {
        if (chatRoomMessage == null) {
            return;
        }
        IMChatRoomMember imChatRoomMember = chatRoomMessage.getImChatRoomMember();
        if (imChatRoomMember == null) {
            return;
        }
        List<IMChatRoomMember> imChatRoomMembers = new ArrayList<>(1);
        imChatRoomMembers.add(imChatRoomMember);

        List<OnlineChatMember> onlineChatMemberList = mHomePartyUserListMode.memberToOnlineMember(imChatRoomMembers,
                false, onlineChatMembers);
        if (getMvpView() != null) {
            getMvpView().onRequestChatMemberByPageSuccess(onlineChatMemberList, page);
        }
    }

    public void onMemberDownUpMic(String account, boolean isUpMic, List<OnlineChatMember> dataList, final int page) {
        mHomePartyUserListMode.onMemberDownUpMic(account, isUpMic, dataList).subscribe(
                onlineChatMembers -> {
                    if (getMvpView() != null) {
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                    }
                });
    }


    public void onUpdateMemberManager(String account, List<OnlineChatMember> dataList,
            boolean isRemoveManager, final int page) {
        mHomePartyUserListMode.onUpdateMemberManager(account, isRemoveManager, dataList).subscribe(
                onlineChatMembers -> {
                    if (getMvpView() != null) {
                        getMvpView().onRequestChatMemberByPageSuccess(onlineChatMembers, page);
                    }
                });
    }
}

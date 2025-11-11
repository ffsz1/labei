package com.vslk.lbgx.room.avroom.fragment.base;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.listener.IDisposableAddListener;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRoomFragment<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>> extends BaseMvpFragment<V, P> {

    public abstract void showActivity(List<ActionDialogInfo> dialogInfo);

    protected abstract void onReconnection(RoomEvent roomEvent);

    protected abstract void onIMReconnection();

    protected abstract void onExitRoom();

    protected abstract void upCharm(RoomEvent roomEvent);

    protected abstract void releaseView();

    protected abstract void upRoom(int event, RoomEvent roomEvent);

    protected abstract void onRoomOnlineNumberSuccess(int onlineNumber);


    //显示座驾接口
    protected UserComeAction userComeAction;

    public void setUserComeAction(UserComeAction userComeAction) {
        this.userComeAction = userComeAction;
    }

    public interface UserComeAction {
        void showCar(String carImageUrl);
    }

    //消息过滤器
    protected List<ChatRoomMessage> msgFilter(List<ChatRoomMessage> chatRoomMessages) {
        if (ListUtils.isListEmpty(chatRoomMessages)) {
            return null;
        }
        List<ChatRoomMessage> messages = new ArrayList<>();
        for (int i = chatRoomMessages.size() - 1; i >= 0; i--) {
            if (chatRoomMessages.get(i).getAttachment() instanceof IMCustomAttachment) {
                if (((IMCustomAttachment) chatRoomMessages.get(i).getAttachment()).getFirst()
                        == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT ||
                        ((IMCustomAttachment) chatRoomMessages.get(i).getAttachment()).getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
                    messages.add(chatRoomMessages.get(i));
                }
            }
        }
        return messages;
    }


    protected void initSubscribe(IDisposableAddListener iDisposableAddListener) {
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            if (roomEvent == null) return;
            int event = roomEvent.getEvent();
            switch (event) {
                case RoomEvent.ROOM_CHAT_RECONNECTION://断网重连
                    //从新获取队列信息
                    onReconnection(roomEvent);
                    break;
                case RoomEvent.ROOM_EXIT://退出房间
                    onExitRoom();
                    break;
                case RoomEvent.ROOM_CHARM://房间魅力值更新
                    upCharm(roomEvent);
                    break;
                case RoomEvent.ROOM_RECONNECT://IM重连
                case RoomEvent.ENTER_ROOM:
                    onIMReconnection();
                    break;
                case RoomEvent.KICK_OUT_ROOM:
                    if (roomEvent.getReason_no() == 3) {
                        //清除资源
                        releaseView();
                    }
                    break;
                default:
                    upRoom(event, roomEvent);
                    break;
            }
        }, iDisposableAddListener);
    }

}

package com.vslk.lbgx.room.avroom.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

/**
 * <p> 房间在线人数列表 （上麦，房主，游客，管理员）  </p>
 *
 * @author Administrator
 * @date 2017/12/4
 */
public class OnlineUserAdapter extends BaseQuickAdapter<OnlineChatMember, BaseViewHolder> {

    private Disposable mDisposable;

    public OnlineUserAdapter() {
        super(R.layout.list_item_online_user);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        registerRoomEvent();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OnlineChatMember onlineChatMember) {

        if (onlineChatMember != null && onlineChatMember.chatRoomMember != null) {
            final ImageView sexImage = baseViewHolder.getView(R.id.sex);
            IMChatRoomMember chatRoomMember = onlineChatMember.chatRoomMember;
            if (chatRoomMember.getGender() == GenderEnum.MALE.getValue()) {
                sexImage.setVisibility(View.VISIBLE);
                sexImage.setImageResource(R.drawable.icon_man);
            } else if (chatRoomMember.getGender() == GenderEnum.FEMALE.getValue()) {
                sexImage.setVisibility(View.VISIBLE);
                sexImage.setImageResource(R.drawable.icon_woman);
            } else {
                sexImage.setVisibility(View.GONE);
            }

            ImageView ivOne = baseViewHolder.getView(R.id.room_owner_logo);
            ImageView ivTwo = baseViewHolder.getView(R.id.manager_logo);
            ivOne.setVisibility(View.GONE);
            ivTwo.setVisibility(View.GONE);

            if (onlineChatMember.isOnMic) {//如果在麦上
                if (onlineChatMember.isRoomOwer) {//如果是房主
                    ivOne.setImageResource(R.mipmap.icon_user_list_room_owner);
                    ivOne.setVisibility(View.VISIBLE);
                    ivTwo.setImageResource(R.mipmap.icon_user_list_up_mic);
                    ivTwo.setVisibility(View.VISIBLE);
                } else if (onlineChatMember.isAdmin) {//如果是管理员
                    ivOne.setImageResource(R.mipmap.icon_admin_logo);
                    ivOne.setVisibility(View.VISIBLE);
                    ivTwo.setImageResource(R.mipmap.icon_user_list_up_mic);
                    ivTwo.setVisibility(View.VISIBLE);
                } else {//如果是普通成员
                    ivOne.setImageResource(R.mipmap.icon_user_list_up_mic);
                    ivOne.setVisibility(View.VISIBLE);
                }
            } else {//如果不在麦上
                if (onlineChatMember.isRoomOwer) {//如果是房主
                    ivOne.setImageResource(R.mipmap.icon_user_list_room_owner);
                    ivOne.setVisibility(View.VISIBLE);
                } else if (onlineChatMember.isAdmin) {//如果是管理员
                    ivOne.setImageResource(R.mipmap.icon_admin_logo);
                    ivOne.setVisibility(View.VISIBLE);
                }
            }

            baseViewHolder.setText(R.id.nick, chatRoomMember.getNick());

            ImageView avatar = baseViewHolder.getView(R.id.avatar);
            ImageLoadUtils.loadAvatar(mContext, chatRoomMember.getAvatar(), avatar);

            TextView nick = baseViewHolder.getView(R.id.nick);
            nick.setTextColor(ContextCompat.getColor(mContext, R.color.color_1A1A1A));
        }
    }

    private void registerRoomEvent() {
        mDisposable = IMNetEaseManager.get()
                .getChatRoomEventObservable()
                .subscribe(roomEvent -> {
                    if (roomEvent == null) {
                        return;
                    }
                    int event = roomEvent.getEvent();
                    if (roomEvent.getEvent() == RoomEvent.ADD_BLACK_LIST ||
                            roomEvent.getEvent() == RoomEvent.DOWN_MIC ||
                            event == RoomEvent.ROOM_MEMBER_EXIT ||
                            roomEvent.getEvent() == RoomEvent.KICK_OUT_ROOM) {
                        if (roomEvent.getEvent() == RoomEvent.ADD_BLACK_LIST ||
                                roomEvent.getEvent() == RoomEvent.KICK_OUT_ROOM) {
                            if (mListener != null && !AvRoomDataManager.get().isOwner(CoreManager.getCore(IAuthCore.class).getCurrentUid())) {
                                mListener.addMemberBlack();
                                return;
                            }
                        }
                        if (ListUtils.isListEmpty(mData)) {
                            return;
                        }
                        if (roomEvent.getEvent() == RoomEvent.DOWN_MIC) {
                            updateDownUpMic(roomEvent.getAccount(), false);
                            return;
                        }
                        ListIterator<OnlineChatMember> iterator = mData.listIterator();
                        for (; iterator.hasNext(); ) {
                            OnlineChatMember onlineChatMember = iterator.next();
                            if (onlineChatMember.chatRoomMember != null && Objects.equals(onlineChatMember.chatRoomMember.getAccount(), roomEvent.getAccount())) {
                                iterator.remove();
                            }
                        }
                        notifyDataSetChanged();
                    } else if (roomEvent.getEvent() == RoomEvent.ROOM_MANAGER_ADD
                            || roomEvent.getEvent() == RoomEvent.ROOM_MANAGER_REMOVE) {
                        updateManager(roomEvent);
                    } else if (roomEvent.getEvent() == RoomEvent.UP_MIC) {
                        updateDownUpMic(roomEvent.getAccount(), true);
                    } else if (event == RoomEvent.ROOM_MEMBER_IN) {
                        updateMemberIn(roomEvent);
                    }
                });
    }

    private void updateMemberIn(RoomEvent roomEvent) {
        if (mListener != null) {

            mListener.onMemberIn(roomEvent.getAccount(), mData, roomEvent.getChatRoomMessage());
        }
    }

    private void updateManager(RoomEvent roomEvent) {
        if (mListener != null) {
            mListener.onUpdateMemberManager(roomEvent.getAccount(), roomEvent.getEvent() == RoomEvent.ROOM_MANAGER_REMOVE, mData);
        }
    }


    private void updateDownUpMic(String account, boolean isUpMic) {
        if (mListener != null) {
            mListener.onMemberDownUpMic(account, isUpMic, mData);
        }
    }

    public void release() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private OnRoomOnlineNumberChangeListener mListener;

    public void setListener(OnRoomOnlineNumberChangeListener listener) {
        mListener = listener;
    }

    public interface OnRoomOnlineNumberChangeListener {
        /**
         * 成员进来回调
         */
        void onMemberIn(String account, List<OnlineChatMember> dataList, ChatRoomMessage chatRoomMessage);

        /**
         * 成员上下麦更新
         */
        void onMemberDownUpMic(String account, boolean isUpMic, List<OnlineChatMember> dataList);

        /**
         * 设置管理员回调
         */
        void onUpdateMemberManager(String account, boolean isRemoveManager, List<OnlineChatMember> dataList);

        void addMemberBlack();
    }
}

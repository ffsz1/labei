package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.room.avroom.other.ChatMemberDiffUtilCallback;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/21
 */
public class RoomInviteAdapter extends RecyclerView.Adapter<RoomInviteAdapter.RoomInviteViewHolder> {

    private List<IMChatRoomMember> mChatRoomMemberList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater mInflater;
    private Disposable mDisposable;

    public RoomInviteAdapter(Context context, OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        if (mChatRoomMemberList == null)
            mChatRoomMemberList = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mDisposable = IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(new Consumer<RoomEvent>() {
                    @Override
                    public void accept(RoomEvent roomEvent) throws Exception {
                        if (roomEvent == null) return;
                        int event = roomEvent.getEvent();
                        if (roomEvent.getEvent() == RoomEvent.ADD_BLACK_LIST ||
                                roomEvent.getEvent() == RoomEvent.DOWN_MIC ||
                                event == RoomEvent.ROOM_MEMBER_EXIT ||
                                roomEvent.getEvent() == RoomEvent.KICK_OUT_ROOM) {
                            if (ListUtils.isListEmpty(mChatRoomMemberList)) return;
                            ListIterator<IMChatRoomMember> iterator = mChatRoomMemberList.listIterator();
                            for (; iterator.hasNext(); ) {
                                IMChatRoomMember onlineChatMember = iterator.next();
                                if (onlineChatMember != null
                                        && Objects.equals(onlineChatMember.getAccount(), roomEvent.getAccount())) {
                                    iterator.remove();
                                }
                            }
                            notifyDataSetChanged();
                            if (mOnRoomOnlineNumberChangeListener != null)
                                mOnRoomOnlineNumberChangeListener.onRoomOnlineNumberChange(getItemCount());
                        } else if (event == RoomEvent.ROOM_MEMBER_IN) {
                            updateMemberIn(roomEvent);
                        }
                    }
                });
    }

    public void onRelease() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private void updateMemberIn(RoomEvent roomEvent) {
        IMChatRoomMember chatRoomMember = AvRoomDataManager.get().getChatRoomMember(roomEvent.getAccount());
        if (chatRoomMember == null) return;
        if (!ListUtils.isListEmpty(mChatRoomMemberList)) {
            for (IMChatRoomMember temp : mChatRoomMemberList) {
                if (Objects.equals(temp.getAccount(), chatRoomMember.getAccount()))
                    return;
            }
        }
        List<IMChatRoomMember> list = new ArrayList<>(1);
        list.add(chatRoomMember);
        addChatRoomMemberList(list);
        if (mOnRoomOnlineNumberChangeListener != null)
            mOnRoomOnlineNumberChangeListener.onRoomOnlineNumberChange(getItemCount());
    }

    public void addChatRoomMemberList(final List<IMChatRoomMember> chatRoomMemberList) {
        if (ListUtils.isListEmpty(mChatRoomMemberList)) {
            mChatRoomMemberList = chatRoomMemberList;
            notifyItemRangeChanged(0, chatRoomMemberList.size());
        } else {
            mChatRoomMemberList.addAll(chatRoomMemberList);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                    new ChatMemberDiffUtilCallback(mChatRoomMemberList, chatRoomMemberList), true);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public void setNewData(List<IMChatRoomMember> chatRoomMemberList) {
        if (ListUtils.isListEmpty(chatRoomMemberList))
            return;
        mChatRoomMemberList = chatRoomMemberList;
        notifyDataSetChanged();
    }

    public void addNewData(List<IMChatRoomMember> chatRoomMemberList) {
        if (ListUtils.isListEmpty(chatRoomMemberList))
            return;
        if (mChatRoomMemberList == null)
            mChatRoomMemberList = new ArrayList<>();
        mChatRoomMemberList.addAll(chatRoomMemberList);
        notifyDataSetChanged();
    }

    public List<IMChatRoomMember> getChatRoomMemberList() {
        return mChatRoomMemberList;
    }

    @Override
    public RoomInviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoomInviteViewHolder(mInflater.inflate(R.layout.room_invite_list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RoomInviteViewHolder holder, int position) {

        final IMChatRoomMember chatRoomMember = mChatRoomMemberList.get(position);
        if (chatRoomMember == null) return;
        NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(chatRoomMember.getAccount());
        final ImageView sexImage = holder.sexImage;
        if (nimUserInfo == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(chatRoomMember.getAccount(),
                    new RequestCallbackWrapper<NimUserInfo>() {
                        @Override
                        public void onResult(int i, NimUserInfo nimUserInfo, Throwable throwable) {
                            if (nimUserInfo != null) {
                                if (nimUserInfo.getGenderEnum() == GenderEnum.MALE) {
                                    sexImage.setVisibility(View.VISIBLE);
                                    sexImage.setImageResource(R.drawable.icon_man);
                                } else if (nimUserInfo.getGenderEnum() == GenderEnum.FEMALE) {
                                    sexImage.setVisibility(View.VISIBLE);
                                    sexImage.setImageResource(R.drawable.icon_woman);
                                } else {
                                    sexImage.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        } else {
            if (nimUserInfo.getGenderEnum() == GenderEnum.MALE) {
                sexImage.setVisibility(View.VISIBLE);
                sexImage.setImageResource(R.drawable.icon_man);
            } else if (nimUserInfo.getGenderEnum() == GenderEnum.FEMALE) {
                sexImage.setVisibility(View.VISIBLE);
                sexImage.setImageResource(R.drawable.icon_woman);
            } else {
                sexImage.setVisibility(View.GONE);
            }
        }
//        holder.mViewLine.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        ImageLoadUtils.loadAvatar(mContext, chatRoomMember.getAvatar(), holder.mIvAvatar);
        holder.mTvMemberName.setText(chatRoomMember.getNick());

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onClick(chatRoomMember);
        });
    }

    @Override
    public int getItemCount() {
        return mChatRoomMemberList != null ? mChatRoomMemberList.size() : 0;
    }


    static class RoomInviteViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvAvatar;
        private ImageView sexImage;
        private TextView mTvMemberName;
//        View mViewLine;

        RoomInviteViewHolder(View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            sexImage = itemView.findViewById(R.id.sex);
            mTvMemberName = itemView.findViewById(R.id.tv_member_name);
//            mViewLine = itemView.findViewById(R.id.view_line);
        }
    }


    public interface OnItemClickListener {
        void onClick(IMChatRoomMember chatRoomMember);
    }

    private OnRoomOnlineNumberChangeListener mOnRoomOnlineNumberChangeListener;

    public void setOnRoomOnlineNumberChangeListener(OnRoomOnlineNumberChangeListener listener) {
        mOnRoomOnlineNumberChangeListener = listener;
    }

    public interface OnRoomOnlineNumberChangeListener {
        void onRoomOnlineNumberChange(int number);
    }

}

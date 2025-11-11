package com.vslk.lbgx.room.avroom.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenran
 * @date 2017/7/28
 */
public class UserListViewAdapter extends RecyclerView.Adapter<UserListViewAdapter.UserListViewHolder> {
    private List<IMChatRoomMember> chatRoomMembers = new ArrayList<>();
    private UserListViewItemClickListener listener;
    private int type;

    public UserListViewAdapter(int type) {
        this.type = type;
    }

    public void setListener(UserListViewItemClickListener listener) {
        this.listener = listener;
    }

   public void updateList() {
        if (chatRoomMembers.size() > 0)
            chatRoomMembers.clear();
        SparseArray<RoomQueueInfo> roomQueueInfoMap = AvRoomDataManager.get().mMicQueueMemberMap;
        int size = roomQueueInfoMap.size();
        for (int i = 0; i < size; i++) {
            RoomQueueInfo roomQueueInfo = roomQueueInfoMap.valueAt(i);
            if (roomQueueInfo.mChatRoomMember != null
                    && !AvRoomDataManager.get().isRoomOwner(roomQueueInfo.mChatRoomMember.getAccount())) {
                chatRoomMembers.add(roomQueueInfo.mChatRoomMember);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public UserListViewAdapter.UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        if (type == 1) {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_room_user, parent, false);
        } else {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_room_user_big, parent, false);
        }
        return new UserListViewAdapter.UserListViewHolder(item);
    }

    @Override
    public void onBindViewHolder(UserListViewAdapter.UserListViewHolder holder, int position) {
        final IMChatRoomMember chatRoomMember = chatRoomMembers.get(position);
        // clear animation
        holder.speakState.setImageDrawable(null);
        holder.speakState.clearAnimation();

        ImageLoadUtils.loadAvatar(holder.avatar.getContext(), chatRoomMember.getAvatar(), holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClicked(chatRoomMember);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomMembers == null ? 0 : chatRoomMembers.size();
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private ImageView micImage;
        private ImageView speakState;

        UserListViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            micImage = itemView.findViewById(R.id.user_mic);
            speakState = itemView.findViewById(R.id.user_speek);
        }

    }

    public interface UserListViewItemClickListener {
        void onItemClicked(IMChatRoomMember chatRoomMember);
    }
}

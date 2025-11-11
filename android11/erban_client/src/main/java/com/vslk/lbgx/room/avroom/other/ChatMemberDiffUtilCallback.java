package com.vslk.lbgx.room.avroom.other;

import android.support.v7.util.DiffUtil;

import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 */
public class ChatMemberDiffUtilCallback extends DiffUtil.Callback {
    private List<IMChatRoomMember> mOldMemberList, mNewMemberList;

    public ChatMemberDiffUtilCallback(List<IMChatRoomMember> oldMemberList, List<IMChatRoomMember> newMemberList) {
        mOldMemberList = oldMemberList;
        mNewMemberList = newMemberList;
    }

    @Override
    public int getOldListSize() {
        return mOldMemberList == null ? 0 : mOldMemberList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMemberList == null ? 0 : mNewMemberList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(mOldMemberList.get(oldItemPosition).getAccount(),
                mNewMemberList.get(newItemPosition).getAccount());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        IMChatRoomMember oldItem = mOldMemberList.get(oldItemPosition);
        IMChatRoomMember newItem = mNewMemberList.get(newItemPosition);
        return Objects.equals(oldItem.getAccount(), newItem.getAccount())
                && Objects.equals(oldItem.getNick(), newItem.getNick());
    }
}
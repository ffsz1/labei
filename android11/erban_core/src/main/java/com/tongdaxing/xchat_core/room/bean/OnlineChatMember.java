package com.tongdaxing.xchat_core.room.bean;

import android.support.annotation.NonNull;

import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * <p> 在线用户列表，包装 {@link IMChatRoomMember} </p>
 *
 * @author jiahui
 * @date 2017/12/20
 */
public class OnlineChatMember implements Comparable<OnlineChatMember> {
    public IMChatRoomMember chatRoomMember;

    public boolean isOnMic;
    public boolean isAdmin;
    public boolean isRoomOwer;

    public OnlineChatMember() {
    }

    public OnlineChatMember(IMChatRoomMember chatRoomMember) {
        this.chatRoomMember = chatRoomMember;
    }

    public OnlineChatMember(IMChatRoomMember chatRoomMember, boolean isOnMic, boolean isAdmin, boolean isRoomOwer) {
        this.chatRoomMember = chatRoomMember;
        this.isOnMic = isOnMic;
        this.isAdmin = isAdmin;
        this.isRoomOwer = isRoomOwer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnlineChatMember)) return false;

        OnlineChatMember that = (OnlineChatMember) o;
        return !(chatRoomMember == null || that.chatRoomMember == null)
                && Objects.equals(chatRoomMember.getAccount(), that.chatRoomMember.getAccount());
    }

    @Override
    public int hashCode() {
        return chatRoomMember.getAccount().hashCode();
    }

    public static List<OnlineChatMember> coverToOnlineChatMember(List<IMChatRoomMember> oldList, List<IMChatRoomMember> newList) {
        HashSet<OnlineChatMember> treeSet = new HashSet<>();
        if (!ListUtils.isListEmpty(oldList)) {
            List<OnlineChatMember> list = new ArrayList<>(oldList.size());
            for (IMChatRoomMember chatRoomMember : oldList) {
                list.add(new OnlineChatMember(chatRoomMember));
            }
            treeSet.addAll(list);
        }
        if (!ListUtils.isListEmpty(newList)) {
            List<OnlineChatMember> list = new ArrayList<>(newList.size());
            for (IMChatRoomMember chatRoomMember : newList) {
                list.add(new OnlineChatMember(chatRoomMember));
            }
            treeSet.addAll(list);
        }
        ArrayList<OnlineChatMember> list = new ArrayList<>(treeSet);
        Collections.sort(list);
        return list;
    }

    public static List<IMChatRoomMember> converOnlineToNormal(List<OnlineChatMember> onlineChatMembers) {
        if (ListUtils.isListEmpty(onlineChatMembers)) return null;
        List<IMChatRoomMember> chatRoomMembers = new ArrayList<>();
        for (OnlineChatMember temp : onlineChatMembers) {
            if (temp.chatRoomMember == null) continue;
            chatRoomMembers.add(temp.chatRoomMember);
        }
        return chatRoomMembers;
    }

    @Override
    public int compareTo(@NonNull OnlineChatMember o) {
        if (chatRoomMember == null) return 1;
        if (o.chatRoomMember == null) return -1;
        return (int) (o.chatRoomMember.getEnter_time() - chatRoomMember.getEnter_time());
    }
}

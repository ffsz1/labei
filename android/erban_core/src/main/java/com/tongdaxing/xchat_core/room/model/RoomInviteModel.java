package com.tongdaxing.xchat_core.room.model;

import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.BiFunction;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/21
 */
public class RoomInviteModel extends RoomBaseModel {

    /**
     * 分页获取房间成员：第一页包含队列成员，固定成员，游客50人，之后每一页获取游客50人
     *
     * @param page 页数
     * @param time 固定成员列表用updateTime,
     *             游客列表用进入enterTime，
     *             填0会使用当前服务器最新时间开始查询，即第一页，单位毫秒
     */
    public Single<List<IMChatRoomMember>> getPageMembers(final int page, long time) {
        Single<List<IMChatRoomMember>> onlineUserObservable = queryOnlineList(500);
        Single<List<IMChatRoomMember>> firstGuestObservable = queryGuestList(ROOM_MEMBER_SIZE, 0);
        if (page == Constants.PAGE_START) {
            return Single.zip(onlineUserObservable, firstGuestObservable,
                    new BiFunction<List<IMChatRoomMember>, List<IMChatRoomMember>, List<IMChatRoomMember>>() {
                        @Override
                        public List<IMChatRoomMember> apply(List<IMChatRoomMember> chatRoomMemberList, List<IMChatRoomMember> chatRoomMemberList2) throws Exception {
                            List<IMChatRoomMember> memberList = new ArrayList<>();
                            if (!ListUtils.isListEmpty(chatRoomMemberList)) {
                                memberList.addAll(chatRoomMemberList);
                            }
                            if (!ListUtils.isListEmpty(chatRoomMemberList2)) {
                                memberList.addAll(chatRoomMemberList2);
                            }
                            return memberList;
                        }
                    });
        } else {
            return queryGuestList(ROOM_MEMBER_SIZE, time);
        }
    }



}

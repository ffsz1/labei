package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/21
 */
public interface IRoomInviteView extends IHomePartyUserListView {

    void onRequestMemberByPageSuccess(List<IMChatRoomMember> memberList, int page);

}

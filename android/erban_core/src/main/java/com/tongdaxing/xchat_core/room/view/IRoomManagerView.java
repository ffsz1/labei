package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public interface IRoomManagerView extends IRoomMemberView {

    void queryManagerListSuccess(List<IMChatRoomMember> chatRoomMemberList);

    void queryManagerListFail();


}

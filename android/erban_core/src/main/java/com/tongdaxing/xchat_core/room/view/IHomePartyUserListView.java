package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.room.bean.OnlineChatMember;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public interface IHomePartyUserListView extends IMvpBaseView {
    void onRequestChatMemberByPageSuccess(List<OnlineChatMember> memberList, int page);

    void onRequestChatMemberByPageFail(String errorStr, int page);
}

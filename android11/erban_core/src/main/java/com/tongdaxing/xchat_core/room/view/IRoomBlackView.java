package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public interface IRoomBlackView extends IMvpBaseView {
    /**
     * 获取黑名单列表成功这里为了分页获取的是正常用户列表)
     *
     * @param chatRoomMemberList
     */
    void queryNormalListSuccess(List<IMChatRoomMember> chatRoomMemberList);

    /**
     * 获取黑名单列表失败(这里为了分页获取的是正常用户列表)
     */
    void queryNormalListFail();

    /**
     * 拉黑操作成功
     *
     * @param   account
     * @param mark           true，拉黑，false：移除拉黑
     */
    void makeBlackListSuccess( String account, boolean mark);

    /**
     * 拉黑操作失败
     *
     */
    void makeBlackListFail(String error);
}

package com.tongdaxing.xchat_core.room.view;

import android.util.SparseArray;

import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/25
 */
public interface ILightChatOnlineView extends IHomePartyUserListView {
    /**
     * 获取按钮相关操作集合
     *
     * @param chatRoomMember 操作当前的成员
     * @param position       麦序
     * @return
     */
    SparseArray<ButtonItem> getButtonItemList(IMChatRoomMember chatRoomMember, int position);

    /**
     * 显示列表点击对话框
     *
     * @param buttonItemList
     */
    void showItemClickDialog(List<ButtonItem> buttonItemList);

    /**
     * 显示用户信息对话框
     *
     * @param account
     */
    void showUserInfoDialog(String account);

}

package com.tongdaxing.xchat_core.room.view;

import android.util.SparseArray;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;

import java.util.List;

/**
 * <p> 轰趴房View层  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public interface IHomePartyView extends IMvpBaseView {

    void showFingerGuessingGame(boolean isShow);

    void showVerifiedDialog(int code, String message);

    void resultLoadNormalMembers(List<IMChatRoomMember> chatRoomMemberList);

    /**
     * 获取点击头像Button 列表
     *
     * @param micPosition    麦上位置
     * @param chatRoomMember 麦上位置
     * @param currentRoom    麦上位置
     * @return
     */
    SparseArray<ButtonItem> getAvatarButtonItemList(int micPosition, IMChatRoomMember chatRoomMember, RoomInfo currentRoom);

    /**
     * 点击麦上用户头像，显示操作对话框
     *
     * @param buttonItemList
     */
    void showMicAvatarClickDialog(List<ButtonItem> buttonItemList);

    /**
     * 点击麦上用户头像直接显示送礼物弹窗
     */
    void showGiftDialog(IMChatRoomMember chatRoomMember);

    /**
     * 点击麦上头像，显示用户信息对话框
     *
     * @param uId
     */
    void showMicAvatarUserInfoDialog(String uId);

    /**
     * 被踢下麦成功
     */
    void kickDownMicroPhoneSuccess();

    /**
     * 麦上没人点击坑位处理
     *
     * @param roomMicInfo 坑位信息
     * @param micPosition
     * @param currentUid
     */
    void showOwnerClickDialog(RoomMicInfo roomMicInfo, int micPosition, long currentUid, boolean isOwner);

    /**
     * 断网重连回调
     */
    void chatRoomReConnectView();

    /**
     * 房主点击自己头像
     */
    void showOwnerSelfInfo(IMChatRoomMember chatRoomMember);

    /**
     * 房主自动上麦成功后通知刷新
     */
    void notifyRefresh();

    void notifyBottomBtnState();

    //刷新神豪榜数据
    void reDaySuperRich(RoomConsumeInfo roomConsumeInfo);

    void reDaySuperRichFailure(String error);
}

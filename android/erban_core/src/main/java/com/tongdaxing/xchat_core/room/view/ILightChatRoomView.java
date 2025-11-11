package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * <p> 轻聊房</p>
 *
 * @author jiahui
 * @date 2017/12/24
 */
public interface ILightChatRoomView extends IMvpBaseView {
    /**
     * 赞操作成功
     *
     * @param type     type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
     * @param likedUid 被点赞人uid，必填
     */
    void praiseSuccess(int type, long likedUid);

    /**
     * 赞操作失败
     *
     * @param type     type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
     * @param likedUid 被点赞人uid，必填
     */
    void praiseFail(int type, long likedUid);

    /** 被踢下麦成功 */
    void kickDownMicroPhoneSuccess();

}

package com.tongdaxing.xchat_core.follow;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * @author xiaoyu
 * @date 2017/12/26
 */

public interface IFollowView extends IMvpBaseView {
    /**
     * 关注是否成功
     *
     * @param success-
     */
    void onFollow(boolean success);

    /**
     * 取消关注是否成功
     *
     * @param success-
     */
    void onUnFollow(boolean success);
}

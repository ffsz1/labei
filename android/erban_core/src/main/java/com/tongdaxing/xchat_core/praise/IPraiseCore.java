package com.tongdaxing.xchat_core.praise;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/5/18.
 */

public interface IPraiseCore extends IBaseCore {

    /**
     * uid：点赞人uid，必填
     * likedUId：被点赞人uid，必填
     * type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
     * ticket：必填
     * 如果双方为相互喜欢，业务方像网易云发送直接加好友请求，双方直接成为好友。
     * 详见：
     */
    void praise(long likedUid);

    void cancelPraise(long cancelUid, boolean showNotice);

    void deleteLike(long deletedUid);

    void getAllFans(long uid, int pageSize, int pageNo);

    void isPraised(long uid, long isLikeUid);

}

package com.tongdaxing.xchat_core.pk;

import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

public interface IPkCore extends IBaseCore {


    /**
     * 发起保存一个PK
     */
    void savePK(long roomId, PkVoteInfo info);


    void getPkHistoryList(long roomId, int page);

    /**
     * 取消一个PK
     * @param roomId
     * @param uid
     */
    void cancelPK(long roomId, long uid);
}

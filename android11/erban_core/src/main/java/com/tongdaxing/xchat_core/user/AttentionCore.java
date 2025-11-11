package com.tongdaxing.xchat_core.user;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public interface AttentionCore extends IBaseCore {
    void getAttentionList(long uid,int page,int pageSize);

    /**
     * 获取粉丝列表
     * @param uid
     * @param pageCount
     * @param pageSize
     * @param pageType 每个页面的唯一标识
     */
    void getFansList(long uid,int pageCount, int pageSize, int pageType);
}

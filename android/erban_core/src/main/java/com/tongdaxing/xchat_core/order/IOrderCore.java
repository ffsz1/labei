package com.tongdaxing.xchat_core.order;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by zhouxiangfeng on 2017/6/15.
 */

public interface IOrderCore extends IBaseCore {

    void getOrderList(long uid);

    void getOrderById(int order);

    void finishOrder(int orderId,long uid);
}

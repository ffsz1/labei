package com.tongdaxing.xchat_core.order;

import com.tongdaxing.xchat_core.order.bean.OrderListInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/6/15.
 */

public interface IOrderCoreClient extends ICoreClient {
    public static final String METHOD_GET_ORDER_LIST = "onGetOrderList";
    public static final String METHOD_GET_ORDER_LIST_ERROR = "onGetOrderListFail";
    public static final String METHOD_GET_ORDER = "onGetOrder";
    public static final String METHOD_GET_ORDER_ERROR = "onGetOrderFail";

    public static final String METHOD_FINISH_ORDER = "onFinishOrder";
    public static final String METHOD_FINISH_ORDER_ERROR = "onFinishOrderFail";


    public void onGetOrderList(List<OrderListInfo> orderBeanList);
    public void onGetOrderListFail(String error);

    public void onGetOrder(OrderListInfo orderBean);
    public void onGetOrderFail(String error);


    public void onFinishOrder();
    public void onFinishOrderFail(String error);
}

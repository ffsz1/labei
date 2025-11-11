package com.tongdaxing.xchat_core.order;

import android.util.Log;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.order.bean.OrderListInfo;
import com.tongdaxing.xchat_core.utils.Logger;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/6/15.
 */

public class OrderCoreImpl extends AbstractBaseCore implements IOrderCore {

    private static final String TAG = "OrderCoreImpl";

    @Override
    //
    public void getOrderList(long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("type", String.valueOf(1));

        OkHttpManager.getInstance().getRequest(UriProvider.getOrderList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<OrderListInfo>>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_GET_ORDER_LIST_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<OrderListInfo>> response) {
                if (null != response) {
                    Log.i(TAG, "onResponse: !null");
                    if (response.isSuccess()) {
                        Log.i(TAG, "onResponse:走了成功 ");
                        notifyClients(IOrderCoreClient.class,IOrderCoreClient.METHOD_GET_ORDER_LIST,response.getData());
                    } else {
                        notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_GET_ORDER_LIST_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    //获取指定订单
    public void getOrderById(int orderId) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("orderId", String.valueOf(orderId));

        OkHttpManager.getInstance().getRequest(UriProvider.getOrder(), params, new OkHttpManager.MyCallBack<ServiceResult<OrderListInfo>>() {
            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_GET_ORDER_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<OrderListInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IOrderCoreClient.class,IOrderCoreClient.METHOD_GET_ORDER,response.getData());
                    } else {
                        notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_GET_ORDER_ERROR, response.getMessage());
                    }
                }
            }
        });
    }



    //完成订单
    @Override
    public void finishOrder(int orderId, long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(uid));
        params.put("orderId", String.valueOf(orderId));

        OkHttpManager.getInstance().getRequest(UriProvider.finishOrder(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_FINISH_ORDER_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IOrderCoreClient.class,IOrderCoreClient.METHOD_FINISH_ORDER);
                    } else {
                        notifyClients(IOrderCoreClient.class, IOrderCoreClient.METHOD_FINISH_ORDER_ERROR, response.getMessage());
                    }
                }
            }
        });
    }
}

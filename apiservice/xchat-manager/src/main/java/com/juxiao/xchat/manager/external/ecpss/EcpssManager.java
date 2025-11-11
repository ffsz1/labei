package com.juxiao.xchat.manager.external.ecpss;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.ecpss.bo.OrderCallBackBO;
import com.juxiao.xchat.manager.external.ecpss.bo.QueryOrderBO;

/**
 * @ClassName: EcpssManager
 * @Description: 汇潮支付Manager
 * @Author: alwyn
 * @Date: 2019/5/27 20:07
 * @Version: 1.0
 */
public interface EcpssManager {

    /**
     * 创建订单
     *
     * @param orderNo 订单号
     * @param amount  订单金额 单位分
     * @param uid     支付用户
     * @param mchItem 商品描述
     * @return
     */
    String alipayOrder(String orderNo, int amount, Long uid, String mchItem);

    /**
     * 查询订单状态
     *
     * @param orderNo
     * @return
     */
    QueryOrderBO queryOrder(String orderNo) throws WebServiceException;

    /**
     * 回调验签
     *
     * @return
     * @throws WebServiceException
     */
    OrderCallBackBO verifySignature(OrderCallBackBO callBackBO) throws WebServiceException;

}

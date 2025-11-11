package com.juxiao.xchat.manager.external.ecpss.bo;

import lombok.Data;

/**
 * @ClassName: QueryOrderBO
 * @Description: 查询订单信息BO
 * @Author: alwyn
 * @Date: 2019/5/27 21:14
 * @Version: 1.0
 */
@Data
public class QueryOrderBO {

    /**
     * 商户订单号
     */
    private String merchantOutOrderNo;
    /**
     * 商户号
     */
    private String merid;
    /**
     * 订单金额
     */
    private Double orderMoney;
    /**
     * 随机参数,下单时传过去的,汇潮原样返回
     */
    private String noncestr;
    /**
     * 汇潮订单号
     */
    private String orderNo;
    /**
     * 支付结果
     */
    private Integer payResult;
    /**
     * 支付时间
     */
    private String payTime;

    private Integer code;
    private String msg;

}

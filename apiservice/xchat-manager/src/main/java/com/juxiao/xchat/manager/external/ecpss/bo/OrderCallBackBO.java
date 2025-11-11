package com.juxiao.xchat.manager.external.ecpss.bo;

import lombok.Data;

/**
 * @ClassName: OrderCallBackBO
 * @Description: 支付订单回调参数BO
 * @Author: alwyn
 * @Date: 2019/5/27 21:24
 * @Version: 1.0
 */
@Data
public class OrderCallBackBO {

    /**
     * 商户订单号(本系统生产的单号)
     */
    private String merchantOutOrderNo;
    /**
     * 商户号
     */
    private String merid;
    /**
     * 订单的详细信息, json 字符串 payMoney 交易金额
     */
    private String msg;
    /**
     * 随机参数
     */
    private String noncestr;
    /**
     * 汇潮 订单号
     */
    private String orderNo;
    /**
     * 支付结果 1成功
     */
    private Integer payResult;
    /**
     * 签名
     */
    private String sign;
    /**
     * 商户自定义参数,汇潮原样返回
     */
    private String id;
    /**
     * 支付宝订单号
     */
    private String aliNo;
}

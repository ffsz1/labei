package com.juxiao.xchat.manager.external.sand.vo;

import lombok.Data;

@Data
public class SandpayReciver {
    /** 商户号 */
    private String mid;
    /** 商户订单号 */
    private String orderCode;
    /**订单金额*/
    private String totalAmount; // 衫德返回格式 000000000002 = 0.02
    /**订单状态 	1-成功  3-退款失败 */
    private String orderStatus;
    /**交易流水号 */
    private String tradeNo;
    /** 应结订单金额 */
    private String settleAmount;
    /** 买家付款金额 */
    private String buyerPayAmount;
    /** 优惠金额*/
    private String discAmount;
    /** 通道支付时间*/
    private String txnCompleteTime;
    /** 支付时间*/
    private String payTime;
    /** 交易日期*/
    private String clearDate;
    /** 卡号*/
    private String accNo;
    private String accLogonNo;
    /** 商户手续费*/
    private String midFee;
    /** 额外手续费*/
    private String extraFee;
    /** 节假日手续费*/
    private String specialFee;
    /** 平台手续费*/
    private String plMidFee;
//    /** 清算日期*/
//    private String clearDate;
    /** 实际退货金额*/
    private String refundAmount;
    /** 剩余可退金额*/
    private String surplusAmount;
    /** 通道订单号*/
    private String bankserial;
    /** 渠道订单号*/
    private String payordercode;
    /** 产品编号*/
    private String externalProductCode;
    /** 卡号*/
    private String cardNo;
    /** 借贷记*/
    private String creditFlag;
    /** 签约号*/
    private String bid;
    /** 扩展域*/
    private String extend;
}

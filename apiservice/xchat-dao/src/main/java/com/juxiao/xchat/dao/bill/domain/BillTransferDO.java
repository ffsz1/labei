package com.juxiao.xchat.dao.bill.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户提现账单
 *
 * @class: BillTransferDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillTransferDO {
    private Long id;
    private Long uid;
    private Byte tranType;  // 提现类型: 1.钻石提现 2.红包提现
    private Byte realTranType;  // 提现方式: 1.微信提现 2.支付宝提现 3.银行卡
    private Double cost;
    private Integer money;
    private Byte billStatus;
    private Date createTime;
    private Date updateTime;
    private String billId;
    private String applyWithdrawalAccount;
    private String applyWithdrawalName;
    private String bankCard;
    private String bankCardName;
}

package com.juxiao.xchat.dao.bill.enumeration;

/**
 * 提现订单状态：1，新建提现记录；2，提现成功；3，提现异常
 *
 * @class: BillTransferStatus.java
 * @author: chenjunsheng
 * @date 2018/5/22
 */
public enum BillTransferStatus {
    ING(1),

    FINISH(2),

    ERROR(3);

    private byte value;

    BillTransferStatus(Integer value) {
        this.value = value.byteValue();
    }

    public byte getValue() {
        return value;
    }
}

package com.juxiao.xchat.dao.bill.enumeration;

/**
 * 提现类型
 *
 * @class: BillTransferType.java
 * @author: chenjunsheng
 * @date 2018/5/22
 */
public enum BillTransferType {
    DIAMOND(1),

    RED_PACKET(2)
    ;

    private byte value;

    BillTransferType(Integer value) {
        this.value = value.byteValue();
    }

    public byte getValue() {
        return value;
    }
}

package com.juxiao.xchat.dao.charge.enumeration;

/**
 * 支付业务的类型
 *
 * @class: ChargeRecordBussType.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public enum ChargeRecordBussType {
    CHARGE(Byte.parseByte("0"));

    private byte value;

    ChargeRecordBussType(byte value) {
        this.value = value;
    }

    public boolean compareTo(Byte value) {
        return value != null && value.byteValue() == this.value;
    }

    public byte getValue() {
        return value;
    }
}

package com.juxiao.xchat.dao.charge.enumeration;

/**
 * 充值记录状态
 *
 * @class: ChargeRecordStatus.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public enum ChargeRecordStatus {
    CREATE((byte) 1),

    FINISH((byte) 2),

    ERROR((byte) 3),

    TIMEOUT((byte) 4);

    private byte value;

    ChargeRecordStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    /**
     * @param value
     * @return
     */
    public boolean valueEquals(Byte value) {
        return value != null && value.byteValue() == this.value;
    }
}

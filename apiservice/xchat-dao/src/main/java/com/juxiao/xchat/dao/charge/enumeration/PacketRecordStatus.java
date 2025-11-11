package com.juxiao.xchat.dao.charge.enumeration;

/**
 *
 */
public enum PacketRecordStatus {

    /**
     * 创建提现申请
     */
    CREATE((byte) 1),
    /**
     * 成功提现并且已经转账
     */
    SUCCESS((byte) 2),
    /**
     * 判定为刷红包行为
     */
    ERROR((byte) 3);

    private byte value;

    PacketRecordStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

package com.juxiao.xchat.dao.user.enumeration;

/**
 * @class: UserDrawRecordStatus.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public enum UserDrawRecordStatus {
    CREATE((byte) 1),
    NONE_PRIZE((byte) 2),
    HAS_PRIZE((byte) 3);

    private byte value;

    UserDrawRecordStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public boolean statusEquals(Byte status) {
        if (status == null) {
            return false;
        }
        return this.value == status.byteValue();
    }
}

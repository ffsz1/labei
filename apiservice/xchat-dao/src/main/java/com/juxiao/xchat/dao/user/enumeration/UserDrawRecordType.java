package com.juxiao.xchat.dao.user.enumeration;

/**
 * @class: UserDrawRecordStatus.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public enum UserDrawRecordType {
    CHARGE((byte) 1),
    SHARE((byte) 2),
    WORD_DRAW((byte)3)   //文字抽奖额外奖励
    ;

    private byte value;

    UserDrawRecordType(byte value) {
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

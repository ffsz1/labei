package com.juxiao.xchat.dao.guild.enumeration;

public enum GuildHallApplyStatus {

    /**
     * 审核状态：0，审核中；1，审核拒绝；2，审核通过；3，已完成；4，已失效
     */
    AUDITING((byte) 0),

    AUDIT_FAILED((byte) 1),

    VERIFIED((byte) 2),

    JOINED((byte) 3),

    INVALID((byte) 4);

    private byte value;

    GuildHallApplyStatus(byte value) {
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

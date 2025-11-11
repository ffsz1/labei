package com.juxiao.xchat.dao.guild.enumeration;

public enum GuildHallApplyType {
    /**
     * 申请类型：0，申请加入；1，申请退出；2，逐出；
     */
    JOIN((byte) 0),

    EXIT((byte) 1),

    KICK_OUT((byte) 2);

    private byte value;

    GuildHallApplyType(byte value) {
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

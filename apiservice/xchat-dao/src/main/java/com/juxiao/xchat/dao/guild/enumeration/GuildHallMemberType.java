package com.juxiao.xchat.dao.guild.enumeration;

public enum GuildHallMemberType {
    /**
     * 成员类型：0，会长；1，厅主；2，成员；
     */
    PRESIDENT((byte) 0),

    HALL_MASTER((byte) 1),

    MEMBER((byte) 2);

    private byte value;

    GuildHallMemberType(byte value) {
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

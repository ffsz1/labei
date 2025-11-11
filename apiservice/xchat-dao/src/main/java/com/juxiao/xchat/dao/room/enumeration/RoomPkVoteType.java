package com.juxiao.xchat.dao.room.enumeration;

public enum RoomPkVoteType {

    USER_COUNT_PK((byte) 1),

    GOLD_COUNT_PK((byte) 2);

    private byte value;

    RoomPkVoteType(byte value) {
        this.value = value;
    }

    public boolean compareToValue(Byte value) {
        return value != null && value.intValue() == this.value;
    }

    public byte getValue() {
        return value;
    }

    public static boolean isSupport(Byte value) {
        if (value == null) {
            return false;
        }

        for (RoomPkVoteType type : RoomPkVoteType.values()) {
            if (type.value == value.byteValue()) {
                return true;
            }
        }
        return false;
    }

}

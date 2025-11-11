package com.juxiao.xchat.dao.user.enumeration;

/**
 * 用户红包记录
 *
 * @class: UserPacketRecordDO.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
public enum UserPacketRecordTypeEnum {

    PACKET_FOR_NEW((byte) 1, "新人红包"),

    PACKET_FOR_SHARE((byte) 2, "分享红包"),

    PACKET_FOR_INVITE((byte) 3, "邀请红包"),

    PACKET_FOR_PROFITS((byte) 4, "分成红包"),

    PACKET_FOR_DEPOSITS((byte) 5, "提现红包"),

    PACKET_FOR_SUPERIOR((byte) 6, "上级推荐人充值分成奖励");

    private byte value;

    private String desc;

    UserPacketRecordTypeEnum(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据type值进行转换
     *
     * @param type
     * @return
     * @author: chenjunsheng
     * @date 2018/6/5
     */
    public static UserPacketRecordTypeEnum typeOf(Integer type) {
        if (type == null) {
            return null;
        }

        for (UserPacketRecordTypeEnum recordType : UserPacketRecordTypeEnum.values()) {
            if (type.byteValue() == recordType.value) {
                return recordType;
            }
        }
        return null;
    }
}
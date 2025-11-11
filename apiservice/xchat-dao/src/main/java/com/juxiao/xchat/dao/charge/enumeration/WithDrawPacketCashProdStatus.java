package com.juxiao.xchat.dao.charge.enumeration;

public enum WithDrawPacketCashProdStatus {
    USING(1),
    DELETED(2);

    private int value;

    WithDrawPacketCashProdStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
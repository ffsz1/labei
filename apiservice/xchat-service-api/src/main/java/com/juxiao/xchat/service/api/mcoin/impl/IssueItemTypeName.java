package com.juxiao.xchat.service.api.mcoin.impl;

public enum IssueItemTypeName {
    PRETY_NO((byte) 1, "靓号");

    private Byte itemType;
    private String itemTypeName;

    IssueItemTypeName(byte itemType, String itemTypeName) {
        this.itemType = itemType;
        this.itemTypeName = itemTypeName;
    }


}

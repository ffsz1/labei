package com.juxiao.xchat.dao.sysconf.enumeration;

public enum AppVersionStatus {

    /**
     *
     */
    online((byte) 1),
    /**
     * 审核
     */
    audit((byte) 2),
    /**
     * 强制更新
     */
    force_update((byte) 3),
    /**
     * 推荐更新
     */
    recomm_update((byte) 4),
    /**
     *
     */
    deleted((byte) 5);

    private Byte status;

    AppVersionStatus(java.lang.Byte status) {
        this.status = status;
    }

    public Byte getStatus() {
        return status;
    }
}

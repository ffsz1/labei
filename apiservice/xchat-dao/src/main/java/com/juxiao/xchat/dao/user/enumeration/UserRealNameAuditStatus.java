package com.juxiao.xchat.dao.user.enumeration;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 15:35
 */
public enum UserRealNameAuditStatus {

    /**
     * 审核状态：0，审核中；1，审核通过；2，审核拒绝
     */
    AUDITING((byte) 0),

    VERIFIED((byte) 1),

    AUDIT_FAILED((byte) 2);

    private byte auditStatus;

    UserRealNameAuditStatus(byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public boolean checkValue(Byte auditStatus) {
        if (auditStatus == null) {
            return false;
        }

        return auditStatus == this.auditStatus;
    }

}

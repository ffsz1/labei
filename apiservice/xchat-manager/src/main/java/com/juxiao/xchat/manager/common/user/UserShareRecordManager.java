package com.juxiao.xchat.manager.common.user;

public interface UserShareRecordManager {
    /**
     * @param uid
     * @param chargeRecordProdId
     * @param chargeAmount
     */
    void saveUserBonusRecord(Long uid, Long gainBonusUid, String chargeRecordProdId, Integer chargeAmount);
}

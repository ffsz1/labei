package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.charge.ret.ReceiptRet;
import com.juxiao.xchat.service.api.charge.vo.IOSOrderPlaceVO;
import com.juxiao.xchat.service.api.charge.vo.ReceiptVO;

/**
 * iOS端发过来的购买凭证验证处理
 *
 * @class: IOSChargeService.java
 * @author: chenjunsheng
 * @date 2018/7/20
 */
public interface IOSChargeService {

    IOSOrderPlaceVO placeOrder(String ip, Long uid, String chargeProdId, Integer isJailbroken, String os) throws WebServiceException;

    /**
     * 验证苹果收据数据
     *
     * @param uid
     * @param chooseEnv
     * @param receipt
     * @return
     */
    ReceiptRet verifyReceipt(String uid, String chooseEnv, String receipt, Integer isJailbroken, String trancid, String os, String appVersion) throws WebServiceException;

    /**
     * 更新充值记录
     *
     * @param uid
     * @param chargeRecordId
     * @param receiptRet
     * @param receiptmd5
     * @return
     * @throws WebServiceException
     */
    ReceiptVO updateChargeRecord(String ip, Long uid, String chargeRecordId, ReceiptRet receiptRet, String receiptmd5, String trancid) throws WebServiceException;
}

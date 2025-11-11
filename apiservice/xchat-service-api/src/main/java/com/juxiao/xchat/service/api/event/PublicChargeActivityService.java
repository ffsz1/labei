package com.juxiao.xchat.service.api.event;


import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;

/**
 * 公用充值活动service接口
 * @author  laizhilong
 */
public interface PublicChargeActivityService {


    /**
     * 新手首充签到大礼包
     * @param uid uid
     */
    void noviceFirstChargeCheckInActivity(Long uid,int amount,String chargeId) throws Exception;


    /**
     * 根据uid获取是否满足领取条件
     * @param uid
     * @return
     */
    WebServiceMessage getNoviceFirstChargeRecord(Long uid);

    /**
     * 领取礼物
     * @param uid
     * @return
     */
    WebServiceMessage receiveActivityItem(Long uid,Integer itemId);
}

package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.domain.UserDrawDO;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.dto.UserDrawDTO;
import com.juxiao.xchat.dao.user.enumeration.UserDrawRecordType;

public interface UserDrawManager {

    /**
     * 获取抽奖机会-- 内部会对金额进行判断
     * @param uid
     * @param amount
     * @param chargeId
     */
    void saveChargeDraw(Long uid, Integer amount, String chargeId);

    /**
     * 获取抽奖机会
     * @param uid
     * @param userDrawRecordType
     * @param amount
     * @param srcObjId
     * @param srcObjName
     */
    void saveChargeDraw(Long uid, UserDrawRecordType userDrawRecordType, Integer amount, String srcObjId, String srcObjName);

    /**
     * 保存用户的新建的抽奖记录
     * @param uid
     * @param userDrawRecordType
     * @param amount
     * @param srcObjId
     * @param srcObjName
     * @return
     */
    UserDrawRecordDO saveUserDrawRecord(Long uid, UserDrawRecordType userDrawRecordType, Integer amount, String srcObjId, String srcObjName);

    /**
     * 更新用户抽奖情况
     *
     * @param drawDo
     */
    void updateUserDraw(UserDrawDO drawDo);

    /**
     * 获取用户的抽奖信息
     *
     * @param uid
     * @return
     */
    UserDrawDTO getUserDraw(Long uid);


    /**
     * 首次充值获取抽奖机会-- 内部会对金额进行判断
     * @param uid
     * @param amount
     * @param chargeId
     */
    void saveFirstChargeDraw(Long uid, Integer amount, String chargeId);
}

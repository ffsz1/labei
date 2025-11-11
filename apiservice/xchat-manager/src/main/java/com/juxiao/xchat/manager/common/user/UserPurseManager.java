package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.domain.UserPurseDO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;

import java.util.Map;

/**
 * 用户钱包通用服务
 *
 * @class: UserPurseManager.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface UserPurseManager {

    /**
     * @param purseDo
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    void save(UserPurseDO purseDo);

    /**
     * 砖石兑换金币
     *
     * @param uid
     * @param goldAmount
     * @param diamondCost
     */
    void updateAddGoldReduceDiamond(Long uid, Long goldAmount, Double diamondCost, boolean pushMsg) throws WebServiceException;

    /**
     * 增加钱包中的金币
     *
     * @param uid
     * @param goldAmount
     */
    void updateAddGold(Long uid, Long goldAmount, boolean isCharge, boolean pushMsg, String type, Map<String, Long> num, String chargeDesc) throws WebServiceException;

    /**
     * 减少钱包中的金币
     *
     * @param uid
     * @param goldCost
     */
    void updateReduceGold(Long uid, Integer goldCost, boolean pushMsg, String type, Map<String, Long> num) throws WebServiceException;

    /**
     * 只减少缓存中的金币，目前用于送礼物
     *
     * @param uid
     * @param goldCost
     */
    void reduceGoldCache(Long uid, Long goldCost, boolean pushMsg) throws WebServiceException;

    /**
     * 更新用户金币消耗和用户海螺次数增加
     *
     * @param uid
     * @param conchAmount
     * @return
     */
    void updateReduceGoldAddConch(Long uid, Long goldCost, Long conchAmount, boolean pushMsg) throws WebServiceException;


    /**
     * 更新用户海螺次数减少
     *
     * @param uid
     * @param conchCost
     * @param conchCost
     * @return
     */
    void updateReduceConch(Long uid, Integer conchCost, boolean pushMsg , String type, Map<String, Long> num) throws WebServiceException;

    void updateReduceTryCoin(Long uid, Integer tryCoinCost, boolean pushMsg , String type, Map<String, Long> num) throws WebServiceException;


    /**
     * 增加钱包中的砖石
     *
     * @param uid
     * @param diamondAmount
     */
    void updateAddDiamond(Long uid, Double diamondAmount, boolean pushMsg) throws WebServiceException;

    /**
     * 减少钱包中的钻石
     *
     * @param uid
     * @param diamondCost
     */
    void updateReduceDiamond(Long uid, Double diamondCost, boolean pushMsg) throws WebServiceException;

    /**
     * 查询用户钱包
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    UserPurseDTO getUserPurse(Long uid);
}

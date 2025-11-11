package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserPurseExchangeDTO;
import com.juxiao.xchat.service.api.charge.vo.GiveGoldVO;
import com.juxiao.xchat.service.api.charge.vo.WithDrawCashVO;

/**
 * 砖石兑换金币服务接口
 *
 * @class: DiamondExchangeService.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface DiamondExchangeService {
    /**
     * 钻石兑换成金币业务处理
     *
     * @param uid
     * @param diamondNum
     * @param os
     * @param appVersion
     * @param passwordSecond
     * @return
     * @throws WebServiceException
     */
    UserPurseExchangeDTO exchange2Gold(Long uid, Double diamondNum, String os, String appVersion, String phone,
                                       String passwordSecond) throws WebServiceException;

    /**
     * 钻石兑换金币
     *
     * @param uid         uid
     * @param exchangeNum 兑换数
     * @param type        类型 1、钻石
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    WithDrawCashVO exchangeGold(Long uid, Double exchangeNum, int type, String token) throws WebServiceException;

    /**
     * 钻石兑换金币
     *
     * @param uid         uid
     * @param exchangeNum 兑换数
     * @param type        类型 1、钻石
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    WithDrawCashVO exchangeGoldCoin(Long uid, Double exchangeNum, int type, String phone, String code, String token) throws WebServiceException;

    /**
     * 赠送金币业务处理
     *
     * @param sendUid sendUid 赠送者
     * @param recvUid recvUid 接受者
     * @param goldNum 金币赠送数量
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    GiveGoldVO userGiveGold(Long sendUid, Long recvUid, Integer goldNum, String smsCode) throws WebServiceException;

    /**
     * 金币转赠权限校验
     *
     * @param uid sendUid 用户Uid
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    boolean giveGoldCheck(Long uid) throws WebServiceException;

}

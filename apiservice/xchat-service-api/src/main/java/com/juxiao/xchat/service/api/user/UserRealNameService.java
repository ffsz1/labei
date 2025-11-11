package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.vo.UserRealNameVO;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 15:48
 */
public interface UserRealNameService {

    /**
     * 实名认证或者短信验证码
     *
     * @param uid
     * @param phone
     * @param deviceId
     * @throws Exception
     */
    void getSmsCode(Long uid, String phone, String deviceId) throws Exception;

    /**
     * 保存用户实名认证信息
     *
     * @param uid
     * @param realName
     * @param idcardNo
     * @param idcardFront
     * @param idcardOpposite
     * @param idcardHandheld
     * @throws WebServiceException
     */
    void saveUserRealName(Long uid, String realName, String idcardNo, String idcardFront, String appVersion, String idcardOpposite, String idcardHandheld) throws WebServiceException;

    /**
     * 获取用户实名认证信息
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    UserRealNameVO getUserRealName(Long uid) throws WebServiceException;
    
    /**
          * 用于兑换金币实名认证信息
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    UserRealNameVO getValidateUserRealName(Long uid) throws WebServiceException;
}

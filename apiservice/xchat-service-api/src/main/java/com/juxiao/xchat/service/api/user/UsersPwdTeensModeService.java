package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.vo.UsersTeensModeVO;

/**
 * @author chris
 * @date 2019-07-03
 */
public interface UsersPwdTeensModeService {


    /**
     * 获取青少年信息
     * @param uid uid
     * @return UsersTeensModeVO
     * @throws WebServiceException WebServiceException
     */
    UsersTeensModeVO getUsersTeensMode(Long uid) throws WebServiceException;

    /**
     * 新增或更新
     * @param uid uid
     * @param cipherCode cipherCode
     * @return WebServiceMessage
      @throws WebServiceException WebServiceException
     */
    WebServiceMessage saveOrUpdate(Long uid, String cipherCode)throws WebServiceException;

    /**
     * 校验密码
     * @param uid uid
     * @param cipherCode cipherCode
     * @return WebServiceMessage
     * @throws WebServiceException WebServiceException
     */
    WebServiceMessage checkCipherCode(Long uid, String cipherCode)throws WebServiceException;

    /**
     * 关闭青少年模式
     * @param uid uid
     * @return WebServiceMessage
     */
    WebServiceMessage closeTeensMode(Long uid)throws WebServiceException;
}

package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;

/**
 * 账户服务
 *
 * @class: AccountManager.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
public interface AccountManager {

    /**
     * 拉黑账户
     *
     */
    void block(Long uid, Integer adminId, Integer day, Integer blockType) throws Exception;

    /**
     * 强制下线
     *
     * @param uid
     * @throws Exception
     */
    void blockAccount(Long uid) throws Exception;


    void validateThirdInfo(String openId, String accessToken, int type, String app,String os) throws WebServiceException;


    WXUserInfoVO getWXUserInfo(String accessToken, String openId)throws WebServiceException;

}

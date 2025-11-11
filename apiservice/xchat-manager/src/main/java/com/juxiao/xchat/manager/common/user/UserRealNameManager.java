package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserRealNameDTO;

/**
 * 用户实名认证信息管理
 *
 * @author chris
 * @Title:
 * @date 2019-05-08 15:29
 */
public interface UserRealNameManager {
    /**
     * 保存用户实名认证信息
     *
     * @param userRealNameDTO 用户实名认证信息
     * @return
     */
    int save(UserRealNameDTO userRealNameDTO);

    /**
     * 获取用户实名认证信息
     *
     * @param uid 用户ID
     * @return
     */
    UserRealNameDTO getOneByJedisId(String uid);

    /**
     * 判断该用户是否进行了实名验证
     *
     * @param uid  用户ID
     * @param type 开关类型
     * @throws WebServiceException
     */
    void verifyUserRealName(Long uid, String type) throws WebServiceException;
}

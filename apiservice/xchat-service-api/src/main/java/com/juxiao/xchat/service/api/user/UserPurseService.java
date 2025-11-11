package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.user.vo.UserPurseVO;

/**
 * @class: UserPurseService.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserPurseService {

    /**
     * @param uid
     * @return
     * @throws WebServiceException
     */
    UserPurseVO getUserPurse(Long uid) throws WebServiceException;

    boolean queryFirst(Long uid) throws WebServiceException;

    void houseOwnerShare(String date, Long uid) throws WebServiceException;
}

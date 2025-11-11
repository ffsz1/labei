package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.vo.AccountBannedVO;

public interface AccountBannedService {

    /**
     * 检查用户是否被禁言
     * @param uid
     */
    AccountBannedVO checkBanned(Long uid) throws WebServiceException;
}

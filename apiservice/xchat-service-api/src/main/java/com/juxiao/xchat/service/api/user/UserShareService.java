package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.vo.WxappShareInfoVO;

public interface UserShareService {

    /**
     * 获取用户当天分享情况
     *
     * @param uid
     * @return
     */
    WxappShareInfoVO getUserWxappShareInfo(Long uid) throws WebServiceException;
}

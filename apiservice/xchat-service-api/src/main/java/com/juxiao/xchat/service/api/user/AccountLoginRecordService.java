package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.service.api.user.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

public interface AccountLoginRecordService {

    /**
     * 保存用户登录记录
     * @param request
     * @param user
     */
    void saveRecord(HttpServletRequest request, UserVO user);
}

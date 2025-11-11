package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.vo.UserVO;

import java.util.List;

public interface UsersRecommendService {

    /**
     * 获取推荐用户列表
     * @param size
     * @return
     */
    List<UserVO> getRecommendUsers(Long uid, Integer size) throws WebServiceException;


}

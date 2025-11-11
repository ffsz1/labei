package com.juxiao.xchat.service.api.user.draw;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawWordType;

public interface WordDrawService {

    /**
     * 执行抽字逻辑
     *
     * @param uid 用户ID
     * @return 成功返回抽中的字体
     */
    UserWordDrawWordType doDraw(Long uid) throws WebServiceException;

}

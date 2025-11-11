package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;
import com.juxiao.xchat.service.api.user.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @class: LevelServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/7/9
 */
@Service
public class LevelServiceImpl implements LevelService {

    @Autowired
    private LevelManager levelManager;

    @Override
    public LevelVO getLevelExperience(Long uid) throws WebServiceException {
        //
        return levelManager.getLevelExperience(uid);
    }


    @Override
    public LevelVO getLevelCharm(Long uid) throws WebServiceException {
        //
        return levelManager.getLevelCharm(uid);
    }
}

package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.FeedbackDao;
import com.juxiao.xchat.dao.user.domain.FeedbackDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.FeedbackService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 北岭山下 on 2017/7/18.
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private UsersManager usersManager;


    /**
     * @see com.juxiao.xchat.service.api.user.FeedbackService#saveFeedback(Long, String, String, String)
     */
    @Override
    public void saveFeedback(Long uid, String feedbackDesc, String img, String contact) throws WebServiceException {
        //输入参数检测
        if (uid == null || uid == 0L || StringUtils.isEmpty(feedbackDesc)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        //创建feedbackID
        String feedbackId = UUIDUtils.get();
        //写入feedback相关信息
        FeedbackDO feedback = new FeedbackDO();
        feedback.setFeedbackId(feedbackId);
        feedback.setUid(uid);
        feedback.setFeedbackDesc(feedbackDesc);
        feedback.setImgUrl(img);
        feedback.setContact(contact);
        feedback.setCreateTime(new Date());
        feedbackDao.save(feedback);
    }
}

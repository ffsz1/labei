package com.juxiao.xchat.service.api.user;


import com.juxiao.xchat.base.web.WebServiceException;

public interface FeedbackService {

    /**
     * 保持用户反馈信息
     *
     * @param uid
     * @param feedbackDesc
     * @param img
     * @param contact
     */
    void saveFeedback(Long uid, String feedbackDesc, String img, String contact) throws WebServiceException;
}

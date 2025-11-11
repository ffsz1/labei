package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.FeedbackService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户信息接口",description = "用户信息接口")
public class FeedBackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * @param uid          用户ID
     * @param feedbackDesc 反馈描述
     * @param img          反馈图片URL
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public WebServiceMessage appFeedback(@RequestParam("uid") Long uid,
                                         @RequestParam("feedbackDesc") String feedbackDesc,
                                         @RequestParam(value = "img", required = false) String img,
                                         @RequestParam(value = "contact", required = false) String contact) throws WebServiceException {
        feedbackService.saveFeedback(uid, feedbackDesc, img, contact);
        return WebServiceMessage.success(null);
    }
}

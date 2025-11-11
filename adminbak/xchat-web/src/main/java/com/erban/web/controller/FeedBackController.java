package com.erban.web.controller;

import com.erban.main.service.FeedbackService;
import com.erban.main.util.StringUtils;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 北岭山下 on 2017/7/18.
 */

/**
 *                      用户反馈
 */

@Controller

public class FeedBackController {

        private static final Logger LOGGER = LoggerFactory.getLogger ( FeedBackController.class );

        @Autowired
        private FeedbackService feedbackService;

        /**
         *
         * @param uid 用户ID
         * @param feedbackDesc 反馈描述
         * @param img   反馈图片URL
         * @return
         */
        @ResponseBody
        @Authorization
        @SignVerification
        @RequestMapping(value = "/feedback",method = RequestMethod.POST)
        public BusiResult appFeedback ( Long uid, String feedbackDesc, String img,String contact){

                LOGGER.info ( "用户："+uid+"发送反馈："+ feedbackDesc );
                //输入参数检测
                if( StringUtils.isEmpty ( feedbackDesc ) || uid == 0L || uid == null){
                        return new BusiResult(BusiStatus.PARAMETERILLEGAL);
                }
                BusiResult busiResult;
                busiResult = feedbackService.feedbackUpdate ( uid, feedbackDesc,img,contact);
                return busiResult;

        }


}

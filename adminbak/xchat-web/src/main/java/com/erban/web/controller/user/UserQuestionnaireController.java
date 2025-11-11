package com.erban.web.controller.user;

import com.erban.main.service.user.UserQuestionnaireService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/questionnaire")
public class UserQuestionnaireController extends BaseController {
    @Autowired
    private UserQuestionnaireService userQuestionnaireService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult get(Long uid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try{
            return userQuestionnaireService.get(uid);
        }catch (Exception e){
            logger.error("questionnaire/get error..uid=" + uid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult save(Long uid, String list) {
        if (uid == null || uid == 0L || list==null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try{
            return userQuestionnaireService.save(uid, list);
        }catch (Exception e){
            logger.error("questionnaire/save error..uid=" + uid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult draw(Long uid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try{
            return userQuestionnaireService.draw(uid);
        }catch (Exception e){
            logger.error("questionnaire/draw error..uid=" + uid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}

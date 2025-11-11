package com.erban.web.controller.drawprize;

import com.erban.main.service.drawprize.UserDrawService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/draw")
public class UserDrawController extends BaseController {

    @Autowired
    private UserDrawService userDrawService;

    /**
     * 获取我的抽奖信息
     *
     * @param uid
     **/
    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public BusiResult getUserDrawInfo(Long uid) {
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return userDrawService.getUserDrawByUid(uid);
        } catch (Exception e) {
            logger.error("/draw/get error " ,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 发起一次抽奖
     *
     * @param uid
     **/
    @ResponseBody
    @RequestMapping(value = "/do", method = RequestMethod.GET)
    public BusiResult doUserDraw(Long uid) {
        if(uid==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return userDrawService.doUserDraw(uid);
        } catch (Exception e) {
            logger.error("/draw/do error " ,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 获取已经中奖的记录，用于滚屏
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BusiResult doUserDraw() {
        try {
            return userDrawService.getUserDrawRecordListByStatus();
        } catch (Exception e) {
            logger.error("/draw/list error " ,e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}

package com.erban.web.controller.user;

import com.erban.main.service.user.UserPurseService;
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
@RequestMapping("/purse")
public class UserPurseController extends BaseController {
    @Autowired
    private UserPurseService userPurseService;

    @ResponseBody
    @Authorization
    @RequestMapping(value = "query",method = RequestMethod.GET)
    public BusiResult queryMyUserPurse(Long uid){
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        if(uid==null||uid==0L){
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        busiResult=userPurseService.queryUserPurse(uid);
        return busiResult;
    }

    @RequestMapping(value = "queryFirst",method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult queryFirst(Long uid){
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        if(uid==null||uid==0L){
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        return userPurseService.queryFirst(uid);
    }

}

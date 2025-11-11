package com.juxiao.xchat.api.controller.praise;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.praise.dto.UsersPraiseDTO;
import com.juxiao.xchat.service.api.praise.UsersPraiseService;
import com.juxiao.xchat.service.api.praise.vo.UsersPraisedVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/praise")
public class UsersPraiseController {

    @Autowired
    private UsersPraiseService usersPraiseService;

    //点赞数量及点赞状态
    @RequestMapping("/status")
    public WebServiceMessage getStatus(Long uid,Long praisedUid){
        if(uid==null||praisedUid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        UsersPraisedVo usersPraisedVo = usersPraiseService.findStatus(uid, praisedUid);
        return WebServiceMessage.success(usersPraisedVo);
    }
    //点赞
    @RequestMapping("/clickPraise")
    public WebServiceMessage doPraise(Long uid,Long praisedUid){
        if(uid==null||praisedUid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        return  usersPraiseService.doPraise(uid, praisedUid);
    }
    //点赞历史记录
    @RequestMapping("/history")
    public WebServiceMessage getHistory(Long praisedUid, PageBo pageBo){
        String message = pageBo.checkParam();
        if(message!=null) return WebServiceMessage.failure(message);
        if(praisedUid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        List<UsersPraiseDTO> history = usersPraiseService.findHistory(praisedUid, pageBo);
        return WebServiceMessage.success(history);
    }
}

package com.juxiao.xchat.api.controller.praise;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.praise.domain.Praise;
import com.juxiao.xchat.service.api.praise.PraiseService;
import com.juxiao.xchat.service.api.praise.vo.PraiseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/praise")
public class PraiseController {
    @Autowired
    private PraiseService praiseService;

    /**
     *  声友 动态话题点赞
     * @param praise
     * @return
     */
    @RequestMapping("/click")
    public WebServiceMessage clickPraise(Praise praise){
        if(praise.getUid()==null||praise.getTopicid()==null){
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        PraiseVo praiseVo = praiseService.doPraiseTopic(praise);
        return new WebServiceMessage(200,praiseVo,"点赞成功");
    }
    /**
     *  声友 评论点赞
     * @return
     */
    @RequestMapping("/comment/click")
    public WebServiceMessage clickComment(Long uid,Long commentid){
        if(uid==null||commentid==null){
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        Praise praise=new Praise();
        praise.setUid(uid);
        praise.setTopicid(commentid);
        PraiseVo praiseVo = praiseService.doPraiseComment(praise);
        return new WebServiceMessage(200,praiseVo,"点赞成功");
    }
}

package com.juxiao.xchat.api.controller.reply;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.reply.domain.Reply;
import com.juxiao.xchat.service.api.reply.ReplyService;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    /**
     * 保存回复 接口
     * @param reply
     * @return
     */
    @PostMapping("/save")
    public WebServiceMessage save(Reply reply){
        if(reply.getUid()==null||reply.getCommentid()==null|| StringUtils.isEmpty(reply.getContent())){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        reply.setId(null);
        return replyService.add(reply);
    }

    /**
     * 评论回复 接口
     * @return
     */
    @PostMapping("/incomment")
    public WebServiceMessage getbycomment(Long uid, Long commentid, PageBo pageBo){
        return replyService.getReplyByCommentid(commentid,pageBo);
    }
    //删除
    @PostMapping("/delete")
    public WebServiceMessage deletebyid(Long id){
        if(id==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR,1);
        return replyService.deleteReply(id);
    }
}

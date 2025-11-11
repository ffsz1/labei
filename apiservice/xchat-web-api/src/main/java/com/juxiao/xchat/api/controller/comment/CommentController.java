package com.juxiao.xchat.api.controller.comment;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.comment.query.CommentQuery;
import com.juxiao.xchat.service.api.comment.CommentService;
import com.juxiao.xchat.service.api.comment.bo.CommentBo;
import com.juxiao.xchat.service.api.comment.vo.CommentVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @RequestMapping("/add")
    public WebServiceMessage add(CommentBo commentBo){
        return commentService.add(commentBo);
    }

    @RequestMapping("/update")
    public WebServiceMessage update(CommentBo commentBo){
        return commentService.update(commentBo);
    }

    @RequestMapping("/delete")
    public WebServiceMessage delete(Long id){
        if(id==null) return WebServiceMessage.failure(WebServiceCode.PARAM_EXCEPTION);
        return commentService.delete(id);
    }

    @RequestMapping("/get")
    public WebServiceMessage get(Long id,Long uid){
        if(id==null||uid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_EXCEPTION);
        return commentService.getComment(id,uid);
    }


    @RequestMapping("/listPage")
    public WebServiceMessage listPage(CommentQuery commentQuery, PageBo pageBo, Integer isorder,Long uid){
        if(isorder==null) isorder=0;
        if(commentQuery.getTopicid()==null||uid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        List<CommentVo> comments = commentService.listComment(commentQuery, pageBo, isorder,uid);
        return WebServiceMessage.success(comments);
    }

}

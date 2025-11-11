package com.juxiao.xchat.service.api.reply.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.comment.CommentDao;
import com.juxiao.xchat.dao.reply.ReplyDao;
import com.juxiao.xchat.dao.reply.domain.Reply;
import com.juxiao.xchat.dao.reply.dto.ReplyDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.service.api.reply.ReplyService;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private CommentDao commentDao;

    @Transactional
    @Override
    public WebServiceMessage add(Reply reply) {
        if(reply.getCommentid()==null||reply.getUid()==null||StringUtils.isEmpty(reply.getContent())){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        Date date=new Date();
        reply.setCreatetime(date);
        reply.setUpdatetime(date);
        replyDao.insertSelective(reply);
        commentDao.upReplynum(reply.getCommentid(),1);
        return WebServiceMessage.success(null);
    }

    @Override
    public WebServiceMessage getReplyByCommentid(Long commentid,PageBo pageBo) {
        if(commentid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR,2);
        String s = pageBo.checkParam();
        if(s!=null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR,2);
        Page page = PageBo.getPage(pageBo);
        List<ReplyDTO> replyDTOS = replyDao.selectByCommentId(commentid,page);
        return WebServiceMessage.success(replyDTOS);
    }
    @Override
    @Transactional
    public WebServiceMessage deleteReply(Long id){
        Reply reply = replyDao.selectByPrimaryKey(id);
        if(reply!=null) {
            commentDao.upReplynum(reply.getCommentid(),-1);
            replyDao.deleteByPrimaryKey(id);
        }
        return WebServiceMessage.success(null);
    }
}

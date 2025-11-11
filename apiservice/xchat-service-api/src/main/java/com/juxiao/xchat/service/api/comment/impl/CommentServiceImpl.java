package com.juxiao.xchat.service.api.comment.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.comment.CommentDao;
import com.juxiao.xchat.dao.comment.domain.Comment;
import com.juxiao.xchat.dao.comment.dto.CommentDTO;
import com.juxiao.xchat.dao.comment.query.CommentQuery;
import com.juxiao.xchat.dao.praise.PraiseDao;
import com.juxiao.xchat.dao.praise.domain.Praise;
import com.juxiao.xchat.dao.reply.ReplyDao;
import com.juxiao.xchat.dao.reply.dto.ReplyDTO;
import com.juxiao.xchat.dao.topic.TrendTopicDao;
import com.juxiao.xchat.dao.topic.domain.TrendTopic;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.service.api.comment.CommentService;
import com.juxiao.xchat.service.api.comment.bo.CommentBo;
import com.juxiao.xchat.service.api.comment.vo.CommentVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private TrendTopicDao trendTopicDao;

    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private PraiseDao praiseDao;

    //添加评论
    @Transactional
    @Override
    public WebServiceMessage add(CommentBo commentBo) {
        String message = checkParam(commentBo);
        if(message!=null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        Comment comment = commentBo.getComments();
        Date date=new Date();
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        commentDao.insertSelective(comment);
        trendTopicDao.upByComment(commentBo.getTopicid());
        trendTopicDao.updateCallTime(commentBo.getTopicid(),date);
        //TrendTopic trendTopic = trendTopicDao.selectByPrimaryKey(commentBo.getTopicid());
        return WebServiceMessage.success(null);
    }

    //修改评论
    @Override
    public WebServiceMessage update(CommentBo commentBo) {
        if(commentBo.getId()==null){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }else{
            Comment comment = commentDao.selectByPrimaryKey(commentBo.getId());
            if(comment==null) return WebServiceMessage.failure(WebServiceCode.PARAM_EXCEPTION);
        }
        if( checkParam(commentBo)!=null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        Comment comment = commentBo.getComments();
        Date date=new Date();
        comment.setUpdateTime(date);
        commentDao.updateByPrimaryKeySelective(comment);
        return WebServiceMessage.success(null);
    }

    //删除评论
    @Transactional
    @Override
    public WebServiceMessage delete(Long id) {
        Comment comment = commentDao.selectByPrimaryKey(id);
        if(comment==null) return WebServiceMessage.failure(WebServiceCode.PARAM_EXCEPTION);
        int i = commentDao.deleteByPrimaryKey(id);
        trendTopicDao.deByComment(comment.getTopicid());
        replyDao.deleteByCommentId(id);
        return WebServiceMessage.success(null);
    }

    //查询单条评论
    @Override
    public WebServiceMessage getComment(Long id,Long uid) {
        if(id==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        CommentDTO commentDTO = commentDao.selectById(id);
        if(commentDTO==null) return new WebServiceMessage(1044,"不存在");
        CommentVo commentVo = formatCommentVoOne(commentDTO, uid);
        return WebServiceMessage.success(commentVo);
    }

    //分页查找评论 无排序
    @Override
    public List<CommentVo> listComment(CommentQuery commentQuery, PageBo pageBo,Long uid) {
        Page page = PageBo.getPage(pageBo);
        List<CommentDTO> comments = commentDao.listComment(commentQuery, page);
        return  formatCommentVo(comments,uid);
    }
    //分页查找评论 是否时间排序 升序
    @Override
    public List<CommentVo> listComment(CommentQuery commentQuery, PageBo pageBo,int isorder,Long uid) {
        // Long size = commentDao.getSize(commentQuery);
        // if(size==0) return new ArrayList<Comment>();
        Page page = PageBo.getPage(pageBo);
        List<CommentDTO> comments = commentDao.listCommentOrderByCTime(commentQuery, page ,isorder);
        return formatCommentVo(comments,uid);
    }

    public CommentVo formatCommentVoOne(CommentDTO comementDTO,Long uid){
        CommentVo commentVo=new CommentVo(comementDTO);
        PageBo pageBo=new PageBo();
        pageBo.setPageNum(1L);//第一页
        pageBo.setPageSize(5L);//每页5条
        Page page = PageBo.getPage(pageBo);
        List<ReplyDTO> replyDTOS = replyDao.selectByCommentId(commentVo.getId(), page);
        commentVo.setReplys(replyDTOS);
        if(replyDTOS.size()<5){
            commentVo.setHasnext(0);
        }else {
            commentVo.setHasnext(1);
        }
        Byte type=2;
        Praise praise = praiseDao.selectPraiseType(uid, comementDTO.getId(), type);
        if(praise==null){
            commentVo.setIsPraise(0);
        }else{
            commentVo.setIsPraise(1);
        }
        return commentVo;
    }

    public List<CommentVo> formatCommentVo(List<CommentDTO> comementDTO,Long uid){
        List<CommentVo> list=new ArrayList<CommentVo>();
        for(CommentDTO commentDTO:comementDTO){
            CommentVo commentVo = formatCommentVoOne(commentDTO,uid);
            list.add(commentVo);
        }
        return list;
    }

    //参数检验
    private String checkParam(CommentBo commentBo){
        if(commentBo.getUid()!=null) {
            UsersDTO user = usersDao.getUser(commentBo.getUid());
            if (user == null) return "不存在用户";
        }else{
            return "uid不能为空";
        }
        if(commentBo.getTopicid()!=null){
            TrendTopic trendTopic = trendTopicDao.selectByPrimaryKey(commentBo.getTopicid());
            if(trendTopic==null) return "不存在话题";
        }else{
            return "话题不能为空";
        }
        if(StringUtils.isEmpty(commentBo.getComment())){
            return "评论不能为空";
        }
        return null;
    }

}

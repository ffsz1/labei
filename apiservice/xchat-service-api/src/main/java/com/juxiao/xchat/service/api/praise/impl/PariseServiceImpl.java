package com.juxiao.xchat.service.api.praise.impl;

import com.juxiao.xchat.dao.comment.CommentDao;
import com.juxiao.xchat.dao.comment.domain.Comment;
import com.juxiao.xchat.dao.praise.PraiseDao;
import com.juxiao.xchat.dao.topic.TrendTopicDao;
import com.juxiao.xchat.dao.praise.domain.Praise;
import com.juxiao.xchat.dao.topic.domain.TrendTopic;
import com.juxiao.xchat.service.api.praise.PraiseService;
import com.juxiao.xchat.service.api.praise.vo.PraiseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PariseServiceImpl implements PraiseService {
    @Autowired
    private TrendTopicDao trendTopicDao;
    @Autowired
    private PraiseDao praiseDao;

    @Autowired
    private CommentDao commentDao;
    @Override
    @Transactional
    public PraiseVo doPraiseTopic(Praise praise) {
        if(praise.getUid()==null||praise.getTopicid()==null) return null;
        Praise praise1 = praiseDao.selectPraise(praise.getUid(), praise.getTopicid());
        PraiseVo praiseVo=new PraiseVo();
        if(praise1==null){
            //无点赞记录
            Date date=new Date();
            praise.setCreateTime(date);
            praise.setType((byte)1);
            praiseDao.insertSelect(praise);
            trendTopicDao.upByPraise(praise.getTopicid());
            trendTopicDao.updateCallTime(praise.getTopicid(),date);
            //设置状态为已经点赞
            praiseVo.setIsPraise(1);
        }else{
            //有点赞记录
            praiseDao.delete( praise.getUid(),praise.getTopicid());
            trendTopicDao.deByPraise(praise.getTopicid());
            //设置状态为未点赞
            praiseVo.setIsPraise(0);
        }
        TrendTopic trendTopic = trendTopicDao.selectByPrimaryKey(praise.getTopicid());
        praiseVo.setPraisenum(trendTopic.getPraisenum());
        return praiseVo;
    }
    @Transactional
    @Override
    public PraiseVo doPraiseComment(Praise praise) {
        if(praise.getUid()==null||praise.getTopicid()==null) return null;
        Byte type=2;
        Praise praise1 = praiseDao.selectPraiseType(praise.getUid(), praise.getTopicid(),type);
        PraiseVo praiseVo=new PraiseVo();
        if(praise1==null){
            //无点赞记录
            Date date=new Date();
            praise.setCreateTime(date);
            praise.setType(type);
            praiseDao.insertSelect(praise);
            commentDao.upPraisenum(praise.getTopicid(),1);
            //设置状态为已经点赞
            praiseVo.setIsPraise(1);
        }else{
            //有点赞记录
            praiseDao.deleteType( praise.getUid(),praise.getTopicid(),type);
            commentDao.upPraisenum(praise.getTopicid(),-1);
            //设置状态为未点赞
            praiseVo.setIsPraise(0);
        }
        Comment comment = commentDao.selectByPrimaryKey(praise.getTopicid());
        praiseVo.setPraisenum(comment.getPraisenum());
        return praiseVo;
    }
}

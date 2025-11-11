package com.juxiao.xchat.service.api.topic.impl;

import com.juxiao.xchat.dao.theme.ThemeDao;
import com.juxiao.xchat.dao.praise.PraiseDao;
import com.juxiao.xchat.dao.topic.TrendTopicDao;
import com.juxiao.xchat.dao.praise.domain.Praise;
import com.juxiao.xchat.dao.topic.domain.TrendTopic;
import com.juxiao.xchat.dao.topic.dto.TrendTopicDTO;
import com.juxiao.xchat.dao.topic.query.TrendTopicQuery;
import com.juxiao.xchat.dao.user.FansDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.api.topic.TrendTopicService;
import com.juxiao.xchat.service.api.topic.bo.TrendTopicBo;
import com.juxiao.xchat.service.api.topic.vo.TrendTopicVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TrendTopicServiceImpl implements TrendTopicService {

    @Autowired
    private TrendTopicDao trendTopicDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private FansDao fansDao;
    @Autowired
    private PraiseDao praiseDao;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Override
    public String add(TrendTopicBo trendTopicBo) {

        String message=checkParam(trendTopicBo);
        if(message!=null) return message;
        UsersDTO user = usersDao.getUser(trendTopicBo.getUid());
        if(user==null) return "用户不存在";
        TrendTopic trendTopic = trendTopicBo.getTopic();

        trendTopic.setGender(user.getGender());
        trendTopic.setErbanNo(user.getErbanNo());
        trendTopic.setNick(user.getNick());
        if(trendTopicBo.getThemeid()!=null){
            themeDao.addNum(trendTopicBo.getThemeid());
        }
        Date date=new Date();
        trendTopic.setCreateTime(date);
        trendTopic.setUpdateTime(date);
        trendTopicDao.insertSelective(trendTopic);
        //发送消息
        asyncNetEaseTrigger.sendMsg(String.valueOf(trendTopicBo.getUid()),"您发布的动态正在加速审核中,请耐心等待~");
        return null;
    }

    @Override
    public List<TrendTopicVo> listTopic(TrendTopicQuery trendTopicQuery, PageBo pageBo) {
        Long uid = trendTopicQuery.getUid();
        if(uid==null){
            return new ArrayList<TrendTopicVo>();
        }
        Long size = trendTopicDao.getSize(trendTopicQuery);
        if(size==0) return new ArrayList<TrendTopicVo>();
        Page page = PageBo.getPage(pageBo);
        List<TrendTopicDTO> trendTopics = trendTopicDao.listTopic(trendTopicQuery, page);
        List<TrendTopicVo> trendTopicVos = formatTrendTopicVo(trendTopics, uid);
        //PageInfo<TrendTopic> pageInfo=new PageInfo<>(pageBo,size, trendTopics);
        return trendTopicVos;
    }

    private List<TrendTopicVo> formatTrendTopicVo(List<TrendTopicDTO> trendTopics,Long uid){
        List<TrendTopicVo> trendTopicVos=new ArrayList<TrendTopicVo>();
        for(TrendTopicDTO trendTopicDTO:trendTopics){
            TrendTopicVo trendTopicVo=new TrendTopicVo(trendTopicDTO);
/*            //填充theme Name
            if(trendTopicDTO.getThemeid()!=null) {
                Theme theme = themeDao.selectByPrimaryKey(trendTopicDTO.getThemeid());
                trendTopicVo.setThemeName(theme.getName());
            }*/

         //   FansDTO fans = fansDao.getUserLike(uid, trendTopicDTO.getUid());
/*            //填充是否可评论
            if(trendTopicDTO.getPermissionType()==1){
                trendTopicVo.setCanComment(1);
            }else if(trendTopicDTO.getPermissionType()==2){
                if(fans==null){
                    trendTopicVo.setCanComment(0);
                }else if(fansDao.getUserLike(trendTopicDTO.getUid(), uid)==null){
                    trendTopicVo.setCanComment(0);
                }else{
                    trendTopicVo.setCanComment(1);
                }
            }else if(trendTopicDTO.getPermissionType()==3){
                trendTopicVo.setCanComment(0);
            }*/
            //填充是否关注
            //发帖的人是自己
            if(uid.equals(trendTopicDTO.getUid())){
                trendTopicVo.setIsFans(2);
            }else if(fansDao.getUserLike(uid, trendTopicDTO.getUid())!=null){
                //已关注
                trendTopicVo.setIsFans(1);
            }else{
                //未关注
                trendTopicVo.setIsFans(0);
            }
            //填充是否已点赞
            Praise praise = praiseDao.selectPraise(uid, trendTopicDTO.getId());
            if(praise==null){
                trendTopicVo.setIsPraise(0);
            }else{
                trendTopicVo.setIsPraise(1);
            }
            //加到list
            trendTopicVos.add(trendTopicVo);
        }
        return trendTopicVos;
    }
    private TrendTopicVo formatTrendTopicVoOne(TrendTopicDTO trendTopic,Long uid){
            TrendTopicVo trendTopicVo=new TrendTopicVo(trendTopic);
            if(uid.equals(trendTopic.getUid())){
                trendTopicVo.setIsFans(2);
            }else if(fansDao.getUserLike(uid, trendTopic.getUid())!=null){
                //已关注
                trendTopicVo.setIsFans(1);
            }else{
                //未关注
                trendTopicVo.setIsFans(0);
            }
            //填充是否已点赞
            Praise praise = praiseDao.selectPraise(uid, trendTopic.getId());
            if(praise==null){
                trendTopicVo.setIsPraise(0);
            }else{
                trendTopicVo.setIsPraise(1);
            }
        return trendTopicVo;
    }
    @Override
    public List<TrendTopicVo> topTopic(Long bottom, Long num,Long uid) {

        if(bottom==null||num==null||uid==null) return null;
        List<TrendTopicDTO> trendTopics = trendTopicDao.topTopic(bottom, num);
        List<TrendTopicVo> trendTopicVos = formatTrendTopicVo(trendTopics, uid);
        return trendTopicVos;
    }

    @Override
    public List<TrendTopicVo> topNew(Long uid,PageBo pageBo) {
        Page page = PageBo.getPage(pageBo);
        List<TrendTopicDTO> trendTopics = trendTopicDao.topNew(uid,page);
        List<TrendTopicVo> trendTopicVos = formatTrendTopicVo(trendTopics, uid);
        return trendTopicVos;
    }
    @Override
    public List<TrendTopicVo> recommend(Long uid, PageBo pageBo){
        Page page = PageBo.getPage(pageBo);
        List<TrendTopicDTO> trendTopics = trendTopicDao.recommend(uid,page);
        List<TrendTopicVo> trendTopicVos = formatTrendTopicVo(trendTopics, uid);
        return trendTopicVos;
    }
    @Override
    public List<TrendTopicVo> likedTopic(Long uid, PageBo pageBo) {
        Page page = PageBo.getPage(pageBo);
        List<TrendTopicDTO> trendTopics = trendTopicDao.likedTopic(uid, page);
        List<TrendTopicVo> trendTopicVos = formatTrendTopicVo(trendTopics, uid);
        return trendTopicVos;
    }
    @Override
    public TrendTopicVo get(Long id,Long uid){
        if(id==null) return null;
        TrendTopicDTO trendTopicDTO= trendTopicDao.getById(id,uid);
        return formatTrendTopicVoOne(trendTopicDTO,uid);
    }

    private String checkParam(TrendTopicBo trendTopicBo){
       // if(trendTopicBo.getName()==null) return "内容不能为空";
/*        if(trendTopicBo.getThemeid()!=null) {
            Theme theme = themeDao.selectByPrimaryKey(trendTopicBo.getThemeid());
            if(theme==null) return "主题不存在";
        }else{
            return "themeid不能为空";
        }*/
        if(trendTopicBo.getUid()==null) {
            return "用户id不能为空";
        }
        return null;
    }

}

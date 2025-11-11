package com.juxiao.xchat.service.api.praise.impl;

import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.praise.UsersPraiseDao;
import com.juxiao.xchat.dao.praise.domain.UsersPraise;
import com.juxiao.xchat.dao.praise.dto.UsersPraiseDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.service.api.praise.UsersPraiseService;
import com.juxiao.xchat.service.api.praise.vo.UsersPraisedVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class UsersPraiseServiceImpl implements UsersPraiseService {

    @Autowired
    private UsersPraiseDao usersPraiseDao;
    @Override
    public WebServiceMessage doPraise(Long uid, Long praisedUid) {
        //不能给自己点赞
        if(uid.equals(praisedUid)) return new WebServiceMessage(505,"不能给自己点赞哦");
        Date now=new Date();
        UsersPraise praise = findStatus(uid, praisedUid, now);
        if(praise==null){
            //今天点赞不存在,插入一条记录
            praise=new UsersPraise();
            praise.setCreateDate(now);
            praise.setCreateTime(now);
            praise.setUid(uid);
            praise.setPraisedUid(praisedUid);
            //todo delete redis size
            usersPraiseDao.insertSelective(praise);
            UsersPraisedVo usersPraisedVo=new UsersPraisedVo();
            Long size = usersPraiseDao.getSize(praisedUid);
            usersPraisedVo.setPraiseNum(size);
            usersPraisedVo.setStatus(1);
            return WebServiceMessage.success(usersPraisedVo);
        }else{
            //今天点赞存在,返回错误提示
            return new WebServiceMessage(506,"一天只能点赞一次哦");
        }
    }

    public UsersPraise findStatus(Long uid,Long praisedUid,Date now){
        //todo redis with outime
        String stingDate = DateUtils.dateFormat(now, "yyyy-MM-dd");
        UsersPraise praise = usersPraiseDao.findPraised(uid, praisedUid, stingDate);
        return praise;
    }

    public UsersPraisedVo findStatus(Long uid, Long praisedUid){
        Date now=new Date();
        UsersPraise  praise= findStatus(uid, praisedUid, now);
        UsersPraisedVo usersPraisedVo=new UsersPraisedVo();
        if(praise==null){
            usersPraisedVo.setStatus(0);
        }
        else{
            usersPraisedVo.setStatus(1);
        }
        //todo redis size
        Long size = usersPraiseDao.getSize(praisedUid);
        usersPraisedVo.setPraiseNum(size);
        return usersPraisedVo;
    }

    public List<UsersPraiseDTO> findHistory(Long praisedUid, PageBo pageBo){
        //todo redis size
        Long size = usersPraiseDao.getSize(praisedUid);
        Page page = PageBo.getPage(pageBo);
        List<UsersPraiseDTO> usersPraiseDTOS = usersPraiseDao.selectByPraisedUid(praisedUid, page);
        return usersPraiseDTOS;
    }
}

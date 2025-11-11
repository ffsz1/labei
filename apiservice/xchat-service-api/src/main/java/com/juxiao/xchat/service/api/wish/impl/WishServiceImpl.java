package com.juxiao.xchat.service.api.wish.impl;


import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.wish.WishDao;
import com.juxiao.xchat.dao.wish.WishLabelDao;
import com.juxiao.xchat.dao.wish.domain.Wish;
import com.juxiao.xchat.dao.wish.dto.WishDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.dao.wish.query.WishQuery;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.service.api.wish.WishService;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.bo.WishBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;
import com.juxiao.xchat.service.api.wish.vo.WishVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.stream.Collectors;

@Service
public class WishServiceImpl implements WishService {

     private Logger logger=LoggerFactory.getLogger(WishServiceImpl.class);

    @Autowired
    private WishDao wishDao;
    @Autowired
    private UsersDao usersDao;

    @Autowired
    private SystemConf systemConf;

    @Autowired
    private ExecutorService pool;
    @Autowired
    private WishLabelDao wishLabelDao;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Override
    @Transactional
    public int save(WishBo wishBo) {
        //不存在用户直接返回
        UsersDTO user = usersDao.getUser(wishBo.getUid());
        if(user==null){
            return 0;
        }
        Wish wish = wishBo.getWish();
        //信息切入
        Date now=new Date();
        Wish wishById = wishDao.getById(wishBo.getUid());
        if(wishById!=null){
            //存在记录 先清除中间表，去除标签绑定
            wishLabelDao.deleteByUid(wishBo.getUid());
            wish.setCreateTime(wishById.getCreateTime());
        }else{
            //不存在
            wish.setCreateTime(now);
        }
        wish.setUpdateTime(now);
        //插入或更新
        wishDao.insertWish(wish);
        List<Integer> personalLabels = wishBo.getPersonalLabels();
        List<Integer> meetLabels = wishBo.getMeetLabels();
        if(personalLabels!=null&&!personalLabels.isEmpty()) {
            wishLabelDao.insertWishLabels(wish.getUid(), personalLabels);
        }
        if(meetLabels!=null&&!meetLabels.isEmpty()) {
            wishLabelDao.insertWishLabels(wish.getUid(), meetLabels);
        }


        //发愿望消息线程
        pool.execute(() -> {
            List<Long> uids=new ArrayList<>();
            String litMessage=null;
            if(user.getGender()==1){
                uids = wishDao.listUidsByRecords(1);
                litMessage="男神";
            }else if(user.getGender()==2){
                uids= wishDao.listUidsByRecords(2);
                litMessage="女神";
            }
            List<String> plabels = wishLabelDao.listLabelName(user.getUid(), "1");
            List<String> mlabels = wishLabelDao.listLabelName(wish.getUid(), "2");
            StringBuilder messageBuilder=new StringBuilder(litMessage);
            messageBuilder.append(user.getNick());
            messageBuilder.append("希望找一个");
            messageBuilder.append(mlabels.toString());
            messageBuilder.append("的朋友");
            for(int i=0,j=20;i<uids.size();j=j+20,i=i+20){
                List<Long> uidCut=uids.subList(i,j);
                //发送消息
                WishVo wishVo = new WishVo(wish);
                wishVo.setMeetLabels(mlabels);
                wishVo.setPersonalLabels(plabels);
                Map<String, Object> data = Maps.newHashMap();
                data.put("userVo", user);
                data.put("wishVo", wishVo);
                List<String> uidss = uidCut.stream().map(x -> x + "").collect(Collectors.toList());

                NeteaseBatchMsgBO msgBO = new NeteaseBatchMsgBO();
                msgBO.setFromAccid(systemConf.getLikeMsgUid());
                msgBO.setToAccids(uidss);
                msgBO.setType(100);
                msgBO.setPushcontent(user.getNick() + messageBuilder.toString());
                msgBO.setBody(new Body(DefMsgType.wishMsg, DefMsgType.wishTip, data));
                msgBO.setPayload(new Payload(Payload.SkipType.APPPAGE, user));
                neteaseMsgManager.sendBatchMsg(msgBO);
            }
            /* for(Long uid:uids) {
                asyncNetEaseTrigger.sendMsg("200010025", messageBuilder.toString());
            }*/

        });

        return 1;
    }

    @Override
    public PageInfo<WishDTO> listWish(WishQuery wishQuery, PageBo pageBo) {
        Long size = wishDao.getSize(wishQuery);
        if(size==0) return new PageInfo<WishDTO>(pageBo);
        Page page = PageBo.getPage(pageBo);
        List<WishDTO> wishes = wishDao.listWish(wishQuery, page);
        PageInfo<WishDTO> pageInfo=new PageInfo<WishDTO>(pageBo,size,wishes);
        return pageInfo;
    }
    @Override
    public List<WishVo> listWish2(WishQuery wishQuery, PageBo pageBo) {
        //Long size = wishDao.getSize(wishQuery);
        //if(size==0) return new ArrayList<WishVo>();
        Page page = PageBo.getPage(pageBo);
        List<WishDTO> wishes = wishDao.listWish(wishQuery, page);
        List<WishVo> wishVos=new ArrayList<WishVo>();
        for(WishDTO wish:wishes){
            WishVo wishVo = new WishVo(wish);
            List<String> pensonalLabels = wishLabelDao.listLabelName(wish.getUid(), "1");
            List<String> meetsLabels = wishLabelDao.listLabelName(wish.getUid(), "2");
            wishVo.setMeetLabels(meetsLabels);
            wishVo.setPersonalLabels(pensonalLabels);
            wishVos.add(wishVo);
        }
        //PageInfo<WishVo> pageInfo=new PageInfo<WishVo>(pageBo,size,wishVos);
        return wishVos;
    }

    public WebServiceMessage getWish(Long uid){

        if(uid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        UsersDTO user = usersDao.getUser(uid);
        if(user==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        Wish wish = wishDao.getById(uid);
        if(wish==null) return new WebServiceMessage(1044,"心愿不存在！");
        WishVo wishVo = new WishVo(wish);
        wishVo.setAvatar(user.getAvatar());
        wishVo.setErbanNo(user.getErbanNo());
        wishVo.setNick(user.getNick());
        wishVo.setGender(user.getGender());
        List<String> pensonalLabels = wishLabelDao.listLabelName(uid, "1");
        List<String> meetsLabels = wishLabelDao.listLabelName(uid, "2");
        wishVo.setMeetLabels(meetsLabels);
        wishVo.setPersonalLabels(pensonalLabels);
        return WebServiceMessage.success(wishVo);
    }


}

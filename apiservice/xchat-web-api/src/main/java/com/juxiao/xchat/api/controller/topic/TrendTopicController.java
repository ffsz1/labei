package com.juxiao.xchat.api.controller.topic;


import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.topic.query.TrendTopicQuery;
import com.juxiao.xchat.service.api.topic.TrendTopicService;
import com.juxiao.xchat.service.api.topic.bo.TrendTopicBo;
import com.juxiao.xchat.service.api.topic.vo.TrendTopicVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/topic")
public class TrendTopicController {
    @Autowired
    private TrendTopicService trendTopicService;

    @RequestMapping("/add")
    public WebServiceMessage add(TrendTopicBo trendTopicBo){
        String result = trendTopicService.add(trendTopicBo);
        if(result!=null) return new WebServiceMessage(505,result);
        return new WebServiceMessage(200,result,"保存成功");
    }

    @RequestMapping("/get")
    public WebServiceMessage get(Long uid,Long id){
        if(uid==null||id==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        TrendTopicVo trendTopicVo = trendTopicService.get(id, uid);
        return WebServiceMessage.success(trendTopicVo);
    }

    @RequestMapping("/listPage")
    public WebServiceMessage listPage(TrendTopicQuery trendTopicQuery, PageBo pageBo){
        List<TrendTopicVo> trendTopics = trendTopicService.listTopic(trendTopicQuery, pageBo);
        return WebServiceMessage.success(trendTopics);
    }

    @RequestMapping("/topnew")
    public WebServiceMessage topnew(Long uid,PageBo pageBo){
        String message = pageBo.checkParam();
        if(message!=null) return WebServiceMessage.failure(message);
        if(uid==null) return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        List<TrendTopicVo> trendTopicVos = trendTopicService.topNew(uid, pageBo);
        return WebServiceMessage.success(trendTopicVos);
    }

    @RequestMapping("/likedTopic")
    public WebServiceMessage likedTopic(Long uid,PageBo pageBo){
        if(uid==null) return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        List<TrendTopicVo> trendTopicVos = trendTopicService.likedTopic(uid, pageBo);
        return new WebServiceMessage(200,trendTopicVos,"查询成功");
    }
    @RequestMapping("/recommend")
    public WebServiceMessage recommend(Long uid,PageBo pageBo){
        String message = pageBo.checkParam();
        if(message!=null) return WebServiceMessage.failure(message);
        if(uid==null) return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        List<TrendTopicVo> trendTopicVos = trendTopicService.recommend(uid, pageBo);
        return WebServiceMessage.success(trendTopicVos);
    }
}

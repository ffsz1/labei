package com.juxiao.xchat.service.api.topic;

import com.juxiao.xchat.dao.topic.domain.TrendTopic;
import com.juxiao.xchat.dao.topic.dto.TrendTopicDTO;
import com.juxiao.xchat.dao.topic.query.TrendTopicQuery;
import com.juxiao.xchat.service.api.topic.bo.TrendTopicBo;
import com.juxiao.xchat.service.api.topic.vo.TrendTopicVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;

import java.util.List;

public interface TrendTopicService {
    /**
     * 增加话题
     * @param trendTopicBo
     * @return
     */
    String add(TrendTopicBo trendTopicBo);

    /**
     * 分页查询话题
     * @param trendTopicQuery 查询条件
     * @param pageBo 分页条件
     * @return 页面数据
     */
    List<TrendTopicVo> listTopic(TrendTopicQuery trendTopicQuery, PageBo pageBo);

    /**
     * 查询热度最高 评论数字段降序
     * @param bottom 数据起始位置
     * @param num 数据条数
     * @return 前num条数据
     */
    List<TrendTopicVo> topTopic(Long bottom, Long num,Long uid);

    /**
     * 查询最新
     * @return
     */
    List<TrendTopicVo> topNew(Long uid,PageBo pageBo);

    /**
     *  查询关注的用户的发帖列表
     * @param uid 用户uid
     * @param pageBo 分页条件
     * @return 页面数据
     */
    List<TrendTopicVo> likedTopic(Long uid, PageBo pageBo);

    /**
     * 查询 推荐记录 根据最近点赞或评论的时间
     * @param uid
     * @param pageBo
     * @return
     */
    List<TrendTopicVo> recommend(Long uid, PageBo pageBo);

    /**
     * 查询 单条的动态记录
     * @param id  动态id
     * @param uid 当前用户id
     * @return
     */
    TrendTopicVo get(Long id,Long uid);
}

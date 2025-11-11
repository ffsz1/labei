package com.juxiao.xchat.dao.topic;

import com.juxiao.xchat.dao.topic.domain.TrendTopic;
import com.juxiao.xchat.dao.topic.dto.TrendTopicDTO;
import com.juxiao.xchat.dao.topic.query.TrendTopicQuery;
import com.juxiao.xchat.dao.wish.query.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TrendTopicDao {

    int deleteByPrimaryKey(Long id);

    int insert(TrendTopic record);

    int insertSelective(TrendTopic record);

    TrendTopic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrendTopic record);

    int updateByPrimaryKey(TrendTopic record);

    /**
     * 分页查询话题
     * @param trendTopicQuery 查询条件
     * @param page 分页条件
     * @return 页面数据
     */
    List<TrendTopicDTO> listTopic(@Param("query") TrendTopicQuery trendTopicQuery, @Param("page") Page page);

    /**
     *  获取符合条件的总数
     * @param trendTopicQuery 查询条件
     * @return 总数
     */
    Long getSize(@Param("query") TrendTopicQuery trendTopicQuery);

    /**
     * 热度查询
     * @param bottom
     * @param num
     * @return
     */
    List<TrendTopicDTO> topTopic(@Param("bottom") Long bottom, @Param("num") Long num);

    /**
     * 最新查询
     * @return
     */
    List<TrendTopicDTO> topNew(@Param("uid")Long uid , @Param("page") Page page);

    /**
     * 评论数加一
     * @param topicid
     */
    void upByComment(Long topicid);

    /**
     * 评论数减一
     * @param topicid
     */
    void deByComment(Long topicid);

    /**
     * 访问数加一
     * @param topicid
     */
    void upByVisis(Long topicid);

    /**
     *  查询 关注的人分享的动态
     * @param page
     * @return
     */
    List<TrendTopicDTO> likedTopic(@Param("uid")Long uid,@Param("page") Page page);


    /**
     * 点赞数加一
     * @param topicid
     */
    void upByPraise(Long topicid);
    /**
     * 点赞数减一
     * @param topicid
     */
    void deByPraise(Long topicid);

    /**
     * 推荐 根据 最近点赞和评论 排序
     * @return
     */
    List<TrendTopicDTO> recommend(@Param("uid")Long uid , @Param("page") Page page);

    /**
     * 更新 callTime 点赞评论 更新
     * @return
     */
    int updateCallTime(@Param("topicid")Long topicid , @Param("callTime") Date callTime);

    /**
     * 查询单条动态
     * @param id
     * @param uid
     * @return
     */
    TrendTopicDTO getById(@Param("id")Long id,@Param("uid")Long uid);
}
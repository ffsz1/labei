package com.juxiao.xchat.dao.praise;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.praise.domain.Praise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 声友点赞接口
 * author :limself
 * date : 2020-09-25
 */
@Mapper
public interface PraiseDao {
    /**
     * 新增点赞记录
     * @param praise
     */
    @TargetDataSource
    void insertSelect(Praise praise);

    /**
     * 取消点赞 删除点赞记录 声友(type=1)
     * @param uid
     * @param topicid
     */
    @TargetDataSource
    void delete(@Param("uid")Long uid,@Param("topicid")Long topicid);

    /**
     * 查询点赞记录 声友(type=1)
     * @param uid
     * @param topicid
     * @return
     */
    @TargetDataSource
    Praise selectPraise(@Param("uid")Long uid,@Param("topicid")Long topicid);

    /**
     * 查询点赞记录
     * @param uid
     * @param topicid
     * @param type
     * @return
     */
    @TargetDataSource
    Praise selectPraiseType(@Param("uid")Long uid,@Param("topicid")Long topicid,@Param("type")Byte type);


    /**
     * 取消点赞 删除点赞记录
     * @param uid
     * @param topicid
     * @param type
     */
    @TargetDataSource
    void deleteType(@Param("uid")Long uid,@Param("topicid")Long topicid,@Param("type")Byte type);
}

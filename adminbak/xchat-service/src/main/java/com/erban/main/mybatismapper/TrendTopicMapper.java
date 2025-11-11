package com.erban.main.mybatismapper;

import com.erban.main.model.TrendTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrendTopicMapper {

    /**
     * 查询需要审核的 动态
     * @param state
     * @return
     */
    List<TrendTopic> getAllByCheck(@Param("uid") Long uid, @Param("state")String state, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 更新审核状态
     * @param state
     * @return
     */
    int updateState(@Param("id") Long id,@Param("state")String state);

    TrendTopic selectById(@Param("id")Long id);
}

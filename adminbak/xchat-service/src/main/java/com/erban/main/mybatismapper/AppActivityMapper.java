package com.erban.main.mybatismapper;

import com.erban.main.model.ActivityHtml;
import com.erban.main.model.AppActivity;
import com.erban.main.model.AppActivityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppActivityMapper {
    int deleteByPrimaryKey(Integer actId);

    int insert(AppActivity record);

    int insertSelective(AppActivity record);

    List<AppActivity> selectByExample(AppActivityExample example);

    AppActivity selectByPrimaryKey(Integer actId);

    int updateByPrimaryKeySelective(AppActivity record);

    int updateByPrimaryKey(AppActivity record);

    List<ActivityHtml> queryActivityHtml(@Param("activityId") String activityId, @Param("activityName") String activityName);

    int insertActivityShare(ActivityHtml activityHtml);

    int updateActivityShare(ActivityHtml activityHtml);
}

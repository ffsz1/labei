package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.dto.ActivityHtmlDTO;
import org.apache.ibatis.annotations.Param;

public interface ActivityHtmlDAO {
     @TargetDataSource(name = "ds2")
     ActivityHtmlDTO queryActivity(@Param("activityId") String activityId);
}

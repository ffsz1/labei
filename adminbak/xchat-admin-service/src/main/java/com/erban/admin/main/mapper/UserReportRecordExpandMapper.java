package com.erban.admin.main.mapper;

import com.erban.main.model.UserReportRecord;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.mapper
 * @date 2018/8/7
 * @time 11:05
 */
public interface UserReportRecordExpandMapper {

    List<UserReportRecord> selectByParam(UserReportRecord userReportRecord);

}

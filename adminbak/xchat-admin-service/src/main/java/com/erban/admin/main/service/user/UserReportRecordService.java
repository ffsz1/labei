package com.erban.admin.main.service.user;

import com.erban.admin.main.mapper.UserReportRecordExpandMapper;
import com.erban.main.model.UserReportRecord;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.user
 * @date 2018/8/7
 * @time 11:06
 */
@Service
public class UserReportRecordService {
    @Autowired
    private UserReportRecordExpandMapper userReportRecordMapper;

    public PageInfo<UserReportRecord> getAll(UserReportRecord userReportRecord, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<UserReportRecord> userReportRecordList = userReportRecordMapper.selectByParam(userReportRecord);
        PageInfo pageInfo = new PageInfo(userReportRecordList);
        return pageInfo;
    }
}


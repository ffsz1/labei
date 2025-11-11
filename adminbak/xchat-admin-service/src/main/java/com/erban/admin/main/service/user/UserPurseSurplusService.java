package com.erban.admin.main.service.user;

import com.erban.main.model.UserPurseSurplus;
import com.erban.main.mybatismapper.UserPurseSurplusMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-15
 * @time 11:00
 */
@Service
public class UserPurseSurplusService {
    @Autowired
    private UserPurseSurplusMapper userPurseSurplusMapper;

    public PageInfo<UserPurseSurplus> getByPage(String beginDate, String endDate , int page, int size) {
        PageHelper.startPage(page, size);
        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            beginDate = beginDate +" 00:00:00";
            endDate = endDate + " 23:59:59";
        }
        List<UserPurseSurplus> list = userPurseSurplusMapper.listByParam(beginDate,endDate);
        return new PageInfo(list);
    }

    public List<UserPurseSurplus> selectByList() {
        return userPurseSurplusMapper.selectByExample(null);
    }
}

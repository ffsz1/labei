package com.erban.admin.main.service;

import com.erban.admin.main.mapper.FeedBackaAdminMapperMgr;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.FeedbackVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedBackAdminService extends BaseService {
    @Autowired
    private FeedBackaAdminMapperMgr feedBackaAdminMapperMgr;

    public PageInfo getFeedbackList(Integer pageSize, Integer pageNum, Long erbanNo, String beginDate, String endDate){
        Map<String, Object> map = new HashMap<>();
        if(erbanNo != null){
            map.put("erbanNo", erbanNo);
        }
        if(beginDate != "") {
            Date date1 = DateTimeUtil.convertStrToDate(beginDate + " 00:00:00");
            map.put("beginDate", date1);
        }
        if(endDate != ""){
            Date date2 = DateTimeUtil.convertStrToDate(endDate + " 23:59:59");
            map.put("endDate", date2);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<FeedbackVo> feedbackList = feedBackaAdminMapperMgr.selectByParam(map);
        return new PageInfo<>(feedbackList);
    }
}

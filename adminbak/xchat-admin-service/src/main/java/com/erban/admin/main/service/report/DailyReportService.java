package com.erban.admin.main.service.report;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.DailyReport;
import com.erban.main.model.DailyReportExample;
import com.erban.main.model.DailyUserPurse;
import com.erban.main.mybatismapper.DailyReportMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyReportService extends BaseService {
    @Autowired
    DailyReportMapper dailyReportMapper;

    public PageInfo<DailyReport> getList(Integer pageSize, Integer pageNum) {
        DailyReportExample dailyReportExample = new DailyReportExample();
        dailyReportExample.setOrderByClause(" report_id desc ");
        PageHelper.startPage(pageNum, pageSize);
        List<DailyReport> accountLoginRecord = dailyReportMapper.selectByExample(dailyReportExample);
        return new PageInfo<>(accountLoginRecord);
    }

    public BusiResult dailyUserPurseList(DailyUserPurse param) {
        PageHelper.startPage(param.getPage(), param.getSize());
        PageHelper.startPage(param.getPage(), param.getSize());
        Map<String, Object> map = new HashMap<>(16);
        map.put("beginDate", param.getBeginDate());
        map.put("endDate", param.getEndDate());
        PageInfo result = new PageInfo<>(dailyReportMapper.selectDailyUserPurse(map));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", result.getTotal());
        jsonObject.put("rows", result.getList());
        return new BusiResult(BusiStatus.SUCCESS, jsonObject);
    }
}

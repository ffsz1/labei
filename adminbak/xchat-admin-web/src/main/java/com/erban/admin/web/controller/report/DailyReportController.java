package com.erban.admin.web.controller.report;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.report.DailyReportService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.DailyReport;
import com.erban.main.model.DailyUserPurse;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class DailyReportController extends BaseController {
    @Autowired
    private DailyReportService dailyReportService;

    @RequestMapping(value = "/dailyReport/list", method = RequestMethod.GET)
    @ResponseBody
    public void getList(Integer pageSize, Integer pageNum) {
        PageInfo<DailyReport> pageInfo = dailyReportService.getList(pageSize, pageNum);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/dailyUserPurse/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getList(@RequestBody DailyUserPurse param) {
        logger.info("用户余额日报查询接口, 接口入参:{}", JSON.toJSONString(param));
        BusiResult busiResult = null;
        if (param == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = dailyReportService.dailyUserPurseList(param);
        } catch (Exception e) {
            logger.error("用户余额日报查询出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }
}

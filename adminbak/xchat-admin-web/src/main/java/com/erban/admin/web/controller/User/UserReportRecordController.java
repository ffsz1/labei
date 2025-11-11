package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.user.UserReportRecordService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.UserReportRecord;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 举报列表
 */
@Controller
@RequestMapping("/admin/user/report")
public class UserReportRecordController extends BaseController {
    @Autowired
    private UserReportRecordService userReportRecordService;

    /**
     * 查询举报信息列表
     *
     * @param userReportRecord 举报记录
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getList(UserReportRecord userReportRecord) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserReportRecord> pageInfo = userReportRecordService.getAll(userReportRecord, getPageNumber(),
                getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }
}


package com.erban.admin.web.controller.advertise;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.ActivityDTO;
import com.erban.admin.main.service.advertise.AppActivityAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.ActivityHtml;
import com.erban.main.model.AppActivity;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/appActivity")
public class AppActivityAdminController extends BaseController {
    @Autowired
    private AppActivityAdminService appActivityAdminService;

    // --------- 活动 ----------

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList() {
        JSONObject jsonObject = new JSONObject();
        PageInfo pageInfo = appActivityAdminService.getList(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(AppActivity appActivity) {
        try {
            int result = appActivityAdminService.save(appActivity);
            if (result > 0) {
                writeJson(true, "保存成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to save appActivity. Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(Integer actId) {
        JSONObject jsonObject = new JSONObject();
        AppActivity appActivity = appActivityAdminService.get(actId);
        if (appActivity != null) {
            jsonObject.put("entity", appActivity);
        }
        writeJson(jsonObject.toJSONString());
    }

    // --------- 活动页 ----------

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    @ResponseBody
    public void getAll(@RequestParam("activityId") String activityId, @RequestParam("activityName") String activityName) {
        JSONObject jsonObject = new JSONObject();
        PageInfo pageInfo = appActivityAdminService.getAll(getPageNumber(), getPageSize(), activityId, activityName);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "addOrModify", method = RequestMethod.GET)
    @ResponseBody
    public void addOrModify(ActivityDTO activityDTO, boolean modify) {
        JSONObject jsonObject = new JSONObject();
        boolean result = appActivityAdminService.addOrModifyActivityPage(activityDTO, getAdminId(), modify);
        jsonObject.put("result", result);
        writeJson(jsonObject.toJSONString());
    }
}

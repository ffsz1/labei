package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.FeedBackAdminService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/feedback")
public class FeedBackAdminController extends BaseController{
    @Autowired
    private FeedBackAdminService feedBackAdminService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public void getFeedback(Integer pageSize,Integer pageNum,Long erbanNo, String beginDate, String endDate){
        PageInfo pageInfo = feedBackAdminService.getFeedbackList(pageSize,pageNum,erbanNo,beginDate,endDate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo.getList());
        writeJson(jsonObject.toJSONString());

    }
}

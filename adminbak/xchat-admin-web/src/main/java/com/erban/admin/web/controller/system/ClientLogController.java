package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.system.ClientLogService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chris
 * @Title:
 * @date 2019-05-13
 * @time 14:16
 */
@RequestMapping("/admin/client/log")
@Controller
public class ClientLogController extends BaseController {

    @Autowired
    private ClientLogService clientLogService;

    @RequestMapping("listLog")
    @ResponseBody
    public void listLog(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo pageInfo = clientLogService.listLog(getPageNumber(), getPageSize(), searchText);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("getLog")
    @ResponseBody
    public BusiResult getLog(Long erbanNo, Integer num) {
        //
        return clientLogService.getLog(erbanNo, num);
    }
}

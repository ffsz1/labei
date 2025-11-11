package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.system.AppSecretService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.AppSecret;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: CXStone
 * @Description: 密钥管理
 * @Date: 2018/12/26 11:43
 */
@RequestMapping("/admin/AppSecret")
@Controller
public class AppSecretController extends BaseController {

    @Autowired
    private AppSecretService appSecretService;

    @RequestMapping("list")
    @ResponseBody
    public void list() {
        JSONObject jsonObject = new JSONObject();
        PageInfo<AppSecret> pageInfo = appSecretService.page(getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("add")
    @ResponseBody
    public BusiResult addAppSecret(String os, String appVersion, String timeStamp, Date createTime) {
        try {
            appSecretService.addAppSecret(os,appVersion,timeStamp,createTime);
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }

    @RequestMapping("del")
    @ResponseBody
    public BusiResult delete(String signKey) {
        try {
            appSecretService.delete(signKey);
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR, e.getMessage(), null);
        }
    }


}

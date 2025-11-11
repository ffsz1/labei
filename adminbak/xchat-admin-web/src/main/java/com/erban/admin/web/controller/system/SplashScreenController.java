package com.erban.admin.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.SplashScreenDTO;
import com.erban.admin.main.service.system.AppSecretService;
import com.erban.admin.main.service.system.SplashScreenService;
import com.erban.admin.main.vo.SplashScreenVO;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/admin/splashScreen")
@Controller
public class SplashScreenController extends BaseController {
    @Autowired
    private SplashScreenService splashScreenService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public void getAll(@RequestParam("type") Integer type, @RequestParam("status") Integer status, @RequestParam("userType") Integer userType) {
        JSONObject jsonObject = new JSONObject();
        PageInfo pageInfo = splashScreenService.getAll(getPageNumber(), getPageSize(), type, status, userType);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ResponseBody
    public void getOne(@RequestParam("id") Integer id) {
        JSONObject jsonObject = new JSONObject();
        SplashScreenVO splashScreenVO = splashScreenService.getOne(id);
        jsonObject.put("result", splashScreenVO);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "/addOrModify", method = RequestMethod.POST)
    @ResponseBody
    public void addOrModify(SplashScreenDTO splashScreenDTO, boolean modify) {
        JSONObject jsonObject = new JSONObject();
        boolean result = splashScreenService.addOrModifyActivityPage(splashScreenDTO, modify);
        jsonObject.put("result", result);
        writeJson(jsonObject.toJSONString());
    }
}

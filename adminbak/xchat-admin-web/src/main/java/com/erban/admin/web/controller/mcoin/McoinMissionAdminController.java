package com.erban.admin.web.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.McoinMissionAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.McoinMission;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/mcoin/mission")
public class McoinMissionAdminController extends BaseController {

    @Autowired
    private McoinMissionAdminService mcoinMissionAdminService;


    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(Byte freebiesType, Byte missionType) {
        if(freebiesType < 0){freebiesType = null;}
        if(missionType < 0){missionType = null;}
        PageInfo<McoinMission> pageInfo = mcoinMissionAdminService.getList(getPageNumber(), getPageSize(), freebiesType, missionType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "/save")
    @ResponseBody
    public void saveMcoinMission(McoinMission mcoinMission) {
        int result = -1;
        try {
            result = mcoinMissionAdminService.saveMcoinMission(mcoinMission);
        } catch (Exception e) {
            logger.warn("saveMcoinMission fail,cause by " + e.getMessage(), e);
            result = ErrorCode.SERVER_ERROR;
        }
        writeJsonResult(result);
    }


    @RequestMapping(value = "/getOne")
    @ResponseBody
    public void getOne(Integer itemId) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(itemId)) {
            try {
                McoinMission mcoinMission = mcoinMissionAdminService.getOne(itemId);
                if (mcoinMission != null) {
                    jsonObject.put("entity", mcoinMission);
                }
            } catch (Exception e) {
                logger.warn("getBanner fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "/update")
    @ResponseBody
    public void updateMcoinMission(McoinMission mcoinMission) {
        int result = -1;
        if (mcoinMission != null) {
            try {
                result = mcoinMissionAdminService.updateMcoinMission(mcoinMission);
            } catch (Exception e) {
                logger.warn("updateMcoinMission fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

}

package com.erban.admin.web.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.McoinAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.McoinDrawIssue;
import com.erban.main.model.dto.McoinDrawIssueDTO;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/mcoin")
public class McoinAdminController extends BaseController {

    @Autowired
    private McoinAdminService mcoinAdminService;


    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(int itemType, int issueStatus) {
        PageInfo<McoinDrawIssueDTO> pageInfo = mcoinAdminService.getList(getPageNumber(), getPageSize(), itemType, issueStatus);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "/save")
    @ResponseBody
    public void saveMcoinDrawIssue(McoinDrawIssue mcoinDrawIssue, String startTimeString) {
        int result = -1;
        if (mcoinDrawIssue == null) {
            result = ErrorCode.ERROR_NULL_ARGU;
            writeJsonResult(result);
            return;
        }

        if (mcoinDrawIssue.getItemType() == 2 || mcoinDrawIssue.getItemType() == 3) {
            if (mcoinDrawIssue.getItemId() == null) {
                result = ErrorCode.ERROR_NULL_ARGU;
                writeJsonResult(result);
                return;
            }
        }

        try {
            result = mcoinAdminService.saveMcoinDrawIssue(mcoinDrawIssue, startTimeString);
        } catch (Exception e) {
            logger.warn("saveMcoinDrawIssue fail,cause by " + e.getMessage(), e);
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
                McoinDrawIssue mcoinDrawIssue = mcoinAdminService.getOne(itemId);
                if (mcoinDrawIssue != null) {
                    jsonObject.put("entity", mcoinDrawIssue);
                }
            } catch (Exception e) {
                logger.warn("getBanner fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping(value = "/update")
    @ResponseBody
    public void updateMcoinDrawIssue(McoinDrawIssue mcoinDrawIssue) {
        int result = -1;
        if (mcoinDrawIssue != null) {
            try {
                result = mcoinAdminService.updateMcoinDrawIssue(mcoinDrawIssue);
            } catch (Exception e) {
                logger.warn("updateMcoinDrawIssue fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

}

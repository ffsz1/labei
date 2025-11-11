package com.erban.admin.web.controller.family;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.family.FamilyTeamRecordService;
import com.erban.main.param.admin.FamilyFlowParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.web.controller.family
 * @date 2018/9/4
 * @time 18:04
 */
@Controller
@RequestMapping("/admin/familyFlow")
public class FamilyTotalFlowAdminController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FamilyTeamRecordService familyTeamRecordService;


    /**
     * 家族流水列表
     *
     * @param familyFlowParam
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getList(@RequestBody FamilyFlowParam familyFlowParam) {
        logger.info("家族流水列表接口,接口入参:{}", JSON.toJSONString(familyFlowParam));
        BusiResult busiResult = null;
        if (familyFlowParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = familyTeamRecordService.getList(familyFlowParam);
        } catch (Exception e) {
            logger.error("家族流水列表数据出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getDetail(Long teamId, Integer page, Integer size) {
        if (teamId == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return familyTeamRecordService.selectByList(teamId, page, size);
        } catch (Exception e) {
            logger.error("统计详情列表数据出错,错误原因:{}", e.getMessage());
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }
}

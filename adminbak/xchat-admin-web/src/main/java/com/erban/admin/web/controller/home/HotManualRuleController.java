package com.erban.admin.web.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.HotManualRuleService;
import com.erban.admin.main.service.room.bo.HotManualRuleBO;
import com.erban.main.model.domain.HotManualRuleDO;
import com.erban.main.model.dto.HotManualRuleDTO;
import com.erban.main.mybatismapper.query.HotManualRuleQuery;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/hotManual/rule")
public class HotManualRuleController {

    @Autowired
    private HotManualRuleService hotManualRuleService;

    @RequestMapping("list")
    @ResponseBody
    public Object list(Integer pageNumber, Integer pageSize, Long erbanNo, HotManualRuleQuery query) {
        PageInfo<HotManualRuleDTO> pageInfo = hotManualRuleService.list(pageNumber, pageSize, query);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo);
        // hotManualRuleService.toRecommByToday();
        return jsonObject;
    }

    @RequestMapping("save")
    @ResponseBody
    public BusiResult save(HotManualRuleBO manualRuleBO) {

        return hotManualRuleService.save(manualRuleBO);
    }

    @RequestMapping("get/{id}")
    @ResponseBody
    public BusiResult get(@PathVariable Long id) {
        HotManualRuleDTO ruleDTO = hotManualRuleService.getById(id);
        return new BusiResult(BusiStatus.SUCCESS, ruleDTO);
    }

    @RequestMapping("delete/{id}")
    @ResponseBody
    public BusiResult delete(@PathVariable Long id) {
        int count = hotManualRuleService.delete(id);
        return count > 0 ? new BusiResult(BusiStatus.SUCCESS) : new BusiResult(BusiStatus.SERVERERROR);
    }

//    @RequestMapping("test")
//    @ResponseBody
//    public BusiResult test() {
//        List<HotManualRuleDO> list = hotManualRuleService.toRecommByToday();
//        return new BusiResult(BusiStatus.SUCCESS, list);
//    }
}

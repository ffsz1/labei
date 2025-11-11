package com.erban.admin.web.controller.trendtopic;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.trendtopic.TrendTopicService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.TrendTopic;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user/trendtopic")
public class TrendTopicCheckController extends BaseController {

    @Autowired
    private TrendTopicService trendTopicService;

    @RequestMapping("/getall")
    public void getAll(Long uid, String state, String startDate, String endDate) {
        state=putNull(state);
        startDate=putNull(startDate);
        endDate=putNull(endDate);
        JSONObject jsonObject = new JSONObject();
        PageInfo<TrendTopic> pageInfo = trendTopicService.getListWithPage(uid, state, startDate, endDate, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    private String putNull(String s){
        if("".equals(s)) return null;
        return s;
    }

    /**
     * 审核通过
     */
    @RequestMapping("/checkSuccess")
    public BusiResult checkSuccess(String ids) throws Exception {
        if(ids == null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return trendTopicService.updateCheckSuccess(ids,getAdminId());
    }

    /**
     * 审核不通过
     */
    @RequestMapping("/checkFailure")
    public BusiResult checkFailure(Long id)throws Exception{
        if(id==null){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return trendTopicService.updateCheckFailure(id,getAdminId());
    }
}

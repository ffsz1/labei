package com.erban.web.controller;

import com.erban.main.service.WeekListService;
import com.erban.main.vo.WeekListVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * yanghaoyu 查询周榜
 */

@RequestMapping("/weeklist")
@Controller
public class WeekListController {

    @Autowired
    private WeekListService weekListService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryList(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        List<WeekListVo> weekLists = weekListService.queryList(roomUid);
        if (weekLists == null) {
            busiResult.setData(new ArrayList<WeekListVo>());
            return busiResult;
        }
        Collections.sort(weekLists);
        busiResult.setData(weekLists);
        return busiResult;
    }
}

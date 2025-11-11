package com.erban.web.controller;

import com.erban.main.service.SumListService;
import com.erban.main.vo.SumListVo;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * yanghaoyu 总榜
 */
@Controller
@RequestMapping("/sumlist")
public class SumListController {
    @Autowired
    private SumListService sumListService;

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public BusiResult querySumList(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (roomUid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        List<SumListVo> sumListVos = sumListService.queryList(roomUid);
        if (sumListVos == null) {
            busiResult.setData(new ArrayList<SumListVo>());
            return busiResult;
        }
        busiResult.setData(sumListVos);
        return busiResult;
    }

}

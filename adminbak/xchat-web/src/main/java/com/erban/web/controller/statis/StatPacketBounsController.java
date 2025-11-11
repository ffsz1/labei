package com.erban.web.controller.statis;

import com.erban.main.service.statis.StatPacketActivityService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/statbouns")
public class StatPacketBounsController {
    @Autowired
    private StatPacketActivityService statPacketActivityService;

    /**
     * 获取用户分成详情
     * @param uid
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ResponseBody
//    @Authorization
    public BusiResult getStatBounsDetail(Long uid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || uid == 0L) {
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        busiResult = statPacketActivityService.queryStatBounsDetail(uid);
        return busiResult;
    }
}

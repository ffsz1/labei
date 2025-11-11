package com.erban.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.user.UserShareRecordService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/usershare")
public class UserShareRecordController {
    private static final Logger logger = LoggerFactory.getLogger(UserShareRecordController.class);
    @Autowired
    private DutyService dutyService;
    @Autowired
    private UserShareRecordService userShareRecordService;

    /**
     * 保存分享记录
     * 邀请之后如果有红包则返回，每天只能拿一个邀请红包。如果没有红包，则data为空
     *
     * @param uid
     * @param shareType
     * @param sharePageId
     * @param targetUid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BusiResult saveUserShareRecord(Long uid, String shareType, int sharePageId, Long targetUid) {
        logger.info("接口调用（/usershare/save）,保存分享记录，接口入参：uid:{},shareType:{},sharePageId:{},targetUid:{}", uid, shareType, sharePageId, targetUid);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || uid == 0L) {
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        busiResult = userShareRecordService.saveUserShareRecord(uid, shareType, sharePageId, targetUid);
        logger.info("保存分享记录(/usershare/save),接口出参：{}", JSON.toJSONString(busiResult));
        // 微信 1 朋友圈 2  qq 3  qq空间 4
        if ("2".equals(shareType)) {
            try {
                dutyService.updateDailyDuty(uid, DutyType.wx_mon_share.getDutyId());
            } catch (Exception e) {
                logger.error(" [ 更新新手任务 ] 异常：", e);
            }
        } else if ("4".equals(shareType)) {
            dutyService.updateDailyDuty(uid, DutyType.qq_space_share.getDutyId());
        }
        return busiResult;
    }

    @RequestMapping(value = "sendPusTest", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult sendPusTest(Long uid) {
        System.out.println("sendPusTest....");
        userShareRecordService.sendPushTest(uid);
        System.out.println("sendPusTest....");
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        return busiResult;
    }

}

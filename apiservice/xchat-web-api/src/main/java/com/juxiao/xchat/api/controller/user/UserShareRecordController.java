package com.juxiao.xchat.api.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.sysconf.NationalDayService;
import com.juxiao.xchat.service.api.user.UserShareRecordService;
import com.juxiao.xchat.service.api.user.vo.UserPacketRecordVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usershare")
@Api(tags = "用户配置接口", description = "用户配置接口")
public class UserShareRecordController {
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private UserShareRecordService shareRecordService;
    @Autowired
    private NationalDayService nationalDayService;

    /**
     * 保存分享记录，邀请之后如果有红包则返回，每天只能拿一个邀请红包。如果没有红包，则data为空
     *
     * @param uid uid
     * @param shareType   1,微信;2,朋友圈;3,qq;4,qq空间
     * @param sharePageId sharePageId
     * @param targetUid targetUid
     * @return WebServiceMessage
     */
    @SignVerification
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public WebServiceMessage saveUserShareRecord(@RequestParam(value = "uid", required = false) Long uid,
                                                 @RequestParam(value = "shareType", required = false) String shareType,
                                                 @RequestParam(value = "sharePageId", required = false) int sharePageId,
                                                 @RequestParam(value = "targetUid", required = false) Long targetUid) throws WebServiceException {
    	
        // todo 1.0.0 运营需要关闭红包分享功能
        return WebServiceMessage.failure("分享成功");

//        UserPacketRecordVO recordVo = shareRecordService.saveShareRecord(uid, shareType, sharePageId, targetUid);
//
//        JSONObject object = new JSONObject();
//        object.put("uid", uid);
//        object.put("shareType", shareType);
//        object.put("sharePageId", sharePageId);
//        object.put("targetUid", targetUid);
//        activeMqManager.sendQueueMessage(MqDestinationKey.USER_SHARE_QUEUE, object.toJSONString());
//        return WebServiceMessage.success(recordVo);
    }

}

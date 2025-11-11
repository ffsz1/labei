package com.juxiao.xchat.api.controller.event;


import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.event.PublicChargeActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/charge")
@Api(tags = "首次充值活动接口")
public class PublicChargeActivityController {

    @Autowired
    private PublicChargeActivityService publicChargeActivityService;

    /**
     * 查询活动接口
     * @param uid  用户ID
     */
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage getList(@RequestParam(value = "uid") Long uid){
        return publicChargeActivityService.getNoviceFirstChargeRecord(uid);
    }


    /**
     * 领取活动礼物项
     * @param uid  用户ID
     */
    @RequestMapping(value = "receiveActivityItem", method = RequestMethod.POST)
    @Authorization
    public WebServiceMessage receiveActivityItem(@RequestParam(value = "uid") Long uid,@RequestParam(value = "itemId")Integer itemId){
        return publicChargeActivityService.receiveActivityItem(uid,itemId);
    }
}

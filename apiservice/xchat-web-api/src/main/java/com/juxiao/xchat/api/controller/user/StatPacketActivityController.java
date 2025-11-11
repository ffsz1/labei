package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;
import com.juxiao.xchat.dao.user.dto.UserPacketRegTeamDTO;
import com.juxiao.xchat.service.api.user.StatPacketActivityService;
import com.juxiao.xchat.service.api.user.vo.StatPacketActivityParentVO;
import com.juxiao.xchat.service.api.user.vo.StatPacketActivityVO;
import com.juxiao.xchat.service.api.user.vo.StatPacketInviteDetailParentVO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @class: StatPacketActivityController.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@RestController
@RequestMapping("/statpacket")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class StatPacketActivityController {
    private final Logger logger = LoggerFactory.getLogger(StatPacketActivityController.class);
    @Autowired
    private StatPacketActivityService packetActivityService;

    /**
     * 我的红包页面(邀请人数，分成奖励，红包金额)
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getUserStatPacketDetail(@RequestParam("uid") Long uid) throws WebServiceException {
        StatPacketActivityVO activityVo = packetActivityService.getUserStatPacketDetail(uid);
        return WebServiceMessage.success(activityVo);

    }

    /**
     * 红包排行榜页面
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "rank", method = RequestMethod.GET)
    public WebServiceMessage listPacketActivityRank(@RequestParam("uid") Long uid) throws WebServiceException {
        StatPacketActivityParentVO parentVo = packetActivityService.listPacketActivityRank(uid);
        return WebServiceMessage.success(parentVo);
    }

    /**
     * 我的邀请详情页面接口
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "invitedetail", method = RequestMethod.GET)
    public WebServiceMessage getInviteDetail(@RequestParam("uid") Long uid) throws WebServiceException {
        StatPacketInviteDetailParentVO parentVo = packetActivityService.getInviteDetail(uid);
        return WebServiceMessage.success(parentVo);
    }

    @RequestMapping(value = "someinvitedetail", method = RequestMethod.GET)
    public WebServiceMessage getSomeInviteDetail(@RequestParam("uidList") String uidList) throws WebServiceException {
        List<StatPacketActivityDTO> list = packetActivityService.listSomeInviteDetail(uidList);
        return WebServiceMessage.success(list);
    }

    /**
     * 我的团队
     *
     * @param uid
     */
    @RequestMapping(value = "team", method = RequestMethod.GET)
    public WebServiceMessage listUserTeam(@RequestParam("uid") Long uid) throws WebServiceException {
        List<UserPacketRegTeamDTO> list = packetActivityService.listUserTeam(uid);
        return WebServiceMessage.success(list);
    }

}

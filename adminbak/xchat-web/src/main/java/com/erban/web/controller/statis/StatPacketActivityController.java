package com.erban.web.controller.statis;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.statis.StatPacketActivityService;
import com.erban.main.vo.*;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/statpacket")
public class StatPacketActivityController extends BaseController {
    @Autowired
    private StatPacketActivityService statPacketActivityService;

    /**
     * 我的红包页面(邀请人数，分成奖励，红包金额)
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
//    @Authorization
    public BusiResult getMyStatPacketDetail(Long uid) {
        BusiResult<StatPacketActivityVo> busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || uid == 0L) {
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        try {
            busiResult=statPacketActivityService.queryStatPacketVo(uid);
        } catch (Exception e) {
            logger.error("getMyStatPacketDetail error...uid="+uid,e);
        }
        return busiResult;
    }

    /**
     * 红包排行榜页面
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "rank", method = RequestMethod.GET)
    @ResponseBody
//    @Authorization
    public BusiResult queryStatPacketActivityRankList(Long uid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return statPacketActivityService.queryStatPacketActivityRankList(uid);
    }

    /**
     * 我的邀请详情页面接口
     * @param uid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "invitedetail", method = RequestMethod.GET)
    public BusiResult getInviteDetail(Long uid) {
        logger.info("接口调用：/statpacket/invitedetail,我的邀请详情页面接口,接口入参：uid:{}",uid);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || uid == 0L) {
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        busiResult =statPacketActivityService.getInviteDetail(uid);
        logger.info("接口调用：/statpacket/invitedetail,我的邀请详情页面接口,接口出参：{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

    @RequestMapping(value = "someinvitedetail", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getSomeInviteDetail(String uidList) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if(BlankUtil.isBlank(uidList)){
            busiResult.setCode(BusiStatus.PARAMETERILLEGAL.value());
            busiResult.setMessage("参数异常");
            return busiResult;
        }
        busiResult = statPacketActivityService.getSomeInviteDetail(uidList);
        return busiResult;
    }

    /**
     * 我的团队
     * @param uid
     */
    @ResponseBody
    @RequestMapping(value = "team", method = RequestMethod.GET)
    public BusiResult getTeam(Long uid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            return statPacketActivityService.getTeam(uid);
        }catch (Exception e){
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}

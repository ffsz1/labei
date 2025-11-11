package com.juxiao.xchat.api.controller.guild;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.guild.GuildHallApplyRecordService;
import com.juxiao.xchat.service.api.guild.bo.ApplyHallParamBo;
import com.juxiao.xchat.service.api.guild.bo.VerifyParamBo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @创建时间： 2020/10/14 16:46
 * @作者： carl
 */
@RestController
@RequestMapping("/guild/hall/apply")
public class GuildHallApplyRecordController {

    @Autowired
    private GuildHallApplyRecordService guildHallApplyRecordService;

    /**
     * 申请加入厅
     */
    @SignVerification
    @RequestMapping("applyJoinHall")
    public WebServiceMessage applyJoinHall(ApplyHallParamBo paramBo) throws WebServiceException {
        return WebServiceMessage.success(guildHallApplyRecordService.applyJoinHall(paramBo));
    }

    /**
     * 申请退出厅
     */
    @SignVerification
    @RequestMapping("applyExitHall")
    public WebServiceMessage applyJoinHall(Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildHallApplyRecordService.applyExitHall(uid));
    }

    /**
     * 获取待审核消息数量
     */
    @SignVerification
    @RequestMapping("getApplyJoinCount")
    public WebServiceMessage getApplyJoinCount(Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildHallApplyRecordService.getApplyJoinCount(uid));
    }

    /**
     * 获取待审核消息记录
     */
    @SignVerification
    @RequestMapping("getApplyJoinRecords")
    public WebServiceMessage getApplyJoinRecords(IndexParam param, Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildHallApplyRecordService.getApplyJoinRecords(param, uid));
    }

    /**
     * 审核操作
     * @param paramBo
     * @return
     */
    @RequestMapping("verify")
    public WebServiceMessage verify(VerifyParamBo paramBo) throws WebServiceException {
        return WebServiceMessage.success(guildHallApplyRecordService.verify(paramBo));
    }
}

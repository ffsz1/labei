package com.juxiao.xchat.service.api.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.UserReportRecordDao;
import com.juxiao.xchat.dao.user.domain.UserAlbumReportRecordDO;
import com.juxiao.xchat.dao.user.domain.UserAvatarReportRecordDO;
import com.juxiao.xchat.dao.user.domain.UserReportRecordDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.bo.MarkdownMessage;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import com.juxiao.xchat.service.api.user.UserReportService;
import com.juxiao.xchat.service.api.user.bo.UserItemReportBO;
import com.juxiao.xchat.service.api.user.bo.UserReportBO;
import com.juxiao.xchat.service.api.user.enumeration.UsersReportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserReportServiceImpl implements UserReportService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserReportRecordDao reportRecordDao;

    @Resource
    private UsersManager usersManager;

    @Resource
    private RoomManager roomManager;

    @Resource
    private DingtalkChatbotManager dingtalkChatbotManager;

    @Resource
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Resource
    private DingTalkConf dingTalkConf;

    @Override
    public void save(UserReportBO reportBo) throws WebServiceException {
        if (reportBo.getInformantsId() == null || reportBo.getReportUid() == null || reportBo.getReportType() == null || reportBo.getType() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        Gson gson = new Gson();
        logger.info("[ 举报信息BO ]请求:>{}", gson.toJson(reportBo));
        UserReportRecordDO recordDo = new UserReportRecordDO();
        BeanUtils.copyProperties(reportBo, recordDo);
        recordDo.setCreateDate(new Date());
        reportRecordDao.save(recordDo);
        asyncNetEaseTrigger.sendMsg(reportBo.getReportUid().toString(), "官方已经收到您的举报, 感谢你的支持！");
        alertMessage(reportBo.getInformantsId(), reportBo.getReportUid(), reportBo.getType(), reportBo.getReportType());
    }

    /**
     * @param uid        被举报用户ID
     * @param reportUid  举报用户ID
     * @param type       类型 [1.用户; 2.房间]
     * @param reportType 举报类型
     */
    private void alertMessage(Long uid, Long reportUid, Integer type, Integer reportType) {
        if (uid.longValue() == reportUid.longValue()) {
            return;
        }

        if (uid == 0 || reportUid == 0) {
            return;
        }

        String text = "";
        UsersDTO whistleBlower = usersManager.getUser(uid);
        UsersDTO reportUserDTO = usersManager.getUser(reportUid);
        if (type.intValue() == 1) {
            text = "预警 用户: " + reportUserDTO.getErbanNo() + " " + reportUserDTO.getNick() + "  举报 用户: " + whistleBlower.getErbanNo() + " " + whistleBlower.getNick() + ",举报类型为:" + UsersReportType.of(reportType).getDesc() + ",请核实!";
        } else if (type.intValue() == 2) {
            RoomDTO roomDTO = roomManager.getUserRoom(whistleBlower.getUid());
            text = "预警 用户: " + reportUserDTO.getErbanNo() + "  " + reportUserDTO.getNick() + " 举报 房间: " + whistleBlower.getErbanNo() + " " + roomDTO.getTitle() + " 举报类型为," + UsersReportType.of(reportType).getDesc() + " 请核实!";
        }
        DingtalkTextMessageBO textMessage = new DingtalkTextMessageBO(text, dingTalkConf.getReport(), false);
        dingtalkChatbotManager.send(dingTalkConf.getReportWebHook(), textMessage);
    }

    /**
     * 头像举报
     *
     * @param reportBo reportBo
     */
    @Override
    public void saveAvatar(UserItemReportBO reportBo) throws WebServiceException {
        if (reportBo.getUid() == null || reportBo.getReportUid() == null || reportBo.getReportType() == null || reportBo.getType() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserAvatarReportRecordDO recordDo = new UserAvatarReportRecordDO();
        BeanUtils.copyProperties(reportBo, recordDo);
        recordDo.setCreateDate(new Date());
        reportRecordDao.saveAvatar(recordDo);
        asyncNetEaseTrigger.sendMsg(reportBo.getReportUid().toString(), "官方官方已经收到您的举报反馈,感谢你对官方的支持！");
        sendDingTalkMsg(reportBo.getUid(), reportBo.getReportUid(), 1, reportBo.getReportType());
    }

    /**
     * 发送钉钉已经
     *
     * @param uid        uid
     * @param reportUid  reportUid
     * @param type       type 1.头像 2.相册
     * @param reportType reportType
     */
    private void sendDingTalkMsg(Long uid, Long reportUid, Integer type, Integer reportType) {
        if (uid.longValue() == reportUid.longValue()) {
            return;
        }
        if (uid == 0 || reportUid == 0) {
            return;
        }
        String text = "";
        UsersDTO whistleblower = usersManager.getUser(uid);
        UsersDTO reportUserDTO = usersManager.getUser(reportUid);
        if (type.intValue() == 1) {
            text = "用户官方号: " + reportUserDTO.getErbanNo() + " " + reportUserDTO.getNick() + "  举报 用户官方号: " + whistleblower.getErbanNo() + " " + whistleblower.getNick() + "头像,请核实!";
        } else if (type.intValue() == 2) {
            text = "用户官方号: " + reportUserDTO.getErbanNo() + "  " + reportUserDTO.getNick() + " 举报 用户官方号: " + whistleblower.getErbanNo() + " " + whistleblower.getNick() + " 相册,请核实!";
        }
        MarkdownMessage message = new MarkdownMessage();
        message.setTitle("用户举报预警");
        message.add(MarkdownMessage.getBoldText(text));
        dingtalkChatbotManager.send(dingTalkConf.getReportWebHook(), message);
    }

    /**
     * 相册举报
     *
     * @param reportBo reportBo
     */
    @Override
    public void saveAlbum(UserItemReportBO reportBo) throws WebServiceException {
        if (reportBo.getUid() == null || reportBo.getReportUid() == null || reportBo.getReportType() == null || reportBo.getType() == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserAlbumReportRecordDO recordDo = new UserAlbumReportRecordDO();
        BeanUtils.copyProperties(reportBo, recordDo);
        recordDo.setCreateDate(new Date());
        reportRecordDao.saveAlbum(recordDo);
        asyncNetEaseTrigger.sendMsg(reportBo.getReportUid().toString(), "官方官方已经收到您的举报反馈,感谢你对官方的支持！");
        sendDingTalkMsg(reportBo.getUid(), reportBo.getReportUid(), 2, reportBo.getReportType());
    }
}

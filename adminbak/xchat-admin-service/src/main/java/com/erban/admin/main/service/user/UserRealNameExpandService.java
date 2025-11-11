package com.erban.admin.main.service.user;

import com.erban.admin.main.dto.UserRealInfoDTO;
import com.erban.admin.main.mapper.UserRealNameDAO;
import com.erban.admin.main.mapper.query.UserRealNameQuery;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.UserRealNameMapper;
import com.erban.main.mybatismapper.UserRealNamePassMapper;
import com.erban.main.param.neteasepush.NeteasePushParam;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.room
 * @date 2018/8/16
 * @time 18:39
 */
@Service
public class UserRealNameExpandService {
    @Autowired
    private UserRealNameMapper userRealNameMapper;

    @Autowired
    private UserRealNamePassMapper userRealNamePassMapper;

    @Autowired
    private UserRealNameDAO userRealNameDAO;

    @Autowired
    private SendSysMsgService sendSysMsgService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<UserRealInfoDTO> getListWithPage(UserRealNameQuery query, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }

        PageHelper.startPage(pageNumber, pageSize);
        if (StringUtils.isNotBlank(query.getStartDate()) && StringUtils.isNotBlank(query.getEndDate())) {
            query.setStartDate(query.getStartDate() + " 00:00:00");
            query.setEndDate(query.getEndDate() + " 23:59:59");
        }

        List<UserRealInfoDTO> list = userRealNameDAO.listRealInfo(query);
        for (UserRealInfoDTO userRealInfoDTO : list) {
            if (userRealInfoDTO.getAdminId() != null && userRealInfoDTO.getAdminId() != 0) {
                AdminUser adminUser = adminUserService.getAdminUserById(userRealInfoDTO.getAdminId());
                if (adminUser != null) {
                    userRealInfoDTO.setOptionName(adminUser.getUsername());
                } else {
                    userRealInfoDTO.setOptionName("未知");
                }

            } else {
                userRealInfoDTO.setOptionName("未知");
            }
        }
        return new PageInfo<>(list);
    }

    /**
     * 获取用户审核记录
     *
     * @param uid
     * @return
     */
    public UserRealInfoDTO getOne(Long uid) {
        return userRealNameDAO.getByUid(uid);
    }

    /**
     * 审核通过
     *
     * @param uid
     * @return
     */
    public BusiResult updateCheckSuccess(Long uid, Integer adminId) {
        UserRealName userRealName = isFirstCheck(uid);
        if (userRealName.getAuditStatus().intValue() == 0 || userRealName.getAuditStatus().intValue() == 2) {
            userRealName.setAuditStatus((byte) 1);
            userRealName.setAdminId(adminId);
            userRealName.setRemark("");
            int status = userRealNameMapper.updateByPrimaryKey(userRealName);
            if (status > 0) {
                jedisService.hdel(RedisKey.user_real_name.getKey(), String.valueOf(userRealName.getUid()));
                sendMsg(uid, "恭喜您，实名认证通过~");
                if (userRealName.getIsFirst()) {
                    //设置状态，新增记录
                    userRealName.setIsFirst(false);
                    userRealNameMapper.updateByPrimaryKey(userRealName);
                    UserRealNamePass userRealNamePass = new UserRealNamePass();
                    userRealNamePass.setIdCardNo(userRealName.getIdCardNo());
                    userRealNamePass.setCreateDate(new Date());
                    userRealNamePassMapper.insert(userRealNamePass);
                }
                return new BusiResult(BusiStatus.SUCCESS);
            } else {
                return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
        }
    }

    /**
     * 审核不通过
     *
     * @param uid
     * @return
     */
    public BusiResult updateCheckFailure(Long uid, Integer adminId, String remark) {
        UserRealName userRealName = userRealNameMapper.selectByPrimaryKey(uid);
        if (userRealName != null) {
            userRealName.setAuditStatus((byte) 2);
            userRealName.setAdminId(adminId);
            userRealName.setRemark(remark);
            int status = userRealNameMapper.updateByPrimaryKey(userRealName);
            if (status > 0) {
                jedisService.hdel(RedisKey.user_real_name.getKey(), String.valueOf(userRealName.getUid()));
                sendMsg(uid, "很遗憾地通知您，您的实名认证审核不通过：" + remark);
                return new BusiResult(BusiStatus.SUCCESS);
            } else {
                return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
    }

    private void sendMsg(Long uid, String msg) {
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody(msg);
        sendSysMsgService.sendMsg(neteaseSendMsgParam);

        NeteasePushParam neteasePushParam = new NeteasePushParam();
        neteasePushParam.setTo(String.valueOf(uid));
        Attach attach = new Attach();
        attach.setFirst(Constant.DefMsgType.userRealAudit);
        attach.setSecond(Constant.DefMsgType.userRealAudit);
        attach.setData("");
        neteasePushParam.setAttach(attach);
        sendSysMsgService.sendSysAttachMsg(neteasePushParam);
    }

    private UserRealName isFirstCheck(Long uid) {
        UserRealName userRealName = userRealNameMapper.selectByPrimaryKey(uid);
        UserRealNameExample userRealNameExample = new UserRealNameExample();
        userRealNameExample.createCriteria().andIdCardNoEqualTo(userRealName.getIdCardNo()).andIsFirstEqualTo(false);
        int count = userRealNameMapper.countByExample(userRealNameExample);
        UserRealNamePassExample userRealNamePassExample = new UserRealNamePassExample();
        userRealNamePassExample.createCriteria().andIdCardNoEqualTo(userRealName.getIdCardNo());
        int count2 = userRealNamePassMapper.countByExample(userRealNamePassExample);
        if (count >= 1 || count2 >= 1) {
            userRealName.setIsFirst(false);
            userRealNameMapper.updateByPrimaryKey(userRealName);
        }
        return userRealName;
    }
}

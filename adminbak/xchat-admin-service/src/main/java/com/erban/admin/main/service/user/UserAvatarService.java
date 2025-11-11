package com.erban.admin.main.service.user;

import com.erban.admin.main.dto.UsersAvatarDTO;
import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.Users;
import com.erban.main.model.UsersAvatar;
import com.erban.main.mybatismapper.UsersAvatarMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserAvatarService {
    @Autowired
    private UsersAvatarMapper usersAvatarMapper;
    @Autowired
    private UserMapperExpand userMapperExpand;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<UsersAvatarDTO> getListWithPage(Long erbanNo, Integer status,
                                                    String startDate, String endDate,
                                                    int pageNumber,
                                                    int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<UsersAvatarDTO> list = userMapperExpand.findUsersAvatar(erbanNo, status, startDate, endDate);
        list.stream().forEach(item -> {
            if(item.getAdminId() != 0) {
                AdminUser adminUser = adminUserService.getAdminUserById(item.getAdminId());
                item.setOptionName(adminUser.getUsername());
            }else{
                item.setOptionName("未知");
            }
        });
        return new PageInfo<>(list);
    }

    public BusiResult updateCheckSuccess(String uids, Integer adminId) throws Exception {
        if (StringUtils.isBlank(uids)) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        String[] uidArr = uids.split(",");
        for(int i = 0; i < uidArr.length; i ++) {
            updateCheckSuccess(Long.valueOf(uidArr[i]),adminId);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult updateCheckSuccess(Long uid, Integer adminId) throws Exception {
        UsersAvatar usersAvatar = usersAvatarMapper.selectByPrimaryKey(uid);
        if (usersAvatar.getAvatarStatus().intValue() == 0 || usersAvatar.getAvatarStatus().intValue() == 2) {
            usersAvatar.setAvatarStatus((byte) 1);
            usersAvatar.setUpdateTime(new Date());
            usersAvatar.setAdminId(adminId);
            int status = usersAvatarMapper.updateByPrimaryKey(usersAvatar);
            if (status > 0) {
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(uid.toString());
                neteaseSendMsgParam.setBody("您上传的头像已经通过审核，快快去查看吧~");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
        }
        return null;
    }

    public BusiResult updateCheckFailure(Long uid, Integer adminId) throws Exception {
        UsersAvatar usersAvatar = usersAvatarMapper.selectByPrimaryKey(uid);
        if (usersAvatar.getAvatarStatus().intValue() == 0 || usersAvatar.getAvatarStatus().intValue() == 1) {
            usersAvatar.setAvatarStatus((byte) 2);
            usersAvatar.setUpdateTime(new Date());
            usersAvatar.setAdminId(adminId);
            int status = usersAvatarMapper.updateByPrimaryKey(usersAvatar);
            if (status > 0) {
                Users users = usersService.getUsersByUid(uid);
                if (users.getGender() == null || users.getGender().intValue() == 1) {
                    users.setAvatar("https://pic.mjiawl.com/default_head_nan.png");
                } else {
                    users.setAvatar("https://pic.mjiawl.com/default_head_nv.png");
                }
                users.setUpdateTime(new Date());
                BaseNetEaseRet baseNetEaseRet = erBanNetEaseService.updateUserInfo(uid.toString(), users.getNick(), users.getAvatar());
                if (200 != baseNetEaseRet.getCode()) {
                    return new BusiResult(BusiStatus.SERVERERROR, "云信更新失败" + baseNetEaseRet.getDesc(), "");
                }
                usersService.updateUser(users);
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(uid.toString());
                neteaseSendMsgParam.setBody("您上传的头像未通过审核，请遵守平台规则，共建绿色平台！");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
        }
        return null;
    }

}

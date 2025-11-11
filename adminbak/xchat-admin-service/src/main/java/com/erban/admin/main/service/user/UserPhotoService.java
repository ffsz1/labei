package com.erban.admin.main.service.user;

import com.erban.admin.main.dto.UsersPhotoDTO;
import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.PrivatePhoto;
import com.erban.main.mybatismapper.PrivatePhotoMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPhotoService {

    private final Gson gson = new Gson();
    @Autowired
    private PrivatePhotoMapper privatePhotoMapper;
    @Autowired
    private UserMapperExpand userMapperExpand;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private SendSysMsgService sendSysMsgService;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<UsersPhotoDTO> getListWithPage(Long erbanNo, Integer status, String startDate, String endDate,
                                                   int pageNumber,
                                                   int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<UsersPhotoDTO> list = userMapperExpand.findUsersPhoto(erbanNo, status, startDate, endDate);
        list.stream().forEach(item ->{
            if(item.getAdminId() != 0) {
                AdminUser adminUser = adminUserService.getAdminUserById(item.getAdminId());
                item.setOptionName(adminUser.getUsername());
            }

        });
        return new PageInfo<>(list);
    }

    public BusiResult updateCheckSuccess(String pids, Integer adminId) {
        if (StringUtils.isBlank(pids)) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        String[] uidArr = pids.split(",");
        for(int i = 0; i < uidArr.length; i ++) {
            updateCheckSuccess(Long.valueOf(uidArr[i]),adminId);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult updateCheckSuccess(Long id, Integer adminId) {
        PrivatePhoto privatePhoto = privatePhotoMapper.selectByPrimaryKey(id);
        if (privatePhoto.getPhotoStatus().intValue() == 0 || privatePhoto.getPhotoStatus().intValue() == 2) {
            privatePhoto.setPhotoStatus((byte) 1);
            privatePhoto.setAdminId(adminId);
            int status = privatePhotoMapper.updateByPrimaryKey(privatePhoto);
            if (status > 0) {
                jedisService.hset(RedisKey.private_photo.getKey() + privatePhoto.getUid(), privatePhoto.getPid().toString(), gson.toJson(privatePhoto));
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(privatePhoto.getUid().toString());
                neteaseSendMsgParam.setBody("您上传的相片已经通过审核，快快去查看吧~");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
        }
        return null;
    }

    public BusiResult updateCheckFailure(Long id, Integer adminId) {
        PrivatePhoto privatePhoto = privatePhotoMapper.selectByPrimaryKey(id);
        if (privatePhoto.getPhotoStatus().intValue() == 0 || privatePhoto.getPhotoStatus().intValue() == 1) {
            privatePhoto.setPhotoStatus((byte) 2);
            privatePhoto.setAdminId(adminId);
            int status = privatePhotoMapper.updateByPrimaryKey(privatePhoto);
            if (status > 0) {
                jedisService.hdel(RedisKey.private_photo.getKey() + privatePhoto.getUid(), privatePhoto.getPid().toString());
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(privatePhoto.getUid().toString());
                neteaseSendMsgParam.setBody("您上传的相片并未通过审核，请遵守平台规则，共建绿色平台！");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } else {
            return new BusiResult(BusiStatus.ADMIN_DATE_SUCCESS);
        }
        return null;
    }
}

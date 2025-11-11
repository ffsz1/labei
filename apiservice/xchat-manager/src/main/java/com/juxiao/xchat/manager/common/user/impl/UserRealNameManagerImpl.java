package com.juxiao.xchat.manager.common.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DataValidationUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.UserRealNameDao;
import com.juxiao.xchat.dao.user.dto.UserRealNameDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.enumeration.UserRealNameAuditStatus;
import com.juxiao.xchat.manager.common.base.CacheBaseManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08 15:31
 */
@Slf4j
@Service
public class UserRealNameManagerImpl extends CacheBaseManager<UserRealNameDTO, UserRealNameDTO> implements UserRealNameManager {
    @Autowired
    private UserRealNameDao userRealNameDao;

    @Autowired
    private SysConfManager sysConfManager;

    @Autowired
    private UsersManager usersManager;

    @Override
    public int save(UserRealNameDTO userRealNameDto) {
        int result = userRealNameDao.save(userRealNameDto);
        if (result == 1) {
            saveOneCache(userRealNameDto, RedisKey.user_real_name.getKey(), userRealNameDto.getUid().toString());
        }
        return result;
    }

    @Override
    public UserRealNameDTO getOneByJedisId(String jedisId) {
        return getOne(RedisKey.user_real_name.getKey(), jedisId, "select * from user_real_name where uid = ?", jedisId);
    }

    @Override
    public void verifyUserRealName(Long uid, String type) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        SysConfigId configId;
        try {
            configId = SysConfigId.valueOf(SysConfigId.real_name_option.name() + "_" + type);
        } catch (IllegalArgumentException e) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        SysConfDTO sysConf = sysConfManager.getSysConf(SysConfigId.real_name_option);
        if (sysConf == null || "0".equals(sysConf.getConfigValue())) {
            return;
        }

        // 验证开关，0为关闭，1为实名，2为绑定手机号
        SysConfDTO optSysConf = sysConfManager.getSysConf(configId);
        if (optSysConf == null || "0".equals(optSysConf.getConfigValue())) {
            return;
        }

        UserRealNameDTO realNameDto = this.getOneByJedisId(String.valueOf(uid));
        if (realNameDto != null && UserRealNameAuditStatus.VERIFIED.checkValue(realNameDto.getAuditStatus())) {
            return;
        }

        if ("2".equals(optSysConf.getConfigValue())) {
            // 验证手机
            String phone = usersDto.getPhone();
            if (DataValidationUtils.validatePhone(phone)) {
                return;
            }
            //创建房间
            if(type.equals("openroom")){
                throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NEED_PHONE_OPEN_ROOM,true);
            }//房间内发言
            else if(type.equals("sendtext")){
                throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NEED_PHONE_ROOM_CHAT,true);
            }//大厅内发言
            else if(type.equals("sendpublic")){
                throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NEED_PHONE_HALL_CHAT,true);
            }
        }

        if (realNameDto != null && UserRealNameAuditStatus.AUDITING.checkValue(realNameDto.getAuditStatus())) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_AUDITING_CHAT,true);
        }
        //创建房间
        if(type.equals("openroom")){
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_OPEN_ROOM,true);
        }//房间内发言
        else if(type.equals("sendtext")){
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_ROOM_CHAT,true);
        }//大厅内发言
        else if(type.equals("sendpublic")){
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_HALL_CHAT,true);
        }
    }

    @Override
    public UserRealNameDTO entityToCache(UserRealNameDTO entity) {
        // StringBuilder str = new StringBuilder(entity.getRealName());
        // entity.setRealName(str.replace(1, entity.getRealName().length(), "*").toString());
        // StringBuilder str = new StringBuilder(entity.getPhone());
        // entity.setPhone(str.replace(3,7,"****").toString());
        StringBuilder str = new StringBuilder(entity.getIdCardNo());
        entity.setIdCardNo(str.replace(entity.getIdCardNo().length() - 4, entity.getIdCardNo().length(), "****").toString());
        return entity;
    }
}

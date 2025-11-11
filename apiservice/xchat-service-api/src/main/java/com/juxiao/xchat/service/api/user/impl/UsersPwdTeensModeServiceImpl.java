package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.UsersPwdTeensModeDao;
import com.juxiao.xchat.dao.user.domain.UsersPwdTeensMode;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.UsersPwdTeensModeService;
import com.juxiao.xchat.service.api.user.vo.UsersTeensModeVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author chris
 * @date 2019-07-03
 */
@Service
public class UsersPwdTeensModeServiceImpl implements UsersPwdTeensModeService {
    @Resource
    private UsersManager usersManager;

    @Resource
    private UsersPwdTeensModeDao usersPwdTeensModeDao;

    /**
     * 获取青少年信息
     *
     * @param uid 用户UID
     * @return UsersTeensModeVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public UsersTeensModeVO getUsersTeensMode(Long uid) throws WebServiceException {
        UsersDTO usersDTO = checkParams(uid);
        UsersPwdTeensMode usersPwdTeensMode = usersPwdTeensModeDao.selectUsersPwdTeensModeByUid(usersDTO.getUid());
        UsersTeensModeVO usersTeensModeVO = new UsersTeensModeVO();
        if (usersPwdTeensMode == null) {
            return usersTeensModeVO;
        }
        usersTeensModeVO.setCipherCode(usersPwdTeensMode.getPwd());
        usersTeensModeVO.setErbanNo(usersDTO.getErbanNo());
        usersTeensModeVO.setNick(usersDTO.getNick());
        usersTeensModeVO.setUid(usersDTO.getUid());
        return usersTeensModeVO;
    }

    /**
     * 保存青少年模式信息或更新青少年模式密码
     *
     * @param uid        用户UID
     * @param cipherCode 青少年模式密码
     * @return WebServiceMessage
     * @throws WebServiceException WebServiceException
     */
    @Override
    public WebServiceMessage saveOrUpdate(Long uid, String cipherCode) throws WebServiceException {
        UsersDTO usersDTO = checkParams(uid);
        UsersPwdTeensMode usersPwdTeensMode = new UsersPwdTeensMode();
        usersPwdTeensMode.setCreateDate(new Date());
        usersPwdTeensMode.setPwd(cipherCode);
        usersPwdTeensMode.setUid(usersDTO.getUid());
        int result = usersPwdTeensModeDao.saveOrUpdate(usersPwdTeensMode);
        return WebServiceMessage.success(null);
    }

    /**
     * 校验密码
     *
     * @param uid        用户UID
     * @param cipherCode 青少年模式密码
     * @return WebServiceMessage
     * @throws WebServiceException WebServiceException
     */
    @Override
    public WebServiceMessage checkCipherCode(Long uid, String cipherCode) throws WebServiceException {
        UsersDTO usersDTO = checkParams(uid);
        UsersPwdTeensMode usersPwdTeensMode = usersPwdTeensModeDao.selectUsersPwdTeensModeByUid(usersDTO.getUid());
        if (usersPwdTeensMode == null) {
            return WebServiceMessage.success(false);
        }
        if (cipherCode.equalsIgnoreCase(usersPwdTeensMode.getPwd())) {
            return WebServiceMessage.success(true);
        }
        return WebServiceMessage.success(false);
    }

    private UsersDTO checkParams(Long uid) throws WebServiceException {
        if (uid == null && uid == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        return usersDTO;
    }

    /**
     * 关闭青少年模式
     *
     * @param uid 用户UID
     * @return WebServiceMessage
     */
    @Override
    public WebServiceMessage closeTeensMode(Long uid) throws WebServiceException {
        UsersDTO usersDTO = checkParams(uid);
        UsersPwdTeensMode usersPwdTeensMode = usersPwdTeensModeDao.selectUsersPwdTeensModeByUid(usersDTO.getUid());
        if (usersPwdTeensMode == null) {
            return WebServiceMessage.success(false);
        }

        usersPwdTeensModeDao.deleteTeensMode(uid);
        return WebServiceMessage.success("关闭成功!");
    }
}

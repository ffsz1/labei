package com.juxiao.xchat.service.api.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.UserRealNameDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.UserRealNameDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.enumeration.UserRealNameAuditStatus;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.bo.UserRealMessageBO;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.user.UserRealNameService;
import com.juxiao.xchat.service.api.user.vo.UserRealNameVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户实名认证实现
 *
 * @author chris
 * @Title:
 * @date 2019-05-08 15:49
 */
@Service
@Slf4j
public class UserRealNameServiceImpl implements UserRealNameService {
    /**
     * 身份证号允许绑定的数量
     */
    public static final int ID_CARD_NO_LIMIT = 5;

    /**
     * 限制版本号
     */
    private final long LIMIT_VERSION = Utils.version2long("1.1.0");

    @Resource
    private UsersManager usersManager;

    @Resource
    private UserRealNameManager userRealNameManager;

    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager netEaseSmsManager;

    @Resource
    private UserRealNameDao userRealNameDao;

    @Resource
    private RedisManager redisManager;

    @Resource
    private AccountDao accountDao;

    @Resource
    private UsersDao usersDao;

    @Resource
    private ActiveMqManager activeMqManager;

    @Resource
    private Gson gson;

    @Override
    public void getSmsCode(Long uid, String phone, String deviceId) throws Exception {
        if (uid == null || StringUtils.isBlank(phone)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        if (StringUtils.isBlank(phone)) {
            throw new WebServiceException("手机号不能为空");
        }

        String beforePhone = usersDto.getPhone();

        if (StringUtils.isBlank(beforePhone)) {
            UsersDO usersDo = new UsersDO();
            usersDo.setUid(uid);
            usersDo.setPhone(phone.trim());
            usersDao.update(usersDo);
        }
        // else {
        //     if (!beforePhone.equals(phone)) {
        //         throw new WebServiceException("认证手机与登录手机不一致");
        //     }
        // }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = netEaseSmsManager.sendSms(phone, deviceId, null, code);
        if (ret == null) {
            throw new WebServiceException(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            throw new WebServiceException("短信发送失败：" + ret.getMsg());
        }
    }

    @Override
    public void saveUserRealName(Long uid, String realName, String idcardNo,
                                 String idcardFront, String appVersion, String idcardOpposite, String idcardHandheld) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(realName) || StringUtils.isBlank(idcardNo) || StringUtils.isBlank(idcardFront)
                || StringUtils.isBlank(idcardOpposite)) {
//                || StringUtils.isBlank(idcardOpposite) || StringUtils.isBlank(idcardHandheld)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

//        if (Utils.version2long(appVersion) < LIMIT_VERSION) {
//            throw new WebServiceException("请更新版本");
//        }

        UserRealNameDTO realNameDto = userRealNameManager.getOneByJedisId(String.valueOf(uid));
        if (realNameDto != null) {
            if (UserRealNameAuditStatus.VERIFIED.checkValue(realNameDto.getAuditStatus())) {
                throw new WebServiceException(WebServiceCode.USER_REAL_NAME_VERIFIED);
            }

            if (UserRealNameAuditStatus.AUDITING.checkValue(realNameDto.getAuditStatus())) {
                throw new WebServiceException(WebServiceCode.USER_REAL_NAME_AUDITING);
            }
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // if (!usersDto.getErbanNo().toString().equals(usersDto.getPhone()) && StringUtils.isNotBlank(usersDto
        // .getPhone())
        //        && !usersDto.getPhone().equals(phone)) {
        //    // 绑定了手机号, 但是输入的手机号和绑定的不一致
        //    throw new WebServiceException(WebServiceCode.PHONE_INVALID);
        //

        // 查询身份证绑定账号的数量
        int count = userRealNameDao.countIdCardNo(idcardNo);
        if (count >= ID_CARD_NO_LIMIT) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_ID_CARD_NO_LIMIT);
        }

        // boolean isSmsCode = netEaseSmsManager.verifySmsCode(phone, smsCode);
        // if (!isSmsCode) {
        //     throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        // }

        realNameDto = new UserRealNameDTO();
        realNameDto.setUid(uid);
        realNameDto.setRealName(realName);
        realNameDto.setIdCardNo(idcardNo);
        realNameDto.setIdCardFront(idcardFront);
        realNameDto.setIdCardOpposite(idcardOpposite);
        realNameDto.setIdCardHandheld(" ");
        realNameDto.setAuditStatus((byte) 0);
        realNameDto.setCreateDate(new Date());
        // realNameDto.setPhone(phone);
        userRealNameDao.save(realNameDto);

        // if (usersDto.getPhone().length() < 11){// 实名时判断是否是erbanNo
        //     redisManager.hdel(RedisKey.phone_uid.getKey(), phone);
        //
        //     AccountDO accountDo = new AccountDO();
        //     accountDo.setUid(uid);
        //     accountDo.setPhone(phone);
        //     accountDao.update(accountDo);
        //
        //     UsersDO userDo = new UsersDO();
        //     userDo.setUid(uid);
        //     userDo.setPhone(phone);
        //     usersDao.update(userDo);
        //     redisManager.hdel(RedisKey.user.getKey(), String.valueOf(uid));
        // }

        redisManager.hdel(RedisKey.user_real_name.getKey(), uid.toString());
        sendQueueMessage(usersDto);
    }

    /**
     * 发送队列消息
     *
     * @param usersDTO
     */
    public void sendQueueMessage(UsersDTO usersDTO) {
        if (usersDTO == null) {
            return;
        }
        UserRealMessageBO messageBO = new UserRealMessageBO(usersDTO);
        activeMqManager.sendQueueMessage(MqDestinationKey.USER_REAL_QUEUE, gson.toJson(messageBO));
    }

    @Override
    public UserRealNameVO getUserRealName(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserRealNameDTO realNameDto = userRealNameManager.getOneByJedisId(String.valueOf(uid));
        if (realNameDto == null) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NTO_VERIFIED);
        }

        UserRealNameVO realNameVo = new UserRealNameVO();
        StringBuilder str = new StringBuilder(realNameDto.getRealName());
        String realName = str.replace(1, realNameDto.getRealName().length(), "*").toString();
        realNameVo.setRealName(realName);
        str = new StringBuilder(realNameDto.getIdCardNo());
        realNameVo.setIdcardNo(str.replace(realNameDto.getIdCardNo().length() - 4, realNameDto.getIdCardNo().length()
                , "****").toString());
        realNameVo.setAuditStatus(realNameDto.getAuditStatus());
        realNameVo.setCreateDate(realNameDto.getCreateDate());
        realNameVo.setRemark(realNameDto.getRemark());
        // str = new StringBuilder(realNameDto.getPhone());
        // realNameVo.setPhone(str.replace(3, 7, "****").toString());
        return realNameVo;
    }

	@Override
	public UserRealNameVO getValidateUserRealName(Long uid) throws WebServiceException {
		if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserRealNameDTO realNameDto = userRealNameManager.getOneByJedisId(String.valueOf(uid));
        if (realNameDto == null||realNameDto.getAuditStatus()==2) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NOT_VERIFIED_ERROR);
        }else if(realNameDto.getAuditStatus()==0) {
        	throw new WebServiceException(WebServiceCode.USER_REAL_NAME_UNDER_REVIEW_ERROR);
        }

        UserRealNameVO realNameVo = new UserRealNameVO();
        StringBuilder str = new StringBuilder(realNameDto.getRealName());
        String realName = str.replace(1, realNameDto.getRealName().length(), "*").toString();
        realNameVo.setRealName(realName);
        str = new StringBuilder(realNameDto.getIdCardNo());
        realNameVo.setIdcardNo(str.replace(realNameDto.getIdCardNo().length() - 4, realNameDto.getIdCardNo().length()
                , "****").toString());
        realNameVo.setAuditStatus(realNameDto.getAuditStatus());
        realNameVo.setCreateDate(realNameDto.getCreateDate());
        realNameVo.setRemark(realNameDto.getRemark());
        return realNameVo;
	}
}

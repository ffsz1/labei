package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.IOSData;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DataValidationUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import com.juxiao.xchat.dao.sysconf.SmsRecordDao;
import com.juxiao.xchat.dao.sysconf.domain.SmsRecordDO;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.IdfaClickRecordDao;
import com.juxiao.xchat.dao.user.PrivatePhotoDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.domain.AccountDO;
import com.juxiao.xchat.dao.user.domain.IdfaClickRecordDO;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.*;
import com.juxiao.xchat.dao.user.query.UserNewQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.sysconf.GeneralManager;
import com.juxiao.xchat.manager.common.user.PrivatePhotoManager;
import com.juxiao.xchat.manager.common.user.UserConfigureManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMemberBO;
import com.juxiao.xchat.manager.external.netease.NetEaseAccManager;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;
import com.juxiao.xchat.manager.external.qiniu.QiniuManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.bo.BindPhoneMessageBO;
import com.juxiao.xchat.manager.mq.bo.UpdateUserInfoMessageBO;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.sysconf.SensitiveWordService;
import com.juxiao.xchat.service.api.sysconf.enumeration.SensitiveWordEnum;
import com.juxiao.xchat.service.api.user.UsersService;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.api.user.bo.UserUpdateBO;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @class: UsersServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Service
public class UsersServiceImpl implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private SmsRecordDao recordDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private GiftCarManager carManager;
    @Autowired
    private HeadwearManager headwearManager;
    @Autowired
    private ImRoomManager imroomManager;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private NetEaseAccManager neteaseAccManager;
    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager neteaseSmsManager;
    @Autowired
    private PrivatePhotoManager photoManager;
    @Autowired
    private QiniuManager qiniuManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserConfigureManager configureManager;
    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private ActiveMqManager activeMqManager;

    @Autowired
    private SystemConf systemConf;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PrivatePhotoDao photoDao;

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private IdfaClickRecordDao idfaClickRecordDao;


    @Override
    public UserVO getUser(Long uid, Long queryUid) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO userDto = usersManager.getUser(uid);
        UserVO userVo = new UserVO();
        userVo.setCarName("");
        userVo.setCarUrl("");
        userVo.setHeadwearName("");
        userVo.setHeadwearUrl("");
        if (userDto == null) {
            AccountDTO account = accountDao.getAccount(uid);
            if (account == null) {
                throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
            }

            userVo.setUid(account.getUid());
            userVo.setErbanNo(account.getErbanNo());
        } else {
            BeanUtils.copyProperties(userDto, userVo);
        }

        List<PrivatePhotoDTO> photos = photoManager.listUserPrivatePhoto(uid);
        List<PrivatePhotoDTO> unaudited = photoDao.listUserPrivatePhoto(uid, 0);
        if (unaudited != null && unaudited.size() > 0) {
            for (PrivatePhotoDTO privatePhotoDTO : unaudited) {
                privatePhotoDTO.setPhotoUrl(systemConf.getDefaultPhoto());
            }
            photos.addAll(unaudited);
        }
        userVo.setPrivatePhoto(photos);


        GiftCarDTO carDto = carManager.getUserGiftCar(uid);
        if (carDto != null) {
            userVo.setCarUrl(carDto.getVggUrl());
            userVo.setCarName(carDto.getCarName());
        }

        int expLevel = levelManager.getUserExperienceLevelSeq(uid);
        userVo.setExperLevel(expLevel);

        int charmLevel = levelManager.getUserCharmLevelSeq(uid);
        userVo.setCharmLevel(charmLevel);

        HeadwearDTO headwearDto = headwearManager.getUserHeadwear(uid);
        if (headwearDto != null) {
            if (headwearDto.getHeadwearId() == 1) {
                userVo.setHeadwearUrl("https://pic.chaoxuntech.com/headwear_level_" + expLevel + ".png");
                userVo.setHeadwearName(expLevel + headwearDto.getHeadwearName());
            } else {
                userVo.setHeadwearUrl(headwearDto.getPicUrl());
                userVo.setHeadwearName(headwearDto.getHeadwearName());
            }
        }
        //
        UserConfigureDTO dto = configureManager.getUserConfigure(uid);
        userVo.setFindNewUsers(dto == null ? (byte) 0 : dto.getNewUsers());
        UserExtendDTO extendDTO = usersManager.getUserExtend(uid);
        if (extendDTO == null) {
            userVo.setLiveness(10);
        } else {
            userVo.setLiveness(extendDTO.getLiveness());
        }

        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO != null) {
            if (StringUtils.isNotBlank(accountDTO.getWeixinOpenid())) {
                userVo.setHasWx(true);
                userVo.setWeixinNick(accountDTO.getWeixinNick());
            } else {
                userVo.setHasWx(false);
            }

            if (StringUtils.isNotBlank(accountDTO.getQqOpenid())) {
                userVo.setHasQq(true);
                userVo.setQqNick(accountDTO.getQqNick());
            } else {
                userVo.setHasQq(false);
            }

            if (StringUtils.isNotBlank(accountDTO.getAppleUser())) {
                userVo.setHasApple(true);
                userVo.setAppleUserName(accountDTO.getAppleUserName());
            } else {
                userVo.setHasApple(false);
            }
            try {
                //TODO upline change
                String uidStr = String.valueOf(accountDTO.getUid());
                neteaseAccManager.createNetEaseAcc(uidStr, accountDTO.getNeteaseToken(), "");
                neteaseAccManager.updateUserInfo(uidStr, userVo.getNick(), userVo.getAvatar());
            } catch (Exception e) {
                try {
                    //TODO upline change
                    String uidStr = String.valueOf(accountDTO.getUid());
                    neteaseAccManager.updateUserInfo(uidStr, userVo.getNick(), userVo.getAvatar());
                } catch (Exception er) {
                    logger.error(" [ user_get updateNetEaseAcc fail ] " + accountDTO.getUid(), e);
                }
                logger.error(" [ user_get createNetEaseAcc fail ] " + accountDTO.getUid(), e);
            }

        } else {
            userVo.setHasWx(false);
            userVo.setHasQq(false);
        }
        UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(uid);
        userVo.setUserShowState("9");
        if (userPurseDTO != null) {
            userVo.setGoldNum(userPurseDTO.getGoldNum());
            if (accountDTO != null) {
                if (accountDTO.getSignTime().after(DateUtils.duDate(new Date(), -7))) {//7天内注册的 不是首次充值就是新晋 否则非新晋
                    if (userPurseDTO.getIsFirstCharge() == false) {
                        userVo.setUserShowState("0");
                    } else {
                        userVo.setUserShowState("1");
                    }
                }
            }
        }
        UserMcoinPurseDTO userMcoinPurseDTO = mcoinManager.getUserMcoinPurse(uid);
        if (userMcoinPurseDTO != null) {
            userVo.setMcoinNum(userMcoinPurseDTO.getMcoinNum());
        }
        this.updateMemberInfo(userVo);
        return userVo;
    }

    @Override
    public UserVO getUserV5(Long uid) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO userDto = usersManager.getUser(uid);
        UserVO userVo = new UserVO();
        if (userDto == null) {
            AccountDTO account = accountDao.getAccount(uid);
            if (account == null) {
                throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
            }

            userVo.setUid(account.getUid());
            userVo.setNick("");
            userVo.setAvatar("");
            userVo.setGender((byte) 0);
            userVo.setDefUser((byte) 1);
            userVo.setCreateTime(new Date());
        } else {
            userVo.setUid(userDto.getUid());
            userVo.setNick(userDto.getNick());
            userVo.setAvatar(userDto.getAvatar());
            userVo.setGender(userDto.getGender());
            userVo.setDefUser(userDto.getDefUser());
            userVo.setCreateTime(userDto.getCreateTime());
        }

        GiftCarDTO carDto = carManager.getUserGiftCar(uid);
        if (carDto != null) {
            userVo.setCarUrl(carDto.getVggUrl());
            userVo.setCarName(carDto.getCarName());
        } else {
            userVo.setCarUrl("");
            userVo.setCarName("");
        }

        int expLevel = levelManager.getUserExperienceLevelSeq(uid);
        userVo.setExperLevel(expLevel);

        int charmLevel = levelManager.getUserCharmLevelSeq(uid);
        userVo.setCharmLevel(charmLevel);


        HeadwearDTO headwearDto = headwearManager.getUserHeadwear(uid);
        if (headwearDto != null) {
            userVo.setHeadwearUrl(headwearDto.getPicUrl());
            userVo.setHeadwearName(headwearDto.getHeadwearName());
        } else {
            userVo.setHeadwearUrl("");
            userVo.setHeadwearName("");
        }

        return userVo;
    }

    /**
     * 绑定手机
     *
     * @param uid
     * @param phone
     * @param code
     * @throws WebServiceException
     */
    @Override
    public boolean bindPhone(Long uid, String phone, String code) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO user = usersManager.getUserByPhone(phone);
        if (user != null && user.getPhone().length() == 11) {
            throw new WebServiceException(WebServiceCode.PHONE_IS_EXISTS);
        }

        String tempPhone = usersDto.getPhone();
        boolean isRightCode = neteaseSmsManager.verifySmsCode(phone, code);
        if (!isRightCode) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);

        AccountDO accountDo = new AccountDO();
        accountDo.setUid(uid);
        accountDo.setPhone(phone);
        accountDao.update(accountDo);

        UsersDO userDo = new UsersDO();
        userDo.setUid(uid);
        userDo.setPhone(phone);
        usersDao.update(userDo);

        usersDto.setPhone(phone);
        redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
        if (StringUtils.isNotBlank(phone) && !DataValidationUtils.validatePhone(tempPhone)) {
            sendQueueMessage(uid, phone);
        }
        return true;
    }

    /**
     * 获取绑定手机验证码
     *
     * @param ip
     * @param phone
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @Override
    public WebServiceMessage getBoundPhoneCode(String ip, String phone, String deviceId, String imei, String os,
                                               String osversion, String channel, String appVersion, String model) throws Exception {
        if (!DataValidationUtils.validatePhone(phone)) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_INVALID);
        }

        int phoneCount = accountDao.countAccountPhone(phone);
        if (phoneCount > 0) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_IS_EXISTS);
        }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = neteaseSmsManager.sendSms(phone, deviceId, null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);

        SmsRecordDO recordDo = new SmsRecordDO();
        recordDo.setPhone(phone);
        recordDo.setIp(ip);
        recordDo.setDeviceId(deviceId);
        recordDo.setImei(imei);
        recordDo.setOs(os);
        recordDo.setOsversion(osversion);
        recordDo.setChannel(channel);
        recordDo.setAppVersion(appVersion);
        recordDo.setModel(model);
        recordDo.setSmsCode(code + "-" + ret.getMsg());
        recordDo.setSmsType((byte) 4);
        recordDao.save(recordDo);
        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }

    /**
     * 点击获取验证码
     *
     * @param ip
     * @param uid
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @Override
    public WebServiceMessage getCode(String ip, Long uid, String deviceId, String imei, String os, String osversion,
                                     String channel, String appVersion, String model) throws Exception {
        if (uid == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String phone = usersDto.getPhone();
        if (StringUtils.isBlank(phone)) {
            return new WebServiceMessage(WebServiceCode.PHONE_INVALID.getValue(), "您还没有绑定手机号码，请先绑定手机号！");
        }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = neteaseSmsManager.sendSms(phone, deviceId, null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);

        SmsRecordDO recordDo = new SmsRecordDO();
        recordDo.setPhone(phone);
        recordDo.setIp(ip);
        recordDo.setDeviceId(deviceId);
        recordDo.setImei(imei);
        recordDo.setOs(os);
        recordDo.setOsversion(osversion);
        recordDo.setChannel(channel);
        recordDo.setAppVersion(appVersion);
        recordDo.setModel(model);
        recordDo.setSmsCode(code + "-" + ret.getMsg());
        recordDo.setSmsType((byte) 5);
        recordDao.save(recordDo);
        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }

    @Override
    public void isBindPhone(Long uid) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UsersDTO userDto = usersManager.getUser(uid);
        if (userDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        String phone = userDto.getPhone();
        if (DataValidationUtils.validatePhone(phone)) {
            return;
        }
        throw new WebServiceException(WebServiceCode.PHONE_INVALID);
    }

    @Override
    public UserVO saveOrUpdateUser(UserUpdateBO updateBo, DeviceInfoBO deviceinfo) throws Exception {
        // 判断用户更新对象是否为空或者UID是否为空
        if (updateBo == null || updateBo.getUid() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 判断昵称是否为空并且昵称的长度是否大于15
        if (StringUtils.isNotBlank(updateBo.getNick()) && updateBo.getNick().length() > 15) {
            throw new WebServiceException(WebServiceCode.NICK_TOO_LONG);
        }

        // 判断昵称是否包含敏感词
        if (hasSensitiveWords(updateBo.getNick())) {
            throw new WebServiceException(WebServiceCode.NICK_SENSITIVE_WORDS);
        }

        // 判断是否存在账户信息
        AccountDTO accountDto = accountDao.getAccount(updateBo.getUid());
        if (accountDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 判断若用户不为空用户能否修改资料
        UsersDTO userDto = usersManager.getUser(updateBo.getUid());
        if (userDto != null) {
            if (generalManager.checkProhibitModification()) {
                throw new WebServiceException(WebServiceCode.CHECK_PROHIBIT_MODIFICATION_ERROR);
            }
        }

        // 判断用户信息是否为空则为第一次登录且没有设置性别
        if (null == userDto && updateBo.getGender() == null) {
            throw new WebServiceException(WebServiceCode.USER_UPDATE_GENDER);
        }

        // 复制BO到DO对象
        UsersDO usersDo = new UsersDO();
        BeanUtils.copyProperties(updateBo, usersDo);

        if (!StringUtils.isBlank(updateBo.getAvatar()) && !updateBo.getAvatar().contains("qc422frwu.bkt.clouddn.com") && !"/0".equalsIgnoreCase(updateBo.getAvatar())) {
            try {
                String fileName = qiniuManager.uploadByUrl(updateBo.getAvatar());
                // 图片迁移到七牛
                String newAvatar = qiniuManager.mergeUrlAndSlim(fileName);
                usersDo.setAvatar(newAvatar);
            } catch (Exception e) {
                logger.error("[ 更新用户信息 ]上传图片到七牛出现异常，请求：uid={},avatar={}", updateBo.getUid(), updateBo.getAvatar(), e);
            }
        } else {
            if (StringUtils.isNotBlank(updateBo.getAvatar())) {
                boolean isCan = qiniuManager.imageCensor(updateBo.getAvatar());
                // 把上传上来的图片插入待审核记录
                String newAvatar = "";
                if (isCan) {
                    usersDo.setAvatar(updateBo.getAvatar());
                    jdbcTemplate.update("REPLACE INTO `users_avatar` (`uid`, `avatar`, `avatar_status`, " +
                                    "`create_time`, `update_time`) VALUES (?,?,?,now(),now())", updateBo.getUid(), newAvatar,
                            0);
                } else {
                    if (userDto == null) {
                        if (usersDo.getGender() == null || usersDo.getGender().intValue() == 1) {
                            usersDo.setAvatar(systemConf.getDefaultHeadNan());
                        } else {
                            usersDo.setAvatar(systemConf.getDefaultHeadNv());
                        }
                    } else {
                        usersDo.setAvatar(userDto.getAvatar());
                    }
                    jdbcTemplate.update("REPLACE INTO `users_avatar` (`uid`, `avatar`, `avatar_status`, " +
                                    "`create_time`, `update_time`) VALUES (?,?,?,now(),now())", updateBo.getUid(),
                            updateBo.getAvatar(), 2);
                    asyncNetEaseTrigger.sendMsg(updateBo.getUid().toString(), "您上传的头像未通过审核，请遵守平台规则，共建绿色平台！");
                }
            }
        }

        // 用户新增或更新操作时返回操作类型: 1.更新用户资料; 2.第一次注册补全用户资料
        byte operType;
        // 如果有被邀请人, 判断是被邀请人是否能收到邀请红包
        boolean hasRegPacket = false;
        // 若用户信息为空则表示第一次登录
        if (null == userDto) {
            logger.info("[ 更新用户信息 ]用户第一次注册，uid:>{}", updateBo.getUid());

            operType = 2;
            // 若手机号不为空则存入手机号
            String phone = accountDto.getPhone();
            if (StringUtils.isNotBlank(phone)) {
                usersDo.setPhone(phone);
            }

            // 若手机号不为空并且手机号正确则绑定手机号(MQ)
            if (StringUtils.isNotBlank(phone) && DataValidationUtils.validatePhone(phone)) {
                // 绑定手机任务消息队列
                sendQueueMessage(accountDto.getUid(), accountDto.getPhone());
            }

            if (deviceinfo != null) {
                usersDo.setOsversion(deviceinfo.getOsVersion());
                usersDo.setApp(deviceinfo.getApp());
                usersDo.setAppVersion(deviceinfo.getAppVersion());
                usersDo.setChannel(deviceinfo.getChannel());
                usersDo.setLinkedmeChannel(deviceinfo.getLinkedmeChannel());
                usersDo.setDeviceId(deviceinfo.getDeviceId());
                usersDo.setImei(deviceinfo.getImei());
                usersDo.setIspType(deviceinfo.getIspType());
                usersDo.setModel(deviceinfo.getModel());
                usersDo.setNetType(deviceinfo.getNetType());
                usersDo.setOs(deviceinfo.getOs());
            }

            logger.info("[绑定邀请用户]shareUid:{}", updateBo.getShareUid());
            // 若分享人ID不为空并且为数字则存入分享人ID否则存入推广渠道
            if (StringUtils.isNotBlank(updateBo.getShareUid()) && StringUtils.isNumeric(updateBo.getShareUid())) {
                usersDo.setShareUid(Long.valueOf(updateBo.getShareUid()));
                if (StringUtils.isNotBlank(updateBo.getRoomUid()) && StringUtils.isNumeric(updateBo.getRoomUid())) {
                    usersDo.setRoomUid(Long.valueOf(updateBo.getRoomUid()));
                }
                hasRegPacket = true;
            } else {
                usersDo.setLinkedmeChannel(updateBo.getShareUid());
            }

            Date date = new Date();
            usersDo.setErbanNo(accountDto.getErbanNo());
            usersDo.setCreateTime(date);
            usersDo.setUpdateTime(date);
            usersDao.save(usersDo);

            // 若性别为NULL则设置默认性别为1.男; 2.女
            if (usersDo.getGender() == null) {
                usersDo.setGender((byte) 1);
            }
            // 更新网易云账户性别
            neteaseAccManager.updateUserGender(String.valueOf(usersDo.getUid()), usersDo.getGender());

            // 首次登陆

            HeadwearDTO headwearDto = headwearManager.getHeadwear(64);
            if (headwearDto != null) {
                List<String> headwearIds = headwearManager.listUserHeadwearid(usersDo.getUid());
                if (headwearIds.contains("64")) {
                    headwearIds.add("64");
                }
                redisManager.hset(RedisKey.headwear_purse_list.getKey(), usersDo.getUid().toString(),
                        StringUtils.join(headwearIds, ","));
                headwearManager.saveUserHeadwear(usersDo.getUid(), 64, headwearDto.getEffectiveTime(), 4,
                        "欢迎加入拉贝星球大家庭，" + headwearDto.getHeadwearName() + "头饰" + headwearDto.getEffectiveTime() +
                                "天已发放，" +
                                "请到商城查收并使用哦，如有任何疑问请联系拉贝星球官方客服~");
                logger.info("[ 首次登陆 ] 送头饰，uid:{} headwearId:{}>", usersDo.getUid(), 64);
            }

        } else {
            logger.info("[绑定邀请用户]shareUid:{}", updateBo.getShareUid());

            // 若分享人ID不为空并且为数字则存入分享人ID否则存入推广渠道
            if (StringUtils.isNotBlank(updateBo.getShareUid()) && StringUtils.isNumeric(updateBo.getShareUid())) {
                usersDo.setShareUid(Long.valueOf(updateBo.getShareUid()));
                if (StringUtils.isNotBlank(updateBo.getRoomUid()) && StringUtils.isNumeric(updateBo.getRoomUid())) {
                    usersDo.setRoomUid(Long.valueOf(updateBo.getRoomUid()));
                }
            } else {
                usersDo.setLinkedmeChannel(updateBo.getShareUid());
                // goldCoin(updateBo.getUid(), updateBo.getShareUid(), deviceinfo.getDeviceId(), request);
            }

            // 若邀请码不为空并且非0则存入邀请码
            if (StringUtils.isNotEmpty(updateBo.getShareCode()) && NumberUtils.toLong(updateBo.getShareCode()) != 0) {
                usersDo.setShareCode(NumberUtils.toLong(updateBo.getShareCode()));
            }

            Date date = new Date();
            operType = 1;
            usersDo.setUpdateTime(date);
            usersDo.setErbanNo(null);
            usersDo.setPhone(null);
            usersDo.setGender(null);
            usersDao.update(usersDo);
        }

        // 获取最新的用户信息存入Redis
        userDto = usersDao.getUser(updateBo.getUid());
        redisManager.hset(RedisKey.user.getKey(), String.valueOf(updateBo.getUid()), gson.toJson(userDto));

        String nick = userDto.getNick();
        String avatar = userDto.getAvatar();
        String uidStr = String.valueOf(userDto.getUid());

        // 若头像不为空则打印输出更新数据
        if (!StringUtils.isBlank(avatar)) {
            logger.info("[ 更新用户信息 ] 更新用户信息，uid:>{}, nick:>{} avatar:>{}", updateBo.getUid(), usersDo.getNick(),
                    usersDo.getAvatar());
        }

        // 更新网易云账户信息
        NetEaseRet ret = neteaseAccManager.updateUserInfo(uidStr, nick, avatar);
        if (200 != ret.getCode()) {
            logger.error("[ 更新用户信息 ] 更新网易云账号信息异常，accid:>{},异常编码:>{}", uidStr, ret.getCode());
            throw new WebServiceException(WebServiceCode.UPDATE_UINFO_ERROR);
        }

        // 更新用户信息任务消息队列
        sendQueueMessage(userDto);

        UserVO userVo = this.getUser(updateBo.getUid(), null);
        BeanUtils.copyProperties(userDto, userVo);
        userVo.setOperType(operType);
        userVo.setHasRegPacket(hasRegPacket);
        return userVo;
    }

    /**
     * 发送更新信息队列消息
     *
     * @param usersDTO
     */
    public void sendQueueMessage(UsersDTO usersDTO) {
        if (usersDTO == null) {
            return;
        }
        UpdateUserInfoMessageBO messageBO = new UpdateUserInfoMessageBO(usersDTO);
        activeMqManager.sendQueueMessage(MqDestinationKey.UPDATE_USER_INFO_QUEUE, gson.toJson(messageBO));
    }


    /**
     * 是否包括敏感词
     *
     * @param str
     * @return
     */
    private boolean hasSensitiveWords(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String sensitiveWords = sensitiveWordService.getWords(SensitiveWordEnum.nick);
        if (StringUtils.isBlank(sensitiveWords)) {
            return false;
        }
        return Pattern.compile(".*(" + sensitiveWords + ").*").matcher(str).matches();
    }

    @Override
    public List<UserVO> listUsersByUids(String uids) throws WebServiceException {
        if (StringUtils.isEmpty(uids)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        String array[] = uids.split(",");
        List<UserVO> list = Lists.newArrayList();
        UsersDTO usersDto;
        UserVO usersVo;
        for (String uid : array) {
            if (StringUtils.isBlank(uid) || !StringUtils.isNumeric(uid)) {
                continue;
            }
            usersDto = usersManager.getUser(Long.valueOf(uid));
            if (usersDto == null) {
                continue;
            }
            usersVo = new UserVO();
            BeanUtils.copyProperties(usersDto, usersVo);
            list.add(usersVo);
        }
        return list;
    }

    @Override
    public void replace(Long uid, String phone, String smsCode) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(phone) || StringUtils.isBlank(smsCode)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (!neteaseSmsManager.verifySmsCode(phone, smsCode)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        int phoneCount = accountDao.countAccountPhone(phone);
        if (phoneCount > 1) {
            throw new WebServiceException("手机号码已经被绑定");
        }

        AccountDTO accountDto = accountDao.getAccount(uid);
        if (accountDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);
        AccountDO accountDo = new AccountDO();
        accountDo.setUid(uid);
        accountDo.setPhone(phone);
        accountDao.update(accountDo);

        UsersDO usersDo = new UsersDO();
        usersDo.setUid(uid);
        usersDo.setPhone(phone);
        usersDao.update(usersDo);
        redisManager.hdel(RedisKey.user.getKey(), String.valueOf(uid));
    }

    @Override
    public Long getNetEaseTokenUid(String token) throws WebServiceException {
        if (token == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        AccountDTO accountDto = accountDao.getByNetEaseToken(token);
        if (accountDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        return accountDto.getUid();
    }

    @Override
    public int checkIdfa(String idfa) throws WebServiceException {
        if (StringUtils.isEmpty(idfa)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        String deviceId = redisManager.hget(RedisKey.idfa.getKey(), idfa);
        if (StringUtils.isBlank(deviceId)) {// ios做了特殊处理
            deviceId = redisManager.hget(RedisKey.idfa.getKey(), idfa.replaceAll("-", "").toLowerCase());
        }
        return StringUtils.isBlank(deviceId) ? 0 : 1;
    }

    @Override
    public int checkIdfaXQ(String idfa) throws WebServiceException {
        if (StringUtils.isEmpty(idfa)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String deviceId = redisManager.hget(RedisKey.idfa.getKey(), idfa);
        return StringUtils.isBlank(deviceId) ? 0 : 1;
    }

    @Override
    public int checkIdfaJY(String idfa) throws WebServiceException {
        if (StringUtils.isEmpty(idfa)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        String deviceId = redisManager.hget(RedisKey.idfa.getKey(), idfa);
        return StringUtils.isBlank(deviceId) ? 0 : 1;
    }

    @Override
    public List<UserNewDTO> listNewUsers(String app, Long uid, Byte gender, Integer pageNum, Integer pageSize) {
        Integer charmLevel = levelManager.getUserCharmLevelSeq(uid);
        Integer experienceLevel = levelManager.getUserExperienceLevelSeq(uid);
        if (charmLevel == 0 && experienceLevel == 0) {// 财富魅力等级为0的用户
            AccountDTO accountDTO = accountDao.getAccount(uid);
            if (accountDTO != null && StringUtils.isNotBlank(accountDTO.getQqOpenid()) && accountDTO.getErbanNo().equals(accountDTO.getPhone())) {// 针对QQ注册的并且未绑定手机号
                return listNewUsers4Auditing(app, gender, pageNum);
            }
        }
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 20 : pageSize;
        // 新用户按照时间排序
        List<UserNewDTO> list = usersDao.listNewUsers(new UserNewQuery(uid, gender, pageNum, pageSize));
        for (UserNewDTO newDto : list) {
            // 查询用户等级
            newDto.setExperLevel(levelManager.getUserExperienceLevelSeq(newDto.getUid()));
            // 查询魅力等级
            newDto.setCharmLevel(levelManager.getUserCharmLevelSeq(newDto.getUid()));
        }
        return list;
    }

    @Override
    public List<UserNewDTO> listNewUsers4Auditing(String app, Byte gender, Integer pageNum) {
        List<UserNewDTO> list = Lists.newArrayList();
        if (pageNum != null && pageNum == 1) {
            UserNewDTO newDto = new UserNewDTO();
            Long uid = IOSData.AUDIT_ALIST.get(0);
            UsersDTO users = usersManager.getUser(uid);
            if (users != null) {
                BeanUtils.copyProperties(users, newDto);
                newDto.setCharmLevel(1);
                newDto.setExperLevel(1);
                newDto.setAge(18);
                list.add(newDto);
            }
        }
        return list;
    }

    @Async
    void updateMemberInfo(UserVO userVo) {
        ImRoomMemberBO memberBo = new ImRoomMemberBO();
        BeanUtils.copyProperties(userVo, memberBo);
        imroomManager.updateMemberInfo(memberBo);
    }


    /**
     * 设置密码
     *
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @Override
    public WebServiceMessage setPwd(Long uid, String password, String confirmPwd) {
        if (uid == null && StringUtils.isEmpty(password)) {
            return WebServiceMessage.failure("参数不能为空!");
        }

        if (password.length() < 6) {
            return WebServiceMessage.failure("密码长度必须为6-12以上个字符!");
        }
        if (confirmPwd.length() < 6) {
            return WebServiceMessage.failure("确认密码长度必须为6-12以上个字符!!");
        }
        password = encryptPassword(password);
        int status = accountDao.updateByPassword(uid, password);
        if (status > 0) {
            return WebServiceMessage.success("设置成功!");
        } else {
            return WebServiceMessage.failure("设置失败!");
        }
    }


    /**
     * 修改登陆密码
     *
     * @param uid
     * @param oldPwd
     * @param password
     * @param confirmPwd
     * @return
     */
    @Override
    public WebServiceMessage modifyPwd(Long uid, String oldPwd, String password, String confirmPwd) {
        oldPwd = encryptPassword(oldPwd);
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (!oldPwd.equals(accountDTO.getPassword())) {
            return WebServiceMessage.failure("输入当前登录密码不正确,请确认!");
        }
        if (!confirmPwd.equals(password)) {
            return WebServiceMessage.failure("确认密码与新登陆密码不一致,请确认!");
        }
        confirmPwd = encryptPassword(confirmPwd);
        int status = accountDao.updateByPassword(uid, confirmPwd);
        if (status > 0) {
            return WebServiceMessage.success("修改成功!");
        } else {
            return WebServiceMessage.failure("修改失败!");
        }
    }

    /**
     * 设置二级密码
     *
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @Override
    public WebServiceMessage setSecondPwd(Long uid, String password, String confirmPwd, String code) throws Exception {
        if (uid == null || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            return WebServiceMessage.failure("参数不能为空!");
        }

        if (password.length() < 6) {
            return WebServiceMessage.failure("密码长度必须为6-12以上个字符!");
        }
        if (confirmPwd.length() < 6) {
            return WebServiceMessage.failure("确认密码长度必须为6-12以上个字符!!");
        }

        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            logger.warn("[ 设置二级密码 ]，uid【{}】对应的账号记录不存在", uid);
            return WebServiceMessage.failure("设置失败，请与客服联系!");
        }

//        if (StringUtils.isNotEmpty(accountDTO.getPasswordSecond())) {
//            return WebServiceMessage.failure("已有二级密码!");
//        }

        if (!neteaseSmsManager.verifySmsCode(accountDTO.getPhone(), code)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        password = encryptPassword(password);
        int status = accountDao.updateBySecondPassword(uid, password);
        if (status > 0) {
            return WebServiceMessage.success("设置成功!");
        } else {
            return WebServiceMessage.failure("设置失败!");
        }
    }


    /**
     * 修改二级密码
     *
     * @param uid
     * @param oldPwd
     * @param password
     * @param confirmPwd
     * @return
     */
    @Override
    public WebServiceMessage modifySecondPwd(Long uid, String oldPwd, String password, String confirmPwd, String code) throws Exception {
        if (uid == null || StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            return WebServiceMessage.failure("参数不能为空!");
        }

        if (!confirmPwd.equals(password)) {
            return WebServiceMessage.failure("确认密码与新登陆密码不一致,请确认!");
        }

        oldPwd = encryptPassword(oldPwd);
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            logger.warn("[ 设置二级密码 ]，uid【{}】对应的账号记录不存在", uid);
            return WebServiceMessage.failure("设置失败，请与客服联系!");
        }

        if (!oldPwd.equals(accountDTO.getPasswordSecond())) {
            return WebServiceMessage.failure("输入二级密码不正确,请确认!");
        }

        if (!neteaseSmsManager.verifySmsCode(accountDTO.getPhone(), code)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        confirmPwd = encryptPassword(confirmPwd);
        int status = accountDao.updateBySecondPassword(uid, confirmPwd);
        if (status > 0) {
            return WebServiceMessage.success("修改成功!");
        } else {
            return WebServiceMessage.failure("修改失败!");
        }
    }

    /**
     * 校验手机验证码
     *
     * @param ip
     * @param phone
     * @param code
     * @return
     */
    @Override
    public WebServiceMessage validateCode(String ip, String phone, String code, Long uid) throws WebServiceException {
        if (!DataValidationUtils.validatePhone(phone)) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_INVALID);
        }

        boolean isRightCode = neteaseSmsManager.verifySmsCode(phone, code);
        if (!isRightCode) {
            return WebServiceMessage.failure("手机验证码验证失败,请确认!");
        }

        AccountDTO accountDto = accountDao.getAccount(uid);
        if (DataValidationUtils.validatePhone(accountDto.getPhone())) {
            return WebServiceMessage.failure("该手机号码已绑定的手机号码不一致,请确认!");
        }
        return WebServiceMessage.success(true);
    }


    /**
     * 获取验证码
     *
     * @param ip
     * @param phone
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @Override
    public WebServiceMessage getSendSms(String ip, String phone, String deviceId, String imei, String os,
                                        String osversion, String channel, String appVersion, String model) throws Exception {
        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = neteaseSmsManager.sendSms(phone, deviceId, null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);
        SmsRecordDO recordDo = new SmsRecordDO();
        recordDo.setPhone(phone);
        recordDo.setIp(ip);
        recordDo.setDeviceId(deviceId);
        recordDo.setImei(imei);
        recordDo.setOs(os);
        recordDo.setOsversion(osversion);
        recordDo.setChannel(channel);
        recordDo.setAppVersion(appVersion);
        recordDo.setModel(model);
        recordDo.setSmsCode(code + "-" + ret.getMsg());
        recordDo.setSmsType((byte) 5);
        recordDao.save(recordDo);
        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }

    /**
     * 检测是否设置过密码
     *
     * @param uid
     * @return
     */
    @Override
    public boolean checkPwd(Long uid) {
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            return false;
        }
        if (StringUtils.isNotBlank(accountDTO.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测是否设置过二级密码
     *
     * @param uid
     * @return
     */
    @Override
    public boolean checkSecondPwd(Long uid) {
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            return false;
        }
        if (StringUtils.isNotBlank(accountDTO.getPasswordSecond())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void confirm(String phone, String smsCode) throws WebServiceException {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(smsCode)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        //检验验证码
        if (!neteaseSmsManager.verifySmsCode(phone, smsCode)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
    }

    private static String encryptPassword(String password) {
        return MD5Utils.getMD5(password);
    }

    /**
     * 发送绑定手机队列消息
     *
     * @param uid
     * @param phone
     */
    public void sendQueueMessage(Long uid, String phone) {
        BindPhoneMessageBO messageBO = new BindPhoneMessageBO();
        messageBO.setPhone(phone);
        messageBO.setUid(uid);
        messageBO.setType(1);
        activeMqManager.sendQueueMessage(MqDestinationKey.BIND_PHONE_QUEUE, gson.toJson(messageBO));
    }

    @Override
    public int insertIdfaClick(String appid, String idfa, String idfamd5, String clicktime) {
        String cache = redisManager.hget(RedisKey.idfa.getKey(), idfa);
        if (StringUtils.isNotBlank(cache)) {
            return 1;
        }
        IdfaClickRecordDO record = new IdfaClickRecordDO();
        record.setAppid(appid);
        record.setIdfa(idfa);
        record.setIdfamd5(idfamd5);
        record.setClicktime(clicktime);
        idfaClickRecordDao.insertIdfaClickRecord(record);
        redisManager.hset(RedisKey.idfa.getKey(), idfa, "1");
        return 0;
    }

    @Async
    @Override
    public void batchSyncToNetEase(Long start) throws WebServiceException {
        while (true) {
            List<UsersDTO> userList = usersDao.listUsersByPage(start);
            if (null == userList || userList.isEmpty()) {
                break;
            }
            logger.info("[ batchSyncToNetEase ] batch : start:{}", start);
            for (int i = 0; i < userList.size(); i++) {
                try {
                    UsersDTO dto = userList.get(i);
                    logger.info("[ batchSyncToNetEase ] regi progress: start:{}", start);
                    AccountDTO account = accountDao.getAccount(dto.getUid());
                    String uidStr = String.valueOf(account.getUid());
                    neteaseAccManager.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
                    neteaseAccManager.updateUserInfo(uidStr, dto.getNick(), dto.getAvatar());
                } catch (Exception e) {
                    logger.error("[ batchSyncToNetEase ] createNetEaseAcc start {}, 错误{}", start, e);
                    return;
                }
                start++;
            }

        }
    }
}

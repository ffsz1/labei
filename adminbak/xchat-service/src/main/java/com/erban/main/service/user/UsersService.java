package com.erban.main.service.user;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.HeadwearGetRecordMapper;
import com.erban.main.mybatismapper.HeadwearPurseRecordMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.param.UserParam;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.RobotService;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.SmsService;
import com.erban.main.service.api.QiniuService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.gift.GiftCarPurseService;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.headwear.HeadwearPurseService;
import com.erban.main.service.headwear.HeadwearService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.vo.*;
import com.xchat.common.constant.Constant;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.FileUploadRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.CommonUtil;
import com.xchat.common.utils.FileUtils;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.*;

@Service
public class UsersService extends BaseService {
    private static String regEx = "^[0-9]*[1-9][0-9]*$";
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private PrivatePhotoService privatePhotoService;
    @Autowired
    private QiniuService qiniuService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private GiftCarPurseService giftCarPurseService;
    @Autowired
    private GiftCarService giftCarService;
    @Autowired
    private HeadwearPurseService headwearPurseService;
    @Autowired
    private HeadwearService headwearService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;
    @Autowired
    private HeadwearPurseRecordMapper headwearPurseRecordMapperl;
    @Autowired
    private HeadwearGetRecordMapper headwearGetRecordMapper;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private UserPacketService userPacketService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GiftSendRecordService giftSendRecordService;
    @Autowired
    private RobotService robotService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private UserConfigureService userConfigureService;

    /* 绑定手机号 */
    public BusiResult bindPhone(Long uid, String phone, String code) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (smsService.verifySmsCodeByNetEase(phone, code)) {
            boundPhone(uid, phone);
            busiResult.setData(true);
//            try {
//                dutyService.updateFreshDuty(uid, DutyType.phone_bind.getDutyId());
//            } catch (Exception e) {
//            }

            return busiResult;
        } else {
            return new BusiResult(BusiStatus.SMSCODEERROR);
        }
    }

    public Users getUsersByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        UsersExample example = new UsersExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<Users> usersList = usersMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        return usersList.get(0);
    }

    public Users getUsersByErBanNo(Long erBanNo) {
        if (erBanNo == null) {
            return null;
        }
        UsersExample example = new UsersExample();
        example.createCriteria().andErbanNoEqualTo(erBanNo);
        List<Users> usersList = usersMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        return usersList.get(0);
    }

    public BusiResult<UserVo> getUserByUid(Long uid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users user = getUsersByUid(uid);
        if (user == null) {
            Account account = accountMapper.selectByPrimaryKey(uid);
            if (account == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            user = new Users();
            user.setUid(account.getUid());
            user.setErbanNo(account.getErbanNo());
        }
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        UserVo userVo = converToUserVo(user, nobleUsers);
        List<PrivatePhoto> photos = privatePhotoService.getPrivatePhoto(uid);
        if (photos != null) {
            List<PrivatePhotoVo> photoListVo = privatePhotoService.converToPrivatePhotoListVo(photos);
            Collections.sort(photoListVo);
            userVo.setPrivatePhoto(photoListVo);
        }
        String purseStr = giftCarPurseService.getPurse(uid);
        userVo.setCarUrl("");
        userVo.setCarName("");
        if (!StringUtils.isBlank(purseStr)) {
            String[] purse = purseStr.split(",");
            GiftCar giftCar;
            GiftCarPurseRecord giftCarPurseRecord;
            for (String p : purse) {
                giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), p);
                if (giftCarPurseRecord != null && giftCarPurseRecord.getIsUse().intValue() == 1 && giftCarPurseRecord.getCarDate() != 0) {
                    giftCar = giftCarService.getOneByJedisId(giftCarPurseRecord.getCarId().toString());
                    if (giftCar != null) {
                        userVo.setCarUrl(giftCar.getVggUrl());
                        userVo.setCarName(giftCar.getCarName());
                        break;
                    }
                }
            }
        }
        Integer level = 0;
        HeadwearPurseRecord headwearPurseRecord;
        Headwear headwear;
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(userVo.getUid());
        if (levelExerpenceVo != null) {
            level = Integer.valueOf(levelExerpenceVo.getLevelName().substring(2));
        }
        if (level >= 10) {// 10级后，还没有获取等级头饰的就赠送等级头饰给用户
            headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), "1");
            if (headwearPurseRecord == null) {
                headwear = headwearService.getOneByJedisId("1");
                if (headwear != null) {
                    String purse = headwearPurseService.getPurse(uid);// 赠送等级头饰
                    if (StringUtils.isNotBlank(purse)) {
                        if (!StringUtils.splitToList(purse, ",").contains("1")) {
                            purse += ",1";
                        }
                    } else {
                        purse = "1";
                    }
                    jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), uid.toString(), purse);
                    headwearPurseRecord = new HeadwearPurseRecord();
                    headwearPurseRecord.setUid(uid);
                    headwearPurseRecord.setHeadwearId(1L);
                    headwearPurseRecord.setTotalGoldNum(0L);
                    headwearPurseRecord.setHeadwearDate(99);
                    headwearPurseRecord.setIsUse(new Byte("0"));
                    headwearPurseRecord.setCreateTime(new Date());
                    headwearPurseRecordMapperl.insertSelective(headwearPurseRecord);
                    jedisService.hset(RedisKey.headwear_purse.getKey(), uid.toString() + "_" + "1", gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
                    HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
                    headwearGetRecord.setUid(uid);
                    headwearGetRecord.setHeadwearId(1L);
                    headwearGetRecord.setHeadwearDate(99);
                    headwearGetRecord.setType(new Byte("4"));
                    headwearGetRecord.setCreateTime(new Date());
                    headwearGetRecordMapper.insert(headwearGetRecord);
                    // 发送消息给用户
                    NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                    neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                    neteaseSendMsgParam.setOpe(0);
                    neteaseSendMsgParam.setType(0);
                    neteaseSendMsgParam.setTo(uid.toString());
                    neteaseSendMsgParam.setBody("恭喜您获取等级头饰,快点去搭配吧！");
                    sendSysMsgService.sendMsg(neteaseSendMsgParam);
                }
            }
        }
        purseStr = headwearPurseService.getPurse(uid);
        userVo.setHeadwearUrl("");
        userVo.setHeadwearName("");
        if (!StringUtils.isBlank(purseStr)) {
            String[] purse = purseStr.split(",");
            for (String p : purse) {
                headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), p);
                if (headwearPurseRecord != null && headwearPurseRecord.getIsUse().intValue() == 1 && headwearPurseRecord.getHeadwearDate() != 0) {
                    headwear = headwearService.getOneByJedisId(headwearPurseRecord.getHeadwearId().toString());
                    if (headwear != null) {
                        if (headwear.getHeadwearId() == 1) {
                            userVo.setHeadwearUrl("http://res.91fb.com/headwear_level_" + level.toString() + ".png");
                            userVo.setHeadwearName(level.toString() + headwear.getHeadwearName());
                        } else {
                            userVo.setHeadwearUrl(headwear.getPicUrl());
                            userVo.setHeadwearName(headwear.getHeadwearName());
                        }
                        break;
                    }
                }
            }
        }
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(uid.toString());
        userVo.setFindNewUsers(userConfigure == null ? new Byte("0") : userConfigure.getNewUsers());
        busiResult.setData(userVo);
        return busiResult;
    }

    public UserVo getUserVoByUid(Long uid) {
        Users user = getUsersByUid(uid);
        if (user == null) {
            return new UserVo();
        }
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        UserVo userVo = converToUserVo(user, nobleUsers);
        return userVo;
    }

    public boolean checkWxPubFansOpenidExists(String wxPubFansOpenid) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andWxPubFansOpenidEqualTo(wxPubFansOpenid);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (CollectionUtils.isEmpty(usersList)) {
            return false;
        } else {
            return true;
        }
    }


    public List<UserVo> getUsersVoListByUids(List<Long> uids) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andUidIn(uids);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        String[] uidsArray = new String[usersList.size()];
        for (int i = 0; i < usersList.size(); i++) {
            uidsArray[i] = usersList.get(i).getUid().toString();
        }
        Map<Long, NobleUsers> nobleMap = nobleUsersService.getNobleUserMap(uidsArray);
        List<UserVo> userVoList = Lists.newArrayList();
        for (Users users : usersList) {
            UserVo userVo = converToUserVo(users, nobleMap.get(users.getUid()));
            userVoList.add(userVo);
        }
        return userVoList;
    }
    // phone

    private UserHasPhoneVo converToUserHasVo(Users user) {
        UserHasPhoneVo userHasVo = new UserHasPhoneVo();
        userHasVo.setUid(user.getUid());
        userHasVo.setErbanNo(user.getErbanNo());
        userHasVo.setAvatar(user.getAvatar());
        userHasVo.setBirth(user.getBirth());
        userHasVo.setGender(user.getGender());
        userHasVo.setNick(user.getNick());
        userHasVo.setRegion(user.getRegion());
        userHasVo.setSignture(user.getSignture());
        userHasVo.setStar(user.getStar());
        userHasVo.setUserDesc(user.getUserDesc());
        userHasVo.setUserVoice(user.getUserVoice());
        userHasVo.setFollowNum(user.getFollowNum());
        userHasVo.setFansNum(user.getFansNum());
        userHasVo.setVoiceDura(user.getVoiceDura());
        userHasVo.setFortune(user.getFortune());
        userHasVo.setDefUser(user.getDefUser());
        userHasVo.setPhone(user.getPhone());
        return userHasVo;
    }

    public List<UserHasPhoneVo> getByPhone(List<String> phones) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andPhoneIn(phones);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        List<UserHasPhoneVo> userVoList = Lists.newArrayList();
        for (Users users : usersList) {
            UserHasPhoneVo userVo = converToUserHasVo(users);
            userVoList.add(userVo);
        }
        return userVoList;
    }

    public static void main(String[] args) {

        /*
         * List<String > list = new ArrayList <> ( ); list.add ( "18520395290"
         * ); UsersService usersService = new UsersService ();
         * List<UserHasPhoneVo> list1 = new ArrayList <> ( ); list1.add ( (
         * UserHasPhoneVo ) usersService.getByPhone(list) ); System.out.println
         * (list1.get ( 0 ) ); System.out.println (list1.get ( 1 ) );
         */
        List<UserVo> list1 = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        list.add(900183L);
        UsersService usersService = new UsersService();
        list1 = (List<UserVo>) usersService.getResultUsersVoListByUids(list);
        System.out.println(list1.get(0).getNick());

    }

    public BusiResult<List<UserVo>> getResultUsersVoListByUids(List<Long> uids) {
        BusiResult<List<UserVo>> busiResult = new BusiResult<List<UserVo>>(BusiStatus.SUCCESS);
        List<UserVo> userVoList = getUsersVoListByUids(uids);
        if (CollectionUtils.isEmpty(userVoList)) {
            return busiResult;
        }
        busiResult.setData(userVoList);
        return busiResult;
    }

    public Users getUsersByUid(Long uid) {
        Users user = getUsersCache(uid);
        if (user == null) {
            user = usersMapper.selectByPrimaryKey(uid);
            if (user == null) {
                return null;
            } else {
                saveUserCache(user);
            }
        }
        return user;
    }

    private void saveUserCache(Users users) {
        if (users == null) {
            return;
        }
        String usersJson = gson.toJson(users);
        jedisService.hwrite(RedisKey.user.getKey(), users.getUid().toString(), usersJson);
    }

    private Users getUsersCache(Long uid) {
        String userStr = jedisService.hget(RedisKey.user.getKey(), uid.toString());
        if (StringUtils.isNotBlank(userStr)) {
            Users users = gson.fromJson(userStr, Users.class);
            return users;
        } else {
            return null;
        }

    }

    public UserVo converToUserVo(Users user, NobleUsers nobleUsers) {
        UserVo userVo = new UserVo();
        userVo.setHasPrettyErbanNo(user.getHasPrettyErbanNo());
        userVo.setUid(user.getUid());
        userVo.setErbanNo(user.getErbanNo());
        userVo.setPhone(user.getPhone());
        userVo.setAvatar(user.getAvatar());
        userVo.setBirth(user.getBirth());
        userVo.setGender(user.getGender());
        userVo.setNick(user.getNick());
        userVo.setRegion(user.getRegion());
        userVo.setSignture(user.getSignture());
        userVo.setStar(user.getStar());
        userVo.setUserDesc(user.getUserDesc());
        userVo.setUserVoice(user.getUserVoice());
        userVo.setFollowNum(user.getFollowNum());
        userVo.setFansNum(user.getFansNum());
        userVo.setVoiceDura(user.getVoiceDura());
        userVo.setFortune(user.getFortune());
        userVo.setDefUser(user.getDefUser());
        userVo.setCreateTime(user.getCreateTime());
        if (nobleUsers != null) {
            nobleUsers.setOpenTime(null);
            nobleUsers.setRenewTime(null);
            nobleUsers.setStatus(null);
        }
        userVo.setNobleUsers(nobleUsers);
        return userVo;
    }

    public boolean isExist(Long uid) {
        Users users = getUsersByUid(uid);
        if (users == null) {
            return false;
        }
        return true;
    }

    //    @Transactional(rollbackFor = Exception.class)
    public BusiResult<UserVo> saveOrUpdateUserByUidV2(Users users, DeviceInfo deviceInfo) throws Exception {
        Account account = accountMapper.selectByPrimaryKey(users.getUid());
        if (account == null) {
            return new BusiResult<>(BusiStatus.USERNOTEXISTS);
        }
        if (!BlankUtil.isBlank(users.getAvatar()) && !users.getAvatar().contains("res.91fb.com") && !"/0".equalsIgnoreCase(users.getAvatar())) {
            try {
                String fileName = qiniuService.uploadByUrl(users.getAvatar());
                // 图片迁移到七牛
                String newAvatar = qiniuService.mergeUrlAndSlim(fileName);
                users.setAvatar(newAvatar);
            } catch (Exception e) {
                logger.error("uploadByUrl error, uid: " + users.getUid() + ", avatar: " + users.getAvatar(), e);
            }
        }
        Byte operType = null;
        Users userDb = getUsersByUid(users.getUid());
        Date date = new Date();
        boolean hasRegPacket = false;//如果有被邀请人，判断是被邀请人是否能收到邀请红包

        if (null == userDb) {// 第一次登录
            logger.info("用户第一次注册,uid：" + users.getUid());
            operType = 2;
            String phone = account.getPhone();
            if (StringUtils.isNotBlank(phone)) {
                users.setPhone(phone);
            }
            if (deviceInfo != null) {
                fillDeviceInfo(users, deviceInfo);
            }
            if (users.getGender() == null) {
                return new BusiResult<>(BusiStatus.USERUPDATEGENDER);
            }
            hasRegPacket = checkHasInviteRegisterPacket(users.getShareUid(), deviceInfo);
            users.setErbanNo(account.getErbanNo());
            users.setCreateTime(date);
            users.setUpdateTime(date);
            usersMapper.insertSelective(users);
            if (users.getGender() == null) users.setGender((byte) 1);
            erBanNetEaseService.updateUserGenderOnly(String.valueOf(users.getUid()), users.getGender());
        } else {
            operType = 1;
            users.setUpdateTime(date);
            updateUser(users);
        }

        String nick = users.getNick();
        String avatar = users.getAvatar();
        String uidStr = String.valueOf(users.getUid());

        if (!BlankUtil.isBlank(avatar)) {
            logger.info("uploadimgV2 uid:{}, nick:{} avatar:{}", users.getUid(), users.getNick(), users.getAvatar());
        }
        BaseNetEaseRet baseNetEaseRet = erBanNetEaseService.updateUserInfo(uidStr, nick, avatar);
        if (200 != baseNetEaseRet.getCode()) {
            logger.error("更新网易云账号信息异常accid=" + uidStr + ",uid=" + users.getUid() + "异常编码=" + baseNetEaseRet.getCode());
            throw new Exception("更新账号信息异常");
        }
        UserVo userVo = converToUserVo(users, null);
        userVo.setOperType(operType);
        userVo.setHasRegPacket(hasRegPacket);
        return new BusiResult<>(BusiStatus.SUCCESS, userVo);
    }

    public void updateWxpubUser(String ip, JSONObject userinfo, String roomUidStr, String shareUidStr) throws Exception {
        Account account = accountService.getOrGenAccountByOpenid(userinfo.getString("openid"), userinfo.getString("unionid"), 1, null, ip);
        if (account == null) {
            return;
        }


        Long roomUid = null;
        if (StringUtils.isNoneBlank(roomUidStr)) {
            String[] strings = roomUidStr.split("&");
            roomUid = Long.valueOf(strings[0]);
        }

        Long shareUid = null;
        if (StringUtils.isNotBlank(shareUidStr)) {
            try {
                String[] strings = shareUidStr.split("&");
                shareUid = Long.valueOf(strings[0]);
            } catch (Exception e) {
                logger.error("[ 邀请注册 ] 错误的分享人ID，shareUid:>{}，异常:>{}", shareUid, e.getMessage());
            }
        }

        Users users = this.getUsersByUid(account.getUid());
        if (users != null) {
            return;
        }

        users = new Users();
        users.setUid(account.getUid());
        users.setNick(userinfo.getString("nickname"));
        users.setGender(userinfo.getByte("sex"));
        users.setAvatar(userinfo.getString("headimgurl"));
        users.setWxPubFansOpenid(userinfo.getString("openid"));
        users.setWxPubFansGender(userinfo.getByte("sex"));
        users.setShareUid(shareUid);
        users.setRoomUid(roomUid);

        BusiResult<UserVo> result = this.saveOrUpdateUserByUidV2(users, null);
        UserVo userVo = result.getData();
        if (result.getCode() != 200 || (userVo == null || userVo.getOperType() != 2)) {
            return;
        }

        if (userVo.isHasRegPacket() && StringUtils.isNotBlank(shareUidStr)) {
            try {
                userShareRecordService.saveUserShareRegisterRecord(shareUid, account.getUid());
            } catch (Exception e) {
                logger.error("[ 邀请注册 ] 邀请人获得红包处理异常，shareUid:>{}，异常:>{}", shareUid, e.getMessage());
            }
        }
        userPacketService.checkAndGetFirsetPacket(account.getUid());
    }


    public boolean checkHasInviteRegisterPacket(Long shareUid, DeviceInfo curDeviceInfo) {
        boolean result = false;
        if (shareUid != null) {
            return true;
        } else {
            return false;
        }
//        if(shareUid==null){
//            return result;
//        }
//        if(curDeviceInfo==null){//版本稳定之后修改
//            return true;
//        }
//        Users users=getUsersByUid(shareUid);
//        if(users==null){
//            return result;
//        }
//        //被邀请人与邀请人有着相同的imei号或者deviceId，邀请无效，不给红包
////        if(curDeviceInfo.getImei().equals(users.getImei())||curDeviceInfo.getDeviceId().equals(curDeviceInfo.getDeviceId())) {
////            return result;
////        }
//        result =true;
//        return result;
    }

    public Users fillDeviceInfo(Users users, DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            users.setOsversion(deviceInfo.getOsVersion());
            users.setApp(deviceInfo.getAppid());
            users.setAppVersion(deviceInfo.getAppVersion());
            users.setChannel(deviceInfo.getChannel());
            users.setLinkedmeChannel(deviceInfo.getLinkedmeChannel());
            users.setDeviceId(deviceInfo.getDeviceId());
            users.setImei(deviceInfo.getImei());
            users.setIspType(deviceInfo.getIspType());
            users.setModel(deviceInfo.getModel());
            users.setNetType(deviceInfo.getNetType());
            users.setOs(deviceInfo.getOs());
        }
        return users;
    }

    public String changeAvatarToNetease(String avatar) throws Exception {
        InputStream is = FileUtils.downloadFileInputStream(avatar);
        FileUploadRet fileUploadRet = erBanNetEaseService.uploadFileByInputStream(is);
        return fileUploadRet.getUrl();
    }

    /* 绑定手机 */
    public void boundPhone(Long uid, String phone) throws Exception {
        Users user = new Users();
        user.setUid(uid);
        user.setPhone(phone);
        usersMapper.updateByPrimaryKeySelective(user);
        Account account = new Account();
        account.setUid(uid);
        account.setPhone(phone);
        accountMapper.updateByPrimaryKeySelective(account);
        Users userDb = usersMapper.selectByPrimaryKey(user.getUid());
        saveUserCache(userDb);
    }

    /**
     * 更新用户资料 拉贝号、手机号码、性别不能修改。必须通过单独的接口修改
     *
     * @param user
     */
    public void updateUser(Users user) {
        user.setErbanNo(null);
        user.setPhone(null);
        user.setGender(null);
        if (!BlankUtil.isBlank(user.getAvatar())) {
            logger.info("updateUserAvatar uid:{}, nick:{} avatar:{}", user.getUid(), user.getNick(), user.getAvatar());
        }
        usersMapper.updateByPrimaryKeySelective(user);
        Users userDb = usersMapper.selectByPrimaryKey(user.getUid());
        saveUserCache(userDb);
    }

    public BusiResult<List<UserVo>> searchUsersBykey(String key) {
        boolean rs = key.matches(regEx);
        BusiResult<List<UserVo>> busiResult = null;
        if (rs) {
            busiResult = getUsersByErbanNo(Long.valueOf(key));
        } else {
            busiResult = searchUsersByNick(key);
        }
        return busiResult;

    }

    /**
     * 更改用户的关注数量
     *
     * @param uid
     * @param increaseNum
     */
    public void updateFollowNum(Long uid, int increaseNum) {
        Date date = new Date();
        Users users = getUsersByUid(uid);
        if (users == null) {
            return;
        }
        Integer followNum = users.getFollowNum();
        followNum = followNum + increaseNum;
        if (followNum < 0) {
            return;
        }
        users.setFollowNum(followNum);
        users.setUpdateTime(date);
        updateUser(users);
    }

    /**
     * 更新用户的粉丝数量
     *
     * @param uid
     * @param increaseNum
     */
    public void updateFansNum(Long uid, int increaseNum) {
        Date date = new Date();
        Users users = getUsersByUid(uid);
        if (users == null) {
            return;
        }
        Integer fansNum = users.getFansNum();
        fansNum = fansNum + increaseNum;
        users.setFansNum(fansNum);
        users.setUpdateTime(date);
        updateUser(users);
    }

    public void updateWxOpenIdAndGender(Users users) {
        if (users == null) {
            return;
        }
        Users user = new Users();
        user.setUid(users.getUid());
        user.setWxPubFansOpenid(users.getWxPubFansOpenid());
        user.setWxPubFansGender(users.getWxPubFansGender());
        updateUser(user);
    }

    public BusiResult<List<UserVo>> getUsersByErbanNo(Long erbanNo) {
        BusiResult<List<UserVo>> busiResult = new BusiResult<List<UserVo>>(BusiStatus.SUCCESS);
        Users users = getUsresByErbanNo(erbanNo);
        if (users == null) {
            return busiResult;
        }
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(users.getUid());
        UserVo userVo = converToUserVo(users, nobleUsers);
        List<UserVo> userVoList = Lists.newArrayList();
        userVoList.add(userVo);
        busiResult.setData(userVoList);
        return busiResult;
    }

    public Users getUsresByErbanNo(Long erbanNo) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andErbanNoEqualTo(erbanNo);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        } else {
            return usersList.get(0);
        }
    }

    private BusiResult<List<UserVo>> searchUsersByNick(String nick) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andNickLike("%" + nick + "%");

        List<Users> usersList = usersMapper.selectByExample(usersExample);
        BusiResult<List<UserVo>> busiResult = new BusiResult<List<UserVo>>(BusiStatus.SUCCESS);
        if (CollectionUtils.isEmpty(usersList)) {
            return busiResult;
        }
        List<UserVo> userVoList = Lists.newArrayList();
        for (Users user : usersList) {
            UserVo userVo = converToUserVo(user, null);
            userVoList.add(userVo);
        }
        busiResult.setData(userVoList);
        return busiResult;
    }

    public BusiResult isBindPhone(Long uid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        String phone = users.getPhone();
        if (CommonUtil.checkValidPhone(phone)) {
            return busiResult;
        }
        return new BusiResult(BusiStatus.PHONEINVALID);
    }

    public Users convertUserParamToUsers(UserParam userParam) throws Exception {
        Users users = new Users();
        users.setUid(userParam.getUid());
        users.setBirth(userParam.getBirth());
        users.setStar(userParam.getStar());
        users.setNick(userParam.getNick());
        users.setEmail(userParam.getEmail());
        users.setSignture(userParam.getSignture());
        users.setUserVoice(userParam.getUserVoice());
        users.setVoiceDura(userParam.getVoiceDura());
        users.setGender(userParam.getGender());
        users.setAvatar(userParam.getAvatar());
        users.setRegion(userParam.getRegion());
        users.setUserDesc(userParam.getUserDesc());
        String shareUidStr = userParam.getShareUid();
        if (StringUtils.isBlank(shareUidStr)) {
            logger.info("---------用户注册分享人uid为null---------------");
            return users;
        }
        try {
            String[] strings = shareUidStr.split("&");
            Long shareUid = Long.valueOf(strings[0]);
            users.setShareUid(shareUid);
            if (!StringUtils.isBlank(userParam.getRoomUid())) {
                strings = userParam.getRoomUid().split("&");
                users.setRoomUid(Long.valueOf(strings[0]));
            }
        } catch (Exception e) {
            logger.error("邀请注册---绑定失败" + e.getMessage());
        }
        return users;
    }

    public List<Users> getShareCharge() {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        UsersExample example = new UsersExample();
        example.createCriteria().andShareUidIsNotNull().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        example.setOrderByClause("uid asc");
        List<Users> usersList = usersMapper.selectByExample(example);
        return usersList;
    }

    public List<Users> getShareChargeByShareChannel() {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        UsersExample example = new UsersExample();
        example.createCriteria().andShareChannelIsNotNull().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        example.setOrderByClause("uid asc");
        List<Users> usersList = usersMapper.selectByExample(example);
        return usersList;
    }

    public List<Users> getUsersByRobot(Byte sexType) {
        UsersExample example = new UsersExample();
        example.createCriteria().andDefUserEqualTo(Constant.usersType.robotAccount).andGenderEqualTo(sexType);
        List<Users> usersList = usersMapper.selectByExample(example);
        return usersList;
    }

    public List<Users> queryUsersBeanListByUids(String[] uidArray) {
        List<Users> usersList = getUsersListCache(uidArray);
        if (usersList == null || usersList.size() < uidArray.length) {
            UsersExample usersExample = new UsersExample();
            List<Long> uids = Lists.newArrayList();
            for (String str : uidArray) {
                uids.add(Long.valueOf(str));
            }
            usersExample.createCriteria().andUidIn(uids);
            usersList = usersMapper.selectByExample(usersExample);
            for (Users users : usersList) {
                saveUserCache(users);
            }
        }
        return usersList;
    }

    public Map<Long, Users> getUsersMapBatch(String[] uidsArray) {
        Map<Long, Users> usersMap = getUsersMapBatchCache(uidsArray);
        for (int i = 0; i < uidsArray.length; i++) {
            Long uid = Long.valueOf(uidsArray[i]);
            Users users = usersMap.get(uid);
            if (users == null) {
                users = getUsersByUid(uid);
                if (users != null) {
                    usersMap.put(uid, users);
                }
            }
        }
        return usersMap;
    }

    private Map<Long, Users> getUsersMapBatchCache(String[] uidsArray) {
        List<String> userListStr = jedisService.hmread(RedisKey.user.getKey(), uidsArray);
        if (CollectionUtils.isEmpty(userListStr)) {
            return Maps.newHashMap();
        }
        Map<Long, Users> usersMap = Maps.newHashMap();
        for (String userStr : userListStr) {
            if (StringUtils.isEmpty(userStr)) {
                continue;
            }
            Users users = gson.fromJson(userStr, Users.class);
            usersMap.put(users.getUid(), users);
        }
        return usersMap;
    }

    private List<Users> getUsersListCache(String[] uidArray) {
        List<String> usersListStr = jedisService.hmread(RedisKey.user.getKey(), uidArray);
        if (CollectionUtils.isEmpty(usersListStr)) {
            return null;
        }
        List<Users> usersList = Lists.newArrayList();
        for (String usersStr : usersListStr) {
            if (com.erban.main.util.StringUtils.isEmpty(usersStr)) {
                continue;
            }
            Users users = gson.fromJson(usersStr, Users.class);
            usersList.add(users);
        }
        return usersList;
    }

    public BusiResult replace(Long uid, String phone) {
        if (smsService.checkPhoneExists(phone)) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "手机号码已经被绑定", "");
        }
        Account newAccount;
        Account account = accountMapper.selectByPrimaryKey(uid);
        if (account == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users newUsers;
        Users users = usersMapper.selectByPrimaryKey(account.getUid());
        if (users == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        newAccount = new Account();
        newAccount.setUid(uid);
        newAccount.setPhone(phone);
        accountMapper.updateByPrimaryKeySelective(newAccount);
        newUsers = new Users();
        newUsers.setUid(uid);
        newUsers.setPhone(phone);
        usersMapper.updateByPrimaryKeySelective(newUsers);
        jedisService.hdel(RedisKey.user.getKey(), account.getUid().toString());
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public void update(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }

    public BusiResult getUidByToken(String token) {
        List<Account> accountList = jdbcTemplate.query("select * from account where netease_token = ?", new BeanPropertyRowMapper<>(Account.class), token);
        if (accountList == null || accountList.size() == 0) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        return new BusiResult(BusiStatus.SUCCESS, accountList.get(0).getUid());
    }

    public BusiResult checkIdfa(String idfa) {
        String deviceId = jedisService.hget(RedisKey.idfa.getKey(), idfa);
        if (StringUtils.isBlank(deviceId)) {// ios做了特殊处理
            deviceId = jedisService.hget(RedisKey.idfa.getKey(), idfa.replaceAll("-", "").toLowerCase());
        }
        return new BusiResult(BusiStatus.SUCCESS, StringUtils.isBlank(deviceId) ? 0 : 1);
    }

    public void saveOppositeSex(String gender, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> map = jdbcTemplate.queryForList("SELECT u.uid, u.erban_no AS erbanNo, u.nick, u.gender, u.avatar FROM users u WHERE u.def_user = 1 AND u.gender <> ? AND u.avatar <> 'https://pic.tian9k9.com/default_head.png' AND EXISTS (select 1 from account_login_record a where a.uid = u.uid and a.create_time BETWEEN ? AND ?)", gender, startDate, endDate);
            if (map != null && map.size() > 0) {
                Long levelExer;
                Long levelChar;
                Integer robotNum;
                Integer erbanNo;
                List<Map<String, Object>> result = new ArrayList<>();
                for (Map<String, Object> m : map) {
                    levelExer = giftSendRecordService.getLevelExerpence(Long.valueOf(m.get("uid").toString()));
                    levelChar = giftSendRecordService.getLevelCharm(Long.valueOf(m.get("uid").toString()));
                    robotNum = robotService.getRobotNum(Long.valueOf(m.get("uid").toString()));
                    erbanNo = Integer.valueOf(m.get("erbanNo").toString());
                    if ((levelChar.intValue() >= 1000 || levelExer.intValue() >= 200) && robotNum == 0 && erbanNo > 10000) {// 2级以上，没有机器人，非特殊账号
                        result.add(m);
                    }
                }
                jedisService.hset(RedisKey.opposite_sex.getKey(), gender, gson.toJson(result));
            }
        } catch (Exception e) {
            logger.error("缓存偶遇列表失败" + e.getMessage());
        }
    }

    /**
     * 查询新用户列表
     *
     * @param uid      当前用户
     * @param gender   性别
     * @param pageNum  页码
     * @param pageSize 每页记录条数
     * @return
     */
    public BusiResult findNewUsers(Long uid, Byte gender, Integer pageNum, Integer pageSize) {
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(uid.toString());
        List<SimpleUserVo> list = Lists.newArrayList();
        if (userConfigure != null && userConfigure.getNewUsers().intValue() == 1) {
            LevelExerpenceVo levelExerpenceVo;
            LevelCharmVo levelCharmVo;
            /*
            1）显示最近一周内新注册的用户，排序按照注册时间排列，最新的在最前面；
            2) 过滤掉被封禁的用户*/
            pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
            pageSize = pageSize == null || pageSize < 1 ? 20 : pageSize;
            NewUserParam param = new NewUserParam(uid, gender, (pageNum - 1) * pageSize, pageSize);
            // 新用户按照时间排序
            list = usersMapper.selectNewUser(param);
            for (SimpleUserVo suv : list) {
                // 查询用户等级
                levelExerpenceVo = levelExperienceService.getLevelExperience(suv.getUid());
                if (levelExerpenceVo != null) {
                    suv.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
                } else {
                    suv.setExperLevel(0);
                }
                // 查询魅力等级
                levelCharmVo = levelCharmService.getLevelCharm(suv.getUid());
                if (levelCharmVo != null) {
                    suv.setCharmLevel(Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
                } else {
                    suv.setCharmLevel(0);
                }
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, list);
    }

}

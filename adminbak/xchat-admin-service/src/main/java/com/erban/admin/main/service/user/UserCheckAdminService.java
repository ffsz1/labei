package com.erban.admin.main.service.user;

import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.UsersAdminVo;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.LevelExperienceMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserPurseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.vo.UserAdminListVo;
import com.xchat.oauth2.service.param.AcountListAminParam;
import com.xchat.oauth2.service.service.account.AccountBlockService;
import com.xchat.oauth2.service.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserCheckAdminService extends BaseService {
    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ErBanNetEaseService erbanNetEaseService;

    @Autowired
    private AccountBlockService accountBlockService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GiftSendRecordService giftSendRecordService;

    @Autowired
    private LevelExperienceMapper levelExperienceMapper;

    public List<UserAdminListVo> getUsersList(String erbanNoList, String uidList, Integer gender, Integer defType,
                                              String startDateStr, String endDateStr) throws ParseException {
        // 判断时间格式
        Date startDate = null;
        Date endDate = null;
        if (!StringUtils.isBlank(startDateStr) && !StringUtils.isBlank(endDateStr)) {
            startDate = DateUtil.str2Date(startDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            endDate = DateUtil.str2Date(endDateStr, DateUtil.DateFormat.YYYY_MM_DD);
        } else if (!StringUtils.isBlank(startDateStr)) {
            startDate = DateUtil.str2Date(startDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            endDate = DateUtil.addDay(startDate, 7);
        } else if (!StringUtils.isBlank(endDateStr)) {
            endDate = DateUtil.str2Date(endDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            startDate = DateUtil.addDay(endDate, -7);
        } else if (StringUtils.isBlank(erbanNoList) && StringUtils.isBlank(uidList)) {
            endDate = DateUtil.setTime(new Date(), 0, 0, 0);
            startDate = DateUtil.addDay(endDate, -7);
        }

        String[] erbanNos = null;
        if (!StringUtils.isBlank(erbanNoList)) {
            erbanNos = erbanNoList.split(",");
        }

        String[] uids = null;
        if (!StringUtils.isBlank(uidList)) {
            uids = uidList.split(",");
        }

        List<UserAdminListVo> accounts = accountMapper.listAccountInAdmin(new AcountListAminParam(erbanNos, uids,
                gender, defType, null, startDate, endDate));
        // List<UsersAdminVo> list = new ArrayList<>();
        // for (Account myAccount : accounts) {
        //    UsersAdminVo usersAdminVo = new UsersAdminVo();
        //    usersAdminVo.setAccount(myAccount);
        //
        //    // 获取Purse信息
        //    UserPurseVo myUserPurseVo = userPurseService.getUserPurseVo(myAccount.getUid());
        //    usersAdminVo.setUserPurseVo(myUserPurseVo);
        //
        //    // 获取Users信息
        //    Users myUser = usersService.getUsersByUid(myAccount.getUid());
        //    usersAdminVo.setUsers(myUser);
        //    list.add(usersAdminVo);
        // }
        accounts.forEach(user -> {
            user.setLevelExperience(levelExperienceMapper.getExperienceLevel(giftSendRecordService.getLevelExerpence(user.getUid())));
            user.setLevelCharm(levelExperienceMapper.getCharm(giftSendRecordService.getLevelCharm(user.getUid())));
        });
        return accounts;
    }

    public PageInfo<UserAdminListVo> getUsersListWithPages(String erbanNoList, String uidList, Integer gender,
                                                           Integer defType, Integer hasCharge, String startDateStr, String endDateStr,
                                                           int pageNumber,
                                                           int pageSize) throws ParseException {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("Page Cannot Less Than 1 Or Size Cannot Less Than 0 ~");
        }

        PageHelper.startPage(pageNumber, pageSize);

        // 判断时间格式
        Date startDate = null;
        Date endDate = null;
        if (!StringUtils.isBlank(startDateStr) && !StringUtils.isBlank(endDateStr)) {
            startDate = DateUtil.str2Date(startDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            endDate = DateUtil.str2Date(endDateStr, DateUtil.DateFormat.YYYY_MM_DD);
        } else if (!StringUtils.isBlank(startDateStr)) {
            startDate = DateUtil.str2Date(startDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            endDate = DateUtil.addDay(startDate, 7);
        } else if (!StringUtils.isBlank(endDateStr)) {
            endDate = DateUtil.str2Date(endDateStr, DateUtil.DateFormat.YYYY_MM_DD);
            startDate = DateUtil.addDay(endDate, -7);
        } else if (StringUtils.isBlank(erbanNoList) && StringUtils.isBlank(uidList)) {
            endDate = DateUtil.setTime(new Date(), 0, 0, 0);
            startDate = DateUtil.addDay(endDate, -7);
        }

        String[] erbanNos = null;
        if (!StringUtils.isBlank(erbanNoList)) {
            erbanNos = erbanNoList.split(",");
        }

        String[] uids = null;
        if (!StringUtils.isBlank(uidList)) {
            uids = uidList.split(",");
        }

        List<UserAdminListVo> accounts = accountMapper.listAccountInAdmin(new AcountListAminParam(erbanNos, uids,
                gender, defType, hasCharge, startDate, endDate));
        accounts.forEach(user -> {
            user.setLevelExperience(levelExperienceMapper.getExperienceLevel(giftSendRecordService.getLevelExerpence(user.getUid())));
            user.setLevelCharm(levelExperienceMapper.getCharm(giftSendRecordService.getLevelCharm(user.getUid())));
        });
        return new PageInfo<>(accounts);
    }

    public UsersAdminVo getOne(Long uid) {
        if (uid != null) {
            UsersAdminVo usersAdminVo = new UsersAdminVo();
            usersAdminVo.setUserPurseVo(userPurseService.getUserPurseVo(uid));
            usersAdminVo.setUsers(usersService.getUsersByUid(uid));
            usersAdminVo.setAccount(accountService.getAccountByUid(uid));
            return usersAdminVo;
        }
        return null;
    }

    public BusiResult saveUser(String nick, int gender, String avatar, String erBanNo, Long uid, String phone) throws Exception {
        Account account = accountService.getAccountByUid(uid);
        if (account == null) {
            //用户不存在
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if ((Long.parseLong(erBanNo) != account.getErbanNo()) && isExist(Long.parseLong(erBanNo))) {
            //  拉贝号已被占用
            return new BusiResult(BusiStatus.UNKNOWN);
        }
        Long oldNo = account.getErbanNo();
        //account表的存入
        if (Long.parseLong(erBanNo) != account.getErbanNo()) {
            account.setErbanNo(Long.parseLong(erBanNo));
            account.setPhone(phone);
        }
        if (!checkisPhone(account.getPhone()) && !phone.equals(account.getErbanNo())) {
            account.setPhone(erBanNo);
        }
        accountMapper.updateByPrimaryKey(account);

        // user表的存入
        Users users = usersMapper.selectByPrimaryKey(uid);
        if (users == null) {
            users = new Users();
            users.setUid(uid);
            users.setNick(nick);
            users.setGender((byte) gender);
            users.setAvatar(avatar);
            users.setErbanNo(Long.parseLong(erBanNo));
            users.setPhone(phone);
            users.setCreateTime(new Date());
            usersMapper.insertSelective(users);
            userPurseService.createUserPurse(uid);
        } else {
            users.setNick(nick);
            users.setGender((byte) gender);
            users.setAvatar(avatar);
            users.setPhone(phone);
            if (Long.parseLong(erBanNo) != users.getErbanNo()) {
                users.setErbanNo(Long.parseLong(erBanNo));
            }
            if (!checkisPhone(users.getPhone()) && !phone.equals(users.getErbanNo())) {
                users.setPhone(erBanNo);
            }
            usersMapper.updateByPrimaryKeySelective(users);
        }

        // 网易云信的更新
        String uidStr = String.valueOf(users.getUid());
        BaseNetEaseRet baseNetEaseRet = erbanNetEaseService.updateUserInfoForUserAdmin(uidStr, gender, avatar, nick);
        if (200 != baseNetEaseRet.getCode()) {
            logger.error("更新网易云账号信息异常accid=" + uidStr + ",uid=" + users.getUid() + "异常编码=" + baseNetEaseRet.getCode());
            throw new Exception("更新账号信息异常");
        }
        BusiResult<Users> busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(users);
        // redis更新
        saveUserCache(users);
        // 协议更新
        if (oldNo != account.getErbanNo()) {
            jdbcTemplate.update("UPDATE `room_robot_group` SET `group_no`= ? WHERE `group_no`= ?",
                    account.getErbanNo(), oldNo);
            jdbcTemplate.update("UPDATE `room_robot_group_rela` SET `group_no`= ? WHERE `group_no`= ?",
                    account.getErbanNo(), oldNo);
        }
        return busiResult;
    }

    public BusiResult saveUsers(String nick, int gender, String avatar, String erBanNo, Long uid, String phone,
                                Byte defType) throws Exception {
        Account account = accountService.getAccountByUid(uid);
        if (account == null) {
            //用户不存在
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if ((Long.parseLong(erBanNo) != account.getErbanNo()) && isExist(Long.parseLong(erBanNo))) {
            //  拉贝号已被占用
            return new BusiResult(BusiStatus.UNKNOWN);
        }
        Long oldNo = account.getErbanNo();
        //account表的存入
        if (Long.parseLong(erBanNo) != account.getErbanNo()) {
            account.setErbanNo(Long.parseLong(erBanNo));
            account.setPhone(phone);
        }
        if (!checkisPhone(account.getPhone()) && !phone.equals(account.getErbanNo())) {
            account.setPhone(erBanNo);
        }
        accountMapper.updateByPrimaryKey(account);

        // user表的存入
        Users users = usersMapper.selectByPrimaryKey(uid);
        if (users == null) {
            users = new Users();
            users.setUid(uid);
            users.setNick(nick);
            users.setGender((byte) gender);
            users.setAvatar(avatar);
            users.setErbanNo(Long.parseLong(erBanNo));
            users.setPhone(phone);
            users.setCreateTime(new Date());

            users.setDefUser(defType);

            usersMapper.insertSelective(users);
            userPurseService.createUserPurse(uid);
        } else {
            users.setNick(nick);
            users.setGender((byte) gender);
            users.setAvatar(avatar);
            users.setPhone(phone);

            users.setDefUser(defType);

            if (Long.parseLong(erBanNo) != users.getErbanNo()) {
                users.setErbanNo(Long.parseLong(erBanNo));
            }
            if (!checkisPhone(users.getPhone()) && !phone.equals(users.getErbanNo())) {
                users.setPhone(erBanNo);
            }
            usersMapper.updateByPrimaryKeySelective(users);
        }

        // 网易云信的更新
        String uidStr = String.valueOf(users.getUid());
        BaseNetEaseRet baseNetEaseRet = erbanNetEaseService.updateUserInfoForUserAdmin(uidStr, gender, avatar, nick);
        if (200 != baseNetEaseRet.getCode()) {
            logger.error("更新网易云账号信息异常accid=" + uidStr + ",uid=" + users.getUid() + "异常编码=" + baseNetEaseRet.getCode());
            throw new Exception("更新账号信息异常");
        }
        BusiResult<Users> busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(users);
        // redis更新
        saveUserCache(users);
        // 协议更新
        if (oldNo != account.getErbanNo()) {
            jdbcTemplate.update("UPDATE `room_robot_group` SET `group_no`= ? WHERE `group_no`= ?",
                    account.getErbanNo(), oldNo);
            jdbcTemplate.update("UPDATE `room_robot_group_rela` SET `group_no`= ? WHERE `group_no`= ?",
                    account.getErbanNo(), oldNo);
        }
        return busiResult;
    }

    public BusiResult removePhone(String phone, Long uid) {
        BusiResult<Account> busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        // 改account表
        Account account = accountService.getAccountByUid(uid);
        account.setPhone(phone);
        accountMapper.updateByPrimaryKey(account);
        // 改users表
        Users user = usersService.getUsersByUid(uid);
        if (user != null) {
            user.setPhone(phone);
            usersMapper.updateByPrimaryKey(user);
            saveUserCache(user);
        }
        busiResult.setData(account);
        return busiResult;
    }

    private void saveUserCache(Users users) {
        if (users == null) {
            return;
        }
        String usersJson = gson.toJson(users);
        jedisService.hwrite(RedisKey.user.getKey(), users.getUid().toString(), usersJson);
    }

    public boolean isExist(Long erBanNo) {
        // Users users = usersService.getUsersByErBanNo(erBanNo);
        Account account = accountService.getAccountByErBanNo(erBanNo);
        if (account == null) {
            return false;
        }
        return true;
    }

    private boolean checkisPhone(String phone) {
        // String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        String regExp = "^1\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public BusiResult outRoom(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        try {
            roomService.closeRoom(uid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult out(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        try {
            jedisService.hwrite(RedisKey.uid_access_token.getKey(), uid.toString(), "");
            jedisService.hwrite(RedisKey.uid_ticket.getKey(), uid.toString(), "");
            accountBlockService.doAccountBlock(uid);
            roomService.closeRoom(uid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }
}

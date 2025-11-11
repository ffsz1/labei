package com.erban.admin.main.service.room;

import com.erban.admin.main.dto.RoomMasterDto;
import com.erban.admin.main.mapper.RoomRobotGroupMapperMgr;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.RoomRobotGroupVo;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.RobotService;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.RegexUtil;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.common.utils.UUIDUtils;
import com.xchat.oauth2.service.core.encoder.MD5;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountExample;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RobotAdminService extends BaseService {
    @Autowired
    private RoomRobotGroupMapperMgr roomRobotGroupMapperMgr;

    @Autowired
    private RobotService robotService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private NetEaseService netEaseService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ImRoomManager imRoomManager;

    public PageInfo<RoomRobotGroupVo> getAll(Integer pageNumber, Integer pageSize,
                                             RoomRobotGroupParam roomRobotGroupParam) {
        PageHelper.startPage(pageNumber, pageSize);
        List<RoomRobotGroupVo> roomRobotGroupList = roomRobotGroupMapperMgr.selectByParam(roomRobotGroupParam);
        PageInfo pageInfo = new PageInfo(roomRobotGroupList);
        return pageInfo;
    }

    /**
     * 根据条件获取所有厅主账号
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    public PageInfo<RoomMasterDto> getAllRoomMaster(Integer pageNumber, Integer pageSize,
                                                    String startDate, String endDate) {
        PageHelper.startPage(pageNumber, pageSize);
        String startTime = startDate + " 00:00:00";
        String endTime = endDate + " 23:59:59";
        List<RoomMasterDto> roomMasterDtoList = roomRobotGroupMapperMgr.queryRoomMasterAccountByParams(startTime,
                endTime);
        PageInfo pageInfo = new PageInfo(roomMasterDtoList);
        return pageInfo;
    }

    /**
     * 导出所有厅主账号
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public List<RoomMasterDto> exportAllRoomMaster(String startDate, String endDate) {
        String startTime = startDate + " 00:00:00";
        String endTime = endDate + " 23:59:59";
        List<RoomMasterDto> roomMasterDtoList = roomRobotGroupMapperMgr.queryRoomMasterAccountByParams(startTime,
                endTime);
        return roomMasterDtoList;
    }

    /**
     * 生成厅主号
     *
     * @param number   生成数量
     * @param gender   性别
     * @param phone    手机号码
     * @param password 密码
     * @throws Exception
     */
    public void create(Integer number, Integer gender, String phone, String password) throws Exception {
        Gson gson = new Gson();
        Date date = new Date();
        for (int i = 0; i < number; i++) {
            // 插入Account表
            Long erbanNo = generateErbanNo();
            Account account = new Account();
            account.setPassword(encryptPassword(password));
            account.setNeteaseToken(UUIDUtils.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setSignTime(date);
            account.setErbanNo(erbanNo);
            account.setPhone(phone);
            account.setIsShuijun((byte) 2);
            accountMapper.insert(account);

            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            logger.info("[调用云信 createNetEaseAcc]接口返回: {}", gson.toJson(tokenRet));

            // 插入Users表
            String avatar = "";
            if (gender == 1) {
                avatar = "http://pic.haijiaoxingqiu.cn/FqXU_xxCZbwtlRLvkLihGHsz-XoP?imageslim";
            } else {
                avatar = "http://pic.haijiaoxingqiu.cn/FoTYbhgUsYcEqE-79AfmqiCCiTIJ?imageslim";
            }
            Users users = new Users();
            users.setErbanNo(erbanNo);
            users.setPhone(phone);
            users.setUid(account.getUid());
            users.setAvatar(avatar);
            users.setNick("小明");
            users.setDefUser(new Byte("1"));
            users.setGender(Byte.valueOf(gender.toString()));
            users.setCreateTime(new Date());
            usersMapper.insertSelective(users);
            BaseNetEaseRet baseNetEaseRet = netEaseService.updateUserInfo(uidStr, "小明", avatar);
            logger.info("[调用云信 updateUserInfo]接口返回: {}", gson.toJson(baseNetEaseRet));
        }
    }

    /**
     * 生成 erbanNo
     *
     * @return long
     * @throws Exception
     */
    private Long generateErbanNo() throws Exception {
        int digit = 7;
        Long erBanNo = generalNotPrettyId(digit);
        int num = 0;
        while (isExistErbanAccount(erBanNo)) {
            num++;
            erBanNo = generalNotPrettyId(digit);
            if (num == 3) {
                digit++;
            } else if (num == 6) {
                digit++;
            } else if (num == 10) {
                throw new Exception("拉贝号生成异常!");
            }
        }
        return erBanNo;
    }

    private Long generalNotPrettyId(int digit) throws Exception {
        String generalId = "";
        boolean isPrettyFilter = true;
        int numFilter = 0;
        int condition = 5;
        while (isPrettyFilter) {
            numFilter++;
            generalId = CommonUtil.getRandomNumStr(digit);
            boolean dumpNumber = CommonUtil.checkMaxDumpNumber(generalId, condition);
            isPrettyFilter = RegexUtil.checkPretty(generalId);
            if (!isPrettyFilter && !dumpNumber) {
                break;
            }

            if (numFilter == 3) {
                condition--;
            } else if (numFilter == 6) {
                condition--;
            } else if (numFilter == 10) {
                throw new Exception("拉贝号生成异常!");
            }
            isPrettyFilter = true;

        }
        return Long.valueOf(generalId);
    }

    /**
     * 判断该拉贝号是否存在账户
     *
     * @param erBanNo 拉贝号
     * @return
     */
    private boolean isExistErbanAccount(Long erBanNo) {
        boolean flag = false;
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andErbanNoEqualTo(erBanNo);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (accounts != null && accounts.size() > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 密码MD5加密
     *
     * @param password 密码
     * @return
     */
    private String encryptPassword(String password) {
        return MD5.getMD5(password);
    }

    public BusiResult add(Long erbanNo, Integer num) throws Exception {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }

        Room room = roomService.getRoomByUid(users.getUid());
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }

        List<Map<String, Object>> robotList = jdbcTemplate.queryForList("select uid from users where def_user = 3 and" +
                " uid not in (SELECT robot_uid from room_robot_group) limit ?", num);
        if (robotList.size() < num) {
            return new BusiResult(BusiStatus.ROBOT_NOTEXIT);
        }

        Integer robotNum = robotService.getRobotNum(users.getUid()) + num;
        jedisService.hset(RedisKey.robot_num.getKey(), users.getUid().toString(), robotNum.toString());
        jdbcTemplate.update("INSERT INTO room_robot_group (`robot_uid`, `group_no`) select uid, ? from users where " +
                "def_user = 3 and uid not in (SELECT robot_uid from room_robot_group) limit ?", erbanNo, num);
        Integer isCan = jdbcTemplate.queryForObject("select COUNT(1) from room_robot_group_rela where group_no = ?",
                Integer.class, erbanNo);
        if (isCan == 0) {
            jdbcTemplate.update("INSERT INTO `room_robot_group_rela` (`uid`, `room_id`, `group_no`, `status`) SELECT " +
                    "b.uid, b.room_id, a.erban_no, 1 from users a INNER JOIN room b on a.uid = b.uid where a.erban_no" +
                    " = ?", erbanNo);
        }

        String accounts = "";
        for (Map<String, Object> robot : robotList) {
            if (StringUtils.isNotBlank(accounts)) {
                accounts += "," + robot.get("uid");
            } else {
                accounts = robot.get("uid").toString();
            }
        }

        imRoomManager.addRobotToRoom(room.getRoomId(), accounts);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult remove(Long erbanNo) throws Exception {
        BusiResult busiResult = robotService.removeRobotByErbanNo(erbanNo);
        if (busiResult.getCode() != 200) {
            return busiResult;
        }

        jdbcTemplate.update("DELETE FROM room_robot_group where group_no = ?", erbanNo);
        jdbcTemplate.update("DELETE FROM room_robot_group_rela where group_no = ?", erbanNo);
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users != null) {
            jedisService.hset(RedisKey.robot_num.getKey(), users.getUid().toString(), "0");
        }

        return new BusiResult(BusiStatus.SUCCESS);
    }

    public boolean checkIsPhone(String phone) {
        // String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        String regExp = "^1\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}

package com.erban.admin.main.service.account;

import com.erban.admin.main.mapper.AccountBannedMapper;
import com.erban.admin.main.model.AccountBanned;
import com.erban.admin.main.model.AccountBannedExample;
import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.Users;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AccountBannedAdminService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AccountBannedAdminService.class);
    @Autowired
    private AccountBannedMapper accountBannedMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JedisService jedisService;

    private Gson gson = new Gson();
    /**
     * 默认封禁天数
     */
    private static final int DEFAULT_BLOCK_DAY = 30;

    public static Object JSONToObject(String json, Class beanClass) {
        Gson gson = new Gson();
        Object res = gson.fromJson(json, beanClass);
        return res;
    }

    /**
     * 分页查询
     *
     * @param pageSize
     * @param pageNum
     * @param type
     * @return
     */
    public PageInfo<AccountBanned> getAccountBlockList(Integer pageSize, Integer pageNum, int type, Long erbanNo, String deviceId, String userIp) {
        AccountBannedExample accountBlockExample = new AccountBannedExample();
        AccountBannedExample.Criteria criteria = accountBlockExample.createCriteria();
        if (type != 0) {
            List<String> typeList = Arrays.asList(String.valueOf(type));
            criteria.andBannedTypeIn(typeList);
        }
        if (erbanNo != null) {
            criteria.andErbanNoEqualTo(erbanNo);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            criteria.andDeviceIdEqualTo(deviceId);
        }
        if (!StringUtils.isEmpty(userIp)) {
            criteria.andBannedIpEqualTo(userIp);
        }
        accountBlockExample.setOrderByClause("create_time DESC");
        PageHelper.startPage(pageNum, pageSize);
        List<AccountBanned> accountBlockList = accountBannedMapper.selectByExample(accountBlockExample);
        return new PageInfo<>(accountBlockList);
    }

    /**
     * 查询用户信息
     */
    public Users selectBlockedAccount(Long erbanNo) {
        return usersService.getUsersByErBanNo(erbanNo);
    }

    /**
     * 查询修改信息
     */
    public AccountBanned getUpdateAccountBanned(Integer rowId) {
        AccountBanned accountBlock = accountBannedMapper.selectByPrimaryKey(rowId);
        return accountBlock;
    }


    public int saveBannedAccount(Long erbanNo, String bannedType, String startBannedTime, String endBannedTime, String bannedDesc, int adminId, HttpServletRequest request, HttpServletResponse response) {
        AccountBanned record = new AccountBanned();
        Users users = usersService.getUsersByErBanNo(erbanNo);
        String device = users.getDeviceId();
        Date start = null;
        Date end = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            start = sd.parse(startBannedTime);
            end = sd.parse(endBannedTime);
        }catch (Exception e){
            logger.error("时间解析出错:", e);
            // 时间解析出错 自动设置从当前时间开始封禁默认天数
            start = new Date();
            end = DateUtil.addDay(start, DEFAULT_BLOCK_DAY);
        }
        byte status = 1;
        //判断拉贝号+封禁类型+deviceId 是否还在封禁中
        boolean isExist = isExist(erbanNo, bannedType, device);
        //不存在则新增
        if (!isExist) {
            try {//查询登录表
                if (users != null) {
                    int blockMinute = (int) ((end.getTime() - start.getTime()) / (1000 * 60));
                    record.setPhone(users.getPhone());
                    record.setErbanNo(erbanNo);
                    record.setUid(users.getUid());
                    record.setDeviceId(device);
                    record.setBannedMinute(blockMinute);
                    record.setBannedType(bannedType);
                    record.setAdminId(adminId);
                    record.setBannedStartTime(start);
                    record.setBannedEndTime(end);
                    record.setBannedDesc(bannedDesc);
                    record.setCreateTime(new Date());
                    //状态设置为1，即封禁中
                    record.setBannedStatus(status);
                    accountBannedMapper.insert(record);
                    jedisService.hset(RedisKey.account_banned_record.getKey(), String.valueOf(record.getUid()) , gson.toJson(record));

                } else {
                    logger.info("该" + GlobalConfig.appName + "号不存在！");
                    return 0;
                }
            } catch (Exception e) {
                logger.error("封禁出错！", e);
            }
        }else{
            return -1;
        }
        return 1;
    }



    private boolean isExist(Long erbanNo, String bannedType, String device) {
        // 查询 正在封禁中,解封时间大于当前时间的信息
        AccountBannedExample accountBannedExample = new AccountBannedExample();
        accountBannedExample.createCriteria()
                .andErbanNoEqualTo(erbanNo)
                .andBannedTypeEqualTo(bannedType)
                .andDeviceIdEqualTo(device)
                .andBannedStatusEqualTo((byte) 1)
                .andBannedEndTimeGreaterThanOrEqualTo(new Date());
        List<AccountBanned> accountBlockList = accountBannedMapper.selectByExample(accountBannedExample);
        return (accountBlockList != null && accountBlockList.size() != 0);
    }

    public int updateBannedAccount(Integer rowId, Byte bannedType, String startBannedTime, String endBannedTime, String bannedDesc, int adminId) {
        AccountBanned accountBanned = this.accountBannedMapper.selectByPrimaryKey(rowId);
        int result = 0;
        try {
            Date start = null;
            Date end = null;

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if(accountBanned != null){
                start = sd.parse(startBannedTime);
                end = sd.parse(endBannedTime);
                int blockMinute = (int) ((end.getTime() - start.getTime()) / (1000 * 60));
                accountBanned.setBannedStartTime(start);
                accountBanned.setBannedEndTime(end);
                accountBanned.setBannedMinute(blockMinute);
                accountBanned.setBannedDesc(bannedDesc);
                accountBanned.setAdminId(adminId);
                accountBanned.setBannedStatus(bannedType);
                accountBanned.setCreateTime(new Date());
                result = accountBannedMapper.updateByPrimaryKeySelective(accountBanned);
                if (bannedType == 1) {
                    jedisService.hset(RedisKey.account_banned_record.getKey(), String.valueOf(accountBanned.getUid()) , gson.toJson(accountBanned));
                }
                if (bannedType == 2) {
                    jedisService.hdelete(RedisKey.account_banned_record.getKey(), String.valueOf(accountBanned.getUid())  ,gson.toJson(accountBanned));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



    public int updateBannedStatus(Integer rowId, HttpServletRequest request, HttpServletResponse response) {
        AccountBanned accountBanned = accountBannedMapper.selectByPrimaryKey(rowId);
        //设置状态为已结束禁封
        Byte blockStatus = 2;
        int i = 1;
        if (accountBanned != null && accountBanned.getBannedStatus() == 1) {
            accountBanned.setBannedStatus(blockStatus);
            accountBanned.setCreateTime(new Date());
            accountBannedMapper.updateByPrimaryKeySelective(accountBanned);
            jedisService.hdelete(RedisKey.account_banned_record.getKey(), String.valueOf(accountBanned.getUid())  ,gson.toJson(accountBanned));
        } else if (accountBanned.getBannedStatus() == 2) {
            i = 2;
        }
        return i;
    }
}

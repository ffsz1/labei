package com.erban.admin.main.service.account;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.Users;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountBlockMapper;
import com.xchat.oauth2.service.model.AccountBlock;
import com.xchat.oauth2.service.model.AccountBlockExample;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AccountBlockAdminService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AccountBlockAdminService.class);
    @Autowired
    private AccountBlockMapper accountBlockMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AccountBlockService accountBlockService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private RoomService roomService;
    private Gson gson = new Gson();
    /** 默认封禁天数 */
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
    public PageInfo<AccountBlock> getAccountBlockList(Integer pageSize, Integer pageNum, byte type, Long erbanNo, String deviceId, String userIp) {
        AccountBlockExample accountBlockExample = new AccountBlockExample();
        AccountBlockExample.Criteria criteria = accountBlockExample.createCriteria();
        if (type != 0) {
            criteria.andBlockTypeEqualTo(type);
        }
        if (erbanNo != null) {
            criteria.andErbanNoEqualTo(erbanNo);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            criteria.andDeviceIdEqualTo(deviceId);
        }
        if (!StringUtils.isEmpty(userIp)) {
            criteria.andBlockIpEqualTo(userIp);
        }
        accountBlockExample.setOrderByClause("create_time DESC");
        PageHelper.startPage(pageNum, pageSize);
        List<AccountBlock> accountBlockList = accountBlockMapper.selectByExample(accountBlockExample);
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
    public AccountBlock getUpdateAccountBlocked(Integer rowId) {
        AccountBlock accountBlock = accountBlockMapper.selectByPrimaryKey(rowId);
        return accountBlock;
    }


    /**
     * 编辑
     */
    public int updateBlockedAccount(Integer rowId, byte blockStatus, String startBlockTime, String endBlockTime, String blockDesc, int adminId) {
        AccountBlock accountBlock = accountBlockMapper.selectByPrimaryKey(rowId);
        try {
            if (accountBlock != null) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date start = sd.parse(startBlockTime);
                Date end = sd.parse(endBlockTime);
                int blockMinute = (int) ((end.getTime() - start.getTime()) / (1000 * 60));
                accountBlock.setBlockStartTime(start);
                accountBlock.setBlockEndTime(end);
                accountBlock.setBlockMinute(blockMinute);
                accountBlock.setBlockDesc(blockDesc);
                accountBlock.setAdminId(adminId);
                accountBlock.setBlockStatus(blockStatus);
                if (blockStatus == 1) {
                    jedisService.hwrite(RedisKey.uid_access_token.getKey(),accountBlock.getUid().toString(),"");
                    jedisService.hwrite(RedisKey.uid_ticket.getKey(),accountBlock.getUid().toString(),"");
                    accountBlockService.doAccountBlock(accountBlock.getUid());
                    roomService.closeRoom(accountBlock.getUid());
                    saveBlockedAccountAche(accountBlock, accountBlock.getDeviceId());
                }
                if (blockStatus == 2) {
                    deleteBlockedAccountAche(accountBlock, accountBlock.getDeviceId());
                }
            }
        } catch (Exception e) {
            logger.error("修改出错！", e);
        }
        return accountBlockMapper.updateByPrimaryKeySelective(accountBlock);
    }


    /**
     * 保存封禁
     */
    public int saveBlockedAccount(Long erbanNo, byte blockType, String startBlockTime, String endBlockTime, String blockDesc, int adminId, HttpServletRequest request, HttpServletResponse response) {
        AccountBlock record = new AccountBlock();
        String device = "";
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return -2;
        }
        String login = jedisService.hget(RedisKey.acc_latest_login.getKey(), users.getUid().toString());
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        if (!StringUtils.isEmpty(login)) {
            accountLoginRecord = (AccountLoginRecord) JSONToObject(login, AccountLoginRecord.class);
            device = accountLoginRecord.getDeviceId();
        } else {
            device = users.getDeviceId();
        }
        byte status = 1;
        Date start = null;
        Date end = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            start = sd.parse(startBlockTime);
            end = sd.parse(endBlockTime);
        }catch (Exception e){
            logger.error("时间解析出错:", e);
            // 时间解析出错 自动设置从当前时间开始封禁默认天数
            start = new Date();
            end = DateUtil.addDay(start, DEFAULT_BLOCK_DAY);
        }
        //判断拉贝号+封禁类型+deviceId是否唯一
        boolean isExist = isExist(erbanNo, blockType, device);
        if (!isExist) {//不存在则新增
            try {//查询登录表
                if (users != null) {
                    if (!StringUtils.isEmpty(accountLoginRecord) && !StringUtils.isEmpty(accountLoginRecord.getUid())) {
                        jedisService.hwrite(RedisKey.uid_access_token.getKey(),accountLoginRecord.getUid().toString(),"");
                        jedisService.hwrite(RedisKey.uid_ticket.getKey(),accountLoginRecord.getUid().toString(),"");
                        accountBlockService.doAccountBlock(accountLoginRecord.getUid());
                        roomService.closeRoom(accountLoginRecord.getUid());
                        record.setUid(accountLoginRecord.getUid());
                    } else {
                        jedisService.hwrite(RedisKey.uid_access_token.getKey(),accountLoginRecord.getUid().toString(),"");
                        jedisService.hwrite(RedisKey.uid_ticket.getKey(),accountLoginRecord.getUid().toString(),"");
                        accountBlockService.doAccountBlock(users.getUid());
                        roomService.closeRoom(users.getUid());
                        record.setUid(users.getUid());
                    }

                    int blockMinute = (int) ((end.getTime() - start.getTime()) / (1000 * 60));
                    record.setPhone(users.getPhone());
                    record.setErbanNo(erbanNo);
                    record.setDeviceId(device);
                    record.setBlockMinute(blockMinute);
                    record.setBlockType(blockType);
                    record.setAdminId(adminId);
                    record.setBlockStartTime(start);
                    record.setBlockEndTime(end);
                    record.setBlockDesc(blockDesc);
                    record.setCreateTime(new Date());
                    record.setBlockStatus(status);//状态设置为1，即封禁中
                    accountBlockMapper.insert(record);
                    saveBlockedAccountAche(record, device);
                } else {
                    logger.info("该" + GlobalConfig.appName + "号不存在！");
                    return 0;
                }
            } catch (Exception e) {
                logger.error("封禁出错！", e);
            }
        } else {
            return -1;
        }
        return 1;
    }

    /**
     * 保存封禁的IP
     * @param  blockIp 封禁的IP
     * @param  blockDesc 封禁原因
     * @return
     */
    public int saveBlockIP(String blockIp, String blockDesc, int adminId, String startBlockTime, String endBlockTime){
        byte blockType = 3;// 类型-封禁IP
        byte status = 1;// 封禁中
        AccountBlock record = new AccountBlock();
        Date start = null;
        Date end = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            start = sd.parse(startBlockTime);
            end = sd.parse(endBlockTime);
        }catch (Exception e){
            logger.error("时间解析出错:", e);
            // 时间解析出错 自动设置从当前时间开始封禁默认天数
            start = new Date();
            end = DateUtil.addDay(start, DEFAULT_BLOCK_DAY);
        }
        AccountBlockExample example = new AccountBlockExample();
        AccountBlockExample.Criteria criteria = example.createCriteria();
        // 根据IP查询, 正在封禁中,并且解封时间大于当前时间的信息
        criteria.andBlockIpEqualTo(blockIp);
        criteria.andBlockTypeEqualTo(blockType);
        criteria.andBlockStatusEqualTo(status);
        criteria.andBlockEndTimeGreaterThanOrEqualTo(new Date());
        List<AccountBlock> list = accountBlockMapper.selectByExample(example);

        // TODO 踢用户下线
        if(list.isEmpty()){
            record.setBlockIp(blockIp);
            record.setBlockStatus(status);//状态设置为1，即封禁中
            record.setAdminId(adminId);
            record.setBlockType(blockType);
            record.setBlockDesc(blockDesc);
            record.setCreateTime(new Date());
            int blockMinute = (int) ((end.getTime() - start.getTime()) / (1000 * 60));
            record.setBlockStartTime(start);
            record.setBlockEndTime(end);
            record.setBlockMinute(blockMinute);
            // 添加记录
            accountBlockMapper.insert(record);
            // 写入缓存
            saveBlockedAccountAche(record, "");
            return 1;
        }else{
            // 该IP已经被封禁
            return 2;
        }
    }

    /**
     * 一键封禁
     * 根据ID封禁用户--账号,设备号,IP
     * @param uid 用户ID
     * @return
     */
    public int blockUserByUid(Long uid, int adminId){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return 2;
        }
        // 查询最后登录的信息
        String login = jedisService.hget(RedisKey.acc_latest_login.getKey(), uid.toString());
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        String device = "";
        String ip = "";
        if (!StringUtils.isEmpty(login)) {
            accountLoginRecord = (AccountLoginRecord) JSONToObject(login, AccountLoginRecord.class);
            String user = jedisService.hget(RedisKey.block_account.getKey(), accountLoginRecord.getErbanNo().toString());
            if(!StringUtils.isEmpty(user)){
                return 3;
            }
            device = accountLoginRecord.getDeviceId();
            ip = accountLoginRecord.getLoginIp();
        } else {
            device = users.getDeviceId();
        }

        AccountBlock account = new AccountBlock();
        account.setBlockDesc("一键封禁");
        account.setAdminId(adminId);
        account.setBlockIp(ip);
        account.setBlockStatus(new Byte("1"));
        account.setDeviceId(device);
        account.setErbanNo(users.getErbanNo());
        account.setBlockStartTime(new Date());
        account.setCreateTime(new Date());
        account.setPhone(users.getPhone());
        // 设置封禁时间
        Date endDate = DateUtil.addDay(new Date(), DEFAULT_BLOCK_DAY);
        account.setBlockEndTime(endDate);
        account.setBlockMinute((int) (endDate.getTime() - new Date().getTime() )* 60);
        // 封禁用户账号
        account.setBlockType((byte) 1);
        saveBlockedAccountAche(account, device);
        // 封禁用户设备号
        account.setBlockType((byte) 2);
        saveBlockedAccountAche(account, device);
        // 封禁用户最后一次登录的IP
        account.setBlockType((byte) 3);
        saveBlockedAccountAche(account, device);

        // 踢用户下线
        try {

            accountBlockService.doAccountBlock(uid);
            roomService.closeRoom(uid);
            // 添加一条记录--表示一键封禁
            account.setBlockType((byte) 0);
            accountBlockMapper.insert(account);
            jedisService.hwrite(RedisKey.uid_access_token.getKey(),uid.toString(),"");
            jedisService.hwrite(RedisKey.uid_ticket.getKey(),uid.toString(),"");
            return 1;
        }catch (Exception e){
            logger.info("封禁用户出错:", e);
            return -1;
        }
    }

    /**
     * 一键封禁
     * 根据ID封禁用户--账号,设备号,IP
     * @param uid 用户ID
     * @return
     */
    public int blockUserByUid(Long uid, int adminId,String blockDesc){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return 2;
        }
        // 查询最后登录的信息
        String login = jedisService.hget(RedisKey.acc_latest_login.getKey(), uid.toString());
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        String device = "";
        String ip = "";
        if (!StringUtils.isEmpty(login)) {
            accountLoginRecord = (AccountLoginRecord) JSONToObject(login, AccountLoginRecord.class);
            String user = jedisService.hget(RedisKey.block_account.getKey(), accountLoginRecord.getErbanNo().toString());
            if(!StringUtils.isEmpty(user)){
                return 3;
            }
            device = accountLoginRecord.getDeviceId();
            ip = accountLoginRecord.getLoginIp();
        } else {
            device = users.getDeviceId();
        }

        AccountBlock account = new AccountBlock();
        account.setBlockDesc(blockDesc);
        account.setAdminId(adminId);
        account.setBlockIp(ip);
        account.setBlockStatus(new Byte("1"));
        account.setDeviceId(device);
        account.setErbanNo(users.getErbanNo());
        account.setBlockStartTime(new Date());
        account.setCreateTime(new Date());
        account.setPhone(users.getPhone());
        // 设置封禁时间
        Date endDate = DateUtil.addDay(new Date(), DEFAULT_BLOCK_DAY);
        account.setBlockEndTime(endDate);
        account.setBlockMinute((int) (endDate.getTime() - new Date().getTime() )* 60);
        // 封禁用户账号
        account.setBlockType((byte) 1);
        saveBlockedAccountAche(account, device);
        // 封禁用户设备号
        account.setBlockType((byte) 2);
        saveBlockedAccountAche(account, device);
        // 封禁用户最后一次登录的IP
        account.setBlockType((byte) 3);
        saveBlockedAccountAche(account, device);

        // 踢用户下线
        try {

            accountBlockService.doAccountBlock(uid);
            roomService.closeRoom(uid);
            // 添加一条记录--表示一键封禁
            account.setBlockType((byte) 0);
            accountBlockMapper.insert(account);
            jedisService.hwrite(RedisKey.uid_access_token.getKey(),uid.toString(),"");
            jedisService.hwrite(RedisKey.uid_ticket.getKey(),uid.toString(),"");
            return 1;
        }catch (Exception e){
            logger.info("封禁用户出错:", e);
            return -1;
        }
    }

    /**
     * 解除禁封
     */
    public int updateBlockStatus(Integer rowId, HttpServletRequest request, HttpServletResponse response) {
        AccountBlock accountBlock = accountBlockMapper.selectByPrimaryKey(rowId);
        Byte blockStatus = 2;//设置状态为已结束禁封
        int i = 1;
        if (accountBlock != null && accountBlock.getBlockStatus() != 2) {
            accountBlock.setBlockStatus(blockStatus);
            accountBlockMapper.updateByPrimaryKeySelective(accountBlock);
            //清除缓存
            deleteBlockedAccountAche(accountBlock, accountBlock.getDeviceId());
        } else if (accountBlock.getBlockStatus() == 2) {
            i = 2;
        }
        return i;
    }

    /**
     * 写缓存
     *
     * @param record
     */

    public void saveBlockedAccountAche(AccountBlock record, String deviceId) {
        String type = String.valueOf(record.getBlockType());
        if (type != null) {
            switch (type) {
                case "1"://封账号
                    saveCache(RedisKey.block_account.getKey(), record.getErbanNo().toString(), record);
                    break;
                case "2"://封设备
                    saveCache(RedisKey.block_deivce.getKey(), deviceId, record);
                    break;
                case "3"://封IP
                    saveCache(RedisKey.block_ip.getKey(), record.getBlockIp(), record);
                    break;
            }
        }
    }

    /**
     * 清缓存
     *
     * @param accountBlock
     */
    public void deleteBlockedAccountAche(AccountBlock accountBlock, String deviceId) {
        String type = String.valueOf(accountBlock.getBlockType());
        if (type != null) {
            switch (type) {
                case "1"://封账号
                    deleteCache(RedisKey.block_account.getKey(), accountBlock.getErbanNo().toString(), accountBlock);
                    break;
                case "2"://封设备
                    deleteCache(RedisKey.block_deivce.getKey(), deviceId, accountBlock);
                    break;
                case "3"://封IP
                    deleteCache(RedisKey.block_ip.getKey(), accountBlock.getBlockIp(), accountBlock);
                    break;
                case "4":
                    //安全上报封禁, 封禁了设备和账号
                    deleteCache(RedisKey.block_deivce.getKey(), deviceId, accountBlock);
                    deleteCache(RedisKey.block_account.getKey(), accountBlock.getErbanNo().toString(), accountBlock);
                    break;
                case "0":
                    // 一键封禁
                    deleteCache(RedisKey.block_ip.getKey(), accountBlock.getBlockIp(), accountBlock);
                    deleteCache(RedisKey.block_deivce.getKey(), deviceId, accountBlock);
                    deleteCache(RedisKey.block_account.getKey(), accountBlock.getErbanNo().toString(), accountBlock);
                    break;
            }
        }
    }

    private void deleteCache(String redisKey, String fieldValue, AccountBlock accountBlock) {
        jedisService.hdelete(redisKey, fieldValue, gson.toJson(accountBlock));
    }

    private void saveCache(String redisKey, String fieldValue, AccountBlock accountBlock) {
        jedisService.hset(redisKey, fieldValue, gson.toJson(accountBlock));
    }

    public boolean isExist(Long erbanNo, Byte blockType, String device) {
        // 查询 正在封禁中,解封时间大于当前时间的信息
        AccountBlockExample accountBlockExample = new AccountBlockExample();
        accountBlockExample.createCriteria()
                .andErbanNoEqualTo(erbanNo)
                .andBlockTypeEqualTo(blockType)
                .andDeviceIdEqualTo(device)
                .andBlockStatusEqualTo((byte) 1)
                .andBlockEndTimeGreaterThanOrEqualTo(new Date());
        List<AccountBlock> accountBlockList = accountBlockMapper.selectByExample(accountBlockExample);
        return (accountBlockList != null && accountBlockList.size() != 0);
    }


}

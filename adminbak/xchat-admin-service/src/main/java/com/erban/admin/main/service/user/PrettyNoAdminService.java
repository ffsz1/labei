package com.erban.admin.main.service.user;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.*;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.NobleUsersMapper;
import com.erban.main.mybatismapper.UserNoblePrettyNoAppMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.noble.NobleHelperService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.PrettyErbanNoMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.PrettyErbanNoRecordMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.*;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
    * Created by fxw on 2018/1/5.
    */
    @Service
    public class PrettyNoAdminService extends BaseService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private NobleUsersService nobleUsersService;

    @Autowired
    private PrettyErbanNoMapper prettyErbanNoMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PrettyErbanNoRecordMapper prettyErbanNoRecordMapper;

    @Autowired
    private NobleHelperService nobleHelperService;

    @Autowired
    private UserNoblePrettyNoAppMapper userNoblePrettyNoAppMapper;

    @Autowired
    private NobleUsersMapper nobleUsersMapper;



    /**
     * 靓号分页查询
     *
     * @param pageSize
     * @param pageNum
     * @param goodNoId
     * @param oldNo
     * @param status
     * @param origin
     * @param useDate
     * @param outDate
     * @param createDate
     * @return
     */
    public PageInfo getPrettyNoList(Integer pageSize, Integer pageNum, Long goodNoId, Long oldNo, Byte status, Byte origin,
                                    String useDate, String outDate, String createDate) {
        List<PrettyErbanNo> prettyErbanNoList = new ArrayList<>();
        try {
            SimpleDateFormat sda = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PrettyErbanNoExample prettyErbanNoExample = new PrettyErbanNoExample();
            prettyErbanNoExample.setOrderByClause("create_time DESC");
            PrettyErbanNoExample.Criteria criteria = prettyErbanNoExample.createCriteria();
            if (!StringUtils.isEmpty(goodNoId)) {
                criteria.andPrettyErbanNoEqualTo(goodNoId);
            }
            if (!StringUtils.isEmpty(oldNo)) {
                criteria.andUserErbanNoEqualTo(oldNo);
            }

            if (status != -1) {
                criteria.andStatusEqualTo(status);
            }

            if (origin!=0) {
                criteria.andUseForEqualTo(origin);
            }
            if (!StringUtils.isEmpty(useDate)) {
                Date use = sda.parse(sda.format(useDate));
                criteria.andStartValidTimeEqualTo(use);
            }
            if (!StringUtils.isEmpty(outDate)) {
                Date out = sda.parse(sda.format(outDate));
                criteria.andStartValidTimeEqualTo(out);
            }
            if (!StringUtils.isEmpty(createDate)) {
                Date create = sda.parse(sda.format(createDate));
                criteria.andStartValidTimeEqualTo(create);
            }
            PageHelper.startPage(pageNum, pageSize);
            prettyErbanNoList = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
//            startWriteAche();
        } catch (Exception e) {
            logger.error("时间转换出错", e);
        }

        return new PageInfo<>(prettyErbanNoList);
    }


    /**
     * 生成靓号
     * @param prettyErbanNo
     * @param userId 拉贝号
     * @param isValid
     * @param startDate
     * @param endDate
     * @param origin
     * @param isEffectType
     * @param addRemark
     * @return
     */
    public int savePrettyNo(Long prettyErbanNo, Long userId, byte isValid, String startDate, String endDate, byte origin, int isEffectType, String addRemark) {
        SimpleDateFormat sda = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date start = new Date();
        Date end = new Date();
        if (StringUtils.isEmpty(prettyErbanNo)) {
            logger.error("该靓号为空，请输入");
        }
        try {
            if (isEffectType == 1) {
                start = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(end);
                calendar.add(calendar.YEAR, 15);
                end = calendar.getTime();
            }
            if (isEffectType == 2) {
                start = sda.parse(startDate);
                end = sda.parse(endDate);
            }
            PrettyErbanNoExample prettyErbanNoExample = new PrettyErbanNoExample();
            prettyErbanNoExample.createCriteria().andPrettyErbanNoEqualTo(prettyErbanNo);//1查靓号表
            List<PrettyErbanNo> prettyErbanNoList = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
            if (!CollectionUtils.isEmpty(prettyErbanNoList)) {//结果集不为空,存在此靓号
                for (PrettyErbanNo pErbanNo : prettyErbanNoList) {
                    Long good = pErbanNo.getPrettyErbanNo();
                    if (!StringUtils.isEmpty(good)) {
                        String goodNo = String.valueOf(good);
                        if (!goodNo.equals(String.valueOf(prettyErbanNo))) {//拉贝号不包含prettyErbanNo
                            save(prettyErbanNo, userId, isValid, origin, start, end, addRemark);//保存到prettyErbanNo表+写缓存
                            if (isValid == 1) {//分配使用人则根据uid更新account，users表
                                use(prettyErbanNo, userId, addRemark);
                            }
                        } else {
                            logger.error("该" + GlobalConfig.appName + "号已经存在");
                            return 2;
                        }
                    } else {//靓号不存在
                        save(prettyErbanNo, userId, isValid, origin, start, end, addRemark);//保存到prettyErbanNo表+写缓存
                    }
                }
            } else {
                if (!StringUtils.isEmpty(userId)) {//判断输入的用户是否拥有靓号
                    PrettyErbanNoExample example = new PrettyErbanNoExample();
                    List<PrettyErbanNo> prettyList = prettyErbanNoMapper.selectByExample(example);
                    for (PrettyErbanNo good : prettyList) {
                        if (String.valueOf(good.getUserErbanNo()).equals(String.valueOf(userId))) {
                            logger.error("当前输入的" + GlobalConfig.appName + "号已存在靓号,请换一个重试");
                            return 8;
                        }
                    }
                }
                save(prettyErbanNo, userId, isValid, origin, start, end, addRemark);
                if (isValid == 1) {
                    use(prettyErbanNo, userId, addRemark);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 查询已存在的靓号
     */
    public PrettyErbanNo getUsePrettyNo(int key) {
        return prettyErbanNoMapper.selectByPrimaryKey(key);
    }

    /**
     * 分配靓号
     * @param rowId  靓号ID
     * @param beautyNo 靓号
     * @param userId 拉贝号
     * @param startDate
     * @param endDate
     * @param origin
     * @param remark
     * @param isEffectType
     * @return
     */
    public int usePrettyNo(int rowId, Long beautyNo, Long userId, String startDate, String endDate, byte origin, String remark, int isEffectType) {
        try {
            byte i = 1;
            PrettyErbanNo pretty = prettyErbanNoMapper.selectByPrimaryKey(rowId);
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            Date parseStart = new Date();
            Date parseEnd = new Date();
            if (beautyNo == userId) {//不可设置本身
                return 7;
            }
            if (!StringUtils.isEmpty(isEffectType)) {
                if (isEffectType == 1) {
                    parseStart = new Date();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(parseEnd);
                    calendar.add(calendar.YEAR, 10);
                    parseEnd = calendar.getTime();
                }
                if (isEffectType == 2) {
                    parseStart = sdf.parse(startDate);
                    parseEnd = sdf.parse(endDate);
                }
                if (!StringUtils.isEmpty(pretty)) {//判断输入的用户是否拥有靓号
                    PrettyErbanNoExample example = new PrettyErbanNoExample();
                    List<PrettyErbanNo> prettyList = prettyErbanNoMapper.selectByExample(example);
                    for (PrettyErbanNo prettyErbanNo : prettyList) {
                        if (String.valueOf(prettyErbanNo.getUserErbanNo()).equals(String.valueOf(userId))) {
                            logger.error("当前输入的" + GlobalConfig.appName + "号已存在靓号,请换一个重试");
                            return 8;
                        }
                    }
                    if (pretty.getStatus() == 0) {
                        if (!StringUtils.isEmpty(parseStart) && !StringUtils.isEmpty(parseEnd)) {
                            Date startValidTime = pretty.getStartValidTime();
                            Date endValidTime = pretty.getEndValidTime();
                            if (parseStart.getTime() >= startValidTime.getTime()//开始时间大于有效时间的开始时间
                                    && parseEnd.getTime() <= endValidTime.getTime()) {//结束时间小于有效时间的结束时间
                                if (pretty.getUseFor() == origin) {
                                    save(beautyNo, userId, i, origin, parseStart, parseEnd, remark);
                                    use(pretty.getPrettyErbanNo(), userId, remark);
                                    return 1;//使用成功
                                } else if (pretty.getUseFor() == 1 && origin != 1) {
                                    return 4;//该靓号仅作为活动使用
                                } else if (pretty.getUseFor() == 2 && origin != 2) {
                                    return 5;//该靓号仅为贵族使用
                                } else if (pretty.getUseFor() == 3 && origin != 3) {
                                    return 6;//该靓号为运营赠送使用
                                }
                            } else {
                                return 3;//时间无效
                            }
                        }
                    } else if (!StringUtils.isEmpty(pretty) && pretty.getStatus() == 1) {
                        return 2;//改靓号已经被使用
                    }
                }
            } else {
                return 3;
            }
        } catch (Exception e) {
            logger.error("时间转换错误", e);
            return 3;
        }
        return 1;
    }

    /**
     * 解绑
     *
     * @param key
     */
    public int unBundlingPrettyNo(int key) {
        PrettyErbanNo prettyErbanNo = prettyErbanNoMapper.selectByPrimaryKey(key);
        if (!StringUtils.isEmpty(prettyErbanNo)) {
            byte status = 0;
            if (prettyErbanNo.getStatus() == 0) {
                return 2;//已经是解绑状态，无需解绑
            }
            Long userErbanNo = prettyErbanNo.getUserErbanNo();//获取原拉贝号
            AccountExample accountExample =new AccountExample();
            accountExample.createCriteria().andUidEqualTo(prettyErbanNo.getUserUid());
            List<Account> accounts = accountMapper.selectByExample(accountExample);
            if(!CollectionUtils.isEmpty(accounts)){
                Account account = accounts.get(0);
                if(!StringUtils.isEmpty(account)){
                    if(!checkisPhone(account.getPhone())){
                        account.setPhone(String.valueOf(userErbanNo));
                    }
                    account.setErbanNo(userErbanNo);
                    accountMapper.updateByPrimaryKeySelective(account);//改成普通的拉贝号
                }
            }
            Users user = usersService.getUsersByUid(prettyErbanNo.getUserUid());
            if(!checkisPhone(user.getPhone())){
                user.setPhone(String.valueOf(userErbanNo));
            }
            user.setErbanNo(userErbanNo);
            user.setHasPrettyErbanNo(false);//标记不是靓号
            usersMapper.updateByPrimaryKeySelective(user);//改成普通的拉贝号
            Gson gson =new Gson();
            String usersJson = gson.toJson(user);
            jedisService.hset(RedisKey.user.getKey(), user.getUid().toString(), usersJson);

            prettyErbanNo.setStatus(status);//状态设置为未使用
            prettyErbanNo.setUserErbanNo(0L);
            prettyErbanNo.setUserUid(null);
            Date start = new Date();
            Date end = new Date();
            prettyErbanNo.setStartValidTime(start);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(end);
            calendar.add(calendar.YEAR, 15);
            prettyErbanNo.setEndValidTime(calendar.getTime());
            prettyErbanNoMapper.updateByPrimaryKeySelective(prettyErbanNo);
            delAche(prettyErbanNo);

            PrettyErbanNoRecord record = new PrettyErbanNoRecord();
            record.setPrettyErbanNo(prettyErbanNo.getPrettyErbanNo());
            record.setPrettyDesc(prettyErbanNo.getPrettyDesc());
            record.setUserErbanNo(userErbanNo);
            record.setPrettyId(prettyErbanNo.getPrettyId());
            record.setCreateTime(new Date());
            prettyErbanNoRecordMapper.insert(record);//加入到历史记录
        }
        return 1;
    }

    /**
     * 删除
     * @param
     */
    public int deletePrettyNo(int key) {
        PrettyErbanNo pretty = prettyErbanNoMapper.selectByPrimaryKey(key);
        if (!StringUtils.isEmpty(pretty)) {
            Byte status = pretty.getStatus();
            if(status == 1){//在使用中
                Long userErbanNo = pretty.getUserErbanNo();//获取原拉贝号
                AccountExample accountExample =new AccountExample();
                accountExample.createCriteria().andErbanNoEqualTo(pretty.getPrettyErbanNo());//通过当前靓号获取其用户
                List<Account> accounts = accountMapper.selectByExample(accountExample);
                if(!CollectionUtils.isEmpty(accounts)){
                    Account account = accounts.get(0);
                    if(!StringUtils.isEmpty(account)){
                        account.setErbanNo(userErbanNo);
                        if(!checkisPhone(account.getPhone())){
                            account.setPhone(String.valueOf(userErbanNo));
                        }
                        accountMapper.updateByPrimaryKeySelective(account);//改成普通的拉贝号
                    }
                }
                Users user = usersService.getUsersByErBanNo(pretty.getPrettyErbanNo());
                if(!StringUtils.isEmpty(user)){
                    if(!checkisPhone(user.getPhone())){
                        user.setPhone(String.valueOf(userErbanNo));
                    }
                    user.setErbanNo(userErbanNo);
                    user.setHasPrettyErbanNo(false);//标记不是靓号
                    usersMapper.updateByPrimaryKeySelective(user);//改成普通的拉贝号
                    Gson gson =new Gson();
                    String usersJson = gson.toJson(user);
                    jedisService.hset(RedisKey.user.getKey(), user.getUid().toString(), usersJson);
                }
                NobleUsers nobleUsers =nobleUsersService.getNobleUser(pretty.getUserUid());
                if(!StringUtils.isEmpty(nobleUsers)){
                    nobleUsers.setGoodNum(0L);
                    nobleUsersMapper.updateByPrimaryKey(nobleUsers);
                    nobleUsersService.updateNobleUserCache(pretty.getUserUid());
                }
            }
        }
        int i = prettyErbanNoMapper.deleteByPrimaryKey(key);
        return i;

    }


    /**
     * 保存到靓号表
     * @param prettyErbanNo
     * @param userId 拉贝号
     * @param isValid
     * @param origin
     * @param start
     * @param end
     * @param addRemark
     */
    public void save(Long prettyErbanNo, Long userId, byte isValid, byte origin, Date start, Date end, String addRemark) {
        long uid = 0L;
        PrettyErbanNo pretty = new PrettyErbanNo();
        pretty.setPrettyErbanNo(prettyErbanNo);
        pretty.setUserErbanNo(userId);
        if (!StringUtils.isEmpty(userId)) {
            uid = erbanNoToUid(String.valueOf(userId));
        }
        pretty.setUserUid(uid);
        pretty.setStatus(isValid);
        pretty.setStartValidTime(start);
        pretty.setEndValidTime(end);
        pretty.setCreateTime(new Date());
        pretty.setUseFor(origin);
        pretty.setPrettyDesc(addRemark);
        prettyErbanNoMapper.insert(pretty);
        saveAche(pretty);
    }

    /**
     * 靓号申请分页
     */
    public PageInfo getPrettyNoAppList(Integer pageSize,Integer pageNum,Long prettyErbanNo,Long userErbanNo,byte approveResult){
        UserNoblePrettyNoAppExample userNoblePrettyNoAppExample =new UserNoblePrettyNoAppExample();
        if(!StringUtils.isEmpty(prettyErbanNo)){
            userNoblePrettyNoAppExample.createCriteria().andApproveErbanNoEqualTo(prettyErbanNo);
        }
        if(!StringUtils.isEmpty(userErbanNo)){
            userNoblePrettyNoAppExample.createCriteria().andErbanNoEqualTo(userErbanNo);
        }
        if(!StringUtils.isEmpty(approveResult)){
            userNoblePrettyNoAppExample.createCriteria().andApproveResultEqualTo(approveResult);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UserNoblePrettyNoApp> userNoblePrettyNoApps = userNoblePrettyNoAppMapper.selectByExample(userNoblePrettyNoAppExample);
        return new PageInfo<>(userNoblePrettyNoApps);
    }


    /**
     * 靓号审核
     *
     * @return
     */
    public int checkPrettyNo(Integer rowId) {
        try {
            UserNoblePrettyNoApp userNoblePrettyNoApp = userNoblePrettyNoAppMapper.selectByPrimaryKey(rowId);
            if(!StringUtils.isEmpty(userNoblePrettyNoApp)){
                Long uid = userNoblePrettyNoApp.getUid();
                Long userErbanNum = userNoblePrettyNoApp.getErbanNo();
                Long prettyErbanNo = userNoblePrettyNoApp.getApproveErbanNo();
                Byte approveResult = userNoblePrettyNoApp.getApproveResult();
                if(approveResult==1){//待审核状态
                    PrettyErbanNoExample prettyErbanNoExample = new PrettyErbanNoExample();
                    prettyErbanNoExample.createCriteria().andPrettyErbanNoEqualTo(prettyErbanNo);//1查靓号表
                    List<PrettyErbanNo> prettyErbanNoList = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
                    if (!CollectionUtils.isEmpty(prettyErbanNoList) && !StringUtils.isEmpty(prettyErbanNoList.get(0)) ) {//结果集不为空
                        PrettyErbanNo pErbanNo = prettyErbanNoList.get(0);
                        Long good = pErbanNo.getPrettyErbanNo();
                        if (!StringUtils.isEmpty(good) && pErbanNo.getStatus()==0) {//靓号表有靓号
                            String goodNo = String.valueOf(good);
                            if ((goodNo.equals(String.valueOf(prettyErbanNo))&& userErbanNum!=0)) {//拉贝号不包含prettyErbanNo
                                logger.error("该靓号已经被使用");
                                userNoblePrettyNoAppStatus(uid,(byte)3);
                                nobleHelperService.sendNobleGoodNumFailMess(uid);
                                return 2;
                            } else {
                                if(!StringUtils.isEmpty(pErbanNo)){
                                      logger.error("该" + GlobalConfig.appName + "号已有靓号，不可重复申请");
                                      userNoblePrettyNoAppStatus(uid,(byte)3);
                                      nobleHelperService.sendNobleGoodNumFailMess(uid);
                                      return 3;
                                }else{
                                    NobleUsers nobleUser = nobleUsersService.getNobleUser(userErbanNum);
                                    if(StringUtils.isEmpty(nobleUser)){
                                        logger.error("贵族身份失效");
                                        userNoblePrettyNoAppStatus(uid,(byte)3);
                                        nobleHelperService.sendNobleGoodNumFailMess(uid);
                                        return 4;
                                    }else{
                                        if(!StringUtils.isEmpty(nobleUser.getGoodNum())){
                                            logger.error("该" + GlobalConfig.appName + "号已有靓号，不可重复申请");
                                            return 3;
                                        }
                                        useCheckedPrettyNo(prettyErbanNo,userErbanNum);
                                        userNoblePrettyNoAppStatus(uid,(byte)2);
                                        nobleUser.setGoodNum(prettyErbanNo);//改nobleUer表
                                        nobleUsersMapper.updateByPrimaryKey(nobleUser);
                                        nobleHelperService.sendNobleGoodNumSuccessMess(uid);
                                        savePrettyNoAche(nobleUser);
                                        return 1;
                                    }
                                }
                            }
                        } else if(!StringUtils.isEmpty(good) && pErbanNo.getStatus()==1) {
                            logger.error("该靓号已经被使用");
                            return 2;
                        }
                    }else {//靓号表为空
                        NobleUsers nobleUser = nobleUsersService.getNobleUser(uid);
                            if(!StringUtils.isEmpty(nobleUser)){
                            Date expire = nobleUser.getExpire();
                            if(expire.getTime()-new Date().getTime()<0){
                                logger.error("贵族身份失效");
                                return 4;
                            }
                            save(prettyErbanNo,userErbanNum,(byte)1,(byte)2,new Date(),expire,"");//保存到靓号表
                            useCheckedPrettyNo(prettyErbanNo,userErbanNum);//使用靓号
                            userNoblePrettyNoAppStatus(uid,(byte)2);//改申请表状态
                            nobleUser.setGoodNum(prettyErbanNo);//改nobleUer表
                            nobleUsersMapper.updateByPrimaryKeySelective(nobleUser);
                            nobleHelperService.sendNobleGoodNumSuccessMess(uid);
                            savePrettyNoAche(nobleUser);
                            return 1;
                        }else{
                            logger.error("贵族身份失效");
                            return 4;
                        }
                    }
                } else if(approveResult==2) {
                    return 5;//已审核成功，无需再审核
                }else if(approveResult==3){
                    return 6;//审核不通过，请重新提交
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除审核
     */
   public int  deleteUserNobleApp(Integer rowId){
       UserNoblePrettyNoApp userNoblePrettyNoApp = userNoblePrettyNoAppMapper.selectByPrimaryKey(rowId);
       if(StringUtils.isEmpty(userNoblePrettyNoApp)){
           Long uid = userNoblePrettyNoApp.getUid();
           NobleUsers noble = nobleUsersMapper.selectByPrimaryKey(uid);
           noble.setGoodNum(0L);
           nobleUsersMapper.updateByPrimaryKey(noble);
           nobleUsersService.updateNobleUserCache(uid);
       }
       return userNoblePrettyNoAppMapper.deleteByPrimaryKey(rowId);
   }


    /**
     * 审核状态
     * @param uid
     * @param result
     */
    public void userNoblePrettyNoAppStatus(Long uid,byte result){
        UserNoblePrettyNoAppExample userNoblePrettyNoAppExample =new UserNoblePrettyNoAppExample();
        userNoblePrettyNoAppExample.createCriteria().andUidEqualTo(uid);
        List<UserNoblePrettyNoApp> userNoblePrettyNoApps = userNoblePrettyNoAppMapper.selectByExample(userNoblePrettyNoAppExample);
        if(!CollectionUtils.isEmpty(userNoblePrettyNoApps)&&!StringUtils.isEmpty(userNoblePrettyNoApps.get(0))){
            UserNoblePrettyNoApp userNoblePrettyNoApp =userNoblePrettyNoApps.get(0);
            if(!StringUtils.isEmpty(result)){
                userNoblePrettyNoApp.setApproveResult(result);
            }
            userNoblePrettyNoAppMapper.updateByPrimaryKeySelective(userNoblePrettyNoApp);
        }
    }

    /**
     * 靓号申请成功后的操作
     * @param prettyErbanNo
     * @param userErbanNO
     */
    public void useCheckedPrettyNo(Long prettyErbanNo, Long userErbanNO) {
        try{
            Users user = usersService.getUsersByErBanNo(userErbanNO);
            if(!StringUtils.isEmpty(user)){
                user.setErbanNo(prettyErbanNo);
                user.setHasPrettyErbanNo(true);
                if(!checkisPhone(user.getPhone())){
                    user.setPhone(String.valueOf(prettyErbanNo));
                }
                usersMapper.updateByPrimaryKeySelective(user);//把指定人的拉贝号改成靓号
                Gson gson =new Gson();
                String usersJson = gson.toJson(user);
                jedisService.hset(RedisKey.user.getKey(), user.getUid().toString(), usersJson);
            }
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andErbanNoEqualTo(userErbanNO);
            List<Account> accounts = accountMapper.selectByExample(accountExample);
            Account account = accounts.get(0);
            if (!StringUtils.isEmpty(account)) {
                account.setErbanNo(prettyErbanNo);
                accountMapper.updateByPrimaryKeySelective(account);//把指定人的拉贝号改成靓号
            }
            PrettyErbanNoExample  prettyErbanNoExample =new PrettyErbanNoExample();
            prettyErbanNoExample.createCriteria().andStatusEqualTo((byte)0).andPrettyErbanNoEqualTo(prettyErbanNo);
            prettyErbanNoMapper.deleteByExample(prettyErbanNoExample);
        }catch (Exception e){
            logger.error("use is error",e);
        }
    }


    /**
     * 使用靓号，更新account users表,删除prettyErbanNo
     * @param prettyErbanNo
     * @param userId
     * @param addRemark
     */
    public void use(Long prettyErbanNo, Long userId, String addRemark) {
        try{
            byte mm =0;
            AccountExample accountExample = new AccountExample();
            accountExample.createCriteria().andErbanNoEqualTo(userId);
            List<Account> accounts = accountMapper.selectByExample(accountExample);
            if(!CollectionUtils.isEmpty(accounts)){
                Account account = accounts.get(0);
                if (!StringUtils.isEmpty(account)) {
                    if(!checkisPhone(account.getPhone())){
                        account.setPhone(String.valueOf(prettyErbanNo));
                    }
                    account.setErbanNo(prettyErbanNo);
                    accountMapper.updateByPrimaryKeySelective(account);//把指定人的拉贝号改成靓号
                }
            }
            Users user = usersService.getUsersByErBanNo(userId);
            if(!StringUtils.isEmpty(user)){
                if(!checkisPhone(user.getPhone())){
                    user.setPhone(String.valueOf(prettyErbanNo));
                }
                user.setErbanNo(prettyErbanNo);
                user.setHasPrettyErbanNo(true);
                usersMapper.updateByPrimaryKeySelective(user);//把指定人的拉贝号改成靓号
                Gson gson =new Gson();
                String usersJson = gson.toJson(user);
                jedisService.hset(RedisKey.user.getKey(), user.getUid().toString(), usersJson);
            }
            PrettyErbanNoExample  prettyErbanNoExample =new PrettyErbanNoExample();
            prettyErbanNoExample.createCriteria().andStatusEqualTo(mm).andPrettyErbanNoEqualTo(prettyErbanNo);
            prettyErbanNoMapper.deleteByExample(prettyErbanNoExample);
        }catch (Exception e){
           logger.error("use method is error",e);
        }
    }

    public void saveAche(PrettyErbanNo prettyErbanNo){
        String uid =String.valueOf(prettyErbanNo.getUserUid());
        if(prettyErbanNo.getStatus() == 1){//靓号已经被使用
            saveCache(RedisKey.pretty_number.getKey(),prettyErbanNo.getPrettyErbanNo()+"_"+uid,uid);
        }
        if(prettyErbanNo.getStatus() == 0){//靓号未被使用
            saveCache(RedisKey.pretty_number.getKey(),prettyErbanNo.getPrettyErbanNo()+"_"+uid,"0");
        }

    }

    public void savePrettyNoAche(NobleUsers nobleUsers){
        String uid =String.valueOf(nobleUsers.getUid());
        Gson gson =new Gson();
        saveCache(RedisKey.noble_users.getKey(),uid,gson.toJson(nobleUsers));
    }

    public void delAche(PrettyErbanNo prettyErbanNo){
        String uid =String.valueOf(prettyErbanNo.getUserUid());
        if(prettyErbanNo.getStatus() == 1){//靓号已经被使用
            delCache(RedisKey.pretty_number.getKey(),prettyErbanNo.getPrettyErbanNo()+"_"+uid,uid);
        }
        if(prettyErbanNo.getStatus() == 0){//靓号未被使用
            delCache(RedisKey.pretty_number.getKey(),prettyErbanNo.getPrettyErbanNo()+"_"+uid,"0");
        }

    }

    private void saveCache(String redisKey,String fieldValue,String uid){
        jedisService.hset(redisKey,fieldValue,uid);
    }

    private void delCache(String redisKey,String fieldValue,String uid){
        jedisService.hdelete(redisKey,fieldValue,uid);
    }


    private Long erbanNoToUid(String erBanNo) {
        Long uid =0L;
        if (!StringUtils.isEmpty(erBanNo)) {
            UsersExample example = new UsersExample();
            long longUid = Long.parseLong(erBanNo);
            example.createCriteria().andErbanNoEqualTo(longUid);
            List<Users> usersList = usersMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(usersList)) {
                uid = usersList.get(0).getUid();
            }
        }else{
            logger.error("param is null");
        }
        return uid;
    }

    private boolean checkisPhone(String phone) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}

package com.erban.main.service.prettyNo;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;

import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.UserNoblePrettyNoAppMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.PopstarsService;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.noble.NobleUsersService;

import com.erban.main.service.user.UsersService;
import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.PropertyUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.PrettyErbanNoMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.PrettyErbanNoRecordMapper;
import com.xchat.oauth2.service.model.*;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
    * Created by fxw on 2018/1/5.
     * 靓号
    */
    @Service
    public class PrettyNoService{
    private static final Logger logger = LoggerFactory.getLogger(PopstarsService.class);
    @Autowired
    private UsersService usersService;
    @Autowired
    private PrettyErbanNoMapper prettyErbanNoMapper;
    @Autowired
    private AccountMapper  accountMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private PrettyErbanNoRecordMapper prettyErbanNoRecordMapper;
    @Autowired
    private UserNoblePrettyNoAppMapper userNoblePrettyNoAppMapper;

    @Autowired
    private JedisService jedisService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private SendSysMsgService sendSysMsgService;

    /**
     * 申请靓号
     * @param prettyErbanNo
     * @param uid
     * @return
     */
    public BusiResult applyPrettyNo(Long prettyErbanNo,Long uid) {
            HashMap<String,Long> map =new HashMap();
            map.put("prettyErbanNo",prettyErbanNo);
            //判断是否是国王和皇帝
            NobleUsers nobleUser = nobleUsersService.getNobleUser(uid);
            if(StringUtils.isEmpty(nobleUser)){
                logger.error("贵族身份失效");
                return new BusiResult(BusiStatus.NORIGHT);
            }else if(nobleUser.getGoodNum()!=0){
                logger.error("该" + GlobalConfig.appName + "号已有靓号，不可重复申请");
                return new BusiResult(BusiStatus.HASPRETTYNOERROR);
            }else if(!StringUtils.isEmpty(nobleUser)){
                if(nobleUser.getNobleId()<6){
                    return new BusiResult(BusiStatus.NORIGHT);
                }
                if(nobleUser.getNobleId()==6 && (prettyErbanNo<100000 || prettyErbanNo>999999)){
                    return new BusiResult(BusiStatus.PARAMERROR);
                }
                if(nobleUser.getNobleId()==7 && (prettyErbanNo<1000 || prettyErbanNo>9999)){
                    return new BusiResult(BusiStatus.PARAMERROR);
                }
            }
            //查靓号申请表状态为null或者为3
            UserNoblePrettyNoAppExample userNoblePrettyNoAppExample = new UserNoblePrettyNoAppExample();
            userNoblePrettyNoAppExample.createCriteria().andUidEqualTo(uid);
            userNoblePrettyNoAppExample.setOrderByClause("create_time DESC");
            List<UserNoblePrettyNoApp> userNoblePrettyNoApps = userNoblePrettyNoAppMapper.selectByExample(userNoblePrettyNoAppExample);
            if (!CollectionUtils.isEmpty(userNoblePrettyNoApps) && !StringUtils.isEmpty(userNoblePrettyNoApps.get(0))) {
                Byte approveResult = userNoblePrettyNoApps.get(0).getApproveResult();
                if (StringUtils.isEmpty(approveResult) || approveResult == 3) {
                    insert(map.get("prettyErbanNo"), uid);
                }
                if (approveResult==1) {
                    return new BusiResult(BusiStatus.iSPROVING);
                }
                if (approveResult==2) {
                    return new BusiResult(BusiStatus.PRETTYNOERROR);
                }
            } else {
                BusiResult busiResult = insert(map.get("prettyErbanNo"), uid);
                return new BusiResult(BusiStatus.SUCCESS,busiResult.getData());
            }
            return null;
        }



    /**
     * 查询申请结果
     * @param uid
     * @return
     */
    public BusiResult queryCheckResult(Long uid) {
        if(StringUtils.isEmpty(uid)){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        UserNoblePrettyNoAppExample example =new UserNoblePrettyNoAppExample();
        example.createCriteria().andUidEqualTo(uid);
        List<UserNoblePrettyNoApp> userNoblePrettyNoApps = userNoblePrettyNoAppMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(userNoblePrettyNoApps)&& !StringUtils.isEmpty(userNoblePrettyNoApps.get(0))){
            UserNoblePrettyNoApp userNoblePrettyNoApp= userNoblePrettyNoApps.get(0);
            return new BusiResult(BusiStatus.SUCCESS,userNoblePrettyNoApp);
        }
        return null;
    }

    //扫描靓号过期时间
    public void scanPrettyNo(){
        long today =new Date().getTime();
        PrettyErbanNoExample prettyErbanNoExample = new PrettyErbanNoExample();
        List<PrettyErbanNo> prettyErbanNoList = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
        if(!CollectionUtils.isEmpty(prettyErbanNoList)){
            for (PrettyErbanNo prettyErbanNo:prettyErbanNoList) {
                if(!StringUtils.isEmpty(prettyErbanNo)){
                    long end = prettyErbanNo.getEndValidTime().getTime();
                    long uid =prettyErbanNo.getUserUid();
                    NobleUsers nobleUser = nobleUsersService.getNobleUser(prettyErbanNo.getUserErbanNo());
                    if(nobleUser!=null){
                        if(nobleUser.getExpire().getTime()<=today){
                            unBundPrettyNo(uid);
                            NeteaseSendMsgParam net = new NeteaseSendMsgParam();
                            net.setType(0);
                            net.setOpe(0);
                            net.setFrom(SystemConfig.secretaryUid);
                            net.setTo(String.valueOf(uid));
                            net.setBody("您的靓号："+prettyErbanNo +"因贵族身份过期而过期，恢复贵族身份可立即使用");
                            sendSysMsgService.sendMsg(net);
                        }
                    }
                    if(end <= today){
                        unBundPrettyNo(uid);
                        NeteaseSendMsgParam net = new NeteaseSendMsgParam();
                        net.setOpe(0);
                        net.setType(0);
                        net.setFrom(SystemConfig.secretaryUid);
                        net.setTo(String.valueOf(uid));
                        net.setBody("您的靓号："+prettyErbanNo +"已过期");
                        sendSysMsgService.sendMsg(net);
                    }
                }
            }
        }
    }
        //解绑靓号接口
    public BusiResult unBundPrettyNo(Long uid){
        PrettyErbanNoExample prettyErbanNoExample =new PrettyErbanNoExample();
        prettyErbanNoExample.createCriteria().andUserUidEqualTo(uid);
        List<PrettyErbanNo> prettyErbanNos = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
        if (!CollectionUtils.isEmpty(prettyErbanNos) && !StringUtils.isEmpty( prettyErbanNos.get(0))) {
            byte status = 0;
            PrettyErbanNo prettyErbanNo = prettyErbanNos.get(0);
            if (prettyErbanNo.getStatus() == 0) {
                return new BusiResult(BusiStatus.ISUNBANGSTATUS);//已经是解绑状态，无需解绑
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
            prettyErbanNo.setUserErbanNo(null);
            prettyErbanNo.setUserUid(0L);
            Date start = new Date();
            Date end = new Date();
            prettyErbanNo.setStartValidTime(start);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(end);
            calendar.add(calendar.YEAR, 15);
            prettyErbanNo.setEndValidTime(calendar.getTime());
            prettyErbanNoMapper.updateByPrimaryKeySelective(prettyErbanNo);
            PrettyErbanNoRecord record = new PrettyErbanNoRecord();
            record.setPrettyErbanNo(prettyErbanNo.getPrettyErbanNo());
            record.setPrettyDesc(prettyErbanNo.getPrettyDesc());
            record.setUserErbanNo(userErbanNo);
            record.setPrettyId(prettyErbanNo.getPrettyId());
            record.setCreateTime(new Date());
            prettyErbanNoRecordMapper.insert(record);//加入到历史记录
        }
        return new BusiResult(BusiStatus.SUCCESS);
        }

    public void userNoblePrettyNoAppStatus(Long uid,int result){
        UserNoblePrettyNoAppExample userNoblePrettyNoAppExample =new UserNoblePrettyNoAppExample();
        userNoblePrettyNoAppExample.createCriteria().andUidEqualTo(uid);
        List<UserNoblePrettyNoApp> userNoblePrettyNoApps = userNoblePrettyNoAppMapper.selectByExample(userNoblePrettyNoAppExample);
        if(!CollectionUtils.isEmpty(userNoblePrettyNoApps)&&!StringUtils.isEmpty(userNoblePrettyNoApps.get(0))){
            UserNoblePrettyNoApp userNoblePrettyNoApp =userNoblePrettyNoApps.get(0);
            if(result==1){
                userNoblePrettyNoApp.setApproveResult((byte)1);
            }
            if(result==2){
                userNoblePrettyNoApp.setApproveResult((byte)2);
            }
            if(result==3){
                userNoblePrettyNoApp.setApproveResult((byte)3);
            }
            userNoblePrettyNoAppMapper.updateByPrimaryKeySelective(userNoblePrettyNoApp);
        }
    }


    private boolean checkisPhone(String phone) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }


    public BusiResult insert(Long prettyErbanNo,Long uid){
         UserNoblePrettyNoApp userNoblePrettyNoApp =new UserNoblePrettyNoApp();
         UsersExample usersExample =new UsersExample();
         usersExample.createCriteria().andUidEqualTo(uid);
         NobleUsers nobleUser = nobleUsersService.getNobleUser(uid);
         if(!StringUtils.isEmpty(nobleUser)){
             userNoblePrettyNoApp.setNobleId(nobleUser.getNobleId());
             userNoblePrettyNoApp.setNobleName(nobleUser.getNobleName());
         }else {
             return new BusiResult(BusiStatus.USERNOTEXISTS);//不是贵族用户
         }
         userNoblePrettyNoApp.setApproveErbanNo(prettyErbanNo);
         userNoblePrettyNoApp.setUid(uid);
         Long erbanNo = usersService.getUserByUid(uid).getData().getErbanNo();
         userNoblePrettyNoApp.setErbanNo(erbanNo);
         userNoblePrettyNoApp.setApproveResult((byte)1);//审核中
         userNoblePrettyNoAppMapper.insert(userNoblePrettyNoApp);
         userNoblePrettyNoAppStatus(uid,1);//改状态
         sendMSg(prettyErbanNo,uid);
         return new BusiResult(BusiStatus.SUCCESS,userNoblePrettyNoApp);
    }

    public void sendMSg(Long prettyNo,Long uid){
        NeteaseSendMsgParam neteaseSendMsgParam =new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setTo(String.valueOf(uid));
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setBody("您所申请的靓号:"+prettyNo+"正在审核中...");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
    }



}
